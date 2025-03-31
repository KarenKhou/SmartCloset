package com.example.tesy2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tesy2.ui.theme.Tesy2Theme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import androidx.navigation.compose.rememberNavController
import com.example.tesy2.ui.screens.CameraINOUTScreenPreview
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

        setContent {
            Tesy2Theme {
               val navController = rememberNavController()
               //AppNavHost(navController = navController)
                //ClothingScreen(navController = navController)
                //AddClothingScreen()
                //SuggScreen(navController = navController)
                //CameraScreen()
                CameraINOUTScreenPreview()

            }
        }
    }

}

