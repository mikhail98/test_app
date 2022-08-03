package com.eratart.feature.main.mainactivity.di

import com.eratart.feature.main.mainactivity.viewmodel.MainViewModel
import org.koin.dsl.module

val MainModule = module {
    single { MainViewModel() }
}