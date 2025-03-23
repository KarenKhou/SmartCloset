package com.example.tesy2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tesy2.ui.theme.Tesy2Theme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import io.github.jan.supabase.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.FlowType
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch



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

//        // Signup logic inside coroutine
//        lifecycleScope.launch {
//            try {
//                val user = supabase.auth.signUpWith(Email) {
//                    email = "karenn@email.com"
//                    password = "karenn"
//                }
//                println("✅ Signed up successfully:")
//            } catch (e: Exception) {
//                println("❌ Signup failed: ${e.message}")
//            }
//        }

        setContent {
            Tesy2Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { paddingValues ->

                        var email by remember { mutableStateOf("") }
                        var password by remember { mutableStateOf("") }
                        val scope = rememberCoroutineScope()

                        Column(
                            modifier = Modifier
                                .padding(paddingValues) // 👈 applies Scaffold's padding
                                .padding(16.dp)         // 👈 additional padding inside the screen
                                .fillMaxSize()
                        ) {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password") },
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .fillMaxWidth()
                            )

                            Button(
                                onClick = {
                                    scope.launch {
                                        try {
                                            val user = supabase.auth.signUpWith(Email) {
                                                this.email = email
                                                this.password = password
                                            }
                                            println("✅ Sign up successful:")
                                        } catch (e: Exception) {
                                            println("❌ Sign up failed: ${e.message}")
                                        }
                                    }
                                },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Sign Up")
                            }
                        }
                    }
                )
            }
        }


    }


}



