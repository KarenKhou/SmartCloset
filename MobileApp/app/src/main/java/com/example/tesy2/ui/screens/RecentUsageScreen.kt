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
import com.example.tesy2.data.models.UsageWithItem
import com.example.tesy2.data.supabase.supabase

import com.example.tesy2.viewmodel.getRecentUsage

@Composable
fun RecentUsageScreen(recentItems: List<UsageWithItem>) {
    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        items(recentItems) { usage ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)) {
                Row(modifier = Modifier.padding(16.dp)) {
//                    AsyncImage(
//                        model = usage.clothingitem.image_url,
//                        contentDescription = null,
//                        modifier = Modifier.size(64.dp)
//                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        //Text(text = usage.clothingitem.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Porté le ${usage.worn_date}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@Composable
fun RecentUsageScreenWrapper(userId: String) {
    var recentItems by remember { mutableStateOf<List<UsageWithItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    print("wrapper")
    LaunchedEffect(userId) {
        try {
            recentItems = getRecentUsage(supabase, userId)
            print("fetching usage")
        } catch (e: Exception) {
            println("Erreur Supabase : ${e.message}")
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
