package com.example.tesy2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tesy2.ui.screens.AlertScreen
import com.example.tesy2.ui.screens.EditClothingScreen
import com.example.tesy2.ui.screens.RemoveOutfitScreen
import com.example.tesy2.ui.screens.SignInScreen
import com.example.tesy2.ui.screens.SignUpScreen
import com.example.tesy2.viewmodel.ClothingViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "sign_in"
    ) {
        composable("sign_in") {
            SignInScreen(navController = navController)
        }

        composable("sign_up") {
            SignUpScreen(navController = navController)
        }
        composable("main") {
            MainScreenWithBottomNav(navController)
        }

        composable("alert") {
            AlertScreen(navController = navController)
        }

        composable("removeOutfit") {
            RemoveOutfitScreen()
        }

        composable("edit_clothing/{itemId}") { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
            val clothingViewModel = viewModel<ClothingViewModel>() // ⚠️ import : androidx.lifecycle.viewmodel.compose.viewModel
            EditClothingScreen(
                itemId = itemId,
                navController = navController,
                viewModel = clothingViewModel
            )
        }






        // Tu pourras ajouter ici d'autres routes plus tard (home, profil, etc.)
    }
}
