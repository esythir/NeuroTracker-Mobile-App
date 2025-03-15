package com.example.neurotrack.di

import com.example.neurotrack.ui.screens.addscreen.AddViewModel
import com.example.neurotrack.ui.screens.calendar.viewmodel.CalendarViewModel
import com.example.neurotrack.ui.screens.history.HistoryViewModel
import com.example.neurotrack.ui.screens.home.HomeViewModel
import com.example.neurotrack.ui.screens.dashboard.DashboardViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.time.LocalDate

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { (initialDate: LocalDate?) -> AddViewModel(get(), initialDate) }
    viewModel { CalendarViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { DashboardViewModel(get(), get()) }
} 