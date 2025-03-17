package com.example.neurotrack.di

import com.example.neurotrack.BuildConfig
import com.example.neurotrack.data.service.GeminiService
import com.example.neurotrack.data.repository.BehaviorRecordRepository
import com.example.neurotrack.ui.screens.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dashboardModule = module {
    // GeminiService
    single { GeminiService(BuildConfig.GEMINI_API_KEY) }
    
    // Registrar o DashboardViewModel com o BehaviorRecordDao
    viewModel { DashboardViewModel(get()) }
    
    // Se precisar de outras dependências para o dashboard, declare-as aqui
} 