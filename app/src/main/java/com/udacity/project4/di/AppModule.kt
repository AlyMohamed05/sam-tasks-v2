package com.udacity.project4.di

import androidx.room.Room
import com.udacity.project4.auth.Authenticator
import com.udacity.project4.auth.AuthenticatorImpl
import com.udacity.project4.data.db.SamDB
import com.udacity.project4.ui.activities.host.HostViewModel
import com.udacity.project4.ui.fragments.create_edit.CreateTaskViewModel
import com.udacity.project4.ui.fragments.home.HomeViewModel
import com.udacity.project4.ui.fragments.location.LocationPickerViewModel
import com.udacity.project4.utils.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<Authenticator> {
        AuthenticatorImpl()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            SamDB::class.java,
            "SamDB.db"
        ).build()
    }

    single {
        val samDb: SamDB = get()
        samDb.tasksDao
    }

    single<DispatchersProvider> {
        object : DispatchersProvider {
            override val main: CoroutineDispatcher
                get() = Dispatchers.Main
            override val io: CoroutineDispatcher
                get() = Dispatchers.IO
            override val default: CoroutineDispatcher
                get() = Dispatchers.Default
            override val unconfined: CoroutineDispatcher
                get() = Dispatchers.Unconfined
        }
    }

    viewModel {
        HostViewModel(get())
    }

    viewModel {
        CreateTaskViewModel(
            androidContext(),
            get(),
            get()
        )
    }

    viewModel{
        HomeViewModel(get(),get())
    }

    viewModel{
        LocationPickerViewModel()
    }

}