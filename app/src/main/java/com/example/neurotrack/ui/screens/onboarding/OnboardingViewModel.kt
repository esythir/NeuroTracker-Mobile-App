package com.example.neurotrack.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.preferences.UserPreferencesManager
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userPreferencesManager: UserPreferencesManager
) : ViewModel() {
    
    fun saveUserName(name: String) {
        viewModelScope.launch {
            userPreferencesManager.saveUserName(name)
        }
    }
    
    fun completeOnboarding() {
        viewModelScope.launch {
            userPreferencesManager.completeOnboarding()
        }
    }
} 