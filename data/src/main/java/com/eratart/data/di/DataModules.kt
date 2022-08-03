package com.eratart.data.di

import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.eratart.data.network.IRetrofitBuilder
import com.eratart.data.network.RetrofitBuilder
import com.eratart.data.preferences.Preferences
import com.eratart.data.preferences.impl.AuthPreferences
import com.eratart.data.preferences.impl.OnboardingPreferences
import com.eratart.data.preferences.impl.RecentlySearchedPreferences
import com.eratart.data.repository.DialogsRepository
import com.eratart.data.repository.OtherRepository
import com.eratart.data.repository.UsersRepository
import com.eratart.domain.preferences.IAuthPreferences
import com.eratart.domain.preferences.IOnboardingPreferences
import com.eratart.domain.preferences.IRecentlySearchedPreferences
import com.eratart.domain.repository.IDialogsRepository
import com.eratart.domain.repository.IOtherRepository
import com.eratart.domain.repository.IUsersRepository
import org.koin.dsl.module

val PreferencesModule = module {
    single {
        MasterKey.Builder(get())
            .setKeyGenParameterSpec(Preferences.createAES256GCMKeyGenParameterSpec())
            .build()
    }
    single {
        EncryptedSharedPreferences.create(
            get(),
            Preferences.PREFERENCES_NAME, get<MasterKey>(),
            Preferences.keyEncryptor,
            Preferences.valueEncryptor
        )
    }
    single<IOnboardingPreferences> { OnboardingPreferences(get()) }
    single<IAuthPreferences> { AuthPreferences(get()) }
    single<IRecentlySearchedPreferences> { RecentlySearchedPreferences() }
}

val NetworkModule = module {
    single<IRetrofitBuilder> { RetrofitBuilder() }
    factory { get<IRetrofitBuilder>().getUsersApi() }
    factory { get<IRetrofitBuilder>().getOtherApi() }
}

val RepositoryModule = module {
    single<IUsersRepository> { UsersRepository(get(), get()) }
    single<IDialogsRepository> { DialogsRepository() }
    single<IOtherRepository> { OtherRepository(get()) }
}
