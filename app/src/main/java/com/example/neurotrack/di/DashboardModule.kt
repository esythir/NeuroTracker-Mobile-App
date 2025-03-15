package com.example.neurotrack.di

import com.example.neurotrack.BuildConfig
import com.example.neurotrack.data.service.GeminiService
import org.koin.dsl.module

val dashboardModule = module {
    // GeminiService
    single { GeminiService(BuildConfig.GEMINI_API_KEY) }
} 