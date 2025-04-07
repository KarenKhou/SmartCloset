package com.example.tesy2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.composable
import com.example.tesy2.ui.theme.Tesy2Theme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import androidx.navigation.compose.rememberNavController
import com.example.tesy2.ui.composable.UserPreferences
import com.example.tesy2.ui.navigation.AppNavHost
import com.example.tesy2.ui.screens.AddClothingScreen
import com.example.tesy2.ui.screens.AlertScreen
import com.example.tesy2.ui.screens.CameraINOUTScreenPreview
import com.example.tesy2.ui.screens.CameraScreen
import com.example.tesy2.ui.screens.ClothingScreen
import com.example.tesy2.ui.screens.RemoveOutfitScreen
import com.example.tesy2.ui.screens.SuggScreen
import com.example.tesy2.ui.theme.AppThemeColor
import com.example.tesy2.ui.theme.LocalAppTheme
import com.example.tesy2.ui.theme.MyAppTheme
import io.github.jan.supabase.auth.Auth




val supabase = createSupabaseClient(
    supabaseUrl = "https://rnjccfpgdpzkoptzvcgr.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJuamNjZnBnZHB6a29wdHp2Y2dyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDI1NzkwMTIsImV4cCI6MjA1ODE1NTAxMn0.rFKKVLNDuDNocuKy_6i4qZijmOdgBl0bAwnecvqslu0"
) {
    install(Postgrest)
    install(Auth)
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        val themeName = intent.getStringExtra("userTheme")
//            ?: getSharedPreferences("settings", MODE_PRIVATE).getString("userTheme", "pink")
//            ?: "pink"
//        val selectedTheme = AppThemeColor.fromName(themeName)
        val savedThemeName = UserPreferences.getUserInfo(this)["theme"] ?: "pink"
        val selectedTheme = AppThemeColor.fromName(savedThemeName)
        setContent {
            val themeState = remember { mutableStateOf(selectedTheme) }
//            Tesy2Theme {
            CompositionLocalProvider(LocalAppTheme provides themeState) {
                MyAppTheme(selectedTheme = selectedTheme) {
                    val navController = rememberNavController()
                    AppNavHost(navController = navController)
                }
            }
                //ClothingScreen(navController = navController)
                //AddClothingScreen()
                //SuggScreen(navController = navController)
                //CameraScreen()
                //CameraINOUTScreenPreview()
                //AlertScreen(navController = navController)



//                androidx.navigation.compose.NavHost(
//                    navController = navController,
//                    startDestination = "alert"
//                ) {
//                    composable("alert") {
//                        AlertScreen(navController = navController)
//                    }
//
//                    composable("removeOutfit") {
//                        RemoveOutfitScreen()
//                    }
//
//            }
//      }
    }

}}

