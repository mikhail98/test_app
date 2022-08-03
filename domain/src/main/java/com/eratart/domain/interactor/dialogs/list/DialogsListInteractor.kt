package com.eratart.domain.interactor.dialogs.list

import com.eratart.domain.model.domain.dialogs.Dialog
import com.eratart.domain.model.domain.dialogs.SearchedItem
import com.eratart.domain.preferences.IRecentlySearchedPreferences
import com.eratart.domain.repository.IDialogsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*

class DialogsListInteractor(
    private val dialogsRepository: IDialogsRepository,
    private val recentlySearchedPrefs: IRecentlySearchedPreferences
) : IDialogsListInteractor {

    private val dispatcher: CoroutineDispatcher = IO

    private val recentlySearchedFlow by lazy {
        flow {
            emit(recentlySearchedPrefs.get())
        }.flowOn(dispatcher)
    }

    override fun loadRecentlySearched(): Flow<List<SearchedItem>> {
        return recentlySearchedFlow
    }

    override fun loadDialogs(): Flow<List<Dialog>> {
        return dialogsRepository.loadDialogs().flowOn(dispatcher)
    }

    override fun searchInfo(query: String): Flow<List<SearchedItem>> {
        val searchedInDialogs = dialogsRepository.searchInDialogs()

        return recentlySearchedFlow
            .combine(searchedInDialogs) { recently, inDialogs ->
                recently.plus(inDialogs)
            }
            .flatMapConcat { items ->
                flow {
                    emit(items.filter { item ->
                        item.title.contains(query) || item.message.contains(query)
                    })
                }
            }
    }
}