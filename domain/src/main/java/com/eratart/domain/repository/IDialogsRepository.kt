package com.eratart.domain.repository

import com.eratart.domain.model.domain.dialogs.Dialog
import com.eratart.domain.model.domain.dialogs.SearchedItem
import kotlinx.coroutines.flow.Flow

interface IDialogsRepository {
    fun loadDialogs(): Flow<List<Dialog>>

    fun searchInDialogs(): Flow<List<SearchedItem>>
}