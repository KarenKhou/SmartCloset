package com.example.tesy2.ui.screens

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
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
import com.example.tesy2.MainActivity
import com.example.tesy2.R
import com.example.tesy2.data.supabase.supabase
import com.example.tesy2.viewmodel.AuthViewModel
import com.example.tesy2.ui.theme.AppThemeColor
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.tesy2.ui.theme.surfaceColor
import com.example.tesy2.ui.theme.darkGray
import com.example.tesy2.ui.theme.mediumGray
import com.example.tesy2.ui.theme.lightGray
import com.example.tesy2.ui.theme.subtleGray
import com.example.tesy2.ui.theme.accentGray


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val defaultWidth = 120.dp
    val defaultHeight = 45.dp
    val selectedWidth = 150.dp
    val selectedHeight = 60.dp

    val context = LocalContext.current
    var selectedTheme by remember { mutableStateOf<AppThemeColor?>(null) }

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val name by viewModel.name.collectAsState()
    val signUpSuccess by viewModel.signUpSuccess.collectAsState()

    // Check if all fields are filled
    val isFormFilled = email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && selectedTheme != null
    val buttonColor = if (isFormFilled) darkGray else lightGray

    LaunchedEffect(Unit) {
        try {
            supabase.auth.signOut()
            println("✅ Déconnecté avec succès")
        } catch (e: Exception) {
            println("❌ Erreur logout: ${e.message}")
        }
    }

    LaunchedEffect(signUpSuccess) {
        if (signUpSuccess) {
            delay(200L)
            navController.navigate("sign_in")
            Toast.makeText(context, "✅ Inscription réussie !", Toast.LENGTH_LONG).show()

            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
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
                    "Welcome!",
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
//                            .background(Color.White.copy(alpha = 0.8f))
                            .background(Color.White)

                            .padding(horizontal = 24.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Create an account",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = darkGray
                            )
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
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = viewModel::onPasswordChange,
                            label = { Text("Password", color = mediumGray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = darkGray,
                                focusedBorderColor = darkGray,
                                cursorColor = darkGray,
                                unfocusedBorderColor = lightGray,
                                unfocusedLabelColor = mediumGray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = viewModel::onNameChange,
                            label = { Text("Name", color = mediumGray) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = darkGray,
                                focusedBorderColor = darkGray,
                                cursorColor = darkGray,
                                unfocusedBorderColor = lightGray,
                                unfocusedLabelColor = mediumGray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            "Choose  A Theme Color :",
                            color = mediumGray,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf(AppThemeColor.Pink, AppThemeColor.Blue).forEach { theme ->
                                val isSelected = theme == selectedTheme

                                val animatedWidth by animateDpAsState(
                                    targetValue = if (isSelected) selectedWidth else defaultWidth,
                                    label = "buttonWidth"
                                )

                                val animatedHeight by animateDpAsState(
                                    targetValue = if (isSelected) selectedHeight else defaultHeight,
                                    label = "buttonHeight"
                                )

                                Button(
                                    onClick = { selectedTheme = theme },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) theme.primary else theme.primary.copy(alpha = 0.6f)),
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(animatedWidth)
                                        .height(animatedHeight),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (theme.name == "pink") "Pink" else "Blue",
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                println("📤 Click du bouton")

                                if (selectedTheme == null) {
                                    Toast.makeText(context, "❗Choisis une couleur avant de t'inscrire", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.onthemeChange(selectedTheme!!.name)

                                viewModel.signUp()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            enabled = true,
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Sign Up", color = Color.White)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Already Have an Account?",
                                color = mediumGray,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            TextButton(
                                onClick = { navController.navigate("sign_in") },
                                contentPadding = PaddingValues(start = 4.dp)
                            ) {
                                Text(
                                    "Sign In",
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