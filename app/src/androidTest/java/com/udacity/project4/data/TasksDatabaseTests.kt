package com.udacity.project4.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.data.db.SamDB
import com.udacity.project4.data.db.TasksDao
import com.udacity.project4.data.models.Task
import com.udacity.project4.di.instrumentedTestModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import org.junit.Assert.*
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest
import org.koin.test.inject

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class TasksDatabaseTests : KoinTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val database: SamDB by inject()

    private val tasksDao: TasksDao by inject()

    @Before
    fun createDB() {
        loadKoinModules(instrumentedTestModule)
    }

    @After
    fun clear(){
        unloadKoinModules(instrumentedTestModule)
    }

    @After
    @Throws(IOException::class)
    fun clean() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsert() {
        runTest {
            tasksDao.upsert(
                Task(
                    title = "Title",
                    content = "Content",
                )
            )
            val tasks = tasksDao.getTasks()
            assertEquals(1, tasks.size)
            val task = tasks[0]
            assertEquals("Title", task.title)
            assertEquals("Content", task.content)
        }
    }

    @Test
    @Throws(Exception::class)
    fun testDelete() {
        runTest {
            repeat(5) {
                tasksDao.upsert(Task())
            }
            var tasks = tasksDao.getTasks()
            assertEquals(5, tasks.size)
            repeat(3) {
                tasksDao.delete(tasks[it])
            }
            tasks = tasksDao.getTasks()
            assertEquals(2, tasks.size)
        }
    }
}