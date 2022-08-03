package com.eratart.feature.about.app.di

import com.eratart.feature.about.app.viewmodel.AboutAppViewModel
import org.koin.dsl.module

val AboutAppModule = module {
    single { AboutAppViewModel() }
}