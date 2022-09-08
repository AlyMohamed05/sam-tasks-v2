package com.udacity.project4.ui.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.ui.fragments.welcome.WelcomeFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.mockito.Mockito
import com.udacity.project4.R
import com.udacity.project4.ui.fragments.welcome.WelcomeFragmentDirections
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
@MediumTest
@RunWith(AndroidJUnit4::class)
class WelcomeFragmentTest {

    @Test
    fun loginButton_clicked_navigatesToAuthActivity() {
        val navController = Mockito.mock(NavController::class.java)
        val scenario = launchFragmentInContainer<WelcomeFragment>(themeResId = R.style.Theme_SAMTasks)
        scenario.onFragment{
            Navigation.setViewNavController(it.requireView(),navController)
        }
        onView(
            withId(R.id.login_button)
        ).perform(click())
        verify(navController).navigate(WelcomeFragmentDirections.actionWelcomeFragmentToAuthActivity())
    }

    @Test
    fun continueWithoutLogin_clicked_navigatesToHome() {
        val navController = Mockito.mock(NavController::class.java)
        val scenario = launchFragmentInContainer<WelcomeFragment>(themeResId = R.style.Theme_SAMTasks)
        scenario.onFragment{
            Navigation.setViewNavController(it.requireView(),navController)
        }
        onView(
            withId(R.id.continue_text_button)
        ).perform(click())
        verify(navController).navigate(WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment())
    }
}