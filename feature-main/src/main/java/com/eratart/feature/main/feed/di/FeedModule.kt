package com.eratart.feature.main.feed.di

import com.eratart.feature.main.feed.viewmodel.FeedViewModel
import org.koin.dsl.module

val FeedModule = module {
    single { FeedViewModel() }
}