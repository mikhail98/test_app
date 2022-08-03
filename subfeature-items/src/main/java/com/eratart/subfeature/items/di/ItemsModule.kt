package com.eratart.subfeature.items.di

import com.eratart.subfeature.items.viewmodel.ItemsViewModel
import org.koin.dsl.module

val ItemsModule = module {
    single { ItemsViewModel(get(), get()) }
}