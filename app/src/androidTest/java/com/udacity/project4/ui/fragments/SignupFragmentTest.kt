package com.udacity.project4.ui.fragments

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.udacity.project4.ui.fragments.signup.SignupFragment
import com.udacity.project4.utils.launchFragmentInHiltActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.udacity.project4.R
import com.udacity.project4.utils.HasHintError

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class SignupFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun signupButton_pressedOnEmptyFields_showsHintError() {
        launchFragmentInHiltActivity<SignupFragment>()

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