package com.example.samtasks.ui.fragments

import androidx.test.filters.MediumTest
import com.example.samtasks.ui.fragments.welcome.WelcomeFragment
import com.example.samtasks.utils.launchFragmentInHiltActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
    fun welcomeFragment_launchedUsingHiltLauncher_notFail() {
        launchFragmentInHiltActivity<WelcomeFragment> {

        }
    }
}