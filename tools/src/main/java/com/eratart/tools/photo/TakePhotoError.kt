package com.eratart.tools.photo

import androidx.annotation.StringRes
import com.eratart.tools.R

/**
 * Contains error types that can be occurred during photo taking
 */
enum class TakePhotoError(@StringRes val message: Int) {
    CAMERA_NOT_FOUND(R.string.camera_not_found),
    SAVE_ERROR(R.string.saving_exeption)
}
