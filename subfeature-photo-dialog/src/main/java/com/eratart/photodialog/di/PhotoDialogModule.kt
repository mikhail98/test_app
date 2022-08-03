package com.eratart.photodialog.di

import com.eratart.photodialog.viewmodel.PhotoDialogViewModel
import org.koin.dsl.module

val PhotoDialogModule = module {
    single { PhotoDialogViewModel() }
}