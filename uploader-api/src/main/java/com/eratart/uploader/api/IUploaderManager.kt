package com.eratart.uploader.api

import android.net.Uri
import com.eratart.domain.model.enums.MediaFileType
import com.eratart.domain.model.enums.MediaType
import kotlinx.coroutines.flow.Flow
import java.io.File

interface IUploaderManager {

    fun uploadFile(
        file: File, userId: Long, type: MediaType, fileType: MediaFileType,
    ): Flow<String>

    fun uploadFileByPath(
        path: String, userId: Long, type: MediaType, fileType: MediaFileType,
    ): Flow<String>

    fun uploadFileByUri(
        uri: Uri, userId: Long, type: MediaType, fileType: MediaFileType,
    ): Flow<String>
}