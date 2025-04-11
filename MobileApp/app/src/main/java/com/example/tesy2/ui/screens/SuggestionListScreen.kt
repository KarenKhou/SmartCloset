package com.example.tesy2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.ui.composable.ClothingCard
import com.example.tesy2.viewmodel.ClothingViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggScreen(viewModel: ClothingViewModel = viewModel()) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Filter options
    val outfitTypes = listOf("top+bottom", "dress")
    val genders = listOf("male", "female")
    val seasons = listOf("summer", "winter", "spring", "autumn")
    val occasions = listOf("casual", "formal", "both")
    val styles = listOf("casual", "sporty", "elegant", "girly", "minimal", "edgy", "boho", "classic")

    // Selected values
    var selectedOutfitType by remember { mutableStateOf(outfitTypes[0]) }
    var selectedGender by remember { mutableStateOf(genders[0]) }
    var selectedSeason by remember { mutableStateOf(seasons[0]) }
    var selectedOccasion by remember { mutableStateOf(occasions[0]) }
    var selectedStyle by remember { mutableStateOf(styles[0]) }

    // Loading state
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFEE6F1)
            ),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Outfit Suggestions",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF69B4)
                )
                Text(
                    text = "Find your perfect look",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        // Filters section
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Customize Your Recommendation",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Two-column layout for filters
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        FilterDropdown(
                            label = "Outfit Type",
                            options = outfitTypes,
                            selected = selectedOutfitType,
                            onSelected = { selectedOutfitType = it }
                        )

                        FilterDropdown(
                            label = "Season",
                            options = seasons,
                            selected = selectedSeason,
                            onSelected = { selectedSeason = it }
                        )

                        FilterDropdown(
                            label = "Style",
                            options = styles,
                            selected = selectedStyle,
                            onSelected = { selectedStyle = it }
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        FilterDropdown(
                            label = "Gender",
                            options = genders,
                            selected = selectedGender,
                            onSelected = { selectedGender = it }
                        )

                        FilterDropdown(
                            label = "Occasion",
                            options = occasions,
                            selected = selectedOccasion,
                            onSelected = { selectedOccasion = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Generate button
                Button(
                    onClick = {
                        isLoading = true
                        scope.launch {
                            viewModel.generateRecommendation(
                                outfitType = selectedOutfitType,
                                gender = selectedGender,
                                season = selectedSeason,
                                occasion = selectedOccasion,
                                //style = selectedStyle,
                                randomize = false
                            )
                            isLoading = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF69B4))
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Outfit Suggestion")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Results section
        val suggList = viewModel.suggestions.collectAsState().value

        if (suggList.isNotEmpty()) {
            Text(
                text = "Suggested Outfits ✨",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 8.dp)
            )

            // Display outfit categories if needed
            suggList.groupBy { it.category }.forEach { (category, items) ->
                if (category != null) {
                    Text(
                        text = category,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(vertical = 8.dp)
                    )
                }

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items) { item ->
                        ClothingCard(
                            item = item,
                            onEditClick = {}
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        } else if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFFF69B4))
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Generate your first outfit suggestion!",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}