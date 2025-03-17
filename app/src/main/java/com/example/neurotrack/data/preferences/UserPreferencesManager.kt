package com.example.neurotrack.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserPreferencesManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("neurotrack_prefs", Context.MODE_PRIVATE)
    
    private val _preferencesFlow = MutableStateFlow(getUserPreferences())
    val userPreferencesFlow: StateFlow<UserPreferences> = _preferencesFlow
    
    private fun getUserPreferences(): UserPreferences {
        val useDarkTheme = prefs.getBoolean("dark_mode", false)
        val notificationsEnabled = prefs.getBoolean("notifications_enabled", true)
        return UserPreferences(useDarkTheme = useDarkTheme, notificationsEnabled = notificationsEnabled)
    }
    
    suspend fun updateDarkTheme(useDarkTheme: Boolean) {
        prefs.edit().putBoolean("dark_mode", useDarkTheme).apply()
        _preferencesFlow.value = getUserPreferences()
    }
    
    fun getDarkMode(): Boolean {
        return prefs.getBoolean("dark_mode", false)
    }
    
    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean("dark_mode", enabled).apply()
    }
    
    fun getNotificationsEnabled(): Boolean {
        return prefs.getBoolean("notifications_enabled", true)
    }
    
    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
    }
    
    companion object {
        private const val DARK_THEME_KEY = "dark_theme"
    }
}

data class UserPreferences(
    val useDarkTheme: Boolean = false,
    val notificationsEnabled: Boolean = true
) 