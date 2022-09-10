package com.udacity.project4.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.di.testModule
import com.udacity.project4.ui.fragments.home.RemindersListViewModel
import com.udacity.project4.utils.getOrAwaitValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.junit.Assert.*

/**
 *  ** Notice ** this will test HomeViewModel which acts exactly as the list that hosts
 *     Reminders (Tasks) in this project.
 */
@RunWith(AndroidJUnit4::class)
class RemindersListViewModelTest: KoinTest {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create{
        modules(testModule)
    }

    private val remindersListViewModel: RemindersListViewModel by inject()

    @Test
    fun `sendTaskCreateEvent() updates the livedata value`(){

        // When sendTaskCreateEvent() is called on Home View Model
        remindersListViewModel.sendCreateTaskEvent()

        // Then value of createNewTask livedata should be true
        val shouldCreateNewTask = remindersListViewModel.createNewTask.getOrAwaitValue()
        assertTrue(shouldCreateNewTask)
    }

    @Test
    fun `sendShowDatePickerEvent() updates showDatePicker livedata`(){

        // When sendShowDatePickerEvent() is called on homeViewModel
        remindersListViewModel.sendShowDatePickerEvent()

        // Then value of showDatePicker should be true
        val showDatePicker = remindersListViewModel.showDatePicker.getOrAwaitValue()
        assertTrue(showDatePicker)
    }

    @Test
    fun `createNewTask value resets when resetCreateNewTaskEvent() is called`(){

        // Given that value of createNewTask is true
        remindersListViewModel.sendCreateTaskEvent()
        val shouldCreateNewTask = remindersListViewModel.createNewTask.getOrAwaitValue()
        assertTrue(shouldCreateNewTask)

        // When resetCreateNewTaskEvent() is called
        remindersListViewModel.resetCreateNewTaskEvent()

        // Then value of createNewTask must be false
        val currentValue = remindersListViewModel.createNewTask.getOrAwaitValue()
        assertFalse(currentValue)
    }

    @Test
    fun `showDatePicker livedata value resets when resetShowDatePickerEvent() is called`(){

        // Given that value of showDatePicker is true
        remindersListViewModel.sendShowDatePickerEvent()
        val showDatePicker = remindersListViewModel.showDatePicker.getOrAwaitValue()
        assertTrue(showDatePicker)

        // When resetShowDatePickerEvent() is called
        remindersListViewModel.resetShowDatePickerEvent()

        // Then value must go back to false
        val currentValue = remindersListViewModel.showDatePicker.getOrAwaitValue()
        assertFalse(currentValue)
    }
}