package com.eratart.feature.main.feed.view.adverts.viewmodel

import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.replaceAllWith
import com.eratart.core.coroutines.launchFlow
import com.eratart.domain.interactor.users.IUsersInteractor
import com.eratart.domain.model.domain.Advert
import com.eratart.domain.preferences.IAuthPreferences
import com.eratart.feature.main.feed.view.adverts.entity.AdvertTab
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AdvertsViewModel(
    private val userInteractor: IUsersInteractor,
    private val authPreferences: IAuthPreferences,
) : BaseViewModel() {

    private val _adverts = MutableSharedFlow<List<Advert>>()
    val adverts = _adverts.asSharedFlow()

    private val advertsList = ArrayList<Advert>()

    fun fetchAdverts(type: AdvertTab) {
        val userId = authPreferences.getDroppUser()?.id ?: return
        val flow = when (type) {
            AdvertTab.ADVERTS -> userInteractor.fetchAdverts(userId)
            AdvertTab.FAVORITES -> userInteractor.fetchFavoriteAdverts(userId)
            AdvertTab.MY_ADVERTS -> userInteractor.fetchMyAdverts(userId)
        }
        launchFlow {
            flow
                .applyLoader()
                .subscribeWithError { result ->
                    advertsList.replaceAllWith(result)
                    _adverts.emit(result)
                }
        }
    }

    fun handleFavorites(type: AdvertTab, advert: Advert, isFavorite: Boolean) {
        when (type) {
            AdvertTab.ADVERTS -> advertsList.find { it.id == advert.id }?.isFavorite = isFavorite
            AdvertTab.FAVORITES -> advertsList.removeIf { it.id == advert.id && !isFavorite }
            AdvertTab.MY_ADVERTS -> {}
        }
        launch { _adverts.emit(advertsList) }
    }
}