package com.eratart.firebase.auth.auth.manager

import android.app.Activity
import com.eratart.core.ext.printError
import com.eratart.domain.model.other.TokenResult
import com.eratart.domain.model.other.FirebaseUser
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseAuthManager(
    private val firebaseAuth: FirebaseAuth,
) : IFirebaseAuthManager {

    override fun authFirebaseWithGoogle(
        activity: Activity,
        idToken: String,
    ): Flow<TokenResult> {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        return authFirebase(activity, firebaseCredential)
    }

    override fun authFirebaseWithFacebook(
        activity: Activity,
        idToken: String,
    ): Flow<TokenResult> {
        val facebookCredential = FacebookAuthProvider.getCredential(idToken)
        return authFirebase(activity, facebookCredential)
    }

    private fun authFirebase(
        activity: Activity,
        credential: AuthCredential,
    ): Flow<TokenResult> {
        return callbackFlow {
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        getToken { result -> trySendBlocking(result) }
                    } else {
                        task.exception?.printError()
                        trySendBlocking(TokenResult(false, null, null))
                    }
                }
                .addOnFailureListener {
                    trySendBlocking(TokenResult(false, null, it))
                }
            awaitClose()
        }
    }

    override fun getToken(): Flow<TokenResult> {
        return callbackFlow {
            getToken { result -> trySendBlocking(result) }
            awaitClose()
        }
    }

    private fun getToken(listener: (TokenResult) -> Unit) {
        firebaseAuth.currentUser?.getIdToken(false)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                listener.invoke(
                    TokenResult(true, task.result.token, null)
                )
            } else {
                listener.invoke(
                    TokenResult(false, null, null)
                )
            }
        }?.addOnFailureListener {
            listener.invoke(TokenResult(false, null, it))
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getFirebaseUser(): FirebaseUser? {
        val user = FirebaseAuth.getInstance().currentUser ?: return null
        return with(user) {
            FirebaseUser(displayName, photoUrl.toString(), email, uid)
        }
    }
}