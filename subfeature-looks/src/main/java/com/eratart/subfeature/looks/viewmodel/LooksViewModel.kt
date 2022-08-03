package com.eratart.subfeature.looks.viewmodel

import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.postDelayed
import com.eratart.core.coroutines.launchFlow
import com.eratart.domain.interactor.users.IUsersInteractor
import com.eratart.domain.model.domain.Look
import com.eratart.domain.preferences.IAuthPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LooksViewModel(
    private val authPreferences: IAuthPreferences,
    private val usersInteractor: IUsersInteractor,
) : BaseViewModel() {

    private val _looks = MutableSharedFlow<List<Look>>()
    val looks = _looks.asSharedFlow()

    fun fetchItems() {
        val userId = authPreferences.getDroppUser()?.id ?: return
        postDelayed(100) {
            launchFlow {
                usersInteractor.getLooksList(userId)
                    .subscribeWithError { _looks.emit(it) }
            }
        }
    }
}