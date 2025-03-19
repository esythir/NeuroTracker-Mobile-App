package com.example.neurotrack.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.neurotrack.data.repository.BehaviorRecordRepository
import com.example.neurotrack.ui.screens.addscreen.AddViewModel
import com.example.neurotrack.ui.screens.calendar.viewmodel.CalendarViewModel
import com.example.neurotrack.ui.screens.history.HistoryViewModel
import com.example.neurotrack.ui.screens.home.HomeViewModel
import com.example.neurotrack.ui.screens.onboarding.OnboardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { (initialDate: LocalDate?) -> AddViewModel(get(), initialDate) }
    viewModel { CalendarViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
    viewModel { OnboardingViewModel(get()) }
} 