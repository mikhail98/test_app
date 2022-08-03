package com.eratart.domain.di

import com.eratart.domain.interactor.dialogs.list.DialogsListInteractor
import com.eratart.domain.interactor.dialogs.list.IDialogsListInteractor
import com.eratart.domain.interactor.other.OtherInteractor
import com.eratart.domain.interactor.other.IOtherInteractor
import com.eratart.domain.interactor.preferences.IPreferencesInteractor
import com.eratart.domain.interactor.preferences.PreferencesInteractor
import com.eratart.domain.interactor.users.IUsersInteractor
import com.eratart.domain.interactor.users.UsersInteractor
import org.koin.dsl.module

val InteractorModule = module {
    single<IUsersInteractor> { UsersInteractor(get()) }
    single<IOtherInteractor> { OtherInteractor(get()) }
    single<IPreferencesInteractor> { PreferencesInteractor(get(), get()) }
    single<IDialogsListInteractor> { DialogsListInteractor(get(), get()) }
}