package com.eratart.tools.files.uploader

import android.net.Uri
import com.eratart.domain.model.enums.MediaFileType
import com.eratart.domain.model.enums.MediaType
import com.eratart.uploader.api.IUploaderManager
import kotlinx.coroutines.flow.Flow
import java.io.File

class FileUploader(
    private val firebaseUploader: IUploaderManager
) : IFileUploader {

    override fun uploadFile(
        file: File, userId: Long, type: MediaType, fileType: MediaFileType,
    ): Flow<String> {
        return firebaseUploader.uploadFile(file, userId, type, fileType)
    }

    override fun uploadFileByPath(
        path: String, userId: Long, type: MediaType, fileType: MediaFileType,
    ): Flow<String> {
        return firebaseUploader.uploadFileByPath(path, userId, type, fileType)
    }

    override fun uploadFileByUri(
        uri: Uri, userId: Long, type: MediaType, fileType: MediaFileType,
    ): Flow<String> {
        return firebaseUploader.uploadFileByUri(uri, userId, type, fileType)
    }
}