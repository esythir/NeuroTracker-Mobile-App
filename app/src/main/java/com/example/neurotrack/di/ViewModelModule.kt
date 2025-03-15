package com.example.neurotrack.di

import com.example.neurotrack.ui.screens.calendar.viewmodel.CalendarViewModel
import com.example.neurotrack.ui.screens.history.HistoryViewModel
import com.example.neurotrack.ui.screens.home.HomeViewModel
import com.example.neurotrack.ui.screens.addscreen.AddViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { CalendarViewModel() }
    viewModel { HistoryViewModel(get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { AddViewModel(get()) }
} 