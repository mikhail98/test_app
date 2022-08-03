package com.eratart.firebase.auth.auth.google

import android.app.Activity
import android.content.Intent
import com.eratart.domain.model.other.SignInData
import kotlinx.coroutines.flow.Flow

interface IGoogleAuthManager {

    fun authWithGoogle(activity: Activity, requestCode: Int): Flow<Boolean>

    fun getSignInCredentialFromIntent(data: Intent?): SignInData

}