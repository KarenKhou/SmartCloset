package com.example.tesy2.ui.screens

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.viewmodel.CameraViewModel
import android.Manifest



@Composable
fun CameraScreen() {
    val context = LocalContext.current  // Get the context
    val cameraViewModel: CameraViewModel = viewModel()
    val locallifecycleowner = LocalLifecycleOwner.current

    // Request Camera and Storage Permissions before opening the camera
    RequestPermissions(context)

    // Button to start capturing and saving the image
    Button(
        onClick = {
            cameraViewModel.startCamera(context, locallifecycleowner)
            cameraViewModel.startTakingPictures(context)
            //cameraViewModel.captureAndSaveImage(context) // Capture and save the image
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text("Capture and Save Photo")
    }
}

@Composable
fun RequestPermissions(context: Context) {
    // Initialize the permission launcher for camera and storage permissions
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if ((permissions[Manifest.permission.CAMERA] == true) &&
                (permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true)
            ) {
                Log.d("Permission", "Permissions granted")
            } else {
                Log.d("Permission", "Permissions denied")
            }
        }
    )

    // Check if permissions are granted for camera and storage
    val cameraPermissionCheck = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    )
    val storagePermissionCheck = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    // If any permission is not granted, request them
    if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED ||
        storagePermissionCheck != PackageManager.PERMISSION_GRANTED) {
        // Launch the permission request
        LaunchedEffect(Unit) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        }
    } else {
        // Permissions already granted, proceed with camera functionality
        Log.d("Permission", "Permissions already granted!")
    }
}
