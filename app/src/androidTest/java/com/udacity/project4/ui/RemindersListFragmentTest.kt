package com.udacity.project4.ui

import android.widget.DatePicker
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.di.instrumentationTestModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import com.udacity.project4.R
import com.udacity.project4.di.appModule
import com.udacity.project4.ui.fragments.home.RemindersListFragment
import com.udacity.project4.ui.fragments.home.RemindersListFragmentDirections
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
class RemindersListFragmentTest : KoinTest{

    @Before
    fun setup() {
        stopKoin()
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(
                appModule,
                instrumentationTestModule,
            )
        }
    }

    @Test
    fun newTaskFab_clicked_navigatesToCreateTaskFragment(){

        // Given a Home fragment with mocked nav controller
        val navController = mock(NavController::class.java)
        val homeFragment = launchFragmentInContainer<RemindersListFragment>(themeResId = R.style.Theme_SAMTasks)
        homeFragment.onFragment{
            Navigation.setViewNavController(it.requireView(),navController)
        }

        // When clicking on create task fab
        onView(
            withId(R.id.new_task_fab)
        ).perform(click())

        // Then navController should navigate to CreateTaskFragment
        verify(navController).navigate(
            RemindersListFragmentDirections.actionHomeFragmentToCreateTaskFragment()
        )
    }

    @Test
    fun calenderFab_clicked_showDatePicker(){

        // Given a Home fragment
        val homeFragment = launchFragmentInContainer<RemindersListFragment>(themeResId = R.style.Theme_SAMTasks)

        // When clicking on calendar fab
        onView(
            withId(R.id.calender_fab)
        ).perform(click())

        // When clicking on calender fab
        onView(
            isAssignableFrom(DatePicker::class.java)
        ).check(matches(isDisplayed()))
    }
}