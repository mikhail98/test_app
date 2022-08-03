package com.eratart.tools.di

import android.os.Build
import com.eratart.baristashandbook.tools.customtabs.ICustomTabTool
import com.eratart.tools.analytics.AnalyticsManager
import com.eratart.tools.analytics.IAnalyticsManager
import com.eratart.tools.gallery.saver.GalleryImageSaver
import com.eratart.tools.gallery.saver.LegacyGalleryImageSaver
import com.eratart.tools.auth.AuthTool
import com.eratart.tools.auth.IAuthTool
import com.eratart.tools.customtab.CustomTabTool
import com.eratart.tools.files.compress.IImageCompressor
import com.eratart.tools.files.compress.ImageCompressor
import com.eratart.tools.files.manager.FileManager
import com.eratart.tools.files.manager.IFileManager
import com.eratart.tools.files.uploader.FileUploader
import com.eratart.tools.files.uploader.IFileUploader
import com.eratart.tools.gallery.saver.IGallerySaver
import com.eratart.uploader.api.UploaderType
import com.google.firebase.analytics.FirebaseAnalytics
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ToolsModule = module {
    single { FirebaseAnalytics.getInstance(get()) }
    single<IAnalyticsManager> { AnalyticsManager(get()) }
    single<IFileManager> { FileManager(get()) }
    factory<IGallerySaver> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            LegacyGalleryImageSaver(get())
        } else {
            GalleryImageSaver(get())
        }
    }
    single<ICustomTabTool> { CustomTabTool(get()) }
    single<IFileUploader> {
        FileUploader(get(named(UploaderType.FIREBASE_STORAGE.name)))
    }
    single<IAuthTool> { AuthTool(get(), get(), get()) }

    single<IImageCompressor> { ImageCompressor(get(), get()) }
}