package com.udacity.project4.di

import com.udacity.project4.data.TasksDataSource
import com.udacity.project4.data.repository.FakeDataSource
import com.udacity.project4.ui.fragments.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val testModule = module {

    factory<TasksDataSource> {
        FakeDataSource()
    }

    viewModel {
        HomeViewModel(get())
    }

}