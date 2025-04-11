package com.example.tesy2.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tesy2.R
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
    val completeProfileSuccess by viewModel.completeProfileSuccess.collectAsState()

    LaunchedEffect(completeProfileSuccess) {
        if (completeProfileSuccess) {
            navController.navigate("home") {
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 64.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Compléter le profil",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    "Quelques informations supplémentaires",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            val image = painterResource(id = R.drawable.auth_background1)

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Image(
                        painter = image,
                        contentDescription = "Smart Closet Background",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.65f))
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Informations personnelles",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))


                        OutlinedTextField(
                            value = gender,
                            onValueChange = viewModel::onGenderChange,
                            label = { Text("Genre") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = job,
                            onValueChange = viewModel::onJobChange,
                            label = { Text("Profession") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = location,
                            onValueChange = viewModel::onLocationChange,
                            label = { Text("Lieu de résidence") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = birthDate,
                            onValueChange = viewModel::onBirthDateChange,
                            label = { Text("Date de naissance (YYYY-MM-DD)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                cursorColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = Color.LightGray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        viewModel.completeProfile()
                                    } catch (e: Exception) {

                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Terminer", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(
                            onClick = { navController.navigate("home") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Compléter plus tard",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}