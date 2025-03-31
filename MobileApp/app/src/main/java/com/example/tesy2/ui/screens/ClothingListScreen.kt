package com.example.tesy2.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tesy2.ui.components.ClothingCard
import com.example.tesy2.viewmodel.AuthViewModel
import com.example.tesy2.viewmodel.ClothingViewModel
import androidx.compose.foundation.lazy.items


@Composable
fun ClothingScreen(
    modifier: Modifier = Modifier,
    viewModel: ClothingViewModel = viewModel()
) {
    val clothingList = viewModel.clothingItems.collectAsState().value

    // Charger les vêtements (par exemple du closet 1)
    LaunchedEffect(Unit) {
        viewModel.loadClothes(closetId = 1)
    }

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(clothingList) { item ->
            ClothingCard(item)
        }
    }
}
