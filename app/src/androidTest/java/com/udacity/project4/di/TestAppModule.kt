package com.udacity.project4.di

import androidx.room.Room
import com.udacity.project4.auth.Authenticator
import com.udacity.project4.auth.FakeAuthenticator
import com.udacity.project4.data.db.SamDB
import com.udacity.project4.utils.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

@ExperimentalCoroutinesApi
val instrumentedTestModule = module(override = true) {

    factory<Authenticator> {
        FakeAuthenticator()
    }

    factory {
        Room.inMemoryDatabaseBuilder(
            androidContext(),
            SamDB::class.java,
        )
            .allowMainThreadQueries()
            .build()
    }

    factory {
        get<SamDB>().tasksDao
    }

    factory<DispatchersProvider> {
        val testScope = StandardTestDispatcher()
        object : DispatchersProvider {
            override val main: CoroutineDispatcher
                get() = testScope
            override val io: CoroutineDispatcher
                get() = testScope
            override val default: CoroutineDispatcher
                get() = testScope
            override val unconfined: CoroutineDispatcher
                get() = testScope
        }
    }
}