package com.example.neurotrack.di

import com.example.neurotrack.BuildConfig
import com.example.neurotrack.data.service.GeminiService
import com.example.neurotrack.data.repository.BehaviorRepository
import com.example.neurotrack.data.repository.BehaviorRecordRepository
import org.koin.dsl.module

val appModule = module {
    // Services
    single { GeminiService(BuildConfig.GEMINI_API_KEY) }

    // Repositories
    single { BehaviorRepository(get()) }
    single { BehaviorRecordRepository(get()) }
} 