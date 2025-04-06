package com.example.tesy2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.viewmodel.AlertViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import android.Manifest


//@Composable
//fun AlertScreen(
//    navController: NavController,
//    viewModel: AlertViewModel = viewModel()
//) {
//    val alertText by viewModel.alertText.collectAsState()
//
//    // Sur réception de "ALERT", on navigue
//    LaunchedEffect(alertText) {
//        if (alertText == "ALERT") {
//            navController.navigate("removeOutfit")
//        }
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = alertText.ifEmpty { "Attente de données..." },
//            style = MaterialTheme.typography.headlineMedium
//        )
//    }
//}

@Composable
fun RequestBluetoothPermissions(context: Context, onGranted: () -> Unit = {}) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            val granted = permissions[Manifest.permission.BLUETOOTH_CONNECT] == true &&
                    permissions[Manifest.permission.BLUETOOTH_SCAN] == true
            if (granted) {
                Log.d("Permission", "Bluetooth permissions granted")
                onGranted()
            } else {
                Log.d("Permission", "Bluetooth permissions denied")
            }
        }
    )

    val connectPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.BLUETOOTH_CONNECT
    )
    val scanPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.BLUETOOTH_SCAN
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
        (connectPermission != PackageManager.PERMISSION_GRANTED ||
                scanPermission != PackageManager.PERMISSION_GRANTED)
    ) {
        LaunchedEffect(Unit) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN
                )
            )
        }
    } else {
        Log.d("Permission", "Bluetooth permissions already granted")
        onGranted()
    }
}



@Composable
fun AlertScreen(
    bottomnavController: NavController,
    viewModel: AlertViewModel = viewModel()
) {
    val alertText by viewModel.alertText.collectAsState()
    val context = LocalContext.current  // Get the context

    // Request Camera and Storage Permissions before opening the camera

    RequestBluetoothPermissions(context)
        {
            viewModel.connectBluetooth() //naymo lal error
        }


    LaunchedEffect(alertText) {
        if (alertText == "ALERT") {
            bottomnavController.navigate("removeOutfit")



        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = alertText.ifEmpty { "Attente de données..." },
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
