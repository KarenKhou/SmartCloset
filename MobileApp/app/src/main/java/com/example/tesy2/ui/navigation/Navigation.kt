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
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.NavigationBar
import androidx.compose.ui.Alignment
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
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
//import com.example.tesy2.ui.screens.AddClosetScreen
import com.example.tesy2.ui.screens.ProfileScreen
import com.example.tesy2.ui.screens.AlertScreen

import com.example.tesy2.ui.screens.EditClothingScreen
import com.example.tesy2.ui.screens.RecentUsageScreenWrapper
import com.example.tesy2.ui.screens.RemoveOutfitScreen
import com.example.tesy2.ui.screens.RequestBluetoothPermissions
import android.content.res.Resources
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavHostController

sealed class Screen(val route: String, val icon: Any,val title:String) {
    object MyCloset : Screen("my_closet", Icons.Filled.Home,"My Closet")
    object PastOutfits : Screen("past_outfits", Icons.Filled.AccessTime, "Past Outfits")
    object DataAnalysis: Screen("data_analysis", Icons.Filled.BarChart, "Data Analysis")
    object AddItem : Screen("add_item", Icons.Filled.Add,  "Add Item")
    object Suggestion : Screen("suggestion", R.drawable.wand_magic_sparkles_solid, "Suggestions")
    object Profile : Screen("profile", Icons.Filled.Person, "Profile")
}


@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        Screen.MyCloset,
//      Screen.PastOutfits,
        Screen.DataAnalysis,
        Screen.AddItem,
        Screen.Suggestion,
        Screen.Profile
    )

    val navBarHeight = if (isTablet()) 80.dp else 56.dp
    val iconSize = if (isTablet()) 32.dp else 24.dp
    val fabSize = if (isTablet()) 72.dp else 60.dp
    val fabIconSize = if (isTablet()) 36.dp else 24.dp
    val fabOffset = if (isTablet()) (-24).dp else (-20).dp

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .height(navBarHeight),
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
                                        tint = if (selected) MaterialTheme.colorScheme.primary
                                        else Color.Gray,
                                        modifier = Modifier.size(iconSize)
                                    )
                                }
                                is Int -> {
                                    Image(
                                        painter = painterResource(id = screen.icon as Int),
                                        contentDescription = screen.title,
                                        modifier = Modifier.size(iconSize),
                                        colorFilter = if (selected) ColorFilter.tint(MaterialTheme.colorScheme.primary
                                        ) else ColorFilter.tint(Color.Gray)
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
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = fabOffset)  // Responsive offset
                .size(fabSize)  // Responsive FAB size
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Item",
                tint = Color.White,
                modifier = Modifier.size(fabIconSize)  // Responsive FAB icon size
            )
        }
    }
}


fun isTablet(): Boolean {
    val config = Resources.getSystem().configuration
    return config.smallestScreenWidthDp >= 600
}

@Composable
fun MainScreenWithBottomNav(rootNavController: NavHostController) {
    val navController = rememberNavController()
    //val bottomNavController = navController
    val viewModel: com.example.tesy2.viewmodel.MainViewModel = viewModel()
    val alertText by viewModel.alertText.collectAsState()

    val context = LocalContext.current  // Get the context

    RequestBluetoothPermissions(context) {

        viewModel.connectBluetooth() //naymo lal error, byemche
    }


    // Écoute de l'alerte
    LaunchedEffect(alertText) {
        if (alertText == "ALERT") {
            navController.navigate("removeOutfit")
            viewModel.clearAlert()
        }
    }


    Scaffold(
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController,
                startDestination = Screen.MyCloset.route
            ) {
                composable(Screen.MyCloset.route) {
                    ClothingScreen(navController = navController)
                }

                composable(Screen.PastOutfits.route) {
                    RecentUsageScreenWrapper()

//                composable(Screen.DataAnalysis.route) {
//
//
                }
                composable(Screen.AddItem.route) {
                    AddClothingScreen()
                }
                composable(Screen.Suggestion.route) {
                    SuggScreen()
                }
                composable(Screen.Profile.route) {
//                    AddClosetScreen(navController = navController)
                    ProfileScreen(navController = rootNavController)
                }
//                composable("alert") {
//                    RemoveOutfitScreen()
//                }
                composable("alert") {
                    AlertScreen(navController)
                }

                composable("removeOutfit") {
                    RemoveOutfitScreen(navController = navController)
                }




            }
        }
    }
}