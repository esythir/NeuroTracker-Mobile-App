package com.example.neurotrack.di

import com.example.neurotrack.data.repository.DataExportRepository
import com.example.neurotrack.data.preferences.UserPreferencesManager
import com.example.neurotrack.data.repository.ConvertApiRepository
import com.example.neurotrack.ui.screens.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single { DataExportRepository(get(), get()) }
    single { UserPreferencesManager(get()) }
    viewModel { SettingsViewModel(get<ConvertApiRepository>()) }
} 