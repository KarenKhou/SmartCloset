package com.example.tesy2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color



@Composable
fun MyAppTheme(selectedTheme: AppThemeColor, content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme(
        primary = selectedTheme.primary,
        onPrimary = Color.White,
        primaryContainer = selectedTheme.primaryContainer,
        onPrimaryContainer = Color.Black,

        secondary = selectedTheme.secondary,
        onSecondary = Color.White,
        secondaryContainer = selectedTheme.secondaryContainer,
        onSecondaryContainer = Color.Black,

        background = Color.White,
        onBackground = Color.Black,
        surface = Color.White,
        onSurface = Color.Black
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}

//@Composable
//fun MyAppTheme(
//    selectedTheme: AppThemeColor,
//    content: @Composable () -> Unit
//) {
//    val currentTheme = remember { mutableStateOf(selectedTheme) }
//
//    CompositionLocalProvider(LocalAppTheme provides currentTheme) {
//        val colorScheme = lightColorScheme(
//            primary = currentTheme.value.primary,
//            onPrimary = Color.White,
//            primaryContainer = currentTheme.value.primaryContainer,
//            onPrimaryContainer = Color.Black,
//            secondary = currentTheme.value.secondary,
//            onSecondary = Color.White,
//            secondaryContainer = currentTheme.value.secondaryContainer,
//            onSecondaryContainer = Color.Black,
//            background = Color.White,
//            onBackground = Color.Black,
//            surface = Color.White,
//            onSurface = Color.Black
//        )
//
//        MaterialTheme(
//            colorScheme = colorScheme,
//            typography = Typography(),
//            shapes = Shapes(),
//            content = content
//        )
//    }
//}
