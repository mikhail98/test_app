package com.eratart.feature.auth.di

import com.eratart.feature.auth.viewmodel.AuthViewModel
import org.koin.dsl.module

val AuthModule = module {
    single { AuthViewModel(get(), get(), get(), get()) }
}