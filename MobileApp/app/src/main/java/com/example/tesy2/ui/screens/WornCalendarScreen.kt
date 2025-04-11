package com.example.tesy2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.viewmodel.WornCalendarViewModel
import java.time.LocalDate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.auth.auth
import java.time.YearMonth



@Composable
fun WornCalendarScreen(viewModel: WornCalendarViewModel = viewModel()) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val daysInMonth = YearMonth.from(selectedDate).lengthOfMonth()
    val firstDayOfWeek = YearMonth.from(selectedDate).atDay(1).dayOfWeek.value % 7
    val userId = supabase.auth.currentUserOrNull()?.id

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(
            text = "✨ Vêtements portés en ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ✨",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.Magenta,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Grid calendar (7 columns)
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFE0F0)) // light pink bg
        ) {
            // Empty cells before the first day
            items(firstDayOfWeek) {
                Box(modifier = Modifier.size(40.dp)) { }
            }

            // Days
            items(daysInMonth) { index ->
                val day = index + 1
                val date = selectedDate.withDayOfMonth(day)
                Button(
                    onClick = {
                        selectedDate = date
                        viewModel.loadItemsForDate(date.toString(), userId!! )
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA6C9))
                ) {
                    Text(
                        text = "$day",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White  // ou Color.Black selon ton fond
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Items worn
        Text(
            text = "Porté le ${selectedDate}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn {
            items(viewModel.wornItemsForDate) { item ->
                Text("🎀 ${item.clothingitem.name}")
            }
        }
    }
}
