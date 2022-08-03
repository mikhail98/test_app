package com.eratart.tools.gallery.saver

import android.net.Uri

/**
 * This interface provide saving pictures by camera to gallery operation
 * */
interface IGallerySaver {

    companion object {
        internal const val PHOTO_NAME_TIME_STAMP_FORMAT = "yyyyMMdd_HHmmss_SSS"
        internal const val BUCKET_NAME = "Dropp"

        const val MIME_TYPE = "image/jpeg"
    }

    /**
     * Confirm saving
     * @return uri where file was saved or null if save is not successful
     * */
    suspend fun save(uri: Uri): Uri?
}
