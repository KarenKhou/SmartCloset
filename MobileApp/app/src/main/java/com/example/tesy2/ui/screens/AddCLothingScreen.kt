package com.example.tesy2.ui.screens

import android.graphics.Bitmap
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.data.models.Closet
import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.data.models.UserData
import com.example.tesy2.ui.theme.lightPink
import com.example.tesy2.ui.theme.pinkColor
import com.example.tesy2.viewmodel.ClothingViewModel
import java.io.ByteArrayOutputStream

@Composable
fun AddClothingScreen(
    viewModel: ClothingViewModel = viewModel()
) {
    var closetId by remember { mutableStateOf<Int?>(null) }


    var name by remember { mutableStateOf("") }
    var material by remember { mutableStateOf("") }
    var  season by remember { mutableStateOf("")}
    var stylee by remember { mutableStateOf("")}
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var nameError by remember { mutableStateOf(false) }
    var materialError by remember { mutableStateOf(false) }
    var seasonError by remember { mutableStateOf(false) }
    var styleeError by remember { mutableStateOf(false) }
    var photoError by remember { mutableStateOf(false) }

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

            viewModel.uploadImageToSupabase(byteArray, "photo_${System.currentTimeMillis()}.png","picture-clothes")
        }
    }
    //ghayart methode
//    LaunchedEffect(Unit) {
//        closetId = getCurrentUserClosetIdSuspend()
//        if (closetId != null) {
//            println("closetid = ${closetId}")
//        } else {
//            println("❌ Aucun closet_id trouvé pour l'utilisateur")
//        }
//    }
    Column ( modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState()) // ✅ Ajoute ça ici
        .background(lightPink)
        .padding(24.dp)) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(lightPink)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = false
                        },
                        label = { Text("Nom du vêtement") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = pinkColor
                        ),
                        isError = nameError
                    )

                    OutlinedTextField(
                        value = material,
                        onValueChange = {
                            material = it
                            materialError = false
                        },
                        label = { Text("Materiel") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = pinkColor
                        ),
                        isError = materialError
                    )

                    OutlinedTextField(
                        value = season,
                        onValueChange = {
                            season = it
                            seasonError = false
                        },
                        label = { Text("Season : Winter - Spring - Summer") }, //iza fina naamela drop down
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = pinkColor
                        ),
                        isError = seasonError
                    )

                    OutlinedTextField(
                        value = stylee,
                        onValueChange = {
                            stylee = it
                            styleeError = false
                        },
                        label = { Text("Style : Formal - Casual - Both") }, //drop down
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedBorderColor = pinkColor
                        ),
                        isError = styleeError
                    )

                    Button(
                        onClick = { cameraLauncher.launch(null) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = lightPink
                        )
                    ) {
                        Text("📷 Prendre une photo")
                    }

                    photoBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .height(180.dp)
                                .fillMaxWidth()
                                .border(
                                    width = 2.dp,
                                    color = if (photoError) MaterialTheme.colorScheme.error else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    }

                    val viewModel: ClothingViewModel = viewModel()
                    val publicUrl by viewModel.publicUrl.collectAsState()


                    val closetList by viewModel.closets
                    var expanded by remember { mutableStateOf(false) }
                    var selectedCloset by remember { mutableStateOf<Closet?>(null) }

                    LaunchedEffect(Unit) {
                        viewModel.loadUserClosets()
                    }


                    Spacer(modifier = Modifier.height(8.dp))

                    Box {
                        Button(onClick = { expanded = true }) {
                            Text(selectedCloset?.closet_name ?: "Your Closet")
                        }

                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            closetList.forEach { closet ->
                                DropdownMenuItem(
                                    text = { Text(closet.closet_name) },
                                    onClick = {
                                        selectedCloset = closet
                                        expanded = false
                                        viewModel.loadClothes(closet.closet_id!!)
                                    }
                                )
                            }
                        }
                    }


                    Button(
                        onClick = {
                            nameError = name.isBlank()
                            materialError = material.isBlank()
                            photoError = photoBitmap == null

                            if (nameError || materialError || photoError) {
                                Toast.makeText(
                                    context,
                                    "Veuillez remplir tous les champs et prendre une photo.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val newItem = ClothingItem(
                                    // id item sera auto-généré

                                    closet_id = selectedCloset!!.closet_id, // à adapter
                                    name = name,
                                    category = null, //hole l AI MODEL B HOTON
                                    color = null, //ai
                                    material = material,
                                    season = season,
                                    last_worn = null, // a changer lors de remove item
                                    image_url = publicUrl ?: "",
                                    style = stylee,
                                    availability = 1
                                )

                                viewModel.addClothingItem(newItem)

                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = pinkColor
                        )
                    ) {
                        Text("✅ Enregistrer le vêtement")
                    }
                }
            }
        }
    }
}
