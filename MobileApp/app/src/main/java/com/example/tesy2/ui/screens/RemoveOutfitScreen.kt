package com.example.tesy2.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.viewmodel.ClothingViewModel
import java.io.ByteArrayOutputStream

@Composable
fun RemoveOutfitScreen(
    viewModel: ClothingViewModel = viewModel()
) {
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var hasLaunchedCamera by remember { mutableStateOf(false) }



    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            photoBitmap = it

            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            viewModel.uploadImageToSupabase(byteArray, "photo_${System.currentTimeMillis()}.png")
        }
    }

    // Open camera automatically on first launch
    LaunchedEffect(Unit) {
        if (!hasLaunchedCamera) {
            hasLaunchedCamera = true
            cameraLauncher.launch(null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (photoBitmap != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Photo capturée :", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    bitmap = photoBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(250.dp)
                )
            }
        } else {
            Text("Ouverture de la caméra...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
