package com.eratart.tools.auth

import android.app.Activity
import android.content.Intent
import com.eratart.domain.model.enums.AuthType
import com.eratart.domain.model.other.SignInData
import com.eratart.domain.model.other.TokenResult
import com.eratart.domain.model.other.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface IAuthTool {

    fun authWithGoogle(activity: Activity, requestCode: Int): Flow<Boolean>

    fun getSignInCredentialFromIntent(data: Intent?): SignInData

    fun authWithFacebook(activity: Activity): Flow<Pair<Boolean, SignInData?>>

    fun registerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun authFirebase(activity: Activity, idToken: String, authType: AuthType): Flow<TokenResult>

    fun logout()

    fun getFirebaseUser(): FirebaseUser?

    fun getToken(): Flow<TokenResult>
}