package com.eratart.feature.onboarding.di

import com.eratart.feature.onboarding.viewmodel.OnboardingViewModel
import org.koin.dsl.module

val OnboardingModule = module {
    single { OnboardingViewModel(get()) }
}