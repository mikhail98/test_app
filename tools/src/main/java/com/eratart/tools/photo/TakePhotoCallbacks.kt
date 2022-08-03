package com.eratart.tools.photo

import android.net.Uri
import com.eratart.tools.gallery.saver.IGallerySaver

/**
 * Callback for handling saving result @see [IGallerySaver]
 * */
interface TakePhotoCallbacks {
    fun onPhotoTaken(uri: Uri)
    fun onCancelled()
    fun onFailed(error: TakePhotoError)
}
