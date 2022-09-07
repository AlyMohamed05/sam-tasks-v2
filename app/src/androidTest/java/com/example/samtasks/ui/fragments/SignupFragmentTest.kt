package com.example.samtasks.ui.fragments

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoActivityResumedException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.samtasks.ui.fragments.signup.SignupFragment
import com.example.samtasks.utils.launchFragmentInHiltActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import com.example.samtasks.R
import com.example.samtasks.utils.HasHintError

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