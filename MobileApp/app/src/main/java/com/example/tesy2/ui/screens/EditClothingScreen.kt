//package com.example.tesy2.ui.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Delete
//import androidx.compose.material.icons.filled.Save
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavController
//import com.example.tesy2.viewmodel.ClothingViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditClothingScreen(
//    itemId: String,
//    navController: NavController,
//    viewModel: ClothingViewModel
//) {
//    val item by viewModel.selectedItem
//    val customPink = Color.White
//
//    // Local state for form fields
//    var name by remember { mutableStateOf("") }
//    var category by remember { mutableStateOf("") }
//    var color by remember { mutableStateOf("") }
//    var style by remember { mutableStateOf("") }
//    var availability by remember { mutableStateOf("") }
//
//
//    // Fetch item data
//    LaunchedEffect(Unit) {
//        viewModel.fetchClothingItemByIdFromSupabase(itemId)
//    }
//
//    // Populate form fields when item data is loaded
//    LaunchedEffect(item) {
//        item?.let {
//            name = it.name
//            category = it.category ?: ""
//            color = it.color ?: ""
//            style = it.style ?: ""
//            availability = (it.availability ).toString()
//        }
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Modifier le vêtement") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = customPink
//                )
//            )
//        },
//        containerColor = customPink // Setting the scaffold background to the custom pink
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                // Form fields section
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color.White // White background for the card to contrast with pink background
//                    )
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Informations du vêtement",
//                            style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        OutlinedTextField(
//                            value = name,
//                            onValueChange = { name = it },
//                            label = { Text("Nom") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 12.dp)
//                        )
//
//                        OutlinedTextField(
//                            value = category,
//                            onValueChange = { category = it },
//                            label = { Text("Catégorie") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 12.dp)
//                        )
//
//                        OutlinedTextField(
//                            value = color,
//                            onValueChange = { color = it },
//                            label = { Text("Couleur") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 12.dp)
//                        )
//
//                        OutlinedTextField(
//                            value = style,
//                            onValueChange = { style = it },
//                            label = { Text("Style") },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//
//                        OutlinedTextField(
//                            value = availability,
//                            onValueChange = { availability = it },
//                            label = { Text("Availability") },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(bottom = 12.dp)
//                        )
//                    }
//                }
//
//                // Action buttons
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Button(
//                        onClick = {
//                            viewModel.updateClothingItem(
//                                itemId = itemId,
//                                name = name,
//                                category = category,
//                                color = color,
//                                style = style,
//                                availability = availability
//
//                            )
//                            navController.popBackStack()
//                        },
//                        modifier = Modifier.weight(1f),
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = MaterialTheme.colorScheme.primary
//                        )
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Icon(
//                                Icons.Default.Save,
//                                contentDescription = "Enregistrer",
//                                modifier = Modifier.padding(end = 8.dp)
//                            )
//                            Text("Enregistrer")
//                        }
//                    }
//
//                    OutlinedButton(
//                        onClick = {
//                            viewModel.deleteClothingItem(itemId)
//                            navController.popBackStack()
//                        },
//                        modifier = Modifier.weight(1f),
//                        colors = ButtonDefaults.outlinedButtonColors(
//                            contentColor = Color.Red
//                        )
//                    ) {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Icon(
//                                Icons.Default.Delete,
//                                contentDescription = "Supprimer",
//                                modifier = Modifier.padding(end = 8.dp)
//                            )
//                            Text("Supprimer")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

package com.example.tesy2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tesy2.viewmodel.ClothingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClothingScreen(
    itemId: String,
    navController: NavController,
    viewModel: ClothingViewModel
) {
    val item by viewModel.selectedItem
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var style by remember { mutableStateOf("") }
    var availability by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchClothingItemByIdFromSupabase(itemId)
    }

    LaunchedEffect(item) {
        item?.let {
            name = it.name
            category = it.category ?: ""
            color = it.color ?: ""
            style = it.style ?: ""
            availability = (it.availability).toString()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary))

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            // Header row: back button + title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)

                    .padding(start = 24.dp, end = 24.dp, top = 32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        "Edit Clothing",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )

                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = color,
                        onValueChange = { color = it },
                        label = { Text("Colour") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = style,
                        onValueChange = { style = it },
                        label = { Text("Style") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = availability,
                        onValueChange = { availability = it },
                        label = { Text("Availability") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.updateClothingItem(
                                    itemId = itemId,
                                    name = name,
                                    category = category,
                                    color = color,
                                    style = style,
                                    availability = availability
                                )
                                navController.popBackStack()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Save, contentDescription = "Save", modifier = Modifier.padding(end = 8.dp))
                            Text("Save")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.deleteClothingItem(itemId)
                                navController.popBackStack()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.padding(end = 8.dp))
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
