package com.eratart.firebase.uploader.di

import com.eratart.firebase.uploader.manager.FirebaseFileUploaderManager
import com.eratart.uploader.api.IUploaderManager
import com.eratart.uploader.api.UploaderType
import com.google.firebase.storage.FirebaseStorage
import org.koin.core.qualifier.named
import org.koin.dsl.module

val FirebaseUploaderModule = module {
    single { FirebaseStorage.getInstance().reference }
    single<IUploaderManager>(named(UploaderType.FIREBASE_STORAGE.name)) {
        FirebaseFileUploaderManager(get())
    }
}