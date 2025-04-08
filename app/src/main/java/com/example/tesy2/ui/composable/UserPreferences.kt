package com.example.tesy2.ui.composable
import android.content.Context
import android.content.SharedPreferences

object UserPreferences {

    private const val PREF_NAME = "settings"
    private const val KEY_NAME = "userName"
    private const val KEY_EMAIL = "userEmail"
    private const val KEY_THEME = "userTheme"
    private const val KEY_GENDER = "userGender"
    private const val KEY_JOB = "userJob"
    private const val KEY_LOCATION = "userLocation"
    private const val KEY_BIRTHDATE = "userBirthDate"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    fun clear(context: Context) {
        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }

    fun saveUserInfo(
        context: Context,
        name: String,
        email: String,
        theme: String,
        gender: String,
        job: String,
        location: String,
        birthDate: String
    ) {
        getPrefs(context).edit().apply {
            putString(KEY_NAME, name)
            putString(KEY_EMAIL, email)
            putString(KEY_THEME, theme)
            putString(KEY_GENDER, gender)
            putString(KEY_JOB, job)
            putString(KEY_LOCATION, location)
            putString(KEY_BIRTHDATE, birthDate)
            apply()
        }
    }

     fun getUserInfo(context: Context): Map<String, String> {
        val prefs = getPrefs(context)
        return mapOf(
            "name" to (prefs.getString(KEY_NAME, "") ?: ""),
            "email" to (prefs.getString(KEY_EMAIL, "") ?: ""),
            "theme" to (prefs.getString(KEY_THEME, "pink") ?: "pink"),
            "gender" to (prefs.getString(KEY_GENDER, "") ?: ""),
            "job" to (prefs.getString(KEY_JOB, "") ?: ""),
            "location" to (prefs.getString(KEY_LOCATION, "") ?: ""),
            "birthDate" to (prefs.getString(KEY_BIRTHDATE, "") ?: "")
        )
    }
}
