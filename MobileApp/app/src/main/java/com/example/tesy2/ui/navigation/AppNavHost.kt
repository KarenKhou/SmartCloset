package com.example.tesy2.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.tesy2.ui.screens.SignInScreen
import com.example.tesy2.ui.screens.SignUpScreen

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

        // Tu pourras ajouter ici d'autres routes plus tard (home, profil, etc.)
    }
}
