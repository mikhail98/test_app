package com.eratart.feature.item.di

import com.eratart.feature.item.viewmodel.ItemViewModel
import org.koin.dsl.module

val ItemModule = module {
    single { ItemViewModel() }
}