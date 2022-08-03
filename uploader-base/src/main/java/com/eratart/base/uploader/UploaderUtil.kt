package com.eratart.base.uploader

import com.eratart.core.constants.StringConstants
import com.eratart.domain.model.enums.MediaFileType
import com.eratart.domain.model.enums.MediaType

object UploaderUtil {

    private const val FOLDER_USERS = "users"
    private const val FOLDER_PHOTOS = "photos"
    private const val FOLDER_VOICES = "voices"
    private const val FOLDER_VIDEOS = "videos"

    private const val FOLDER_AVATAR = "avatars"
    private const val FOLDER_ITEM = "items"

    const val WEBM_EXT = ".webm"
    const val MP4_EXT = ".mp4"
    const val TMP_EXT = ".tmp"
    const val JPEG_EXT = ".jpeg"

    const val URL_SLASH = "%2F"

    fun getFolderForFile(
        userId: Long, type: MediaType, fileType: MediaFileType,
    ): String {
        val contentFolder = when (type) {
            MediaType.AVATAR -> FOLDER_AVATAR
            MediaType.ITEM -> FOLDER_ITEM
        }
        val mediaFolder = when (fileType) {
            MediaFileType.PHOTO -> FOLDER_PHOTOS
            MediaFileType.VOICE -> FOLDER_VOICES
            MediaFileType.VIDEO -> FOLDER_VIDEOS
        }
        val mediaExtension = when (fileType) {
            MediaFileType.PHOTO -> JPEG_EXT
            MediaFileType.VOICE -> MP4_EXT
            MediaFileType.VIDEO -> MP4_EXT
        }
        return FOLDER_USERS.plus(StringConstants.SLASH)
            .plus(userId.toString())
            .plus(StringConstants.SLASH)
            .plus(contentFolder)
            .plus(StringConstants.SLASH)
            .plus(mediaFolder)
            .plus(StringConstants.SLASH)
            .plus(System.currentTimeMillis())
            .plus(mediaExtension)
    }
}