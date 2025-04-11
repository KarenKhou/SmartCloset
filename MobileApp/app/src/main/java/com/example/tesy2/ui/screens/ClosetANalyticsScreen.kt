package com.example.tesy2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tesy2.data.models.UsagePreview
import com.example.tesy2.data.supabase.supabase

import com.example.tesy2.viewmodel.getRecentUsage


import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.viewmodel.WornCalendarViewModel
import io.github.jan.supabase.auth.auth
import java.time.YearMonth

@Composable
fun ClosetAnalyticsScreen(viewModel: WornCalendarViewModel = viewModel()) {
    var selectedDate by remember { mutableStateOf(java.time.LocalDate.now()) }
    var recentItems by remember { mutableStateOf<List<UsagePreview>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val userId = supabase.auth.currentUserOrNull()?.id
    val daysInMonth = YearMonth.from(selectedDate).lengthOfMonth()
    val firstDayOfWeek = YearMonth.from(selectedDate).atDay(1).dayOfWeek.value % 7

    LaunchedEffect(userId) {
        try {
            recentItems = getRecentUsage(userId!!)
        } catch (e: Exception) {
            errorMessage = "Erreur Supabase : ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // 👈 Important
            .padding(16.dp)
    ) {
        // Recently used items
        Text("Recently used items :", style = MaterialTheme.typography.titleMedium)

        when {
            isLoading -> {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            errorMessage != null -> {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        text = errorMessage ?: "Une erreur inconnue est survenue",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> {
                recentItems.forEach { usage ->
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

        Spacer(Modifier.height(24.dp))
        Text(
            text = "✨ Vêtements portés en ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ✨",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Magenta,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Calendar grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // ou ajustable selon ton besoin
                .background(Color(0xFFFFE0F0))
                .padding(bottom = 16.dp)
        ) {
            items(firstDayOfWeek) {
                Box(modifier = Modifier.size(40.dp)) { }
            }

            items(daysInMonth) { index ->
                val day = index + 1
                val date = selectedDate.withDayOfMonth(day)
                Button(
                    onClick = {
                        selectedDate = date
                        viewModel.loadItemsForDate(date.toString(),userId!!)
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA6C9))
                ) {
                    Text(
                        text = "$day",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                }
            }
        }

        // Items for selected date
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Porté le $selectedDate",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        viewModel.wornItemsForDate.forEach { item ->
            Text("🎀 ${item.clothingitem.name}")
        }


        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "💖 Tes stats fashion 💖",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFFFF69B4), // hot pink
            modifier = Modifier.padding(bottom = 16.dp)
        )

// Stat 1 – Vêtement le plus porté
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE0F0)),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(
                text = "👑 Vêtement favori : Robe rose à paillettes (7 fois)",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

// Stat 2 – Couleur ou style dominant
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF0FA)),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(
                text = "🎨 Couleur la plus portée : Rose\n🧸 Style dominant : Kawaii",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

// Stat 3 – Habits oubliés
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEDF7)),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("❌ Vêtements oubliés :", style = MaterialTheme.typography.bodyLarge)
                Text("• Jean flare 😢")
                Text("• Pull fluffy blanc 😥")
                Text("• Boots plateforme 🥺")
            }
        }

// Stat 4 – Répétitions d’outfits
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEDF7)),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🔁 Portés plusieurs jours :", style = MaterialTheme.typography.bodyLarge)
                Text("• Hoodie noir (3 jours d'affilée 🖤)")
            }
        }

    }




}
