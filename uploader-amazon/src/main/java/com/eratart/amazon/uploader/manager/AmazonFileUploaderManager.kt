package com.eratart.amazon.uploader.manager

import android.net.Uri
import com.amazonaws.event.ProgressEventType
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.transfer.TransferManager
import com.eratart.amazon.uploader.AmazonConstants
import com.eratart.base.uploader.BaseUploaderManager
import com.eratart.base.uploader.UploaderUtil
import com.eratart.domain.model.enums.MediaFileType
import com.eratart.domain.model.enums.MediaType
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File

class AmazonFileUploaderManager(
    private val amazonS3Client: AmazonS3Client,
    private val transferManager: TransferManager,
) :
    BaseUploaderManager() {

    override fun uploadFileByUri(
        uri: Uri, userId: Long, type: MediaType, fileType: MediaFileType,
    ): Flow<String> {
        return callbackFlow {
            val path = uri.path ?: throw handleUploaderError(fileType)
            val fileUrl = UploaderUtil.getFolderForFile(userId, type, fileType)
            val myUpload = transferManager.upload(AmazonConstants.BUCKET_NAME, fileUrl, File(path))

            myUpload.addProgressListener {
                when (it.eventType) {
                    ProgressEventType.TRANSFER_COMPLETED_EVENT -> {
                        val url = amazonS3Client
                            .getResourceUrl(AmazonConstants.BUCKET_NAME, fileUrl)
                        trySendBlocking(url)
                        close()
                    }
                    ProgressEventType.TRANSFER_FAILED_EVENT -> {
                        throw handleUploaderError(fileType)
                    }
                    else -> {}
                }
            }

            awaitClose {
                transferManager.shutdownNow()
            }
        }
    }
}