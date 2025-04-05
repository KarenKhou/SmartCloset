package com.example.tesy2.ui.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.NavigationBar
import androidx.compose.ui.Alignment
import com.example.tesy2.ui.theme.pinkColor
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tesy2.ui.screens.AddClothingScreen
import com.example.tesy2.ui.screens.ClothingScreen
import com.example.tesy2.ui.screens.SuggScreen
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.tesy2.R
import androidx.compose.foundation.Image
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tesy2.ui.screens.AlertScreen
import com.example.tesy2.ui.screens.EditClothingScreen
import com.example.tesy2.ui.screens.RemoveOutfitScreen
import com.example.tesy2.ui.screens.RequestBluetoothPermissions
import com.example.tesy2.viewmodel.ClothingViewModel
import com.example.tesy2.viewmodel.MainViewModel


sealed class Screen(val route: String, val icon: Any, val title: String) {
    object MyCloset : Screen("my_closet", Icons.Filled.Home, "My Closet")
    object PastOutfits : Screen("past_outfits", Icons.Filled.AccessTime, "Past Outfits")
    object AddItem : Screen("add_item", Icons.Filled.Add, "Add Item")
    object Suggestion : Screen("suggestion", R.drawable.wand_magic_sparkles_solid, "Suggestion")
    object Profile : Screen("profile", Icons.Filled.Person, "Profile")
}


@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        Screen.MyCloset,
        Screen.PastOutfits,
        Screen.AddItem,
        Screen.Suggestion,
        Screen.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            containerColor = Color.White,
            tonalElevation = 8.dp
        ) {
            items.forEachIndexed { index, screen ->
                val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

                if (index != 2) {
                    NavigationBarItem(
                        icon = {
                            when (screen.icon) {
                                is ImageVector -> {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.title,
                                        tint = if (selected) pinkColor else Color.Gray
                                    )
                                }
                                is Int -> {
                                    val painter = painterResource(id = screen.icon as Int)
                                    Image(
                                        painter = painter,
                                        contentDescription = screen.title,
                                        modifier = Modifier.size(24.dp),
                                        colorFilter = if (selected) ColorFilter.tint(pinkColor) else ColorFilter.tint(Color.Gray)
                                    )
                                }
                            }
                        },
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                } else {
                    NavigationBarItem(
                        icon = {},
                        selected = false,
                        onClick = {}
                    )
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.AddItem.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            shape = CircleShape,
            containerColor = pinkColor,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp)
                .size(60.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Item",
                tint = Color.White
            )
        }
    }
}


@Composable
fun MainScreenWithBottomNav(navController:NavHostController) {
    val bottomNavController = rememberNavController()
    val viewModel: com.example.tesy2.viewmodel.MainViewModel = viewModel()
    val alertText by viewModel.alertText.collectAsState()

    val context = LocalContext.current  // Get the context

    RequestBluetoothPermissions(context) {

        viewModel.connectBluetooth() //naymo lal error, byemche
    }


    // Écoute de l'alerte
    LaunchedEffect(alertText) {
        if (alertText == "ALERT") {
            bottomNavController.navigate("alert")
            viewModel.clearAlert()
        }
    }

    Scaffold(
        bottomBar = {
            BottomBar(bottomNavController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.MyCloset.route
            ) {
                composable(Screen.MyCloset.route) {
                    ClothingScreen(navController = navController)
                }
                composable(Screen.PastOutfits.route) {

                }
                composable(Screen.AddItem.route) {
                    AddClothingScreen()
                }
                composable(Screen.Suggestion.route) {
                    SuggScreen()
                }
                composable(Screen.Profile.route) {

                }
                composable("alert") {
                    RemoveOutfitScreen()
                }




            }
        }
    }
}