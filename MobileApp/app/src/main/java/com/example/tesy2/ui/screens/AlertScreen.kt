package com.example.tesy2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.viewmodel.AlertViewModel

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

@Composable
fun AlertScreen(
    navController: NavController,
    viewModel: AlertViewModel = viewModel()
) {
    val alertText by viewModel.alertText.collectAsState()

    // Sur réception de "ALERT", on navigue
    LaunchedEffect(alertText) {
        if (alertText == "ALERT") {
            navController.navigate("removeOutfit")
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = alertText.ifEmpty { "Attente de données..." },
            style = MaterialTheme.typography.headlineMedium
        )
    }
}
