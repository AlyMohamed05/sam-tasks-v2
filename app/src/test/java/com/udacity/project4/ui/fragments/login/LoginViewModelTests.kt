package com.udacity.project4.ui.fragments.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.udacity.project4.di.localTestModule
import com.udacity.project4.utils.TestDispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.Assert.*
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get


class LoginViewModelTests : KoinTest{

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create{
        modules(localTestModule)
    }

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @ExperimentalCoroutinesApi
    val dispatchersProvider = TestDispatchersProvider(testDispatcher)

    private lateinit var loginViewModel: LoginViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        loginViewModel = LoginViewModel(get(),dispatchersProvider)
    }

    @Test
    fun `passwordError is not null when attempting to login with short password`(){

        // Given that password is too short
        loginViewModel.password.value = "short"

        // passwordError is null by default
        assertNull(loginViewModel.passwordError.value)

        // When trying to login with short password
        loginViewModel.login()

        // Then passwordError cannot be null
        assertNotNull(loginViewModel.passwordError.value)
    }

}