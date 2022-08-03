package com.eratart.tools.gallery.choose

import android.net.Uri

interface GalleryControllerCallbacks {
    fun onPhotoTaken(uri: Uri)
    fun onFailure()
}