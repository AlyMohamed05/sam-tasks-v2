package com.example.samtasks

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.samtasks.data.db.SamDB
import com.example.samtasks.data.db.TasksDao
import com.example.samtasks.data.models.Task
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class TasksDatabaseTests {

    private lateinit var database: SamDB
    private lateinit var tasksDao: TasksDao

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, SamDB::class.java).build()
        tasksDao = database.tasksDao
    }

    @After
    @Throws(IOException::class)
    fun clean() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsert() {
        runBlocking {
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
        runBlocking {
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