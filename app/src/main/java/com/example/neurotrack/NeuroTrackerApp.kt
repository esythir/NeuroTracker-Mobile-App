package com.example.neurotrack

import android.app.Application
import com.example.neurotrack.di.appModule
import com.example.neurotrack.di.databaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class NeuroTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidLogger()
            androidContext(this@NeuroTrackerApp)
            modules(listOf(appModule, databaseModule))
        }
    }
} 