package com.eratart.dropp.di

import com.eratart.amazon.uploader.di.AmazonUploaderModule
import com.eratart.data.di.NetworkModule
import com.eratart.data.di.PreferencesModule
import com.eratart.data.di.RepositoryModule
import com.eratart.domain.di.InteractorModule
import com.eratart.firebase.auth.di.AuthModule
import com.eratart.firebase.uploader.di.FirebaseUploaderModule
import com.eratart.navigator.LauncherManager
import com.eratart.navigator.GlobalNavigator
import com.eratart.navigator.api.ILauncherManager
import com.eratart.navigator.api.IGlobalNavigator
import com.eratart.tools.di.ToolsModule
import org.koin.core.module.Module
import org.koin.dsl.module

object AppModules {
    fun getModules(): List<Module> {
        val modulesList = mutableListOf<Module>()
        //App
        modulesList.add(AppModule)

        //Auth
        modulesList.add(AuthModule)

        //Uploader
        modulesList.add(FirebaseUploaderModule)
        modulesList.add(AmazonUploaderModule)

        //Data
        modulesList.add(NetworkModule)
        modulesList.add(RepositoryModule)
        modulesList.add(PreferencesModule)

        //Domain
        modulesList.add(InteractorModule)

        //Tools
        modulesList.add(ToolsModule)

        return modulesList
    }
}

val AppModule = module {
    factory<ILauncherManager> { LauncherManager() }
    factory<IGlobalNavigator> { GlobalNavigator(get(), get()) }
}

