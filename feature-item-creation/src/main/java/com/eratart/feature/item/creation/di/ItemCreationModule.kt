package com.eratart.feature.item.creation.di

import com.eratart.feature.item.creation.viewmodel.ItemCreationViewModel
import org.koin.dsl.module

val ItemCreationModule = module {
    single { ItemCreationViewModel(get()) }
}