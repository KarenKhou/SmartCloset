package com.example.tesy2.viewmodel

import android.content.Context
import android.graphics.Bitmap
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.Executors

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
                delay(2000)  // 2 seconds
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
                // Now upload the captured image to Supabase
                //uploadImageToSupabase(photoFile, bucketName, "photo_${System.currentTimeMillis()}.jpg")
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", "Error capturing photo: ${exception.message}")
            }
        })
    }

    // Function to upload the image to Supabase bucket
//    private fun uploadImageToSupabase(imageFile: File, bucketName: String, fileName: String) {
//        val storage = supabase.storage.from(bucketName)
//        val response: ApiResponse = storage.upload(fileName, imageFile)
//
//        if (response.isSuccessful) {
//            val uploadedUrl = "https://YOUR_PROJECT_URL.supabase.co/storage/v1/object/public/$bucketName/$fileName"
//            Log.d("Supabase", "File uploaded successfully: $uploadedUrl")
//        } else {
//            Log.e("Supabase", "Upload failed: ${response.error}")
//        }
//    }

    override fun onCleared() {
        super.onCleared()
        cameraExecutor.shutdown()
    }
}
