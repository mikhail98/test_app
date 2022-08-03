package com.eratart.feature.main.profile.viewmodel

import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.postDelayed
import com.eratart.core.constants.FloatConstants
import com.eratart.core.coroutines.catchUi
import com.eratart.core.coroutines.launchFlow
import com.eratart.domain.interactor.users.IUsersInteractor
import com.eratart.domain.model.domain.Capsule
import com.eratart.domain.model.domain.DroppUser
import com.eratart.domain.model.domain.Item
import com.eratart.domain.preferences.IAuthPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authPreferences: IAuthPreferences,
    private val usersInteractor: IUsersInteractor,
) : BaseViewModel() {

    private val _droppUser = MutableSharedFlow<DroppUser>()
    val droppUser = _droppUser.asSharedFlow()

    private val _wardrobeMark = MutableSharedFlow<Float>()
    val wardrobeMark = _wardrobeMark.asSharedFlow()

    private val _items = MutableSharedFlow<List<Item>>()
    val items = _items.asSharedFlow()

    private val _capsules = MutableSharedFlow<List<Capsule>>()
    val capsules = _capsules.asSharedFlow()

    fun fetchUser() {
        postDelayed(50) {
            launch {
                val user = authPreferences.getDroppUser() ?: return@launch
                _droppUser.emit(user)
            }
            val userId = authPreferences.getDroppUser()?.id ?: return@postDelayed
            launchFlow {
                usersInteractor.getItemsList(userId)
                    .zip(usersInteractor.getCapsulesList(userId)) { data1, data2 ->
                        Pair(data1, data2)
                    }
                    .catchUi {
                        _items.emit(listOf())
                        _capsules.emit(listOf())
                        _wardrobeMark.emit(FloatConstants.MINUS_ONE)
                    }
                    .subscribeWithError {
                        _items.emit(it.first)
                        _capsules.emit(it.second)
                        _wardrobeMark.emit(0.91f)
                    }
            }
        }
    }
}