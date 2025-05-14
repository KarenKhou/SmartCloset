package com.example.tesy2.ui.screens

import android.graphics.Bitmap
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.data.models.Closet
import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.ui.composable.ClosetDropdown
import com.example.tesy2.viewmodel.ClothingViewModel
import com.example.tesy2.ui.composable.SeasonDropdown
import com.example.tesy2.ui.composable.StyleDropdown
import java.io.ByteArrayOutputStream

@Composable
fun AddClothingScreen(
    viewModel: ClothingViewModel = viewModel()
) {
    var closetId by remember { mutableStateOf<Int?>(null) }
    var name by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var season by remember { mutableStateOf("") }
    var stylee by remember { mutableStateOf("") }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var nameError by remember { mutableStateOf(false) }
    var materialError by remember { mutableStateOf(false) }
    var seasonError by remember { mutableStateOf(false) }
    var styleeError by remember { mutableStateOf(false) }
    var photoError by remember { mutableStateOf(false) }
    var closetError by remember { mutableStateOf(false) }


    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        photoBitmap = bitmap
        photoError = false // reset error if picture taken

        bitmap?.let {
            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            viewModel.uploadImageToSupabase(byteArray, "photo_${System.currentTimeMillis()}.png", "picture-clothes")
        }
    }

    val closetList by viewModel.closets
    var selectedCloset by remember { mutableStateOf<Closet?>(null) }
    val publicUrl by viewModel.publicUrl.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadUserClosets()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Title
            Text(
                "Add A Clothing Item",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 26.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Content Surface
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White.copy(alpha = 0.95f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Clothing Information",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = false
                        },
                        label = { Text("Clothing Item Name") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = nameError,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = material,
                        onValueChange = {
                            material = it
                            materialError = false
                        },
                        label = { Text("Material") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = materialError,
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    SeasonDropdown(
                        season = season,
                        onSeasonSelected = {
                            season = it
                            seasonError = false
                        },
                        seasonError = seasonError
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    StyleDropdown(
                        style = stylee,
                        onStyleSelected = {
                            stylee = it
                            styleeError = false
                        },
                        styleError = styleeError
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Choose closet dropdown

                    ClosetDropdown(
                            closetList = closetList,
                            selectedCloset = selectedCloset,
                            onClosetSelected = { closet ->
                                selectedCloset = closet
                            },
                            closetError = closetError
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Photo button
                    Button(
                        onClick = { cameraLauncher.launch(null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("📷 Take A Picture", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Photo preview
                    photoBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .height(180.dp)
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp,
                                    color = if (photoError) MaterialTheme.colorScheme.error else Color.Gray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Save button
                    Button(
                        onClick = {
                            nameError = name.isBlank()
                            materialError = material.isBlank()
                            photoError = photoBitmap == null

                            if (nameError || materialError || photoError) {
                                Toast.makeText(
                                    context,
                                    "Please fill all the fields and take a picture",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val newItem = ClothingItem(
                                    closet_id = selectedCloset!!.closet_id,
                                    name = name,
                                    category = null,
                                    color = null,
                                    material = material,
                                    season = season,
                                    last_worn = null,
                                    image_url = publicUrl ?: "",
                                    style = stylee,
                                    availability = 1
                                )

                                viewModel.addClothingItem(newItem)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("✅ Save", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            // ✨ Reset all your fields to blank
                            name = ""
                            material = ""
                            season = ""
                            stylee = ""
                            photoBitmap = null
                            selectedCloset = null
                        }
                    )

                }
            }
        }
    }

}

