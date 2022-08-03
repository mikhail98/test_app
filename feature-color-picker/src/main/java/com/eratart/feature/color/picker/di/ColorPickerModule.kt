package com.eratart.feature.color.picker.di

import com.eratart.feature.color.picker.viewmodel.ColorPickerViewModel
import org.koin.dsl.module

val ColorPickerModule = module{
    single { ColorPickerViewModel() }
}