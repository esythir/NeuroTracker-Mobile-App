package com.example.neurotrack.di

import com.example.neurotrack.BuildConfig
import com.example.neurotrack.data.service.GeminiService
import com.example.neurotrack.data.repository.BehaviorRepository
import com.example.neurotrack.data.repository.BehaviorRecordRepository
import com.example.neurotrack.data.preferences.UserPreferencesManager
import com.example.neurotrack.data.repository.DataExportRepository
import com.example.neurotrack.data.repository.ConvertApiRepository
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.neurotrack.ui.screens.settings.SettingsViewModel
import com.example.neurotrack.ui.screens.dashboard.DashboardViewModel

val appModule = module {
    // Services
    single { GeminiService(BuildConfig.GEMINI_API_KEY) }

    // Repositories
    single { BehaviorRepository(get()) }
    single { BehaviorRecordRepository(get()) }
    single { DataExportRepository(get(), get()) }
    single { ConvertApiRepository() }

    // Preferences
    single { UserPreferencesManager(androidContext()) }

    // ViewModels
    viewModel { SettingsViewModel(get(), get()) }
    viewModel { DashboardViewModel(get()) }
} 