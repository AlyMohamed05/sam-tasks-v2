package com.udacity.project4.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.maps.model.LatLng
import com.udacity.project4.di.testModule
import com.udacity.project4.ui.fragments.create_edit.SaveReminderViewModel
import com.udacity.project4.utils.DispatchersProvider
import com.udacity.project4.utils.getOrAwaitValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SaveReminderViewModelTest : KoinTest {

    @get:Rule
    val instantExecutor = InstantTaskExecutorRule()

    val dispatcher = StandardTestDispatcher()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            testModule,
            module {

                single<DispatchersProvider> {
                    object : DispatchersProvider {
                        override val main: CoroutineDispatcher
                            get() = dispatcher
                        override val io: CoroutineDispatcher
                            get() = dispatcher
                        override val default: CoroutineDispatcher
                            get() = dispatcher
                        override val unconfined: CoroutineDispatcher
                            get() = dispatcher
                    }
                }

                viewModel {
                    SaveReminderViewModel(
                        ApplicationProvider.getApplicationContext(),
                        get(),
                        get()
                    )
                }

            }
        )
    }

    private val createTaskViewModel: SaveReminderViewModel by inject()

    @Test
    fun `create task will sets jobFinished value to true`(){

        // When creating a new task
        createTaskViewModel.createTask()

        // Then value of jobFinished must be true
        val jobFinished = createTaskViewModel.jobFinished.getOrAwaitValue()
        assertTrue(jobFinished)
    }

    @Test
    fun `setDate() changes value of value of date`(){

        // Given that no date or time set
        assertFalse(createTaskViewModel.isDateOrTimeSet)

        // When setting date
        createTaskViewModel.setDate("2022109")

        // Then isDateOrTimeSet must be true
        assertTrue(createTaskViewModel.isDateOrTimeSet)
    }

    @Test
    fun `setLocation() updates the value of taskLocation livedata`(){

        // Given that taskLocation is initially null
        val location = createTaskViewModel.taskLocation.getOrAwaitValue()
        assertNull(location)

        // When calling setLocation
        createTaskViewModel.setTaskLocation(LatLng(0.0,0.0))

        // Then taskLocation can't be null
        val newValue = createTaskViewModel.taskLocation.getOrAwaitValue()
        assertNotNull(newValue)
    }
}