package com.eratart.firebase.uploader.util

object FirebaseFileUploaderUtil {

    private const val FIREBASE_PROJECT_NAME = "dropp-mvp"

    fun getContentFullUrl(path: String): String {
        return "https://firebasestorage.googleapis.com/v0/b/$FIREBASE_PROJECT_NAME.appspot.com/o/$path?alt=media"
    }
}