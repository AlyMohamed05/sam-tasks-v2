package com.example.samtasks.di

import android.content.Context
import androidx.room.Room
import com.example.samtasks.auth.Authenticator
import com.example.samtasks.auth.AuthenticatorImpl
import com.example.samtasks.data.db.SamDB
import com.example.samtasks.data.db.TasksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object AppModule {

    @Provides
    @Singleton
    fun provideAuthenticator(): Authenticator {
        return AuthenticatorImpl()
    }

    @Provides
    @Singleton
    fun provideSamDB(@ApplicationContext context: Context): SamDB {
        return Room.databaseBuilder(
            context,
            SamDB::class.java,
            "SamDB.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTasksDao(samDB: SamDB): TasksDao {
        return samDB.tasksDao
    }
}