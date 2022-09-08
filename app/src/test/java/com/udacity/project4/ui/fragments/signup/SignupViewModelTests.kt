package com.udacity.project4.ui.fragments.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.udacity.project4.auth.FakeAuthenticator
import com.udacity.project4.di.localTestModule
import com.udacity.project4.utils.TestDispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.TestRule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.get

class SignupViewModelTests: KoinTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val koinTestRule = KoinTestRule.create{
        modules(localTestModule)
    }

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @ExperimentalCoroutinesApi
    val dispatchersProvider = TestDispatchersProvider(testDispatcher)

    private lateinit var signupViewModel: SignupViewModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup(){
        signupViewModel = SignupViewModel(get(),dispatchersProvider)
    }

    @Test
    fun `signup sets nameError when name field is empty`(){

        // Given that name field is empty
        signupViewModel.name.value = ""

        // When trying to signup
        signupViewModel.signup()

        // Then nameError value cannot be null
        assertNotNull(signupViewModel.nameError.value)

    }

    @Test
    fun `nameError is null when name field is not empty`(){

        // Given that name field is not empty
        signupViewModel.name.value = "Fake Name"

        // When trying to signup
        signupViewModel.signup()

        // Then nameError value must be null
        assertNull(signupViewModel.nameError.value)
    }

    @Test
    fun `passwordError value is not null if password is too short`(){

        // Given that password field is shorted that 8 chars
        signupViewModel.password.value = "short"

        // passwordError is null by default
        assertNull(signupViewModel.passwordError.value)

        // When attempting to signup with short password
        signupViewModel.signup()

        // Then passwordError cannot be null
        assertNotNull(signupViewModel.emailError.value)
    }

}