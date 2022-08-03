package com.eratart.data.model.mapper.dialogs

import com.eratart.data.model.mapper.IMapper
import com.eratart.data.model.response.DialogResponse
import com.eratart.domain.model.domain.dialogs.Dialog
import com.eratart.domain.model.domain.dialogs.LastMessage

class DialogsMapper : IMapper<DialogResponse, Dialog> {
    override fun mapFrom(item: DialogResponse): Dialog {
        return Dialog(
            title = item.title,
            unreadCount = item.unreadCount,
            avatarUrl = item.avatarUrl,
            lastMessage = LastMessage(
                isYouAuthor = item.isYouLastMessageAuthor,
                date = item.lastMessageDate,
                text = item.lastMessageText,
                isRead = item.isLastMessageRead
            )
        )
    }

    override fun mapTo(item: Dialog): DialogResponse {
        error("${item.javaClass} to DataDialog is not supported")
    }
}