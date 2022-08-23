package com.example.samtasks.di

import android.content.Context
import androidx.room.Room
import com.example.samtasks.auth.Authenticator
import com.example.samtasks.auth.AuthenticatorImpl
import com.example.samtasks.data.db.SamDB
import com.example.samtasks.data.db.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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
            context.applicationContext,
            SamDB::class.java,
            "SamTasks.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTasksDao(samDB: SamDB): TaskDao {
        return samDB.tasksDao
    }
}