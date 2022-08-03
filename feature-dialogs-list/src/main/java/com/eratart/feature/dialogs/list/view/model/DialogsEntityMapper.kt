package com.eratart.feature.dialogs.list.view.model

import android.content.Context
import android.text.SpannedString
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import com.eratart.domain.model.domain.dialogs.Dialog
import com.eratart.domain.model.domain.dialogs.LastMessage
import com.eratart.domain.model.domain.dialogs.SearchedItem
import com.eratart.feature.dialogs.list.R
import com.eratart.baseui.R as baseUiR
import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems.DefaultDialog

class DialogsEntityMapper(private val context: Context) {
    fun mapDialogs(dialogs: List<Dialog>): DialogsListEntity {
        val isRecentlySearchVisible = false
        val uiDialogs = dialogs.map { dialog ->
            DefaultDialog(
                avatarUrl = dialog.avatarUrl,
                title = dialog.title,
                message = buildMessageText(dialog),
                time = resolveTime(dialog.lastMessage.date),
                unreadCount = dialog.unreadCount,
                isReadUnreadIconVisible = dialog.lastMessage.isYouAuthor,
                readUnreadIconId = resolveReadUnreadIcon(dialog.lastMessage)
            )
        }.sortedBy { it.time }
        return DialogsListEntity(
            isRecentlySearchVisible,
            uiDialogs
        )
    }

    fun mapSearchedItems(
        searchedItems: List<SearchedItem>,
        isOnlyRecentlySearch: Boolean
    ): DialogsListEntity {
        val uiItems = searchedItems.map { searchedItem ->
            DialogsListScreenItems.SearchItem(
                avatarUrl = searchedItem.avatarUrl,
                title = searchedItem.title,
                message = searchedItem.message,
                buttonAction = when (searchedItem.actionType) {
                    SearchedItem.ActionType.NEXT -> DialogsListScreenItems.SearchItem.ButtonAction.NEXT
                    SearchedItem.ActionType.REMOVE -> DialogsListScreenItems.SearchItem.ButtonAction.REMOVE
                }
            )
        }
        return DialogsListEntity(
            isOnlyRecentlySearch,
            uiItems.ifEmpty { listOf(DialogsListScreenItems.NotFound) }
        )
    }

    private fun resolveReadUnreadIcon(message: LastMessage): Int {
        return if (message.isYouAuthor) {
            if(message.isRead) {
                R.drawable.ic_read_message
            } else {
                R.drawable.ic_unread_message
            }
        } else 0
    }

    private fun resolveTime(date: String): String {
        return date.takeLast(5)
    }

    private fun buildMessageText(dialog: Dialog): SpannedString {
        val message = dialog.lastMessage
        val eminem = context.getColor(baseUiR.color.eminem)
        return buildSpannedString {
            if (message.isYouAuthor) {
                color(eminem) {
                    val prefix =
                        context.getString(R.string.feature_dialog_list_current_user_message_prefix)
                    append(prefix)
                }
            }
            if (dialog.unreadCount > 0) {
                color(eminem) {
                    append(message.text)
                }
            } else {
                append(message.text)
            }
        }
    }
}