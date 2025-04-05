package com.example.tesy2.ui.screens

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tesy2.viewmodel.ClothingViewModel



@Composable
fun EditClothingScreen(
    itemId: String,
    navController: NavController,
    viewModel: ClothingViewModel
) {
    val item by viewModel.selectedItem


    LaunchedEffect(Unit) {
        viewModel.fetchClothingItemByIdFromSupabase(itemId)
    }

//    // 1. Charger l'item depuis le ViewModel
//    val item by viewModel.getClothingItemById(itemId).collectAsState(initial = null)

    // 2. États locaux pour l'édition
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var style by remember { mutableStateOf("") }

    // 3. Pré-remplir les champs si l’item est chargé
    LaunchedEffect(item) {
        item?.let {
            name = it.name
            category = it.category ?: ""
            color = it.color ?: ""
            style = it.style ?: ""
        }
    }

    // 4. UI de l'écran
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text("Modifier le vêtement", style = MaterialTheme.typography.headlineMedium)


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Catégorie") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = color,
            onValueChange = { color = it },
            label = { Text("Couleur") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = style,
            onValueChange = { style = it },
            label = { Text("Style") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.updateClothingItem(
                    itemId = itemId,
                    name = name,
                    category = category,
                    color = color,
                    style = style
                )
                navController.popBackStack() // retour à l'écran précédent
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enregistrer")
        }
    }
}
