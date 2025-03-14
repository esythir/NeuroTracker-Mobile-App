package com.example.neurotrack.di

import android.app.Application
import androidx.room.Room
import com.example.neurotrack.data.local.AppDatabase
import com.example.neurotrack.data.repository.BehaviorRecordRepository
import com.example.neurotrack.data.repository.BehaviorRepository
import com.example.neurotrack.data.repository.ScheduleRepository
import com.example.neurotrack.ui.viewmodels.BehaviorViewModel
import com.example.neurotrack.ui.viewmodels.ScheduleViewModel
import com.example.neurotrack.ui.viewmodels.BehaviorRecordViewModel
import com.example.neurotrack.ui.screens.calendar.viewmodel.CalendarViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Database
    single { 
        Room.databaseBuilder(
            androidApplication(),
            AppDatabase::class.java,
            "neurotrack_database"
        ).build() 
    }

    // DAOs
    single { get<AppDatabase>().scheduleDao() }
    single { get<AppDatabase>().behaviorDao() }
    single { get<AppDatabase>().behaviorRecordDao() }

    // Repositories
    single { BehaviorRecordRepository(get()) }
    single { BehaviorRepository(get()) }
    single { ScheduleRepository(get()) }

    // ViewModels
    viewModel { BehaviorViewModel(get()) }
    viewModel { ScheduleViewModel(get()) }
    viewModel { BehaviorRecordViewModel(get()) }
    viewModel { CalendarViewModel(get(), get()) }
} 