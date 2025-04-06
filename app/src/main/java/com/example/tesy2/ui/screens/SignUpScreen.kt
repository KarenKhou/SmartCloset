package com.example.tesy2.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tesy2.data.supabase.supabase
import com.example.tesy2.viewmodel.AuthViewModel
import com.example.tesy2.ui.theme.pinkColor
import com.example.tesy2.ui.theme.lightPink
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay
import com.example.tesy2.ui.theme.AppThemeColor



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
    val gender by viewModel.gender.collectAsState()
    val job by viewModel.job.collectAsState()
    val location by viewModel.location.collectAsState()
    val birthDate by viewModel.birthDate.collectAsState()
    val signUpSuccess by viewModel.signUpSuccess.collectAsState()

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
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(lightPink),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .background(Color.White, shape = RoundedCornerShape(24.dp))
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Créer un compte",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = pinkColor
                )
            )

            OutlinedTextField(value = email, onValueChange = viewModel::onEmailChange, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, focusedBorderColor = pinkColor, cursorColor = pinkColor))
            OutlinedTextField(value = password, onValueChange = viewModel::onPasswordChange, label = { Text("Mot de passe") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, focusedBorderColor = pinkColor, cursorColor = pinkColor))
            OutlinedTextField(value = name, onValueChange = viewModel::onNameChange, label = { Text("Nom") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, focusedBorderColor = pinkColor, cursorColor = pinkColor))
            OutlinedTextField(value = gender, onValueChange = viewModel::onGenderChange, label = { Text("Genre") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, focusedBorderColor = pinkColor, cursorColor = pinkColor))
            OutlinedTextField(value = job, onValueChange = viewModel::onJobChange, label = { Text("Profession") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, focusedBorderColor = pinkColor, cursorColor = pinkColor))
            OutlinedTextField(value = location, onValueChange = viewModel::onLocationChange, label = { Text("Lieu de résidence") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, focusedBorderColor = pinkColor, cursorColor = pinkColor))
            OutlinedTextField(value = birthDate, onValueChange = viewModel::onBirthDateChange, label = { Text("Date de naissance (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, focusedBorderColor = pinkColor, cursorColor = pinkColor))
            val defaultWidth = 120.dp
            val defaultHeight = 45.dp
            val selectedWidth = 150.dp
            val selectedHeight = 60.dp

            Text("Choisis ta couleur de thème :", color = MaterialTheme.colorScheme.primary)

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(AppThemeColor.Pink, AppThemeColor.Blue).forEach { theme ->
                    val isSelected = theme == selectedTheme

                    val animatedWidth by animateDpAsState(
                        targetValue = if (isSelected) 150.dp else 120.dp,
                        label = "buttonWidth"
                    )

                    val animatedHeight by animateDpAsState(
                        targetValue = if (isSelected) 60.dp else 45.dp,
                        label = "buttonHeight"
                    )


                    Button(
                        onClick = { selectedTheme = theme },
                        colors = ButtonDefaults.buttonColors(containerColor = theme.primary),
                        modifier = Modifier
                            .padding(4.dp)
                            .width(animatedWidth)
                            .height(animatedHeight)
                    ) {
                        Text(
                            text = if (theme.name == "pink") "Rose" else "Bleu",
                            color = Color.White
                        )
                    }
                }
            }




//            Button(
//                onClick = {
//                    println("📤 Click du bouton")
//                    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
//                    prefs.edit().putString("userTheme", selectedTheme.name).apply()
//                    viewModel.signUp()
//                },
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(containerColor = pinkColor)
//            ) {
//                Text("S'inscrire", color = Color.White)
//            }
            Button(
                onClick = {
                    println("📤 Click du bouton")

                    if (selectedTheme == null) {
                        Toast.makeText(context, "❗Choisis une couleur avant de t'inscrire", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
                    prefs.edit().putString("userTheme", selectedTheme!!.name).apply()
                    viewModel.signUp()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedTheme != null, // 👈 optional
                colors = ButtonDefaults.buttonColors(containerColor = pinkColor)
            ) {
                Text("S'inscrire", color = Color.White)
            }

            Text("ou", color = Color.Gray)

            TextButton(
                onClick = { navController.navigate("sign_in") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("J'ai déjà un compte", color = pinkColor)
            }
        }
    }
}
