package com.example.neurotrack.di

import com.example.neurotrack.data.repository.BehaviorRecordRepository
import com.example.neurotrack.data.repository.BehaviorRepository
import com.example.neurotrack.data.repository.ScheduleRepository
import com.example.neurotrack.ui.viewmodels.BehaviorViewModel
import com.example.neurotrack.ui.viewmodels.ScheduleViewModel
import com.example.neurotrack.ui.viewmodels.BehaviorRecordViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Repositories
    single { BehaviorRecordRepository(get()) }
    single { BehaviorRepository(get()) }
    single { ScheduleRepository(get()) }

    // ViewModels
    viewModel { BehaviorViewModel(get()) }
    viewModel { ScheduleViewModel(get()) }
    viewModel { BehaviorRecordViewModel(get()) }
} 