package com.eratart.dropp

import android.app.Application
import com.eratart.dropp.di.AppModules
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DroppApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)
        startKoin {
            androidContext(this@DroppApp)
            modules(AppModules.getModules())
        }
    }

}