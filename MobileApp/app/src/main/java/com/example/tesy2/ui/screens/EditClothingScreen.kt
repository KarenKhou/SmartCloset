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
//    val customPink = Color(0xFFFEE6F1) // Custom pink color as requested
//
//    // Local state for form fields
//    var name by remember { mutableStateOf("") }
//    var category by remember { mutableStateOf("") }
//    var color by remember { mutableStateOf("") }
//    var style by remember { mutableStateOf("") }
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
//                                style = style
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


//
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
//import com.example.tesy2.data.supabase.supabase
//import io.github.jan.supabase.postgrest.from
//import kotlinx.coroutines.launch
//import android.widget.Toast
//import androidx.compose.ui.platform.LocalContext
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EditClothingScreen(
//    itemId: String,
//    navController: NavController,
//    viewModel: ClothingViewModel
//) {
//    val item by viewModel.selectedItem
//    val customPink = Color(0xFFFEE6F1)
//    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
//
//    var name by remember { mutableStateOf("") }
//    var category by remember { mutableStateOf("") }
//    var color by remember { mutableStateOf("") }
//    var style by remember { mutableStateOf("") }
//    var showAlert by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        viewModel.fetchClothingItemByIdFromSupabase(itemId)
//    }
//
//    LaunchedEffect(item) {
//        item?.let {
//            name = it.name
//            category = it.category ?: ""
//            color = it.color ?: ""
//            style = it.style ?: ""
//        }
//    }
//
//    if (showAlert) {
//        AlertDialog(
//            onDismissRequest = { showAlert = false },
//            title = { Text("Up To Date") },
//            text = { Text("No changes were made") },
//            confirmButton = {
//                TextButton(onClick = { showAlert = false }) {
//                    Text("OK")
//                }
//            }
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Edit Clothing Item") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = customPink)
//            )
//        },
//        containerColor = customPink
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
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 16.dp),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
//                    colors = CardDefaults.cardColors(containerColor = Color.White)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Clothing Info",
//                            style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(bottom = 16.dp)
//                        )
//
//                        OutlinedTextField(
//                            value = name,
//                            onValueChange = { name = it },
//                            label = { Text("Name") },
//                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
//                        )
//
//                        OutlinedTextField(
//                            value = category,
//                            onValueChange = { category = it },
//                            label = { Text("Category") },
//                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
//                        )
//
//                        OutlinedTextField(
//                            value = color,
//                            onValueChange = { color = it },
//                            label = { Text("Colour") },
//                            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
//                        )
//
//                        OutlinedTextField(
//                            value = style,
//                            onValueChange = { style = it },
//                            label = { Text("Style") },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                    }
//                }
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    Button(
//                        onClick = {
//                            coroutineScope.launch {
//                                if (name == item?.name &&
//                                    category == (item?.category ?: "") &&
//                                    color == (item?.color ?: "") &&
//                                    style == (item?.style ?: "")
//                                ) {
//                                    showAlert = true
//                                } else {
//                                    try {
//                                        supabase.from("ClothingItem").update(
//                                            mapOf(
//                                                "name" to name,
//                                                "category" to category,
//                                                "color" to color,
//                                                "style" to style
//                                            )
//                                        )
//                                        Toast.makeText(context, "✅ Modifications Saved", Toast.LENGTH_SHORT).show()
//                                        navController.popBackStack()
//                                    } catch (_: Exception) {
//                                        Toast.makeText(context, "❌ Error Updating Clothing", Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//                            }
//                        },
//                        modifier = Modifier.weight(1f),
//                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
//                    ) {
//                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
//                            Icon(Icons.Default.Save, contentDescription = "Save", modifier = Modifier.padding(end = 8.dp))
//                            Text("Save")
//                        }
//                    }
//
//                    OutlinedButton(
//                        onClick = {
//                            viewModel.deleteClothingItem(itemId)
//                            navController.popBackStack()
//                        },
//                        modifier = Modifier.weight(1f),
//                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
//                    ) {
//                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
//                            Icon(Icons.Default.Delete, contentDescription = "Delete", modifier = Modifier.padding(end = 8.dp))
//                            Text("Delete")
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
import androidx.compose.foundation.clickable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tesy2.viewmodel.ClothingViewModel
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditClothingScreen(
    itemId: String,
    navController: NavController,
    viewModel: ClothingViewModel
) {
    val item by viewModel.selectedItem
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var style by remember { mutableStateOf("") }
    var showAlert by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchClothingItemByIdFromSupabase(itemId)
    }

    LaunchedEffect(item) {
        item?.let {
            name = it.name
            category = it.category ?: ""
            color = it.color ?: ""
            style = it.style ?: ""
        }
    }

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { showAlert = false },
            title = { Text("Up To Date") },
            text = { Text("No changes were made") },
            confirmButton = {
                TextButton(onClick = { showAlert = false }) {
                    Text("OK")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background curve
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(MaterialTheme.colorScheme.primary)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(48.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Clothing Item",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Informations du vêtement",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                label = { Text("Nom") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            OutlinedTextField(
                                value = category,
                                onValueChange = { category = it },
                                label = { Text("Catégorie") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            OutlinedTextField(
                                value = color,
                                onValueChange = { color = it },
                                label = { Text("Couleur") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            )

                            OutlinedTextField(
                                value = style,
                                onValueChange = { style = it },
                                label = { Text("Style") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    if (name == item?.name &&
                                        category == (item?.category ?: "") &&
                                        color == (item?.color ?: "") &&
                                        style == (item?.style ?: "")
                                    ) {
                                        showAlert = true
                                    } else {
                                        try {
                                            supabase.from("ClothingItem").update(
                                                mapOf(
                                                    "name" to name,
                                                    "category" to category,
                                                    "color" to color,
                                                    "style" to style
                                                )
                                            )
                                            Toast.makeText(context, "✅ Modifications Saved", Toast.LENGTH_SHORT).show()
                                            navController.popBackStack()
                                        } catch (_: Exception) {
                                            Toast.makeText(context, "❌ Error Updating Clothing", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.Save, contentDescription = "Save", modifier = Modifier.padding(end = 8.dp))
                                Text("Save")
                            }
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.deleteClothingItem(itemId)
                                navController.popBackStack()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
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
}


