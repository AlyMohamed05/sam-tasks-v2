package com.udacity.project4.ui.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.udacity.project4.ui.fragments.login.LoginFragment
import com.udacity.project4.utils.launchFragmentInHiltActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import com.udacity.project4.R
import com.udacity.project4.ui.fragments.login.LoginFragmentDirections
import com.udacity.project4.utils.HasHintError
import org.mockito.Mockito.verify

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class LoginFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun signupButton_clicked_navigatesToSignupFragment() {
        val navController = mock(NavController::class.java)
        launchFragmentInHiltActivity<LoginFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(
            withId(R.id.signup_button)
        ).perform(click())
        verify(navController).navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
    }

    @Test
    fun emailField_whenEmptyAndLoginClicked_showsErrorText() {
        launchFragmentInHiltActivity<LoginFragment>()

        // When clicking login button and all fields are empty
        onView(
            withId(R.id.login_button)
        ).perform(click())

        // Then email input layout should show an error
        onView(
            withId(R.id.email_edit)
        ).check(
            matches(HasHintError())
        )
    }

}
