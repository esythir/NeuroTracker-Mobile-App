package com.example.neurotrack.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.neurotrack.data.local.AppDatabase
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "neurotrack-db"
        )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Pré-popular comportamentos
                db.execSQL("""
                    INSERT INTO behaviors (id, name, description, type)
                    VALUES (1, 'Crise Emocional', 'Momentos de crise emocional', 'NEGATIVE')
                """)
                db.execSQL("""
                    INSERT INTO behaviors (id, name, description, type)
                    VALUES (2, 'Seletividade Alimentar', 'Comportamentos relacionados à alimentação', 'NEGATIVE')
                """)
            }
        })
        .build()
    }
    
    single { get<AppDatabase>().behaviorDao() }
    single<BehaviorRecordDao> { get<AppDatabase>().behaviorRecordDao() }
    single { get<AppDatabase>().scheduleDao() }
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