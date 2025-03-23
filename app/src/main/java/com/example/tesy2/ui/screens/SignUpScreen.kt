package com.example.tesy2.ui.screens


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.viewmodel.AuthViewModel

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel()
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

    // Affiche un Toast si inscription réussie
    LaunchedEffect(signUpSuccess) {
        if (signUpSuccess) {
            Toast.makeText(context, "✅ Inscription réussie !", Toast.LENGTH_LONG).show()
            // Tu peux rediriger ici si tu veux
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(value = email, onValueChange = viewModel::onEmailChange, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = password, onValueChange = viewModel::onPasswordChange, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = name, onValueChange = viewModel::onNameChange, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = gender, onValueChange = viewModel::onGenderChange, label = { Text("Gender") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = job, onValueChange = viewModel::onJobChange, label = { Text("Job") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = location, onValueChange = viewModel::onLocationChange, label = { Text("Home Location") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = birthDate, onValueChange = viewModel::onBirthDateChange, label = { Text("Birth Date (YYYY-MM-DD)") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = { println("📤 Click du boutton")
                        viewModel.signUp() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up")
        }
    }
}
