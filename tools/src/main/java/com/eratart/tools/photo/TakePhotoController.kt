package com.eratart.tools.photo

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.eratart.core.BuildConfig
import com.eratart.tools.gallery.saver.IGallerySaver
import kotlinx.coroutines.launch
import java.io.File


/**
 * This controller allow to take photo and save it into gallery
 *
 * @param fragment - from which fragment camera will be open
 * @param callbacks - @see [TakePhotoCallbacks]
 * @param gallerySaver - the util which save photo into gallery
 * */
class TakePhotoController(
    private val fragment: Fragment,
    private val callbacks: TakePhotoCallbacks,
    private val gallerySaver: IGallerySaver
) {

    companion object {
        private const val TEMP_FILE_NAME = "tmp_file.jpg"
    }

    private var takePhotoLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            saveToGallery()
        } else {
            callbacks.onCancelled()
        }
    }

    private val tempImageFile: File
        get() =
            File(
                fragment.requireContext()
                    .getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath,
                TEMP_FILE_NAME
            )

    fun takePhoto() {
        val packageManager = fragment.requireActivity().packageManager
        val photoUri = createPhotoUri()

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            takePhotoLauncher.launch(photoUri)
        } else {
            callbacks.onFailed(TakePhotoError.CAMERA_NOT_FOUND)
        }
    }

    private fun createPhotoUri(): Uri {
        tempImageFile.also { file ->
            file.createNewFile()

            return@createPhotoUri FileProvider.getUriForFile(
                fragment.requireContext(),
                BuildConfig.AUTHORITIES,
                file
            )
        }
    }

    private fun saveToGallery() {
        fragment.lifecycle.coroutineScope.launch {
            val uri = gallerySaver.save(createPhotoUri())
            if (uri != null) {
                callbacks.onPhotoTaken(uri)
            } else {
                callbacks.onFailed(TakePhotoError.SAVE_ERROR)
            }
        }
    }
}
