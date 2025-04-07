package com.example.tesy2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

import com.example.tesy2.data.models.UsagePreview

import com.example.tesy2.data.models.UsageWithItem
import com.example.tesy2.data.supabase.supabase

import com.example.tesy2.viewmodel.getRecentUsage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import com.example.tesy2.data.supabase.supabase
import com.example.tesy2.viewmodel.getRecentUsage
import com.example.tesy2.data.models.ClothingItemPreview
import io.github.jan.supabase.auth.auth


//@Composable
//fun RecentUsageScreen(recentItems: List<UsagePreview>) {
//    Column(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)) {
//    Text("Recently used items :")
//    LazyColumn(modifier = Modifier
//        .fillMaxSize()
//        .padding(16.dp)) {
//
//        items(recentItems) { usage ->
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 8.dp),
//                shape = RoundedCornerShape(16.dp)
//            ) {
//                Column(modifier = Modifier.padding(16.dp)) {
//                    Text(
//                        text = "🗓️ Porté le ${usage.worn_date}",
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        AsyncImage(
//                            model = usage.clothingitem?.image_url,
//                            contentDescription = usage.clothingitem?.name,
//                            modifier = Modifier
//                                .size(64.dp)
//                                .clip(RoundedCornerShape(10.dp))
//                        )
//                        Spacer(modifier = Modifier.width(16.dp))
//                        Text(
//                            text = usage.clothingitem?.name ?: "Nom inconnu",
//                            style = MaterialTheme.typography.titleMedium
//                        )
//
////@Composable
////fun RecentUsageScreen(recentItems: List<UsageWithItem>) {
////    LazyColumn(modifier = Modifier
////        .fillMaxSize()
////        .padding(16.dp)) {
////        items(recentItems) { usage ->
////            Card(modifier = Modifier
////                .fillMaxWidth()
////                .padding(vertical = 8.dp)) {
////                Row(modifier = Modifier.padding(16.dp)) {
//////                    AsyncImage(
//////                        model = usage.clothingitem.image_url,
//////                        contentDescription = null,
//////                        modifier = Modifier.size(64.dp)
//////                    )
////                    Spacer(modifier = Modifier.width(16.dp))
////                    Column {
////                        //Text(text = usage.clothingitem.name, style = MaterialTheme.typography.titleMedium)
////                        Text(text = "Porté le ${usage.worn_date}", style = MaterialTheme.typography.bodySmall)
////>>>>>>> 5a5de02 (data analysis debut)
////                    }
////                }
////            }
////        }
////<<<<<<< HEAD
////
////    }
////}
////}
//@Composable
//fun RecentUsageScreenWrapper() {
//    var recentItems by remember { mutableStateOf<List<UsagePreview>>(emptyList()) }
//    var isLoading by remember { mutableStateOf(true) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//    val userId = supabase.auth.currentUserOrNull()?.id
//
//    LaunchedEffect(userId) {
//        try {
//            recentItems = getRecentUsage(userId!!)
//        } catch (e: Exception) {
//            errorMessage = "Erreur Supabase : ${e.message}"
//
//        }
//    }
//
//    if (isLoading) {
//        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            CircularProgressIndicator()
//        }
//    } else {
//        RecentUsageScreen(recentItems)
//    }
//
//
//
//
//
////
////@Composable
////fun RecentUsageScreenWrapper(userId: String) {
////    var recentItems by remember { mutableStateOf<List<UsageWithItem>>(emptyList()) }
////    var isLoading by remember { mutableStateOf(true) }
////    print("wrapper")
////    LaunchedEffect(userId) {
////        try {
////            recentItems = getRecentUsage(supabase, userId)
////            print("fetching usage")
////        } catch (e: Exception) {
////            println("Erreur Supabase : ${e.message}")
////>>>>>>> 5a5de02 (data analysis debut)
////        } finally {
////            isLoading = false
////        }
////    }
////
////<<<<<<< HEAD
////    when {
////        isLoading -> {
////            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
////                CircularProgressIndicator()
////            }
////        }
////        errorMessage != null -> {
////            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
////                Text(
////                    text = errorMessage ?: "Une erreur inconnue est survenue",
////                    color = MaterialTheme.colorScheme.error
////                )
////            }
////        }
////        else -> {
////            RecentUsageScreen(recentItems)
////        }
////    }
////}
////
//
////@Composable
////fun RecentUsageScreen(recentItems: List<UsageWithItem>) {
////    LazyColumn(modifier = Modifier
////        .fillMaxSize()
////        .padding(16.dp)) {
////        items(recentItems) { usage ->
////            Card(modifier = Modifier
////                .fillMaxWidth()
////                .padding(vertical = 8.dp)) {
////                Row(modifier = Modifier.padding(16.dp)) {
//////                    AsyncImage(
//////                        model = usage.clothingitem.image_url,
//////                        contentDescription = null,
//////                        modifier = Modifier.size(64.dp)
//////                    )
////                    Spacer(modifier = Modifier.width(16.dp))
////                    Column {
////                        //Text(text = usage.clothingitem.name, style = MaterialTheme.typography.titleMedium)
////                        Text(text = "Porté le ${usage.worn_date}", style = MaterialTheme.typography.bodySmall)
////                    }
////                }
////            }
////        }
////    }
////}
////
////@Composable
////fun RecentUsageScreenWrapper(userId: String) {
////    var recentItems by remember { mutableStateOf<List<UsageWithItem>>(emptyList()) }
////    var isLoading by remember { mutableStateOf(true) }
////    print("wrapper")
////    LaunchedEffect(userId) {
////        try {
////            recentItems = getRecentUsage(supabase, userId)
////            print("fetching usage")
////        } catch (e: Exception) {
////            println("Erreur Supabase : ${e.message}")
////        } finally {
////            isLoading = false
////        }
////    }
////
////    if (isLoading) {
////        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
////            CircularProgressIndicator()
////        }
////    } else {
////        RecentUsageScreen(recentItems)
////    }
////}
////=======
////
////}
////>>>>>>> 5a5de02 (data analysis debut)


@Composable
fun RecentUsageScreen(recentItems: List<UsagePreview>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Recently used items :")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(recentItems) { usage ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "🗓️ Porté le ${usage.worn_date}",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = usage.clothingitem?.image_url,
                                contentDescription = usage.clothingitem?.name,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = usage.clothingitem?.name ?: "Nom inconnu",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecentUsageScreenWrapper() {
    var recentItems by remember { mutableStateOf<List<UsagePreview>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val userId = supabase.auth.currentUserOrNull()?.id

    LaunchedEffect(userId) {
        try {
            recentItems = getRecentUsage(userId!!)
        } catch (e: Exception) {
            errorMessage = "Erreur Supabase : ${e.message}"
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        RecentUsageScreen(recentItems)
    }
}
