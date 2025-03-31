package com.example.tesy2.ui.screens

import android.graphics.Bitmap
import android.util.Base64
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.viewmodel.ClothingViewModel
import java.io.ByteArrayOutputStream

@Composable
fun AddClothingScreen(
    viewModel: ClothingViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        photoBitmap = bitmap

        bitmap?.let {
            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            viewModel.uploadImageToSupabase(byteArray, "photo_${System.currentTimeMillis()}.png","picture-clothes")
        }
    }


    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom du vêtement") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black)

        )

        OutlinedTextField(
            value = material,
            onValueChange = { material = it },
            label = { Text("Materiel") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black)

        )

        Button(
            onClick = { cameraLauncher.launch(null) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("📷 Prendre une photo")
        }

        photoBitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }
        val viewModel: ClothingViewModel = viewModel()
        val publicUrl by viewModel.publicUrl.collectAsState()

        Button(onClick = {
            val newItem = ClothingItem(
                 // id item sera auto-généré
                closet_id = 1, // à adapter
                name = name,
                category = null, //hole l AI MODEL B HOTON
                color = null,
                material = material,
                season = null,
                last_worn = null,
                image_url = publicUrl ?: "",
                style = null
            )



            viewModel.addClothingItem(newItem)
        }){
            Text("✅ Enregistrer le vêtement")
        }
    }
}
