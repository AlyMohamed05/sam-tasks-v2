package com.example.samtasks.ui.fragments.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.samtasks.auth.FakeAuthenticator
import com.example.samtasks.utils.DispatchersProvider
import com.example.samtasks.utils.TestDispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.Assert.*


class LoginViewModelTests {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @ExperimentalCoroutinesApi
    val dispatchersProvider = TestDispatchersProvider(testDispatcher)

    private lateinit var loginViewModel: LoginViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        val fakeAuthenticator = FakeAuthenticator()
        loginViewModel = LoginViewModel(fakeAuthenticator,dispatchersProvider)
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