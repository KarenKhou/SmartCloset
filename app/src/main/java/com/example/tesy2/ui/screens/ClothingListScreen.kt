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
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.tesy2.data.models.Closet
import com.example.tesy2.data.models.UserData
import com.example.tesy2.data.supabase.supabase
import com.example.tesy2.ui.screens.getCurrentUserClosetIdSuspend
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.runBlocking


@Composable
fun ClothingScreen(
    modifier: Modifier = Modifier,
    viewModel: ClothingViewModel = viewModel() ,
    navController: NavController
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
        val closetId = getCurrentUserClosetIdSuspend()
        if (closetId != null) {
            viewModel.loadClothes(closetId = closetId)
        } else {
            println("❌ Aucun closet_id trouvé pour l'utilisateur")
        }
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

            val closetList by viewModel.closets
            var expanded by remember { mutableStateOf(false) }
            var selectedCloset by remember { mutableStateOf<Closet?>(null) }

            LaunchedEffect(Unit) {
                viewModel.loadUserClosets()
            }


            Spacer(modifier = Modifier.height(8.dp))

            Box {
                Button(onClick = { expanded = true }) {
                    Text(selectedCloset?.closet_name ?: "Your Closet")
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    closetList.forEach { closet ->
                        DropdownMenuItem(
                            text = { Text(closet.closet_name) },
                            onClick = {
                                selectedCloset = closet
                                expanded = false
                                viewModel.loadClothes(closet.closet_id!!)
                            }
                        )
                    }
                }
            }

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
                    .padding(horizontal = 2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                items(filteredClothingList) { item ->
                    ClothingCard(item, onEditClick = { selectedItem ->
                        // 👉 Navigue vers un écran d'édition ou ouvre un Dialog
                        navController.navigate("edit_clothing/${selectedItem.item_id}")

                    })
                }


            }
        }
    }



}



//private fun ClothingViewModel.getCurrentUserClosetId(): Int? {
//    val user = supabase.auth.currentUserOrNull() ?: return null
//    val userId = user.id
//    println("test1")
//
//    val userData = runBlocking {
//        try {
//            supabase
//                .from("closet")
//                .select(columns = Columns.list("closet_id")) {
//                    filter {
//                        eq("user_id", userId)
//                    }
//                }.decodeSingle<UserData>() // ✅ on récupère un UserData
//        } catch (e: Exception) {
//            println("❌ Supabase error: ${e.message}")
//            null
//        }
//    }
//
//    val closetId = userData?.closet_id // ✅ on récupère l'int depuis l'objet
//
//    if (closetId != null) {
//        println("✅ closet_id: $closetId")
//    } else {
//        println("❌ Aucun utilisateur trouvé ou closet_id manquant")
//    }
//
//    return closetId
//}

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
                        eq("user_id", userId )
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


