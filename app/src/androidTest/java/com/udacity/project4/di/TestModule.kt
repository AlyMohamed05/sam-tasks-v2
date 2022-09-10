package com.udacity.project4.di

import androidx.room.Room
import com.udacity.project4.data.db.SamDB
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val instrumentationTestModule = module {

    factory {
        Room.inMemoryDatabaseBuilder(
            androidContext(),
            SamDB::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    factory {
        get<SamDB>().tasksDao
    }
}