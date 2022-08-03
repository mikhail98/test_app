package com.eratart.feature.dialogs.list.viewmodel

import com.eratart.baseui.base.viewmodel.BaseViewModel
import com.eratart.baseui.extensions.delayedLaunch
import com.eratart.core.coroutines.launchFlow
import com.eratart.domain.interactor.dialogs.list.IDialogsListInteractor
import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems.SearchItem
import com.eratart.feature.dialogs.list.view.model.DialogsListEntity
import com.eratart.feature.dialogs.list.view.model.DialogsEntityMapper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DialogsListViewModel(
    private val dialogsListInteractor: IDialogsListInteractor,
    private val dialogsEntityMapper: DialogsEntityMapper
) : BaseViewModel() {

    private val _screenData = MutableSharedFlow<DialogsListEntity>()
    val screenInfo get() = _screenData.asSharedFlow()

    private val _isSearchActive = MutableSharedFlow<Boolean>()
    val isSearchActive get() = _isSearchActive

    private val _onNextInSearchItemClick = MutableSharedFlow<Boolean>()
    val onNextInSearchItemClick get() = _onNextInSearchItemClick

    fun handleOnSearchItemActionClick(item: SearchItem) {
        when (item.buttonAction) {
            SearchItem.ButtonAction.REMOVE -> {
            }
            SearchItem.ButtonAction.NEXT -> launch { onNextInSearchItemClick.emit(false) }
        }
    }

    fun onSearchButtonClicked() {
        delayedLaunch {
            _isSearchActive.emit(true)
        }
    }

    fun searchTextUpdated(query: String) {
        if (query.isEmpty()) {
            loadRecentlySearched()
        } else {
            launchFlow {
                dialogsListInteractor.searchInfo(query)
                    .mapLatest { searchedItems ->
                        val dialogsListUi = dialogsEntityMapper.mapSearchedItems(
                            searchedItems,
                            isOnlyRecentlySearch = false
                        )
                        _screenData.emit(dialogsListUi)
                    }
            }
        }
    }

    fun onBackButtonClicked(isSearchBarActive: Boolean) {
        if (isSearchBarActive) {
            delayedLaunch { _isSearchActive.emit(false) }
        } else {
            closeActivity()
        }
    }

    fun loadData(isSearch: Boolean = true) {
        delayedLaunch {
            if(isSearch) {
                loadRecentlySearched()
            } else {
                loadDialogs()
            }
        }
    }

    private fun loadRecentlySearched() {
        launchFlow {
            dialogsListInteractor.loadRecentlySearched()
                .mapLatest { recentlySearched ->
                    val dialogsListUi = dialogsEntityMapper.mapSearchedItems(
                        recentlySearched,
                        isOnlyRecentlySearch = true
                    )
                    _screenData.emit(dialogsListUi)
                }
        }
    }

    private fun loadDialogs() {
        launchFlow {
            dialogsListInteractor.loadDialogs()
                .applyLoader()
                .mapLatest { dialogs ->
                    val dialogsUi = dialogsEntityMapper.mapDialogs(dialogs)
                    _screenData.emit(dialogsUi)
                }
        }
    }
}