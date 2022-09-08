package com.udacity.project4

import android.app.Application
import com.udacity.project4.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class SamApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // TODO : Handle logging in release mode
        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@SamApplication)
            modules(appModule)
        }

    }
}