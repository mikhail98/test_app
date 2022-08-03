package com.eratart.feature.capsule.di

import com.eratart.feature.capsule.viewmodel.CapsuleViewModel
import org.koin.dsl.module

val CapsuleModule = module {
    single { CapsuleViewModel() }
}