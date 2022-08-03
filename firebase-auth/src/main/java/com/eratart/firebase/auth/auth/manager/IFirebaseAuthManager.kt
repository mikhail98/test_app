package com.eratart.firebase.auth.auth.manager

import android.app.Activity
import com.eratart.domain.model.other.TokenResult
import com.eratart.domain.model.other.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface IFirebaseAuthManager {

    fun authFirebaseWithGoogle(activity: Activity, idToken: String): Flow<TokenResult>

    fun authFirebaseWithFacebook(activity: Activity, idToken: String): Flow<TokenResult>

    fun logout()

    fun getFirebaseUser(): FirebaseUser?

    fun getToken(): Flow<TokenResult>
}