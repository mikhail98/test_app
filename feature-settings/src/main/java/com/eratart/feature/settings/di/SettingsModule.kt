package com.eratart.feature.settings.di

import com.eratart.feature.settings.viewmodel.SettingsViewModel
import org.koin.dsl.module

val SettingsModule = module{
    single { SettingsViewModel(get(), get(), get(), get(), get(), get()) }
}