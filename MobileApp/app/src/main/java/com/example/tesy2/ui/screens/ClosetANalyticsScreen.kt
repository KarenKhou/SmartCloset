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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.viewmodel.WornCalendarViewModel
import io.github.jan.supabase.auth.auth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun ClosetAnalyticsScreen(viewModel: WornCalendarViewModel = viewModel()) {
    var currentYearMonth by remember { mutableStateOf(YearMonth.from(LocalDate.now())) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var recentItems by remember { mutableStateOf<List<UsagePreview>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val userId = supabase.auth.currentUserOrNull()?.id

    // Get month info
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    // Create list of all days to display (previous month days, current month, next month days)
    val calendarDays = remember(currentYearMonth) {
        val result = mutableListOf<LocalDate?>()

        // Add empty slots for days from previous month
        repeat(firstDayOfWeek) {
            result.add(null)
        }

        // Add current month days
        for (i in 1..daysInMonth) {
            result.add(currentYearMonth.atDay(i))
        }

        result
    }

    // UI Colors
    val gradientColors = listOf(Color(0xFFFF80AB), Color(0xFFFF4081))
    val calendarBackgroundColor = Color(0xFFFFF5F8)
    val todayHighlightColor = Color(0xFFFF4081)
    val selectedDayColor = Color(0xFFFF80AB)
    val dayBackgroundColor = Color.White
    val headerColor = Color(0xFFFF80AB)

    LaunchedEffect(userId) {
        try {
            recentItems = getRecentUsage(userId!!)
        } catch (e: Exception) {
            errorMessage = "Erreur Supabase : ${e.message}"
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(selectedDate, userId) {
        userId?.let {
            viewModel.loadItemsForDate(selectedDate.toString(), it)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Page Title with gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(
                    Brush.horizontalGradient(gradientColors),
                    RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "✨ Mon Dressing Analytics ✨",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Calendar section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = calendarBackgroundColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Month header with navigation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            currentYearMonth = currentYearMonth.minusMonths(1)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronLeft,
                            contentDescription = "Previous Month",
                            tint = headerColor
                        )
                    }

                    Text(
                        text = currentYearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + currentYearMonth.year,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = headerColor
                    )

                    IconButton(
                        onClick = {
                            currentYearMonth = currentYearMonth.plusMonths(1)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Next Month",
                            tint = headerColor
                        )
                    }
                }

                // Day of week header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (day in DayOfWeek.values()) {
                        // Adjust to start with Sunday (assuming day.value 1 is Monday)
                        val dayIndex = (day.value % 7)
                        val dayOfWeek = DayOfWeek.of(if (dayIndex == 0) 7 else dayIndex)

                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).first().toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)
                                    Color(0xFFFF4081) else Color(0xFF424242)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Calendar days grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    userScrollEnabled = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((calendarDays.size / 7 * 48).dp) // Calculate height based on number of rows
                ) {
                    items(calendarDays) { date ->
                        val today = LocalDate.now()
                        val isSelected = date == selectedDate
                        val isToday = date == today

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .background(
                                    when {
                                        isSelected -> selectedDayColor
                                        isToday -> todayHighlightColor.copy(alpha = 0.2f)
                                        else -> dayBackgroundColor
                                    },
                                    RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = if (isToday) 2.dp else 0.dp,
                                    color = if (isToday) todayHighlightColor else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable(enabled = date != null) {
                                    date?.let {
                                        selectedDate = it
                                        userId?.let { id ->
                                            viewModel.loadItemsForDate(it.toString(), id)
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (date != null) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = when {
                                            isSelected -> Color.White
                                            date.dayOfWeek == DayOfWeek.SATURDAY || date.dayOfWeek == DayOfWeek.SUNDAY ->
                                                Color(0xFFFF4081)
                                            else -> Color(0xFF424242)
                                        }
                                    )

                                    // Add a small dot indicator for days with worn items
                                    // This is just a placeholder - you would need to implement logic
                                    // to check if items were worn on specific dates
                                    if (date.dayOfMonth % 3 == 0) { // Placeholder logic
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .background(
                                                    if (isSelected) Color.White else Color(0xFFFF4081),
                                                    RoundedCornerShape(2.dp)
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Selected date items section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = "Calendar",
                        tint = Color(0xFFFF4081),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tenue du " + selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424242)
                    )
                }

                if (viewModel.wornItemsForDate.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aucun vêtement porté ce jour",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                } else {
                    viewModel.wornItemsForDate.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFF5F5F5))
                            ) {
                                // Placeholder or real image if available
                                item.clothingitem.image_url?.let { url ->
                                    AsyncImage(
                                        model = url,
                                        contentDescription = item.clothingitem.name,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } ?: Icon(
                                    Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color(0xFFFF80AB),
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Text(
                                text = item.clothingitem.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF424242)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recently used items
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Favorite,
                        contentDescription = "Recent",
                        tint = Color(0xFFFF4081),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Récemment portés",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424242)
                    )
                }

                when {
                    isLoading -> {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFFFF4081))
                        }
                    }
                    errorMessage != null -> {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = errorMessage ?: "Une erreur inconnue est survenue",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                    recentItems.isEmpty() -> {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aucun vêtement récemment porté",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Gray
                            )
                        }
                    }
                    else -> {
                        recentItems.forEach { usage ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFAFC))
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFFF5F5F5))
                                    ) {
                                        AsyncImage(
                                            model = usage.clothingitem?.image_url,
                                            contentDescription = usage.clothingitem?.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(16.dp))

                                    Column {
                                        Text(
                                            text = usage.clothingitem?.name ?: "Nom inconnu",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            color = Color(0xFF424242)
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "🗓️ Porté le ${usage.worn_date}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF757575)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Fashion stats section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF5F8)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Stats",
                        tint = Color(0xFFFF4081),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tes stats fashion",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF424242)
                    )
                }

                // Stat 1 – Vêtement le plus porté
                StatCard(
                    icon = "👑",
                    title = "Vêtement favori",
                    value = "Robe rose à paillettes (7 fois)",
                    color = Color(0xFFFFE0F0)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Stat 2 – Couleurs et styles
                StatCard(
                    icon = "🎨",
                    title = "Style",
                    value = "Couleur: Rose • Style: Kawaii",
                    color = Color(0xFFFFF0FA)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Stat 3 – Habits oubliés
                StatCard(
                    icon = "❌",
                    title = "Vêtements oubliés",
                    value = "• Jean flare\n• Pull fluffy blanc\n• Boots plateforme",
                    color = Color(0xFFFFEDF7)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Stat 4 – Répétitions
                StatCard(
                    icon = "🔁",
                    title = "Portés plusieurs jours",
                    value = "• Hoodie noir (3 jours d'affilée 🖤)",
                    color = Color(0xFFFFEDF7)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun StatCard(
    icon: String,
    title: String,
    value: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF424242)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF757575)
                )
            }
        }
    }
}
