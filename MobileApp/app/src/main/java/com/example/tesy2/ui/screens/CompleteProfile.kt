//package com.example.tesy2.ui.screens
//
//import android.widget.Toast
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.tesy2.R
//import com.example.tesy2.viewmodel.AuthViewModel
//import kotlinx.coroutines.launch
//
//@Composable
//fun CompleteProfileScreen(
//    modifier: Modifier = Modifier,
//    viewModel: AuthViewModel = viewModel(),
//    navController: NavController
//) {
//    val context = LocalContext.current
//    val scope = rememberCoroutineScope()
//
//    val gender by viewModel.gender.collectAsState()
//    val job by viewModel.job.collectAsState()
//    val location by viewModel.location.collectAsState()
//    val birthDate by viewModel.birthDate.collectAsState()
//    val completeProfileSuccess by viewModel.completeProfileSuccess.collectAsState()
//
//    LaunchedEffect(completeProfileSuccess) {
//        if (completeProfileSuccess) {
//            navController.navigate("home") {
//                popUpTo("complete_profile") { inclusive = true }
//            }
//        }
//    }
//
//    Box(
//        modifier = modifier.fillMaxSize()
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(MaterialTheme.colorScheme.primary)
//        )
//
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.Top
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 24.dp, end = 24.dp, top = 64.dp),
//                horizontalAlignment = Alignment.Start
//            ) {
//                // Back button
//                IconButton(
//                    onClick = { navController.navigate("main/profile") {
//                        popUpTo("main") { inclusive = true }
//                    } },
//                    modifier = Modifier
//                        .size(48.dp)
//                        .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.ArrowBack,
//                        contentDescription = "Back",
//                        tint = Color.White
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//                Text(
//                    "Compléter le profil",
//                    style = MaterialTheme.typography.headlineLarge.copy(
//                        fontSize = 26.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White
//                    )
//                )
//                Text(
//                    "Quelques informations supplémentaires",
//                    style = MaterialTheme.typography.bodyLarge.copy(
//                        fontSize = 16.sp,
//                        color = Color.White
//                    )
//                )
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
////            val image = painterResource(id = R.drawable.auth_background1)
//
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f),
//                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
//                color = Color.Transparent
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                ) {
////                    Image(
////                        painter = image,
////                        contentDescription = "Smart Closet Background",
////                        contentScale = ContentScale.Crop,
////                        modifier = Modifier.matchParentSize()
////                    )
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(Color.White.copy(alpha = 0.65f))
//                            .padding(horizontal = 24.dp, vertical = 32.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text(
//                            "Informations personnelles",
//                            style = MaterialTheme.typography.titleMedium.copy(
//                                fontSize = 18.sp,
//                                fontWeight = FontWeight.Medium,
//                                color = Color.DarkGray
//                            )
//                        )
//
//                        Spacer(modifier = Modifier.height(24.dp))
//
//
//                        OutlinedTextField(
//                            value = gender,
//                            onValueChange = viewModel::onGenderChange,
//                            label = { Text("Genre") },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedTextColor = Color.Black,
//                                focusedBorderColor = MaterialTheme.colorScheme.primary,
//                                cursorColor = MaterialTheme.colorScheme.primary,
//                                unfocusedBorderColor = Color.LightGray
//                            ),
//                            shape = RoundedCornerShape(8.dp)
//                        )
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        OutlinedTextField(
//                            value = job,
//                            onValueChange = viewModel::onJobChange,
//                            label = { Text("Profession") },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedTextColor = Color.Black,
//                                focusedBorderColor = MaterialTheme.colorScheme.primary,
//                                cursorColor = MaterialTheme.colorScheme.primary,
//                                unfocusedBorderColor = Color.LightGray
//                            ),
//                            shape = RoundedCornerShape(8.dp)
//                        )
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        OutlinedTextField(
//                            value = location,
//                            onValueChange = viewModel::onLocationChange,
//                            label = { Text("Lieu de résidence") },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedTextColor = Color.Black,
//                                focusedBorderColor = MaterialTheme.colorScheme.primary,
//                                cursorColor = MaterialTheme.colorScheme.primary,
//                                unfocusedBorderColor = Color.LightGray
//                            ),
//                            shape = RoundedCornerShape(8.dp)
//                        )
//
//                        Spacer(modifier = Modifier.height(12.dp))
//
//                        OutlinedTextField(
//                            value = birthDate,
//                            onValueChange = viewModel::onBirthDateChange,
//                            label = { Text("Date de naissance (YYYY-MM-DD)") },
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedTextColor = Color.Black,
//                                focusedBorderColor = MaterialTheme.colorScheme.primary,
//                                cursorColor = MaterialTheme.colorScheme.primary,
//                                unfocusedBorderColor = Color.LightGray
//                            ),
//                            shape = RoundedCornerShape(8.dp)
//                        )
//
//                        Spacer(modifier = Modifier.height(24.dp))
//
//                        Button(
//                            onClick = {
//                                scope.launch {
//                                    try {
//                                        viewModel.completeProfile()
//                                    } catch (e: Exception) {
//
//                                    }
//                                }
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(48.dp),
//                            colors = ButtonDefaults.buttonColors(
//                                containerColor = MaterialTheme.colorScheme.primary
//                            ),
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Text("Terminer", color = Color.White)
//                        }
//
//                        Spacer(modifier = Modifier.height(16.dp))
//
//                        TextButton(
//                            onClick = { navController.navigate("main/profile") {
//                                popUpTo("main") { inclusive = true }
//                            } },
//                            modifier = Modifier.fillMaxWidth(),
//                        ) {
//                            Text(
//                                text = "Complete Later",
//                                modifier = Modifier.clickable {
//                                    navController.navigate("main/profile") {
//                                        popUpTo("main") { inclusive = true }
//                                    }
//                                },
//                                color = MaterialTheme.colorScheme.primary,
//                                fontWeight = FontWeight.Bold
//                            )
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
import com.example.tesy2.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun CompleteProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val gender by viewModel.gender.collectAsState()
    val job by viewModel.job.collectAsState()
    val location by viewModel.location.collectAsState()
    val birthDate by viewModel.birthDate.collectAsState()
    val name by viewModel.name.collectAsState()
    val completeProfileSuccess by viewModel.completeProfileSuccess.collectAsState()
    val isProfileAlreadyComplete by viewModel.isProfileAlreadyComplete.collectAsState()

    var showDialog by remember { mutableStateOf(false) }

    // Fetch profile on open
    LaunchedEffect(Unit) {
        viewModel.fetchUserProfile()
    }

    // Show popup if profile is already complete
    LaunchedEffect(isProfileAlreadyComplete) {
        if (isProfileAlreadyComplete) {
            showDialog = true
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("Profile Already Complete") },
            text = { Text("Your Profile is already complete.Do you want to edit?") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Yes,Edit")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    navController.navigate("main/profile") {
                        popUpTo("main") { inclusive = true }
                    }
                }) {
                    Text("No,Go Back")
                }
            }
        )
    }

    // After finishing profile update
    LaunchedEffect(completeProfileSuccess) {
        if (completeProfileSuccess) {
            navController.navigate("main/profile") {
                popUpTo("complete_profile") { inclusive = true }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
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

            // Back Button
            IconButton(
                onClick = {
                    navController.navigate("main/profile") {
                        popUpTo("main") { inclusive = true }
                    }
                },
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

            // Title
            Text(
                "Complete Profile",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 26.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(start = 24.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.White.copy(alpha = 0.95f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Personal Information", style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(24.dp))

                    if (isProfileAlreadyComplete) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = viewModel::onNameChange,
                            label = { Text("Full Name") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    OutlinedTextField(
                        value = gender,
                        onValueChange = viewModel::onGenderChange,
                        label = { Text("Genre") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = job,
                        onValueChange = viewModel::onJobChange,
                        label = { Text("Profession") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = location,
                        onValueChange = viewModel::onLocationChange,
                        label = { Text("Address") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = birthDate,
                        onValueChange = viewModel::onBirthDateChange,
                        label = { Text("Date of Birth (YYYY-MM-DD)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    viewModel.completeProfile()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Update ERROR", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Done", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Complete Later",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate("main/profile") {
                                popUpTo("main") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}

