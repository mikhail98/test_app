package com.eratart.domain.interactor.dialogs.list

import com.eratart.domain.model.domain.dialogs.Dialog
import com.eratart.domain.model.domain.dialogs.SearchedItem
import kotlinx.coroutines.flow.Flow

interface IDialogsListInteractor {
    fun loadRecentlySearched(): Flow<List<SearchedItem>>
    fun loadDialogs(): Flow<List<Dialog>>
    fun searchInfo(query: String): Flow<List<SearchedItem>>
}