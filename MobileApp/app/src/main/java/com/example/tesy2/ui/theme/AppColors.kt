package com.example.tesy2.ui.theme

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
val LocalAppTheme = staticCompositionLocalOf<MutableState<AppThemeColor>> {
    error("No theme provided")
}

sealed class AppThemeColor(
    val name: String,
    val primary: Color,
    val primaryContainer: Color,
    val secondary: Color,
    val secondaryContainer: Color,
    val gradientColors: List<Color>,
    val calendarBackgroundColor: Color,
    val todayHighlightColor: Color,
    val selectedDayColor: Color,
    val dayBackgroundColor: Color,
    val headerColor: Color,
    val favitem: Color,
    val style: Color,
    val stats: Color
) {
    object Pink : AppThemeColor(
        name = "pink",
        primary = Color(0xFFFF69B4),
        primaryContainer = Color(0xFFE36991),
        secondary = Color(0xFFF06292),
        secondaryContainer = Color(0xFFFCE4EC),
         gradientColors = listOf(Color(0xFFFF80AB), Color(0xFFFF4081)),
        calendarBackgroundColor = Color(0xFFFFF5F8),
        todayHighlightColor = Color(0xFFFF4081),
        selectedDayColor = Color(0xFFFF80AB),
        dayBackgroundColor = Color.White,
        headerColor = Color(0xFFFF80AB),
        favitem=Color(0xFFFFE0F0),
        style=Color(0xFFFFF0FA),
        stats=Color(0xFFFFEDF7)
    )

    object Blue : AppThemeColor(
        name = "blue",
        primary = Color(0xFF2196F3),
        primaryContainer = Color(0xFFBBDEFB),
        secondary = Color(0xFF64B5F6),
        secondaryContainer = Color(0xFFE3F2FD),
        gradientColors = listOf(Color(0xFF80D8FF), Color(0xFF40C4FF)),
       calendarBackgroundColor = Color(0xFFF5FBFF),
        todayHighlightColor = Color(0xFF40C4FF),
        selectedDayColor = Color(0xFF80D8FF),
        dayBackgroundColor = Color.White,
        headerColor = Color(0xFF80D8FF),
        favitem=Color(0xFFE0F4FF),
        style=Color(0xFFF0F8FF),
        stats=Color(0xFFEDF7FF)


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