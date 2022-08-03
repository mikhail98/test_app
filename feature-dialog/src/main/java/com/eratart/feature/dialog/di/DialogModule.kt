package com.eratart.feature.dialog.di

import com.eratart.feature.dialog.viewmodel.DialogViewModel
import org.koin.dsl.module

val DialogModule = module {
    single { DialogViewModel() }
}