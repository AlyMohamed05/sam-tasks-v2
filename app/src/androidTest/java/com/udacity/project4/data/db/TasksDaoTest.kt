package com.udacity.project4.data.db

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.data.models.Task
import com.udacity.project4.di.instrumentationTestModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TasksDaoTest : KoinTest {

    private val samDB: SamDB by inject()
    private val tasksDao:TasksDao by inject()

    @Before
    fun setup(){
        stopKoin()
        startKoin{
            androidContext(ApplicationProvider.getApplicationContext())
            modules(instrumentationTestModule)
        }
    }

    @After
    fun clean(){
        samDB.close()
    }

    @Test
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

    @Test
    fun gettingTaskById_taskNotFound_returnsNull()= runTest {
        // Given 3 tasks in table
        repeat(3){
            tasksDao.upsert(Task())
        }
        assertEquals(3,tasksDao.getTasks().size)

        // When trying to fetch a task by id that doesn't exist
        val task = tasksDao.getTaskById(1000)

        // Then task must be null
        assertNull(task)
    }
}