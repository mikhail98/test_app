package com.eratart.firebase.uploader.manager

import android.net.Uri
import com.eratart.base.uploader.BaseUploaderManager
import com.eratart.base.uploader.UploaderUtil
import com.eratart.core.constants.StringConstants
import com.eratart.domain.model.enums.MediaFileType
import com.eratart.domain.model.enums.MediaType
import com.eratart.firebase.uploader.util.FirebaseFileUploaderUtil
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseFileUploaderManager(private val reference: StorageReference) :
    BaseUploaderManager() {

    override fun uploadFileByUri(
        uri: Uri, userId: Long, type: MediaType, fileType: MediaFileType,
    ): Flow<String> {
        val reference = reference.child(UploaderUtil.getFolderForFile(userId, type, fileType))
        return uploadToFirebase(reference.putFile(uri), fileType)
    }

    private fun uploadToFirebase(uploadTask: UploadTask, type: MediaFileType): Flow<String> {
        return callbackFlow {
            uploadTask
                .addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.metadata?.path
                        ?.replace(StringConstants.SLASH, UploaderUtil.URL_SLASH)
                        ?.apply {
                            val url = FirebaseFileUploaderUtil.getContentFullUrl(this)
                            trySendBlocking(url)
                            close()
                        }
                }
                .addOnFailureListener {
                    throw handleUploaderError(type)
                }
            awaitClose()
        }
    }
}