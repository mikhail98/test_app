package com.eratart.firebase.auth.di

import android.content.Context
import com.eratart.firebase.auth.auth.facebook.FacebookAuthManager
import com.eratart.firebase.auth.auth.facebook.IFacebookAuthManager
import com.eratart.firebase.auth.auth.google.GoogleAuthManager
import com.eratart.firebase.auth.auth.google.IGoogleAuthManager
import com.eratart.firebase.auth.auth.manager.FirebaseAuthManager
import com.eratart.firebase.auth.auth.manager.IFirebaseAuthManager
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val AuthModule = module {
    single { FirebaseAuth.getInstance() }
    single { Identity.getSignInClient(get<Context>()) }

    single<IGoogleAuthManager> { GoogleAuthManager(get()) }
    single<IFacebookAuthManager> { FacebookAuthManager() }
    single<IFirebaseAuthManager> { FirebaseAuthManager(get()) }
}