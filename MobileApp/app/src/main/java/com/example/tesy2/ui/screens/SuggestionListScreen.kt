package com.example.tesy2.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tesy2.ui.composable.ClothingCard
import com.example.tesy2.viewmodel.ClothingViewModel

@Composable
fun SuggScreen(
    modifier: Modifier = Modifier,
    viewModel: ClothingViewModel = viewModel()
) {
    val suggList = viewModel.suggestions.collectAsState().value


    LaunchedEffect(Unit) {
        viewModel.loadSuggestion(closetId = 1, recommendationId = 3)
    }

    LazyColumn(modifier = modifier.padding(16.dp)) {
        items(suggList) { item ->
            ClothingCard(item)

        }
    }
}