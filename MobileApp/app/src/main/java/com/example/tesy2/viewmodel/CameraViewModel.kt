package com.example.tesy2.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.YuvImage
import android.os.Environment
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.data.supabase.supabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.Executors
import com.example.tesy2.viewmodel.ClothingViewModel
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class CameraViewModel : ViewModel() {
    private lateinit var imageCapture: ImageCapture
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    // Start camera capture
    fun startCamera(context: Context, lifecycleOwner: LifecycleOwner) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // Bind use cases to lifecycle
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(context))
    }

    // Capture and upload photo every 2 seconds
    //fun startTakingPictures(context: Context, bucketName: String) {
    fun startTakingPictures(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(3000)  // 3 seconds
                captureAndUploadPhoto(context)
            }
        }
    }

    // Capture photo and upload to Supabase
    private fun captureAndUploadPhoto(context: Context) {
        val photoFile = File(context.filesDir, "photo_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.d("Camera", "Photo saved to ${photoFile.absolutePath}")
                val filePath = photoFile.absolutePath

                // Convert the captured image to ByteArray
                val imageBytes = getImageBytesFromFile(filePath)

                // Upload to Supabase
                val fileName = "twosec/${photoFile.name}"
                CoroutineScope(Dispatchers.IO).launch {
                    delay(10000)
                   uploadImageToSupabase(imageBytes, fileName)  // Upload image concurrently
                }           }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "Error capturing photo: ${exception.message}")
            }
        })
    }

    private val _publicUrl = MutableStateFlow<String?>(null)
    val publicUrl: StateFlow<String?> = _publicUrl

     //Function to upload the image to Supabase bucket
     fun uploadImageToSupabase(imageBytes: ByteArray, fileName: String) {

         viewModelScope.launch {
             try {
                 val bucket = supabase.storage.from("twosecpic")

                 // 1. Upload de l'image
                 bucket.upload(
                     path = fileName,
                     data = imageBytes,
                     options = {
                         upsert = true
                     }
                 )
                 // 2. Récupération de l'URL publique
                 val url = bucket.publicUrl(fileName)
                 _publicUrl.value = url
                 println("📸 URL publique : $publicUrl")

             } catch (e: Exception) {
                 println("❌ Upload failed: ${e.message}")
             }
         }
     }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }

    fun getImageBytesFromFile(filePath: String): ByteArray {
        // Decode the image file into a Bitmap object
        val bitmap: Bitmap = BitmapFactory.decodeFile(filePath)

        // Compress the bitmap into a byte array (JPEG format)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)

        // Return the byte array of the image
        return byteArrayOutputStream.toByteArray()
    }
}
