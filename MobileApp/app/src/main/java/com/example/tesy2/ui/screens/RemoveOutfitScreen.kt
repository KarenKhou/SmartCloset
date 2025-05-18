package com.example.tesy2.ui.screens

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.viewmodel.ClothingViewModel
import java.io.ByteArrayOutputStream

@Composable
fun RemoveOutfitScreen(
    viewModel: ClothingViewModel = viewModel(),
            navController: NavController
) {
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var hasLaunchedCamera by remember { mutableStateOf(false) }

    val showDialog by viewModel.showConfirmDialog.collectAsState()
    val matchItemId by viewModel.matchItemId.collectAsState()
    val matchedItem = viewModel.matchedItem.value





    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            photoBitmap = it

            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            viewModel.uploadImageToSupabase(byteArray, "photo_${System.currentTimeMillis()}.png","twosecpic")
        }
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val toastMessage = viewModel.toastMessage.collectAsState(initial = null)

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Open camera automatically on first launch
    LaunchedEffect(Unit) {
        if (!hasLaunchedCamera) {
            hasLaunchedCamera = true
            cameraLauncher.launch(null)
        }
    }

    val publicUrl by viewModel.publicUrl.collectAsState()

    LaunchedEffect(publicUrl) {
        publicUrl?.let { imageUrl ->
            // Ici, tu peux appeler ton API avec cette URL
            println("URL publique = $imageUrl")

            // Ex: envoyer vers ton FastAPI backend
            viewModel.sendToBackend(imageUrl)
        }
    }


    Column{


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (photoBitmap != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Picture taken :", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    bitmap = photoBitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(250.dp)
                )
                Button(onClick = { navController.navigate("my_closet") }) {
                    Text("Back to my closet")
                }
                Button(onClick = {

                    cameraLauncher.launch(null)
                }) {
                    Text("Add Another Photo")
                }
            }
        } else {
            Column {
                Text("Opening camera...", style = MaterialTheme.typography.bodyLarge)
                Button(onClick = { navController.navigate("my_closet") }) {
                    Text("Back to my closet")
                }

            }

        }

        if (showDialog && matchItemId != null) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.dismissDialog()
                },
                title = {
                    Text("Change the availability ?")
                },
                text = {
                    Text("Are you sure you are changing the availability of  ${matchedItem!!.name}?")
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.toggleAvailability(matchItemId!!)
                        viewModel.dismissDialog()
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.dismissDialog()
                    }) {
                        Text("Cancel")
                    }
                }
            )
        }

    }
    }
}
