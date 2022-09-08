package com.udacity.project4.ui.fragments

import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import com.udacity.project4.R
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.di.instrumentedTestModule
import com.udacity.project4.ui.fragments.create_edit.CreateTaskFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class CreateTaskFragmentTest : KoinTest {

    @Before
    fun setup() {
        loadKoinModules(instrumentedTestModule)
    }

    @After
    fun clean() {
        unloadKoinModules(instrumentedTestModule)
    }

    @Test
    fun backButton_clicked_navigateUpIsCalled() {
        val navController = mock(NavController::class.java)
        val scenario = launchFragmentInContainer<CreateTaskFragment>(themeResId = R.style.Theme_SAMTasks)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }
        onView(
            withId(R.id.back_button)
        ).perform(click())
        verify(navController).navigateUp()
    }

    @Test
    fun createFab_clicked_navigatesUp() {
        val navController = mock(NavController::class.java)

        val scenario = launchFragmentInContainer<CreateTaskFragment>(themeResId = R.style.Theme_SAMTasks)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        // Wait for fab button to show
        Thread.sleep(1000L)

        // When Clicking on create button the viewModel creates a task and navigateUp() is called
        // to go back to home page where tasks are presented.
        onView(
            withId(R.id.create_fab)
        ).perform(click())

        // Then navigate up should be called
        verify(navController).navigateUp()
    }

    @Test
    fun calendarButton_clicked_datePickerIsDisplayed() {
        launchFragmentInContainer<CreateTaskFragment>(themeResId = R.style.Theme_SAMTasks)

        // When clicking on calendar icon
        onView(
            withId(R.id.calender_button)
        ).perform(click())

        // Then DatePicker must show
        onView(isAssignableFrom(DatePicker::class.java))
            .check(matches(isDisplayed()))
    }

    @Test
    fun timePickerButton_clicked_timePickerIsDisplayed() {
        launchFragmentInContainer<CreateTaskFragment>(themeResId = R.style.Theme_SAMTasks)

        // When time button clicked
        onView(
            withId(R.id.time_button)
        ).perform(click())

        // Then TimePicker must show
        onView(isAssignableFrom(TimePicker::class.java))
            .check(matches(isDisplayed()))
    }

    @Test
    fun locationButton_clicked_modalBottomSheepShows() {
        launchFragmentInContainer<CreateTaskFragment>(themeResId = R.style.Theme_SAMTasks)

        // When location button clicked
        onView(
            withId(R.id.location_button)
        ).perform(click())

        // Then Bottom sheet must show

        onView(
            withContentDescription("Google Map")
        ).check(matches(isDisplayed()))
    }
}