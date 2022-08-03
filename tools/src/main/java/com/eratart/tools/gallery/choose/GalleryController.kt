package com.eratart.tools.gallery.choose

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class GalleryController(
    val fragment: Fragment,
    callbacks: GalleryControllerCallbacks
) {
    companion object {
        private const val INTENT_TYPE = "image/*"
    }

    private val galleryLauncher = fragment.registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri == null) {
            callbacks.onFailure()
        } else {
            callbacks.onPhotoTaken(uri)
        }
    }

    fun openGallery() {
        galleryLauncher.launch(INTENT_TYPE)
    }
}