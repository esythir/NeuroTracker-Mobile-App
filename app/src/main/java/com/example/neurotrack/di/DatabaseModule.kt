package com.example.neurotrack.di

import android.app.Application
import androidx.room.Room
import com.example.neurotrack.data.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val databaseModule = module {
    single { provideDatabase(androidApplication()) }
    single { provideBehaviorDao(get()) }
    single { provideBehaviorRecordDao(get()) }
    single { provideScheduleDao(get()) }
}

private fun provideDatabase(application: Application): AppDatabase {
    return Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "neurotrack_database"
    ).build()
}

private fun provideBehaviorDao(database: AppDatabase) = database.behaviorDao()

private fun provideBehaviorRecordDao(database: AppDatabase) = database.behaviorRecordDao()

private fun provideScheduleDao(database: AppDatabase) = database.scheduleDao() 