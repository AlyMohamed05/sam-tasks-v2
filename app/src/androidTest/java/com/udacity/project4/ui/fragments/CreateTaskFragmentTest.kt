package com.udacity.project4.ui.fragments

import android.widget.DatePicker
import android.widget.TimePicker
import com.udacity.project4.R
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.MediumTest
import com.udacity.project4.ui.fragments.create_edit.CreateTaskFragment
import com.udacity.project4.utils.launchFragmentInHiltActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class CreateTaskFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun backButton_clicked_navigateUpIsCalled() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltActivity<CreateTaskFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(
            withId(R.id.back_button)
        ).perform(click())
        verify(navController).navigateUp()
    }

    @Test
    fun createFab_clicked_navigatesUp() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltActivity<CreateTaskFragment> {
            Navigation.setViewNavController(requireView(), navController)
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
        launchFragmentInHiltActivity<CreateTaskFragment>()

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
        launchFragmentInHiltActivity<CreateTaskFragment>()

        // When time button clicked
        onView(
            withId(R.id.time_button)
        ).perform(click())

        // Then TimePicker must show
        onView(isAssignableFrom(TimePicker::class.java))
            .check(matches(isDisplayed()))
    }

    @Test
    fun locationButton_clicked_modalBottomSheepShows(){
        launchFragmentInHiltActivity<CreateTaskFragment>()

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