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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tesy2.data.supabase.supabase
import com.example.tesy2.ui.theme.AppThemeColor
import com.example.tesy2.ui.theme.LocalAppTheme
import com.example.tesy2.viewmodel.AuthViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.result.PostgrestResult
import androidx.compose.foundation.Image
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.tesy2.R
import com.example.tesy2.data.models.AppUser
import com.example.tesy2.ui.theme.surfaceColor
import com.example.tesy2.ui.theme.darkGray
import com.example.tesy2.ui.theme.mediumGray
import com.example.tesy2.ui.theme.lightGray
import com.example.tesy2.ui.theme.subtleGray
import com.example.tesy2.ui.theme.accentGray



@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        try {
            supabase.auth.signOut()
            println("✅ Déconnecté avec succès")
        } catch (e: Exception) {
            println("❌ Erreur logout: ${e.message}")
        }
    }

    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val signInSuccess by viewModel.signInSuccess.collectAsState()
    val themeState = LocalAppTheme.current

    // Check if both fields are filled
    val isFormFilled = email.isNotEmpty() && password.isNotEmpty()
    val buttonColor = if (isFormFilled) darkGray else lightGray

    LaunchedEffect(signInSuccess) {
        if (signInSuccess != null) {
            if (signInSuccess == true) {
                Toast.makeText(context, "✅ Connection successful", Toast.LENGTH_LONG).show()

                val user = supabase.auth.currentUserOrNull()
                val userId = user?.id

                if (userId != null) {
                    try {
                        val response = supabase
                            .from("User")
                            .select {
                                filter { eq("user_id", userId) }
                            }
                            .decodeSingle<AppUser>()

                        val themeFromDb = response.theme
                        println("🎨 Theme from DB: $themeFromDb")

                        themeState.value = AppThemeColor.fromName(themeFromDb)
                        println("🌈 Applied theme: ${themeState.value.name}")
                    } catch (e: Exception) {
                        println("❌ Failed to fetch theme: ${e.message}")
                    }
                } else {
                    println("⚠️ userId is null")
                }

                navController.navigate("main") {
                    popUpTo("sign_in") { inclusive = true }
                }
            } else {
                Toast.makeText(context, "❌ Connection failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(accentGray)
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
                    "Hello!",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    "Welcome to SmartCloset",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        color = subtleGray
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            val image = painterResource(id = R.drawable.bgkaren)

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
                        .padding(top = 10.dp)
                ) {
                    Image(
                        painter = image,
                        contentDescription = "Smart Closet Background",
                        contentScale = ContentScale.Crop,
                                modifier = Modifier
                            .matchParentSize()
                            .offset(y = (-100).dp)
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = 0.8f))
                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp, vertical = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Connect Now",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = darkGray
                                ),
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            OutlinedTextField(
                                value = email,
                                onValueChange = viewModel::onEmailChange,
                                label = { Text("Email", color = mediumGray) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = darkGray,
                                    focusedBorderColor = darkGray,
                                    cursorColor = darkGray,
                                    unfocusedBorderColor = lightGray,
                                    unfocusedLabelColor = mediumGray
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Email,
                                        contentDescription = "Email",
                                        tint = mediumGray
                                    )
                                },
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            OutlinedTextField(
                                value = password,
                                onValueChange = viewModel::onPasswordChange,
                                label = { Text("Password", color = mediumGray) },
                                modifier = Modifier.fillMaxWidth(),
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = darkGray,
                                    focusedBorderColor = darkGray,
                                    cursorColor = darkGray,
                                    unfocusedBorderColor = lightGray,
                                    unfocusedLabelColor = mediumGray
                                ),
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Lock,
                                        contentDescription = "Password",
                                        tint = mediumGray
                                    )
                                },
                                trailingIcon = {
                                    val visibilityIcon = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                                    val description = if (passwordVisible) "Hide password" else "Show password"

                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = visibilityIcon,
                                            contentDescription = description,
                                            tint = mediumGray
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { viewModel.signIn() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                                shape = RoundedCornerShape(8.dp),
                                enabled = true
                            ) {
                                Text("Sign In", color = Color.White)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Don't have an account?",
                                    color = mediumGray,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                TextButton(
                                    onClick = { navController.navigate("sign_up") },
                                    contentPadding = PaddingValues(start = 4.dp)
                                ) {
                                    Text(
                                        "Sign up",
                                        color = darkGray,
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
    }
}