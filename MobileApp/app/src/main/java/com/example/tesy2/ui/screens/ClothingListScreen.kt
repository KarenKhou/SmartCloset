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
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.tesy2.ui.composable.ClothingCard
import com.example.tesy2.viewmodel.ClothingViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.tesy2.data.models.AppUser
import com.example.tesy2.data.models.Closet
import com.example.tesy2.data.models.UserData
import com.example.tesy2.data.supabase.supabase
import com.example.tesy2.ui.screens.getCurrentUserClosetIdSuspend
import com.example.tesy2.viewmodel.AlertViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.runBlocking
import com.example.tesy2.viewmodel.AuthViewModel
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

@Composable
fun ClothingScreen(
    modifier: Modifier = Modifier,
    viewModel: ClothingViewModel = viewModel(),
    navController: NavController
) {
    val user = supabase.auth.currentUserOrNull()
    val userId = user?.id

    var userName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        if (userId != null) {
            println("🔍 Attempting to fetch user info for ID: $userId")
            try {
                val response = supabase
                    .from("User")
                    .select {
                        filter { eq("user_id", userId) }
                    }
                    .decodeSingle<AppUser>()
                println("✅ Successfully fetched user: ${response.name}")
                userName = response.name
            } catch (e: Exception) {
                println("❌ Failed to fetch user name: ${e.message}")
            }
        } else {
            println("⚠️ userId is null")
        }
    }

    val context = LocalContext.current

    val clothingList = viewModel.clothingItems.collectAsState().value
    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedStyle by remember { mutableStateOf<String?>(null) }
    var selectedAvailability by remember { mutableStateOf<Boolean?>(null) }


//    val filteredClothingList = clothingList.filter { item ->
//        val matchesSearch = item.name?.contains(searchQuery, ignoreCase = true) == true ||
//                item.category?.contains(searchQuery, ignoreCase = true) == true ||
//                item.color?.contains(searchQuery, ignoreCase = true) == true ||
//                item.style?.contains(searchQuery, ignoreCase = true) == true
//
//        val matchesCategory = selectedCategory == null || item.category?.equals(selectedCategory, ignoreCase = true) == true
//        val matchesStyle = selectedStyle == null || item.style?.equals(selectedStyle, ignoreCase = true) == true
//
//        matchesSearch && matchesCategory && matchesStyle
//    }
    val filteredClothingList = clothingList.filter { item ->
        val matchesSearch = item.name?.contains(searchQuery, ignoreCase = true) == true ||
                item.category?.contains(searchQuery, ignoreCase = true) == true ||
                item.color?.contains(searchQuery, ignoreCase = true) == true ||
                item.style?.contains(searchQuery, ignoreCase = true) == true

        val matchesCategory = selectedCategory == null || item.category?.equals(selectedCategory, ignoreCase = true) == true
        val matchesStyle = selectedStyle == null || item.style?.equals(selectedStyle, ignoreCase = true) == true
        val matchesAvailability = selectedAvailability == null || (item.availability == if (selectedAvailability == true) 1 else 0)

        matchesSearch && matchesCategory && matchesStyle && matchesAvailability
    }



    val itemsPerPage = 4
    val totalPages = max(1, ceil(filteredClothingList.size.toDouble() / itemsPerPage).toInt())
    var currentPage by remember { mutableStateOf(1) }

    LaunchedEffect(filteredClothingList) {
        if (currentPage > totalPages) {
            currentPage = min(currentPage, totalPages)
        }
    }

    val startIndex = (currentPage - 1) * itemsPerPage
    val endIndex = min(startIndex + itemsPerPage, filteredClothingList.size)
    val currentPageItems = filteredClothingList.subList(startIndex, endIndex)

    val alertViewModel: AlertViewModel = viewModel()
    val alertText by alertViewModel.alertText.collectAsState()

    // Load the clothes (for example from closet 1)
    LaunchedEffect(Unit) {
        val closetId = getCurrentUserClosetIdSuspend()
        if (closetId != null) {
            viewModel.loadClothes(closetId = closetId)
        } else {
            println("❌ Aucun closet_id trouvé pour l'utilisateur")
        }
    }
    LaunchedEffect(alertText) {
        if (alertText == "ALERT") {
            println("⚠️ Fake alert triggered, navigating...")
            navController.navigate("removeOutfit")

        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row{

            Text(
                text = "Hi ${userName ?: "there"} 👋",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(onClick = { alertViewModel.triggerFakeAlert() }) {
                Text("Remove/Add Item")
            }
        }



        TextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                currentPage = 1
            },
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
                focusedContainerColor = MaterialTheme.colorScheme.primary,
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
            val closetList by viewModel.closets
            var expanded by remember { mutableStateOf(false) }
            var selectedCloset by remember { mutableStateOf<Closet?>(null) }

            LaunchedEffect(Unit) {
                viewModel.loadUserClosets()
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // All
                Button(onClick = {
                    selectedCategory = null
                    selectedStyle = null
                    currentPage = 1 // Reset to first page when filters change
                }) {
                    Text("All")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Category
                var expandedCategory by remember { mutableStateOf(false) }
                Box {
                    Button(onClick = { expandedCategory = true }) {
                        Text(selectedCategory ?: "Category")
                    }

                    DropdownMenu(expanded = expandedCategory, onDismissRequest = { expandedCategory = false }) {
                        listOf("Tshirt", "Dress", "Jacket", "Pants", "Sweater", "Shirt", "Short", "Skirt").forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category) },
                                onClick = {
                                    selectedCategory = category
                                    expandedCategory = false
                                    currentPage = 1 // Reset to first page when filters change
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Style
                var expandedStyle by remember { mutableStateOf(false) }
                Box {
                    Button(onClick = { expandedStyle = true }) {
                        Text(selectedStyle ?: "Style")
                    }

                    DropdownMenu(expanded = expandedStyle, onDismissRequest = { expandedStyle = false }) {
                        listOf("Formal", "Casual", "Both").forEach { style ->
                            DropdownMenuItem(
                                text = { Text(style) },
                                onClick = {
                                    selectedStyle = style
                                    expandedStyle = false
                                    currentPage = 1 // Reset to first page when filters change
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                // Availability
                var expandedAvailability by remember { mutableStateOf(false) }
                Box {
                    Button(onClick = { expandedAvailability = true }) {
                        Text(
                            when (selectedAvailability) {
                                null -> "Availability"
                                true -> "Available"
                                false -> "Not Available"
                            }
                        )
                    }

                    DropdownMenu(expanded = expandedAvailability, onDismissRequest = { expandedAvailability = false }) {
                        DropdownMenuItem(
                            text = { Text("Available") },
                            onClick = {
                                selectedAvailability = true
                                expandedAvailability = false
                                currentPage = 1
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Not Available") },
                            onClick = {
                                selectedAvailability = false
                                expandedAvailability = false
                                currentPage = 1
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("All") },
                            onClick = {
                                selectedAvailability = null
                                expandedAvailability = false
                                currentPage = 1
                            }
                        )
                    }
                }


                Spacer(modifier = Modifier.weight(1f))

                var expandedCloset by remember { mutableStateOf(false) }
                Box {
                    Button(onClick = { expandedCloset = true }) {
                        Text(selectedCloset?.closet_name ?: "Your Closet")
                    }

                    DropdownMenu(expanded = expandedCloset, onDismissRequest = { expandedCloset = false }) {
                        closetList.forEach { closet ->
                            DropdownMenuItem(
                                text = { Text(closet.closet_name) },
                                onClick = {
                                    selectedCloset = closet
                                    expandedCloset = false
                                    currentPage = 1 // Reset to first page when closet changes
                                    viewModel.loadClothes(closet.closet_id!!)
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Items Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(currentPageItems) { item ->
                        ClothingCard(item, onEditClick = { selectedItem ->
                            // 👉 Navigue vers un écran d'édition ou ouvre un Dialog
                            navController.navigate("edit_clothing/${selectedItem.item_id}")
                        })
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (currentPage > 1) currentPage--
                        },
                        enabled = currentPage > 1
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Previous Page",
                            tint = if (currentPage > 1) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        val pageRange = calculateVisiblePageRange(currentPage, totalPages, 5)

                        for (pageNum in pageRange) {
                            val isCurrentPage = pageNum == currentPage
                            Button(
                                onClick = { currentPage = pageNum },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isCurrentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                    contentColor = if (isCurrentPage) Color.White else MaterialTheme.colorScheme.primary
                                ),
                                modifier = Modifier.size(40.dp),
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    text = pageNum.toString(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = {
                            if (currentPage < totalPages) currentPage++
                        },
                        enabled = currentPage < totalPages
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next Page",
                            tint = if (currentPage < totalPages) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                }


                Text(
                    text = "Page $currentPage of $totalPages (${filteredClothingList.size} items)",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


private fun calculateVisiblePageRange(currentPage: Int, totalPages: Int, maxVisible: Int): IntRange {
    if (totalPages <= maxVisible) {
        return 1..totalPages
    }

    val halfVisible = maxVisible / 2
    var start = currentPage - halfVisible
    var end = currentPage + halfVisible


    if (start < 1) {
        end = end + (1 - start)
        start = 1
    }

    if (end > totalPages) {
        start = start - (end - totalPages)
        end = totalPages
    }

    start = max(1, start)

    return start..end
}

suspend fun getCurrentUserClosetIdSuspend(): Int? {
    val user = supabase.auth.currentUserOrNull() ?: return null
    val userId = user.id

    println("🧪 userId: $userId")

    val response = runBlocking {
        try {
            val raw = supabase
                .from("closet")
                .select(columns = Columns.list("closet_id")) {
                    filter {
                        eq("user_id", userId)
                    }
                }
            println("📥 RAW JSON: ${raw.data}") // ajoute ça temporairement pour debug

            val result = raw.decodeList<UserData>()
            result
        } catch (e: Exception) {
            println("❌ Supabase error: ${e.message}")
            emptyList()
        }
    }

    val closetId = response.firstOrNull()?.closet_id
    println("📦 Final closet_id: $closetId")
    return closetId
}