package com.example.tesy2.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.viewmodel.ImageViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun CameraINOUTScreen(imageViewModel: ImageViewModel = viewModel()) {
    var isRecording by remember { mutableStateOf(false) }

    // Utiliser LaunchedEffect pour déclencher la prise de photo
    LaunchedEffect(isRecording) {
        if (isRecording) {
            // Démarrer la prise de photos toutes les 2 secondes
            takePicturesPeriodically(imageViewModel)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Camera IN/OUT Screen")

        Button(
            onClick = {
                isRecording = !isRecording  // Alterner l'état d'enregistrement
            }
        ) {
            Text(if (isRecording) "Stop Recording" else "Start Recording")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Recording: ${if (isRecording) "Yes" else "No"}")
    }
}

// Fonction pour prendre des photos toutes les 2 secondes
suspend fun takePicturesPeriodically(imageViewModel: ImageViewModel) {
    while (true) {
        // Remplacer par la logique réelle pour capturer une image
        val imageFile = File("path/to/photo.jpg")  // Remplace avec le chemin de ton image réelle

        imageViewModel.uploadImage(imageFile)

        // Attendre 2 secondes avant de prendre la photo suivante
        delay(2000)
    }
}

@Preview(showBackground = true)
@Composable
fun CameraINOUTScreenPreview() {
    CameraINOUTScreen()
}
