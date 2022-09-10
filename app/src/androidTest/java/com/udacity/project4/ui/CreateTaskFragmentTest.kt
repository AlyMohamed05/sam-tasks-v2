package com.udacity.project4.ui

import android.view.View
import android.widget.DatePicker
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.di.appModule
import com.udacity.project4.di.instrumentationTestModule
import com.udacity.project4.ui.fragments.create_edit.CreateTaskFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import com.udacity.project4.R
import com.udacity.project4.ui.fragments.create_edit.CreateTaskFragmentDirections
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
class CreateTaskFragmentTest {

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
    fun topBackButton_clicked_navigatesUp() {

        // Given a CreateTaskFragment with mocked navController
        val navController = mock(NavController::class.java)
        val createTaskFragment =
            launchFragmentInContainer<CreateTaskFragment>(themeResId = R.style.Theme_SAMTasks)
        createTaskFragment.withFragment {
            Navigation.setViewNavController(requireView(), navController)
        }

        // When clicking on back button in top left corner of the fragment
        onView(
            withId(R.id.back_button)
        ).perform(click())

        // Then navigateUp is called
        verify(navController).navigateUp()
    }

    @Test
    fun createButton_clicked_navigatesUpToHome() {

        // Given a CreateTaskFragment with mocked navController
        val navController = mock(NavController::class.java)
        val createTaskFragment =
            launchFragmentInContainer<CreateTaskFragment>(themeResId = R.style.Theme_SAMTasks)
        createTaskFragment.withFragment {
            Navigation.setViewNavController(requireView(), navController)
        }
        Thread.sleep(500L)

        // When clicking on create fab button
        onView(
            withId(R.id.create_fab)
        ).perform(click())

        // Then navigate up is called
        verify(navController).navigateUp()
    }

    @Test
    fun locationButton_clicked_navigatesToLocationFragment() {

        // Given a CreateTaskFragment with mocked navController
        val navController = mock(NavController::class.java)
        val createTaskFragment =
            launchFragmentInContainer<CreateTaskFragment>(themeResId = R.style.Theme_SAMTasks)
        createTaskFragment.withFragment {
            Navigation.setViewNavController(requireView(), navController)
        }

        // When clicking on location button
        onView(
            withId(R.id.location_button)
        ).perform(click())

        // Then navigate to location fragment is called
        verify(navController).navigate(CreateTaskFragmentDirections.actionCreateTaskFragmentToLocationPicker())
    }

    @Test
    fun calendarButton_clicked_showsDatePicker() {

        // Given a CreateTaskFragment
            launchFragmentInContainer<CreateTaskFragment>(themeResId = R.style.Theme_SAMTasks)

        // When calendar button clicked
        onView(
            withId(R.id.calender_button)
        ).perform(click())

        // Then date picker shows
        onView(
            isAssignableFrom(DatePicker::class.java)
        ).check(matches(isDisplayed()))
    }

    @Test
    fun dateIsSet_attemptingToSetLocation_toastAppears() {
        // This test ensures user can't set both date and location

        // Given a CreateTaskFragment
        val createTaskFragment=launchFragmentInContainer<CreateTaskFragment>(themeResId = R.style.Theme_SAMTasks)
        var decorView: View? =null
        createTaskFragment.withFragment {
            decorView  = activity?.window?.decorView
        }

        // When Setting date then attempting to set location
        onView(
            withId(R.id.calender_button)
        ).perform(click())
        onView(
            withId(android.R.id.button1)
        ).perform(click())
        onView(
            withId(R.id.location_button)
        ).perform(click())

        // Then toast message appears
        onView(
            withText(R.string.prevent_set_location)
        ).inRoot(
            withDecorView(not(decorView))
        ).check(matches(isDisplayed()))
    }
}