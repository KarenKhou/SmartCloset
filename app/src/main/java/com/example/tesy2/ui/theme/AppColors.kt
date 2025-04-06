package com.example.tesy2.ui.theme

import androidx.compose.ui.graphics.Color

sealed class AppThemeColor(
    val name: String,
    val primary: Color,
    val primaryContainer: Color,
    val secondary: Color,
    val secondaryContainer: Color
) {
    object Pink : AppThemeColor(
        name = "pink",
        primary = Color(0xFFE91E63),
        primaryContainer = Color(0xFFF8BBD0),
        secondary = Color(0xFFF06292),
        secondaryContainer = Color(0xFFFCE4EC)
    )

    object Blue : AppThemeColor(
        name = "blue",
        primary = Color(0xFF2196F3),
        primaryContainer = Color(0xFFBBDEFB),
        secondary = Color(0xFF64B5F6),
        secondaryContainer = Color(0xFFE3F2FD)
    )

    companion object {
        fun fromName(name: String): AppThemeColor {
            return when (name.lowercase()) {
                "blue" -> Blue
                else -> Pink
            }
        }
    }
}