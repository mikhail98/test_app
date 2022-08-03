package com.eratart.subfeature.looks.di

import com.eratart.subfeature.looks.viewmodel.LooksViewModel
import org.koin.dsl.module

val LooksModule = module {
    single { LooksViewModel(get(), get()) }
}