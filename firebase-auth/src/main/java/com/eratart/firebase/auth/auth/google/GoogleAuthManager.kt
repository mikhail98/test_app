package com.eratart.firebase.auth.auth.google

import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.eratart.core.ext.printError
import com.eratart.domain.model.enums.AuthType
import com.eratart.domain.model.other.SignInData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GoogleAuthManager(private val oneTapClient: SignInClient) : IGoogleAuthManager {

    private fun createSignInRequest() =
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("239484368384-d98hukur8f9su2rvk28mcinsoui8na5i")
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()


    override fun authWithGoogle(activity: Activity, requestCode: Int): Flow<Boolean> {
        return callbackFlow {
            oneTapClient.beginSignIn(createSignInRequest())
                .addOnSuccessListener(activity) { result ->
                    try {
                        startIntentSenderForResult(
                            activity,
                            result.pendingIntent.intentSender, requestCode,
                            null, 0, 0, 0, null
                        )
                        trySendBlocking(true)
                    } catch (e: SendIntentException) {
                        e.printError()
                        trySendBlocking(false)
                    }
                }
                .addOnFailureListener(activity) { e ->
                    e.printError()
                    trySendBlocking(false)
                }
            awaitClose()
        }
    }

    override fun getSignInCredentialFromIntent(data: Intent?): SignInData {
        val credential = oneTapClient.getSignInCredentialFromIntent(data)

        return SignInData(credential.googleIdToken, AuthType.GOOGLE)
    }
}