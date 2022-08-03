package com.eratart.subfeature.items.viewmodel

import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.postDelayed
import com.eratart.core.coroutines.launchFlow
import com.eratart.domain.interactor.users.IUsersInteractor
import com.eratart.domain.model.domain.Item
import com.eratart.domain.preferences.IAuthPreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class ItemsViewModel(
    private val authPreferences: IAuthPreferences,
    private val usersInteractor: IUsersInteractor,
) : BaseViewModel() {

    private val _items = MutableSharedFlow<List<Item>>()
    val items = _items.asSharedFlow()

    fun fetchItems() {
        val userId = authPreferences.getDroppUser()?.id ?: return
        postDelayed(100) {
            launchFlow {
                usersInteractor.getItemsList(userId)
                    .subscribeWithError { _items.emit(it) }
            }
        }
    }
}