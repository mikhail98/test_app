package com.eratart.base.uploader

import android.net.Uri
import com.eratart.domain.exception.PhotoUploaderException
import com.eratart.domain.exception.VideoUploaderException
import com.eratart.domain.exception.VoiceUploaderException
import com.eratart.domain.model.enums.MediaFileType
import com.eratart.domain.model.enums.MediaType
import com.eratart.uploader.api.IUploaderManager
import kotlinx.coroutines.flow.Flow
import java.io.File

abstract class BaseUploaderManager : IUploaderManager {

    override fun uploadFile(
        file: File, userId: Long, type: MediaType, fileType: MediaFileType,
    ): Flow<String> {
        val fileUri = Uri.fromFile(file)
        return uploadFileByUri(fileUri, userId, type, fileType)
    }

    override fun uploadFileByPath(
        path: String, userId: Long, type: MediaType, fileType: MediaFileType,
    ): Flow<String> {
        val fileUri = Uri.fromFile(File(path))
        return uploadFileByUri(fileUri, userId, type, fileType)
    }

    protected fun handleUploaderError(type: MediaFileType) = when (type) {
        MediaFileType.PHOTO -> PhotoUploaderException()
        MediaFileType.VOICE -> VoiceUploaderException()
        MediaFileType.VIDEO -> VideoUploaderException()
    }
}