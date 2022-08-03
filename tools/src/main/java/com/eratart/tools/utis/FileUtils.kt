package com.eratart.tools.utis

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore.Images.ImageColumns
import android.provider.MediaStore.Images.Media
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

private const val DELIMITER = ":"
private const val SELECTION = "=?"
private const val FROM_GALLERY_URI_PREFIX = "com.android.providers."

internal fun Uri.fromContentResolverToFile(context: Context): File? {

    val isFromCamera = !this.toString().contains(FROM_GALLERY_URI_PREFIX)
    val column = arrayOf(if(isFromCamera) ImageColumns.DATA else Media.DATA)

  val cursor = if(isFromCamera) {
        context.contentResolver.query(this, null, null, null, null)
    } else {
        val wholeID = DocumentsContract.getDocumentId(this)
        val id = wholeID.split(DELIMITER).toTypedArray()[1]
        val sel = Media._ID + SELECTION
        context.contentResolver.query(Media.EXTERNAL_CONTENT_URI, column, sel, arrayOf(id), null)
    }

    var filePath: String? = null
    cursor?.apply {
        val columnIndex = getColumnIndex(column.first())
        if (moveToFirst()) filePath = getString(columnIndex)
        close()
    }
    return filePath?.let { File(it) }
}

internal fun ContentResolver.copyFile(from: Uri, to: Uri) {
    val inputStream = checkNotNull(openInputStream(from))
    openOutputStream(to)?.use { fileOutputStream ->
        inputStream.copyTo(fileOutputStream)
    }
}

@SuppressLint("SimpleDateFormat")
internal fun createFileName(timeFormat: String): String {
    val timeStamp = SimpleDateFormat(timeFormat).format(Date())
    return "IMAGE_$timeStamp.jpg"
}
