package com.eratart.feature.splash.di

import com.eratart.feature.splash.viewmodel.SplashViewModel
import org.koin.dsl.module

val SplashModule = module {
    single { SplashViewModel(get(), get()) }
}