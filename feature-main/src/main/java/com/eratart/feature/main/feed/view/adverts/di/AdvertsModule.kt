package com.eratart.feature.main.feed.view.adverts.di

import com.eratart.feature.main.feed.view.adverts.viewmodel.AdvertsViewModel
import org.koin.dsl.module

val AdvertsModule = module {
    factory { AdvertsViewModel(get(), get()) }
}