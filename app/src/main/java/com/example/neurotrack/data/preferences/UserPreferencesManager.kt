package com.example.neurotrack.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow

// Data class to hold user preferences
data class UserPreferences(
    val useDarkTheme: Boolean = false,
    val notificationsEnabled: Boolean = true
)

class UserPreferencesManager(private val context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences("neurotrack_prefs", Context.MODE_PRIVATE)
    
    private val _preferencesFlow = MutableStateFlow(getUserPreferences())
    val userPreferencesFlow: StateFlow<UserPreferences> = _preferencesFlow
    
    val isOnboardingCompleted: Flow<Boolean> = flow {
        emit(prefs.getBoolean("onboarding_completed", false))
    }
    
    val userName: Flow<String> = flow {
        emit(prefs.getString("user_name", "") ?: "")
    }
    
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
    
    fun getNotificationsEnabled(): Boolean {
        return prefs.getBoolean("notifications_enabled", true)
    }
    
    suspend fun toggleDarkMode() {
        val currentMode = getDarkMode()
        updateDarkTheme(!currentMode)
    }
    
    suspend fun toggleNotifications() {
        val current = getNotificationsEnabled()
        prefs.edit().putBoolean("notifications_enabled", !current).apply()
        _preferencesFlow.value = getUserPreferences()
    }
    
    suspend fun completeOnboarding() {
        prefs.edit().putBoolean("onboarding_completed", true).apply()
    }
    
    suspend fun saveUserName(name: String) {
        prefs.edit().putString("user_name", name).apply()
    }
} 