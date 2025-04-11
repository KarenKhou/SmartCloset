package com.example.tesy2.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.tesy2.ui.theme.containerPink
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.tesy2.data.models.ClothingItem
import com.example.tesy2.ui.theme.pinkColor


@Composable
fun ClothingCard(
    item: ClothingItem,
    onEditClick: (ClothingItem) -> Unit // 🔧 Ajout du callback
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerPink
        )
    ) {
        Column(modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            AsyncImage(
                model = item.image_url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = pinkColor,
                    maxLines = 1
                )

                // ✅ Bouton "Edit"
                Button(
                    onClick = { onEditClick(item) },
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .align(Alignment.End)
                ) {
                    Text(text = "Edit")
                }
            }
        }
    }
}

//
//@Composable
//fun ClothingCard(item: ClothingItem) {
//    Card(
//        modifier = Modifier
//            .padding(8.dp)
//            .fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(6.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = containerPink
//        )
//    ) {
//        Column(
//            modifier = Modifier
//                .padding(12.dp)
//                .fillMaxWidth(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            AsyncImage(
//                model = item.image_url,
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .aspectRatio(1f)
//                    .clip(RoundedCornerShape(12.dp))
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.spacedBy(2.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = item.name,
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold,
//                    color = pinkColor,
//                    maxLines = 1
//                )
////                Text(
////                    text = "Category: ${item.category ?: "-"}",
////                    style = MaterialTheme.typography.bodySmall,
////                    maxLines = 1
////                )
////                Text(
////                    text = "Color: ${item.color ?: "-"}",
////                    style = MaterialTheme.typography.bodySmall,
////                    maxLines = 1
////                )
////                Text(
////                    text = "Style: ${item.style ?: "-"}",
////                    style = MaterialTheme.typography.bodySmall,
////                    maxLines = 1
////                )
//            }
//        }
//    }
//}