package com.eratart.tools.auth

import android.app.Activity
import android.content.Intent
import com.eratart.domain.model.enums.AuthType
import com.eratart.domain.model.other.SignInData
import com.eratart.domain.model.other.TokenResult
import com.eratart.firebase.auth.auth.facebook.IFacebookAuthManager
import com.eratart.firebase.auth.auth.google.IGoogleAuthManager
import com.eratart.firebase.auth.auth.manager.IFirebaseAuthManager
import com.eratart.domain.model.other.FirebaseUser
import kotlinx.coroutines.flow.Flow

class AuthTool(
    private val googleAuthManager: IGoogleAuthManager,
    private val facebookAuthManager: IFacebookAuthManager,
    private val firebaseAuthManager: IFirebaseAuthManager,
) : IAuthTool {

    override fun authFirebase(
        activity: Activity, idToken: String, authType: AuthType,
    ): Flow<TokenResult> {
        return when (authType) {
            AuthType.GOOGLE -> firebaseAuthManager.authFirebaseWithGoogle(activity, idToken)
            AuthType.FACEBOOK -> firebaseAuthManager.authFirebaseWithFacebook(activity, idToken)
        }
    }

    override fun getToken(): Flow<TokenResult> {
        return firebaseAuthManager.getToken()
    }

    override fun authWithFacebook(activity: Activity): Flow<Pair<Boolean, SignInData?>> {
        return facebookAuthManager.authWithFacebook(activity)
    }

    override fun authWithGoogle(activity: Activity, requestCode: Int): Flow<Boolean> {
        return googleAuthManager.authWithGoogle(activity, requestCode)
    }

    override fun logout() {
        firebaseAuthManager.logout()
    }

    override fun getFirebaseUser(): FirebaseUser? {
        return firebaseAuthManager.getFirebaseUser()
    }

    override fun getSignInCredentialFromIntent(data: Intent?): SignInData {
        return googleAuthManager.getSignInCredentialFromIntent(data)
    }

    override fun registerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        facebookAuthManager.registerOnActivityResult(requestCode, resultCode, data)
    }
}