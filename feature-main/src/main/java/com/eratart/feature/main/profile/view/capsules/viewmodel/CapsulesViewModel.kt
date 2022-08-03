package com.eratart.feature.main.profile.view.capsules.viewmodel

import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.postDelayed
import com.eratart.core.coroutines.launchFlow
import com.eratart.domain.interactor.users.IUsersInteractor
import com.eratart.domain.model.domain.Capsule
import com.eratart.domain.preferences.IAuthPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CapsulesViewModel(
    private val authPreferences: IAuthPreferences,
    private val usersInteractor: IUsersInteractor,
) : BaseViewModel() {

    private val _capsules = MutableSharedFlow<List<Capsule>>()
    val capsules = _capsules.asSharedFlow()

    fun fetchCapsules() {
        val userId = authPreferences.getDroppUser()?.id ?: return
        postDelayed(100) {
            launchFlow {
                usersInteractor.getCapsulesList(userId)
                    .subscribeWithError { _capsules.emit(it) }
            }
        }
    }
}