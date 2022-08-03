package com.eratart.feature.dialogs.list.di

import com.eratart.feature.dialogs.list.view.model.DialogsEntityMapper
import com.eratart.feature.dialogs.list.viewmodel.DialogsListViewModel
import org.koin.dsl.module

val DialogsListModule = module {
    single { DialogsListViewModel(get(), get()) }
    single { DialogsEntityMapper(get()) }
}