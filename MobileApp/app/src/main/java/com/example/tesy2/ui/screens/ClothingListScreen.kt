package com.example.tesy2.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.tesy2.ui.composable.ClothingCard
import com.example.tesy2.viewmodel.ClothingViewModel
import com.example.tesy2.ui.theme.pinkColor
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight


@Composable
fun ClothingScreen(
    modifier: Modifier = Modifier,
    viewModel: ClothingViewModel = viewModel() ,
) {

    val clothingList = viewModel.clothingItems.collectAsState().value
    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }

    // Filter clothing items based on the search query
    val filteredClothingList = clothingList.filter { item ->
        item.name?.contains(searchQuery, ignoreCase = true) == true ||
                item.category?.contains(searchQuery, ignoreCase = true) == true ||
                item.color?.contains(searchQuery, ignoreCase = true) == true ||
                item.style?.contains(searchQuery, ignoreCase = true) == true
    }

    // Load the clothes (for example from closet 1)
    LaunchedEffect(Unit) {
        viewModel.loadClothes(closetId = 1)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { query -> searchQuery = query },
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .padding(vertical = if (isSearchFocused) 8.dp else 4.dp)
                .shadow(
                    elevation = if (isSearchFocused) 8.dp else 2.dp,
                    shape = RoundedCornerShape(16.dp)
                )
                .onFocusChanged { focusState ->
                    isSearchFocused = focusState.isFocused
                },
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = pinkColor,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            textStyle = TextStyle(
                fontSize = if (isSearchFocused) 30.sp else 16.sp,
                color = Color.White
            ),
            placeholder = {
                Text(
                    "Search by name, category, color, or style",
                    color = if (isSearchFocused) Color.White.copy(alpha = 0.6f) else Color.Gray
                )
            },
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = if (isSearchFocused) Color.White else Color.Gray
                )
            },
            singleLine = true
        )



        Spacer(modifier = Modifier.height(24.dp))


        if (filteredClothingList.isEmpty() && searchQuery.isNotEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No items found matching \"$searchQuery\"")
            }
        } else {
            Text(
                text = if (searchQuery.isEmpty()) "Your Closet" else "Search Results",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                items(filteredClothingList) { item ->
//                    ClothingCard(item)
//                }
//            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal=2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(filteredClothingList) { item ->
                    ClothingCard(item)
                }
            }


        }
    }
}