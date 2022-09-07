package com.example.samtasks.ui.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.samtasks.ui.fragments.welcome.WelcomeFragment
import com.example.samtasks.utils.launchFragmentInHiltActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import com.example.samtasks.R
import com.example.samtasks.ui.fragments.welcome.WelcomeFragmentDirections
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class WelcomeFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun loginButton_clicked_navigatesToAuthActivity() {
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltActivity<WelcomeFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(
            withId(R.id.login_button)
        ).perform(click())
        verify(navController).navigate(WelcomeFragmentDirections.actionWelcomeFragmentToAuthActivity())
    }

    @Test
    fun continueWithoutLogin_clicked_navigatesToHome() {
        val navController = Mockito.mock(NavController::class.java)
        launchFragmentInHiltActivity<WelcomeFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(
            withId(R.id.continue_text_button)
        ).perform(click())
        verify(navController).navigate(WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment())
    }
}