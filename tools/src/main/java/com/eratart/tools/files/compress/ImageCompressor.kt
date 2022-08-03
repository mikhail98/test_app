package com.eratart.tools.files.compress

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.JPEG
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import androidx.core.net.toUri
import com.eratart.core.constants.IntConstants
import com.eratart.tools.files.manager.IFileManager
import com.eratart.tools.utis.fromContentResolverToFile
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.io.FileOutputStream

internal class ImageCompressor(
    private val context: Context,
    private val fileManager: IFileManager
) : IImageCompressor {

    companion object {

        private const val BITMAP_WIDTH = 666
        private const val BITMAP_COMPRESS_LEVEL = 66

        private const val ROTATE_0 = 0
        private const val ROTATE_90 = 90
        private const val ROTATE_180 = 180
        private const val ROTATE_270 = 270
    }

    override fun compressByUri(uri: Uri): Flow<Uri> {
        return compressImage(uri)
    }

    private fun compressImage(uri: Uri): Flow<Uri> =
        callbackFlow {
            try {
                val tmp = fileManager.getFileForPhoto()
                uri.fromContentResolverToFile(context)?.let { file ->
                    compressAndRotateIfNeeded(file, tmp)
                }
                trySendBlocking(tmp.toUri())
            } catch (e: Exception) {
                throw e
            }
            close()
            awaitClose()
        }

    private fun getCameraPhotoOrientation(file: File): Int {
        val orientation = ExifInterface(file.absolutePath)
            .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> ROTATE_90
            ExifInterface.ORIENTATION_ROTATE_180 -> ROTATE_180
            ExifInterface.ORIENTATION_ROTATE_270 -> ROTATE_270
            else -> ROTATE_0
        }
    }

    private fun compressAndRotateIfNeeded(sourceFile: File, destinationFile: File = sourceFile) {
        val bounds = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            inJustDecodeBounds = false
        }
        val sourceBitmap = BitmapFactory.decodeFile(sourceFile.absolutePath, bounds)
        val rotationAngle = getCameraPhotoOrientation(sourceFile)

        val matrix = Matrix().apply {
            postRotate(
                rotationAngle.toFloat(), sourceBitmap.width.toFloat(), sourceBitmap.height.toFloat()
            )
        }
        val rotatedBitmap = Bitmap.createBitmap(
            sourceBitmap, IntConstants.ZERO, IntConstants.ZERO,
            bounds.outWidth, bounds.outHeight, matrix, true
        )
        val fileOutputStream = FileOutputStream(destinationFile.absoluteFile)
        var newBitmap = rotatedBitmap
        val scale = newBitmap.width.toFloat() / BITMAP_WIDTH
        val newHeight = (newBitmap.height / scale).toInt()
        newBitmap = Bitmap.createScaledBitmap(rotatedBitmap, BITMAP_WIDTH, newHeight, false)
        newBitmap.compress(JPEG, BITMAP_COMPRESS_LEVEL, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()
    }
}