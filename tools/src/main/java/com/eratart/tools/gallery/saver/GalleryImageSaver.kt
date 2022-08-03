package com.eratart.tools.gallery.saver

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.eratart.tools.utis.copyFile
import com.eratart.tools.utis.createFileName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.Q)
internal class GalleryImageSaver(
    private val context: Context
) : IGallerySaver {

    companion object {
        private const val RELATIVE_PATH = "Pictures/${IGallerySaver.BUCKET_NAME}/"

        private const val IS_PENDING_TRUE = 1
        private const val IS_PENDING_FALSE = 0
    }

    override suspend fun save(uri: Uri): Uri? = withContext(Dispatchers.IO) {
        return@withContext runCatching {

            val fileName = createFileName(IGallerySaver.PHOTO_NAME_TIME_STAMP_FORMAT)
            val contentResolver = context.contentResolver
            val values = createContentValues(fileName)

            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                ?.also { imageUri ->
                    contentResolver.copyFile(uri, imageUri)

                    values.apply {
                        clear()
                        put(MediaStore.Images.Media.IS_PENDING, IS_PENDING_FALSE)
                    }

                    contentResolver.update(imageUri, values, null, null)
                }
        }.getOrNull()
    }

    private fun createContentValues(name: String) =
        ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, IGallerySaver.MIME_TYPE)
            put(MediaStore.Images.Media.RELATIVE_PATH, RELATIVE_PATH)
            put(MediaStore.Images.Media.IS_PENDING, IS_PENDING_TRUE)
        }
}
