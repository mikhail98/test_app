package com.eratart.feature.splash.viewmodel

import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.postDelayed
import com.eratart.core.coroutines.catchUi
import com.eratart.core.coroutines.launchFlow
import com.eratart.core.coroutines.onNextUi
import com.eratart.domain.interactor.users.IUsersInteractor
import com.eratart.domain.preferences.IAuthPreferences
import com.eratart.feature.splash.entity.SplashConstants
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val usersInteractor: IUsersInteractor,
    private val authPreferences: IAuthPreferences,
) : BaseViewModel() {

    private val _initData = MutableSharedFlow<Pair<Boolean, Boolean>>()
    val initData = _initData.asSharedFlow()

    fun fetchIsAuthorized() {
        postDelayed(SplashConstants.ANIM_DURATION) {
            val isAuthorized = authPreferences.getDroppUser() != null
            if (isAuthorized) {
                launchFlow {
                    usersInteractor.getUserByToken()
                        .catchUi { _initData.emit(Pair(true, true)) }
                        .onNextUi { user ->
                            authPreferences.saveDroppUser(user)
                            val needToSetup =
                                user.username == null || user.description == null || user.gender == null
                            _initData.emit(Pair(true, needToSetup))
                        }
                }
            } else {
                launch { _initData.emit(Pair(false, true)) }
            }
        }
    }
}