package com.udacity.project4.ui.fragments

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.ui.fragments.signup.SignupFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import com.udacity.project4.R
import com.udacity.project4.di.instrumentedTestModule
import com.udacity.project4.utils.HasHintError
import org.junit.After
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class SignupFragmentTest : KoinTest{

    @Before
    fun setup() {
        loadKoinModules(instrumentedTestModule)
    }

    @After
    fun clean() {
        unloadKoinModules(instrumentedTestModule)
    }

    @Test
    fun signupButton_pressedOnEmptyFields_showsHintError() {
        launchFragmentInContainer<SignupFragment>(themeResId = R.style.Theme_SAMTasks)

        // When signup button clicked and fields are empty
        onView(
            withId(R.id.signup_button)
        ).perform(click())

        //Then all InputFieldLayouts must show hint error
        onView(withId(R.id.name_edit)).check(matches(HasHintError()))
        onView(withId(R.id.email_edit)).check(matches(HasHintError()))
        onView(withId(R.id.password_edit)).check(matches(HasHintError()))
    }
}