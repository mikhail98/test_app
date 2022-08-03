package com.eratart.feature.main.profile.view.capsules.di

import com.eratart.feature.main.profile.view.capsules.viewmodel.CapsulesViewModel
import org.koin.dsl.module

val CapsulesModule = module {
    single { CapsulesViewModel(get(), get()) }
}