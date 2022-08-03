package com.eratart.feature.item.view.viewpager.fragment.di

import com.eratart.feature.item.view.viewpager.fragment.viewmodel.ImageViewModel
import org.koin.dsl.module

val ImageModule = module {
    single { ImageViewModel() }
}