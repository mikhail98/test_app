package com.eratart.tools.gallery.saver

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import com.eratart.tools.utis.copyFile
import com.eratart.tools.utis.createFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class LegacyGalleryImageSaver(
    private val context: Context
) : IGallerySaver {

    override suspend fun save(uri: Uri): Uri? = withContext(Dispatchers.IO) {
        runCatching {
            val fileName = createFileName(IGallerySaver.PHOTO_NAME_TIME_STAMP_FORMAT)
            val savingDirectory = createSavingDirectory()
            val destinationFile = File(savingDirectory, fileName).apply { createNewFile() }
            val contentResolver = context.contentResolver

            contentResolver.copyFile(uri, destinationFile.toUri())

            addImageToGallery(contentResolver, destinationFile)
        }.getOrNull()
    }

    private fun createSavingDirectory() = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).path +
                File.separator +
                IGallerySaver.BUCKET_NAME
    ).apply { mkdir() }

    private fun addImageToGallery(contentResolver: ContentResolver, file: File): Uri? =
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
                put(MediaStore.Images.Media.MIME_TYPE, IGallerySaver.MIME_TYPE)
                put(MediaStore.Images.Media.DATA, file.toString())
            })
}
