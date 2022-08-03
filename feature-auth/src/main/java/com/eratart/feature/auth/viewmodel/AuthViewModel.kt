package com.eratart.feature.auth.viewmodel

import android.app.Activity
import android.content.Intent
import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.core.constants.IntConstants
import com.eratart.core.constants.StringConstants
import com.eratart.core.coroutines.catchUi
import com.eratart.core.coroutines.launchFlow
import com.eratart.core.coroutines.onNext
import com.eratart.core.coroutines.onNextUi
import com.eratart.domain.exception.UnauthorizedException
import com.eratart.domain.interactor.users.IUsersInteractor
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.enums.AuthType
import com.eratart.domain.model.other.SignInData
import com.eratart.domain.preferences.IAuthPreferences
import com.eratart.domain.preferences.IOnboardingPreferences
import com.eratart.feature.auth.failure.AuthenticationFailure
import com.eratart.tools.auth.IAuthTool
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch

class AuthViewModel(
    private val onboardingPreferences: IOnboardingPreferences,
    private val authPreferences: IAuthPreferences,
    private val authTool: IAuthTool,
    private val userInteractor: IUsersInteractor,
) : BaseViewModel() {

    private val _authSuccessResult = MutableSharedFlow<Pair<Boolean, Boolean>>()
    val authSuccessResult = _authSuccessResult.asSharedFlow()

    fun login(activity: Activity, requestCode: Int, authType: AuthType) {
        setLoading(true)
        when (authType) {
            AuthType.GOOGLE -> loginWithGoogle(activity, requestCode)
            AuthType.FACEBOOK -> loginWithFacebook(activity)
        }
    }

    private fun loginWithGoogle(activity: Activity, requestCode: Int) {
        launchFlow {
            authTool.authWithGoogle(activity, requestCode)
                .onNext {
                    if (!it) {
                        handleFailure(AuthenticationFailure())
                    }
                }
                .catch { handleError(it) }
        }
    }

    private fun loginWithFacebook(activity: Activity) {
        launchFlow {
            authTool.authWithFacebook(activity)
                .onNext {
                    if (!it.first) {
                        handleFailure(AuthenticationFailure())
                    } else {
                        val data = it.second ?: return@onNext
                        authorizeFirebase(activity, data)
                    }
                }
                .catch { handleError(it) }
        }
    }

    fun authorizeGoogle(activity: Activity, data: Intent?) {
        val signInData = authTool.getSignInCredentialFromIntent(data)
        authorizeFirebase(activity, signInData)
    }

    fun registerOnActivityResultFacebook(
        requestCode: Int, resultCode: Int, data: Intent?,
    ) {
        authTool.registerOnActivityResult(requestCode, resultCode, data)
    }

    private fun authorizeFirebase(activity: Activity, data: SignInData) {
        saveAuthorization(data)
        val flow = authTool.authFirebase(activity, data.idToken.orEmpty(), data.authType)

        launchFlow {
            flow.subscribeWithError {
                if (it.isSuccess) {
                    val token = it.token
                    if (token != null) {
                        authorizeWithBackend()
                    } else {
                        handleFailure(AuthenticationFailure())
                    }
                } else {
                    handleFailure(AuthenticationFailure())
                }
            }
        }
    }

    private fun saveAuthorization(signInData: SignInData) {
        authPreferences.saveUserAuthorization(signInData)
    }

    private fun authorizeWithBackend() {
        launchFlow {
            userInteractor.getUserByToken()
                .catchUi {
                    when (it) {
                        is UnauthorizedException -> {
                            var user = DroppUser(
                                null, null, null, null, null, null, null, null, null, null, null
                            )
                            val firebaseUser = authTool.getFirebaseUser()
                            val name = firebaseUser?.name.orEmpty()
                            val nameData = name.split(StringConstants.SPACE)
                            if (nameData.size >= IntConstants.TWO) {
                                val firstName = nameData.first()
                                val lastName = name.drop(firstName.length).trim()
                                user = user.copy(firstName = firstName, lastName = lastName)
                            }

                            user = user.copy(photo = firebaseUser?.photoUrl)
                            user = user.copy(email = firebaseUser?.email)
                            user = user.copy(authId = firebaseUser?.authId)
                            createUser(user)
                        }
                        else -> handleError(it)
                    }
                }
                .onNextUi { user ->
                    authPreferences.saveDroppUser(user)
                    val needToSetup =
                        user.username == null || user.description == null || user.gender == null
                    successAuth(needToSetup)
                }
        }
    }

    private fun createUser(user: DroppUser) {
        launchFlow {
            userInteractor.createUser(user).subscribeWithError {
                authPreferences.saveDroppUser(user)
                successAuth(true)
            }
        }
    }

    private suspend fun successAuth(needToSetupUser: Boolean) {
        val onboardingShown = onboardingPreferences.isOnboardingShown()
        _authSuccessResult.emit(Pair(onboardingShown, needToSetupUser))
    }
}