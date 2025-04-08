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




    val context = LocalContext.current

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val signInSuccess by viewModel.signInSuccess.collectAsState()


    LaunchedEffect(signInSuccess) {
        if (signInSuccess != null) {
            if (signInSuccess == true) {
                Toast.makeText(context, "✅ Connexion réussie", Toast.LENGTH_LONG).show()
                navController.navigate("main") {
                    popUpTo("sign_in") { inclusive = true }
                }
            } else {
                Toast.makeText(context, "❌ Connexion échouée", Toast.LENGTH_LONG).show()
            }
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Se connecter",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = pinkColor
                )
            )

            OutlinedTextField(
                value = email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedBorderColor = pinkColor,
                    cursorColor = pinkColor
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Mot de passe") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedBorderColor = pinkColor,
                    cursorColor = pinkColor
                )
            )

            Button(
                onClick = { viewModel.signIn() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = pinkColor)
            ) {
                Text("Connexion", color = Color.White)
            }

            Text("ou", color = Color.Gray)

            TextButton(
                onClick = { navController.navigate("sign_up") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Je n’ai pas de compte", color = pinkColor)
            }
        }
    }
}
