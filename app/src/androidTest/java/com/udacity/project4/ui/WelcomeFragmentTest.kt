package com.udacity.project4.ui

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.ui.fragments.welcome.WelcomeFragment
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import com.udacity.project4.R
import com.udacity.project4.ui.fragments.welcome.WelcomeFragmentDirections
import org.junit.Test
import org.koin.test.KoinTest
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
class WelcomeFragmentTest:KoinTest{

    @Test
    fun continueWithoutLoginTextButton_clicked_navigatesToHomeFragment(){

        val navController = mock(NavController::class.java)

        // Given a Welcome fragment with mocked nav controller
        val welcomeFragment = launchFragmentInContainer<WelcomeFragment>(themeResId = R.style.Theme_SAMTasks)
        welcomeFragment.withFragment {
            Navigation.setViewNavController(requireView(),navController)
        }

        // When pressing on continue without login
        onView(
            withId(R.id.continue_text_button)
        ).perform(click())

        // Then should navigate to home fragment
        verify(navController).navigate(
            WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment()
        )
    }
}