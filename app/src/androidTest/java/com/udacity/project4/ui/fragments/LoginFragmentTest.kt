package com.udacity.project4.ui.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.ui.fragments.login.LoginFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import com.udacity.project4.R
import com.udacity.project4.di.instrumentedTestModule
import com.udacity.project4.ui.fragments.login.LoginFragmentDirections
import com.udacity.project4.utils.HasHintError
import org.junit.After
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest
import org.mockito.Mockito.verify

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class LoginFragmentTest : KoinTest{

    @Before
    fun setup() {
        loadKoinModules(instrumentedTestModule)
    }

    @After
    fun clean(){
        unloadKoinModules(instrumentedTestModule)
    }

    @Test
    fun signupButton_clicked_navigatesToSignupFragment() {
        val navController = mock(NavController::class.java)
        val scenario = launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_SAMTasks)
        scenario.onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }
        onView(
            withId(R.id.signup_button)
        ).perform(click())
        verify(navController).navigate(LoginFragmentDirections.actionLoginFragmentToSignupFragment())
    }

    @Test
    fun emailField_whenEmptyAndLoginClicked_showsErrorText() {
        launchFragmentInContainer<LoginFragment>(themeResId = R.style.Theme_SAMTasks)

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
