package com.eratart.firebase.auth.auth.facebook

import android.app.Activity
import android.content.Intent
import com.eratart.domain.model.other.SignInData
import kotlinx.coroutines.flow.Flow

interface IFacebookAuthManager {

    fun authWithFacebook(activity: Activity): Flow<Pair<Boolean, SignInData?>>

    fun registerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}