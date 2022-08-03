package com.eratart.firebase.auth.auth.facebook

import android.app.Activity
import android.content.Intent
import com.eratart.domain.model.enums.AuthType
import com.eratart.domain.model.other.SignInData
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FacebookAuthManager : IFacebookAuthManager {

    private val callbackManager = CallbackManager.Factory.create()

    override fun authWithFacebook(activity: Activity): Flow<Pair<Boolean, SignInData?>> {
        return callbackFlow {
            val callback = FacebookSdk.InitializeCallback {
                if (AccessToken.isCurrentAccessTokenActive()) {
                    LoginManager.getInstance().logOut()
                }
                LoginManager.getInstance().logInWithReadPermissions(
                    activity, listOf("email", "public_profile")
                )
            }
            if(!FacebookSdk.isInitialized()) {
                FacebookSdk.sdkInitialize(activity.applicationContext, callback)
            }
            LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult) {
                        trySendBlocking(
                            Pair(true, SignInData(result.accessToken.token, AuthType.FACEBOOK))
                        )
                    }

                    override fun onCancel() {
                    }

                    override fun onError(error: FacebookException) {
                        trySendBlocking(Pair(false, null))
                    }
                })
            awaitClose()
        }
    }

    override fun registerOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}