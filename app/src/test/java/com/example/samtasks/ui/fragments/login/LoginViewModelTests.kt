package com.example.samtasks.ui.fragments.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.samtasks.auth.FakeAuthenticator
import com.example.samtasks.utils.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.Assert.*


class LoginViewModelTests {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup(){
        val fakeAuthenticator = FakeAuthenticator()
        loginViewModel = LoginViewModel(fakeAuthenticator)
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