package com.example.tesy2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tesy2.data.models.Closet
import com.example.tesy2.viewmodel.AddClosetViewModel
import com.example.tesy2.viewmodel.ClothingViewModel

@Composable
fun AddClosetScreen(
    viewModel: AddClosetViewModel = viewModel(),
    navController: NavController
) {
    var closetName by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var capacity by remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Créer un nouveau closet", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = closetName,
            onValueChange = { closetName = it },
            label = { Text("Nom du closet") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = size,
            onValueChange = { size = it },
            label = { Text("Taille (ex: M, L, XL...)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = capacity,
            onValueChange = { capacity = it },
            label = { Text("Capacité maximale (optionnel)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val userId = viewModel.getCurrentUserId()
                if (userId != null && closetName.isNotBlank()) {
                    val newCloset = Closet(
                        closet_id = null,
                        closet_name = closetName,
                        size = size.takeIf { it.isNotBlank() },
                        capacity = capacity.toIntOrNull(),
                        user_id = userId
                    )
                    viewModel.createNewCloset(newCloset)
                    Toast.makeText(context, "Closet ajouté !", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                } else {
                    Toast.makeText(context, "Remplis au moins le nom du closet", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ajouter le closet")
        }
    }
}
