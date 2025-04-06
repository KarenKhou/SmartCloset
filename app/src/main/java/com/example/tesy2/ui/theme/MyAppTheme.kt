package com.example.tesy2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
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
