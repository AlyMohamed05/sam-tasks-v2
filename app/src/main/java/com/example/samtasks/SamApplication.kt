package com.example.samtasks

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SamApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO : Handle logging in release mode
        Timber.plant(Timber.DebugTree())
    }
}