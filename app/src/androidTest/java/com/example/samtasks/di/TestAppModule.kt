package com.example.samtasks.di

import android.content.Context
import androidx.room.Room
import com.example.samtasks.auth.Authenticator
import com.example.samtasks.auth.FakeAuthenticator
import com.example.samtasks.data.db.SamDB
import com.example.samtasks.data.db.TasksDao
import com.example.samtasks.utils.DispatchersProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
@Suppress("unused")
object TestAppModule {

    @Provides
    fun provideAuthenticator(): Authenticator = FakeAuthenticator()

    @Provides
    fun provideSamDB(@ApplicationContext context: Context): SamDB {
        return Room.inMemoryDatabaseBuilder(
            context,
            SamDB::class.java,
        )
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideTasksDao(samDB: SamDB) : TasksDao = samDB.tasksDao

    @OptIn(ExperimentalCoroutinesApi::class)
    @Provides
    fun provideDispatchersProvider(): DispatchersProvider{
        val testScope = StandardTestDispatcher()
        return object : DispatchersProvider{
            override val main: CoroutineDispatcher
                get() = testScope
            override val io: CoroutineDispatcher
                get() = testScope
            override val default: CoroutineDispatcher
                get() = testScope
            override val unconfined: CoroutineDispatcher
                get() =testScope
        }
    }

}