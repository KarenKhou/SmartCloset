package com.example.tesy2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.tesy2.data.supabase.supabase
import com.example.tesy2.viewmodel.AuthViewModel
import com.example.tesy2.ui.theme.pinkColor
import com.example.tesy2.ui.theme.lightPink
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.delay


@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current

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

            Button(
                onClick = {
                    println("📤 Click du bouton")
                    viewModel.signUp()
                },
                modifier = Modifier.fillMaxWidth(),
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
