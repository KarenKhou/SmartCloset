package com.example.tesy2.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

import com.example.tesy2.R
import com.example.tesy2.data.models.ClothingItem

@Composable
fun ClothingCard(item: ClothingItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = item.image_url,
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
                    .padding(end = 12.dp)
            )




            Column {
                Text(text = item.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Category: ${item.category}")
                Text(text = "Color: ${item.color}")
                Text(text = "Style: ${item.style}")
            }
        }
    }
}
