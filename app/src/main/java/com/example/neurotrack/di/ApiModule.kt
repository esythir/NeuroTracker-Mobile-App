package com.example.neurotrack.di

import com.example.neurotrack.data.repository.ConvertApiRepository
import org.koin.dsl.module

val apiModule = module {
    single { ConvertApiRepository() }
} 