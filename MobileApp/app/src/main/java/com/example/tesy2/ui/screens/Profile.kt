//package com.example.tesy2.ui.screens
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ExitToApp
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.tesy2.data.models.AppUser
//import com.example.tesy2.data.supabase.supabase
//import io.github.jan.supabase.auth.auth
//import io.github.jan.supabase.postgrest.from
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.launch
//import android.widget.Toast
//import com.example.tesy2.ui.theme.LocalAppTheme
//
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(navController: NavController) {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//    val scrollState = rememberScrollState()
//
//    var userName by remember { mutableStateOf("Smart Closet User") }
//    var userEmail by remember { mutableStateOf("user@example.com") }
//    val currentUser = supabase.auth.currentUserOrNull()
//    val userId = currentUser?.id
//
//    LaunchedEffect(userId) {
//        if (userId != null) {
//            userEmail = currentUser?.email ?: "user@example.com"
//            try {
//                val response = supabase.from("User").select {
//                    filter { eq("user_id", userId) }
//                }.decodeSingle<AppUser>()
//                userName = response.name
//            } catch (_: Exception) {}
//        }
//    }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(220.dp)
//                .background(MaterialTheme.colorScheme.primary)
//        )
//
//        Column(modifier = Modifier.fillMaxSize()) {
//            Spacer(modifier = Modifier.height(32.dp))
//            Text(
//                text = "My Profile",
//                style = MaterialTheme.typography.headlineLarge.copy(
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold,
//                    letterSpacing = 1.sp
//                ),
//                modifier = Modifier.padding(start = 24.dp)
//            )
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Surface(
//                modifier = Modifier.fillMaxSize(),
//                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
//                color = MaterialTheme.colorScheme.background
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .verticalScroll(scrollState)
//                        .padding(horizontal = 16.dp, vertical = 24.dp)
//                ) {
//                    UserProfileSection(userName, userEmail)
//                    Spacer(modifier = Modifier.height(20.dp))
//                    ActionButtonsSection(navController, coroutineScope, context)
//                    Spacer(modifier = Modifier.height(20.dp))
//                    AboutSection()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun UserProfileSection(userName: String, userEmail: String) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        shape = RoundedCornerShape(20.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant
//        ),
//        elevation = CardDefaults.cardElevation(6.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(100.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.primaryContainer)
//                    .shadow(8.dp, CircleShape),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = "Profile Picture",
//                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
//                    modifier = Modifier.size(60.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//            Text(
//                text = userName,
//                style = MaterialTheme.typography.headlineSmall,
//                fontWeight = FontWeight.Bold
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(
//                    imageVector = Icons.Default.Email,
//                    contentDescription = "Email",
//                    tint = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.size(16.dp)
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(
//                    text = userEmail,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.outline
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun ActionButtonsSection(navController: NavController, coroutineScope: CoroutineScope, context: android.content.Context) {
//
//    val userId = supabase.auth.currentUserOrNull()?.id
//
//
//    var showLogoutDialog by remember { mutableStateOf(false) }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        shape = RoundedCornerShape(20.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surfaceVariant
//        ),
//        elevation = CardDefaults.cardElevation(6.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Text(
//                text = "Quick Actions",
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 12.dp)
//            )
//
//            Button(
//                onClick = { navController.navigate("add_Closet") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 6.dp),
//                shape = RoundedCornerShape(30.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary
//                )
//            ) {
//                Icon(Icons.Default.Add, contentDescription = null)
//                Spacer(Modifier.width(8.dp))
//                Text("Add Closet")
//            }
//
//            Button(
//                onClick = { navController.navigate("complete_profile") },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 6.dp),
//                shape = RoundedCornerShape(30.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary
//                )
//            ) {
//                Icon(Icons.Default.Edit, contentDescription = null)
//                Spacer(Modifier.width(8.dp))
//                Text("Complete Your Profile")
//            }
//
//
//
//            Button(
//                onClick = { showLogoutDialog = true },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 6.dp),
//                shape = RoundedCornerShape(30.dp),
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color.Gray,
//                    contentColor = Color.White
//                )
//            ) {
//                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
//                Spacer(Modifier.width(8.dp))
//                Text("Log Out")
//            }
//
//            if (showLogoutDialog) {
//                AlertDialog(
//                    onDismissRequest = { showLogoutDialog = false },
//                    title = { Text("Confirm Logout") },
//                    text = { Text("Are you sure you want to log out?") },
//                    confirmButton = {
//                        TextButton(onClick = {
//                            showLogoutDialog = false
//                            Toast.makeText(context, "✅ Déconnexion réussie", Toast.LENGTH_LONG).show()
//                            navController.navigate("sign_in") {
//                                popUpTo(0) { inclusive = true }
//                            }
//                        }) {
//                            Text("Yes")
//                        }
//                    },
//                    dismissButton = {
//                        TextButton(onClick = { showLogoutDialog = false }) {
//                            Text("No")
//                        }
//                    }
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun DropdownMenuBox(selectedTheme: String, onThemeChange: (String) -> Unit) {
//    var expanded by remember { mutableStateOf(false) }
//
//    Box {
//        OutlinedButton(onClick = { expanded = true }) {
//            Text("Theme: ${selectedTheme.replaceFirstChar { it.uppercaseChar() }}")
//        }
//        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//            listOf("pink", "blue").forEach { theme ->
//                DropdownMenuItem(
//                    text = { Text(theme.capitalize()) },
//                    onClick = {
//                        expanded = false
//                        onThemeChange(theme)
//                    }
//                )
//            }
//        }
//    }
//}
//
//
//@Composable
//fun AboutSection() {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        shape = RoundedCornerShape(20.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.secondaryContainer
//        ),
//        elevation = CardDefaults.cardElevation(6.dp)
//    ) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
//                Spacer(Modifier.width(8.dp))
//                Text("\uD83D\uDC57 More Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
//            }
//            Spacer(Modifier.height(8.dp))
//            Text("Meet the Founders:", fontWeight = FontWeight.Bold)
//            Text("Karen Khoury\n" +
//                    "Lea Chamseddine\n" +
//                    "Fatima Rabih\n" +
//                    "Marilyn Abou Tayeh —\n " +
//                    "Four Computer and Communication Engineering (CCE) students with a shared vision and a passion for tech and fashion.")
//            Spacer(Modifier.height(12.dp))
//            Text("\nAbout Smart Closet:", fontWeight = FontWeight.Bold)
//            Text("Smart Closet was born out of a common struggle: standing in front of our wardrobes, unsure of what to wear. We wanted to solve that with a smart solution — a mobile app that helps you organize your clothes, plan outfits, and even get suggestions based on the weather. With Smart Closet, choosing what to wear becomes effortless and fun!")
//            Spacer(Modifier.height(16.dp))
//            Text(
//                text = "Smart Closet v1.0",
//                style = MaterialTheme.typography.bodySmall,
//                textAlign = TextAlign.Center,
//                color = MaterialTheme.colorScheme.outline,
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//    }
//}
//

package com.example.tesy2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tesy2.data.models.AppUser
import com.example.tesy2.data.supabase.supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.tesy2.ui.theme.LocalAppTheme
import com.example.tesy2.ui.theme.AppThemeColor
import androidx.compose.animation.core.animateDpAsState
import io.ktor.http.parameters
import kotlinx.serialization.json.put
import kotlinx.serialization.json.buildJsonObject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var userName by remember { mutableStateOf("Smart Closet User") }
    var userEmail by remember { mutableStateOf("user@example.com") }
    val currentUser = supabase.auth.currentUserOrNull()
    val userId = currentUser?.id

    LaunchedEffect(userId) {
        if (userId != null) {
            userEmail = currentUser.email ?: "user@example.com"
            try {
                val response = supabase.from("User").select().decodeList<AppUser>()
                    .firstOrNull { it.user_id == userId }
                if (response != null) {
                    userName = response.name
                }
            } catch (_: Exception) {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(MaterialTheme.colorScheme.primary)
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "My Profile",
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(start = 24.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))

            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp, vertical = 24.dp)
                ) {
                    UserProfileSection(userName, userEmail)
                    Spacer(modifier = Modifier.height(20.dp))
                    ActionButtonsSection(navController, coroutineScope, context)
                    Spacer(modifier = Modifier.height(20.dp))
                    AboutSection()
                }
            }
        }
    }
}

@Composable
fun UserProfileSection(userName: String, userEmail: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .shadow(8.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile Picture",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = userName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
@Composable
fun ActionButtonsSection(navController: NavController, coroutineScope: CoroutineScope, context: android.content.Context) {

    val userId = supabase.auth.currentUserOrNull()?.id


    var showLogoutDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Button(
                onClick = { navController.navigate("add_Closet") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add Closet")
            }

            Button(
                onClick = { navController.navigate("complete_profile") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Complete Your Profile")
            }
            Spacer(modifier = Modifier.height(16.dp))
            ThemeToggleSection(coroutineScope, context)



            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Log Out")
            }

            if (showLogoutDialog) {
                AlertDialog(
                    onDismissRequest = { showLogoutDialog = false },
                    title = { Text("Confirm Logout") },
                    text = { Text("Are you sure you want to log out?") },
                    confirmButton = {
                        TextButton(onClick = {
                            showLogoutDialog = false
                            Toast.makeText(context, "✅ Déconnexion réussie", Toast.LENGTH_LONG).show()
                            navController.navigate("sign_in") {
                                popUpTo(0) { inclusive = true }
                            }
                        }) {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showLogoutDialog = false }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}
@Composable
fun ThemeToggleSection(coroutineScope: CoroutineScope, context: android.content.Context) {
    val themeState = LocalAppTheme.current
    var selectedTheme by remember { mutableStateOf(themeState.value.name) }
    val userId = supabase.auth.currentUserOrNull()?.id

    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Choose Theme:", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf(AppThemeColor.Pink, AppThemeColor.Blue).forEach { theme ->
                val isSelected = theme.name == selectedTheme
                val animatedWidth by animateDpAsState(if (isSelected) 150.dp else 120.dp, label = "width")
                val animatedHeight by animateDpAsState(if (isSelected) 60.dp else 45.dp, label = "height")

                Button(
                    onClick = {
                        selectedTheme = theme.name
                        themeState.value = theme

                        if (userId != null) {
                            coroutineScope.launch {
                                try {
                                    supabase.from("User")
                                        .update(
                                            buildJsonObject {
                                                put("theme", theme.name)

                                            }
                                        ) {
                                            filter {
                                                eq("user_id", userId)
                                            }
                                        }
                                    Toast.makeText(context, "🎨 Theme updated", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "❌ Failed to update theme", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) theme.primary else theme.primary.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .padding(4.dp)
                        .width(animatedWidth)
                        .height(animatedHeight),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = theme.name.replaceFirstChar { it.uppercaseChar() }, color = Color.White)
                }
            }
        }
    }
}



@Composable
fun DropdownMenuBox(selectedTheme: String, onThemeChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text("Theme: ${selectedTheme.replaceFirstChar { it.uppercaseChar() }}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf("pink", "blue").forEach { theme ->
                DropdownMenuItem(
                    text = { Text(theme.capitalize()) },
                    onClick = {
                        expanded = false
                        onThemeChange(theme)
                    }
                )
            }
        }
    }
}

@Composable
fun AboutSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text("👗 More Info", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            Text("Meet the Founders:", fontWeight = FontWeight.Bold)
            Text("Karen Khoury\nLea Chamseddine\nFatima Rabih\nMarilyn Abou Tayeh —\nFour Computer and Communication Engineering (CCE) students with a shared vision and a passion for tech and fashion.")
            Spacer(Modifier.height(12.dp))
            Text("About Smart Closet:", fontWeight = FontWeight.Bold)
            Text("Smart Closet was born out of a common struggle: standing in front of our wardrobes, unsure of what to wear. We wanted to solve that with a smart solution — a mobile app that helps you organize your clothes, plan outfits, and even get suggestions based on the weather.")
            Spacer(Modifier.height(16.dp))
            Text("Smart Closet v1.0", style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.outline, modifier = Modifier.fillMaxWidth())
        }
    }
}


