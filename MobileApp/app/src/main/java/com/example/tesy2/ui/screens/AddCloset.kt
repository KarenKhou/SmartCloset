//package com.example.tesy2.ui.screens
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.tesy2.data.models.Closet
//import com.example.tesy2.viewmodel.AddClosetViewModel
//import com.example.tesy2.viewmodel.ClothingViewModel
//
//@Composable
//fun AddClosetScreen(
//    viewModel: AddClosetViewModel = viewModel(),
//    navController: NavController
//) {
//    var closetName by remember { mutableStateOf("") }
//    var size by remember { mutableStateOf("") }
//    var capacity by remember { mutableStateOf("") }
//
//    val context = LocalContext.current
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Créer un nouveau closet", style = MaterialTheme.typography.headlineMedium)
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = closetName,
//            onValueChange = { closetName = it },
//            label = { Text("Nom du closet") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = size,
//            onValueChange = { size = it },
//            label = { Text("Taille (ex: M, L, XL...)") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        OutlinedTextField(
//            value = capacity,
//            onValueChange = { capacity = it },
//            label = { Text("Capacité maximale (optionnel)") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Button(
//            onClick = {
//                val userId = viewModel.getCurrentUserId()
//                if (userId != null && closetName.isNotBlank()) {
//                    val newCloset = Closet(
//                        closet_id = null,
//                        closet_name = closetName,
//                        size = size.takeIf { it.isNotBlank() },
//                        capacity = capacity.toIntOrNull(),
//                        user_id = userId
//                    )
//                    viewModel.createNewCloset(newCloset)
//                    Toast.makeText(context, "Closet ajouté !", Toast.LENGTH_SHORT).show()
//                    navController.popBackStack()
//                } else {
//                    Toast.makeText(context, "Remplis au moins le nom du closet", Toast.LENGTH_SHORT).show()
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Ajouter le closet")
//        }
//    }
//}


//
//package com.example.tesy2.ui.screens
//
//import android.widget.Toast
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.tesy2.data.models.Closet
//import com.example.tesy2.viewmodel.AddClosetViewModel
//
//@Composable
//fun AddClosetScreen(
//    viewModel: AddClosetViewModel = viewModel(),
//    navController: NavController
//) {
//    var closetName by remember { mutableStateOf("") }
//    var size by remember { mutableStateOf("") }
//    var capacity by remember { mutableStateOf("") }
//
//    val context = LocalContext.current
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Back button
//        IconButton(
//            onClick = { navController.navigate("main/profile") {
//                popUpTo("main") { inclusive = true }
//            }
//            },
//            modifier = Modifier.align(Alignment.TopStart)
//        ) {
//            Icon(
//                imageVector = Icons.Default.ArrowBack,
//                contentDescription = "Back"
//            )
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 24.dp)
//                .padding(top = 64.dp), // space for the arrow
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Créer un nouveau closet",
//                style = MaterialTheme.typography.headlineMedium,
//                modifier = Modifier.padding(bottom = 24.dp)
//            )
//
//            OutlinedTextField(
//                value = closetName,
//                onValueChange = { closetName = it },
//                label = { Text("Nom du closet") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//            )
//
//            OutlinedTextField(
//                value = size,
//                onValueChange = { size = it },
//                label = { Text("Taille (ex: M, L, XL...)") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//            )
//
//            OutlinedTextField(
//                value = capacity,
//                onValueChange = { capacity = it },
//                label = { Text("Capacité maximale (optionnel)") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp)
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Button(
//                onClick = {
//                    val userId = viewModel.getCurrentUserId()
//                    if (userId != null && closetName.isNotBlank()) {
//                        val newCloset = Closet(
//                            closet_id = null,
//                            closet_name = closetName,
//                            size = size.takeIf { it.isNotBlank() },
//                            capacity = capacity.toIntOrNull(),
//                            user_id = userId
//                        )
//                        viewModel.createNewCloset(newCloset)
//                        Toast.makeText(context, "Closet ajouté !", Toast.LENGTH_SHORT).show()
//                        navController.popBackStack()
//                    } else {
//                        Toast.makeText(context, "Remplis au moins le nom du closet", Toast.LENGTH_SHORT).show()
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(56.dp)
//            ) {
//                Text("Ajouter le closet")
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tesy2.data.models.Closet
import com.example.tesy2.viewmodel.AddClosetViewModel

@Composable
fun AddClosetScreen(
    viewModel: AddClosetViewModel = viewModel(),
    navController: NavController
) {
    var closetName by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Primary colored background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            // Top header section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 48.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Back button
                IconButton(
                    onClick = { navController.navigate("main/profile") {
                popUpTo("main") { inclusive = true }
            } },
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "New Closet",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    "Create a space for your clothes",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Main content area
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Closet Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = closetName,
                        onValueChange = { closetName = it },
                        label = { Text("Closet Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = size,
                        onValueChange = { size = it },
                        label = { Text("Taille (ex: M, L, XL...)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = capacity,
                        onValueChange = { capacity = it },
                        label = { Text("Maximal Capacity (optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            val userId = viewModel.getCurrentUserId()
                            if (userId != null && closetName.isNotBlank()) {
                                val newCloset = Closet(
                                    closet_id = null,
                                    closet_name = closetName,
                                    size = size.takeIf { it.isNotBlank() },
                                    capacity = capacity.toIntOrNull(),
                                    user_id = userId
                                )
                                viewModel.createNewCloset(newCloset)
                                Toast.makeText(context, "Closet added!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                Toast.makeText(context, "At least fill Closet Name", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Add Closet", color = Color.White)
                    }
                }
            }
        }
    }
}

