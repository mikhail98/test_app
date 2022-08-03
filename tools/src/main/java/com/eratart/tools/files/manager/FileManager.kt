package com.eratart.tools.files.manager

import android.content.Context
import android.os.Environment
import java.io.File
import java.util.*

class FileManager(private val context: Context) : IFileManager {

    companion object {
        private const val FILE_PHOTO = "-photo.jpg"
    }

    override fun getFileForPhoto(): File {
        val randomString = UUID.randomUUID().toString()
        val imageFileFolder = context.getExternalFilesDir(Environment.DIRECTORY_DCIM)
        return File(imageFileFolder, "$randomString$FILE_PHOTO")
    }
}