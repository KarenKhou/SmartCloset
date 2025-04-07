package com.example.tesy2.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
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
import com.example.tesy2.data.supabase.supabase
import com.example.tesy2.ui.composable.UserPreferences
import com.example.tesy2.ui.theme.AppThemeColor
import com.example.tesy2.ui.theme.LocalAppTheme
import com.example.tesy2.viewmodel.AuthViewModel
import io.github.jan.supabase.auth.auth

// Define brown theme colors to match the image

val surfaceColor = Color.White

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    // Keep your existing functionality
    LaunchedEffect(Unit) {
        try {
            supabase.auth.signOut()
            println("✅ Déconnecté avec succès")
        } catch (e: Exception) {
            println("❌ Erreur logout: ${e.message}")
        }
    }

    val context = LocalContext.current
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val signInSuccess by viewModel.signInSuccess.collectAsState()
    val themeState = LocalAppTheme.current
    LaunchedEffect(signInSuccess) {
        if (signInSuccess != null) {
            if (signInSuccess == true) {
                Toast.makeText(context, "✅ Connexion réussie", Toast.LENGTH_LONG).show()
//                val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
//                val savedTheme = prefs.getString("userTheme", "pink") ?: "pink"
//                themeState.value = AppThemeColor.fromName(savedTheme)
                val savedTheme = UserPreferences.getUserInfo(context)["theme"] ?: "pink"
                themeState.value = AppThemeColor.fromName(savedTheme)
                navController.navigate("main") {
                    popUpTo("sign_in") { inclusive = true }
                }

            } else {
                Toast.makeText(context, "❌ Connexion échouée", Toast.LENGTH_LONG).show()
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Brown background for the entire screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        )

        // Content column
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            // Header in brown area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 64.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    "Bonjour!",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    "Bienvenu à SmartCloset",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = surfaceColor,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Se connecter",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Email field
                    OutlinedTextField(
                        value = email,
                        onValueChange = viewModel::onEmailChange,
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = "Email",
                                tint = Color.Gray
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password field
                    OutlinedTextField(
                        value = password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            cursorColor =MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.LightGray
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = "Password",
                                tint = Color.Gray
                            )
                        },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )


                    Spacer(modifier = Modifier.height(16.dp))

                    // Login button
                    Button(
                        onClick = { viewModel.signIn() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Connexion", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sign up text
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Je n’ai pas de compte?",
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        TextButton(
                            onClick = { navController.navigate("sign_up") },
                            contentPadding = PaddingValues(start = 4.dp)
                        ) {
                            Text(
                                "S'enregistrer",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}