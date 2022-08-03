package com.eratart.feature.profile.setup.di

import com.eratart.feature.profile.setup.viewmodel.SetupProfileVewModel
import org.koin.dsl.module

val SetupProfileModule= module {
    single { SetupProfileVewModel(get(), get(), get(), get()) }
}