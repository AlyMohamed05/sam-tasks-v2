package com.udacity.project4.di

import com.udacity.project4.auth.Authenticator
import com.udacity.project4.auth.FakeAuthenticator
import org.koin.dsl.module

val localTestModule = module {
    factory<Authenticator> {
        FakeAuthenticator()
    }
}