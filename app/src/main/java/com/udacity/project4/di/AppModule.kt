package com.udacity.project4.di

import androidx.room.Room
import com.udacity.project4.data.TasksDataSource
import com.udacity.project4.data.db.SamDB
import com.udacity.project4.data.repository.TasksLocalDataSource
import com.udacity.project4.ui.activities.host.HostViewModel
import com.udacity.project4.ui.fragments.create_edit.SaveReminderViewModel
import com.udacity.project4.ui.fragments.home.RemindersListViewModel
import com.udacity.project4.ui.fragments.location.LocationPickerViewModel
import com.udacity.project4.utils.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

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

    single<TasksDataSource> {
        TasksLocalDataSource(get())
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
        HostViewModel()
    }

    viewModel {
        SaveReminderViewModel(
            androidContext(),
            get(),
            get()
        )
    }

    viewModel {
        RemindersListViewModel(get())
    }

    viewModel {
        LocationPickerViewModel()
    }

}