package com.eratart.feature.main.profile.di

import com.eratart.feature.main.profile.viewmodel.ProfileViewModel
import org.koin.dsl.module

val ProfileModule = module {
    single { ProfileViewModel(get(),get()) }
}