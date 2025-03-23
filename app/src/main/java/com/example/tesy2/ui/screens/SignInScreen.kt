package com.example.tesy2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tesy2.viewmodel.AuthViewModel

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    navController: NavController,

    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val signInSuccess by viewModel.signInSuccess.collectAsState()

    // Afficher le résultat dans un toast
    LaunchedEffect(signInSuccess) {
        if (signInSuccess != null) {
            Toast.makeText(
                context,
                if (signInSuccess == true) {
                    "✅ Connexion réussie"
                } else "❌ Connexion échouée",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Se connecter avec son compte", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black)
        )

        OutlinedTextField(
            value = password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Mot de passe") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black)
        )

        Button(
            onClick = { viewModel.signIn() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Connexion")
        }
        Text(
            text = "ou",
            modifier = Modifier
                .padding(top = 12.dp)
                .align(Alignment.CenterHorizontally)
        )

        TextButton(
            onClick = {
                navController.navigate("sign_up")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Je n’ai pas de compte")
        }

    }
}
