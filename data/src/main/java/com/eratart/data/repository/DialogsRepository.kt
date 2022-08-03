package com.eratart.data.repository

import com.eratart.data.mock.DialogsMock
import com.eratart.data.model.mapper.IMapper
import com.eratart.data.model.mapper.dialogs.DialogsMapper
import com.eratart.data.model.response.DialogResponse
import com.eratart.domain.model.domain.dialogs.Dialog
import com.eratart.domain.model.domain.dialogs.SearchedItem
import com.eratart.domain.repository.IDialogsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class DialogsRepository : IDialogsRepository {

    private val dialogMocks = DialogsMock()
    private val mapper: IMapper<DialogResponse, Dialog> = DialogsMapper()

    override fun loadDialogs(): Flow<List<Dialog>> {
        return flow {
            val dialogs = dialogMocks.getDialogs().dialogs.map { dialogResponse ->
                mapper.mapFrom(dialogResponse)
            }
            emit(dialogs)
        }
    }

    override fun searchInDialogs(): Flow<List<SearchedItem>> {
        return flow {
            val items = dialogMocks.getSearched()
            emit(items)
        }
    }
}