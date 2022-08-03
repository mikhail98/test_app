package com.eratart.photodialog.view

import android.net.Uri

fun interface PhotoDialogResultListener {
    fun photoResultListener(uri: Uri)
}