package com.eratart.feature.dialogs.list.view.model

import android.text.SpannedString
import androidx.annotation.DrawableRes
import com.eratart.baseui.R

data class DialogsListEntity(
    val isRecentlySearchVisible: Boolean = false,
    val items: List<DialogsListScreenItems>
)

sealed class DialogsListScreenItems {

    data class DefaultDialog(
        val avatarUrl: String?,
        val title: String,
        val message: SpannedString,
        val time: String,
        val unreadCount: Int,
        val isReadUnreadIconVisible: Boolean,
        @DrawableRes val readUnreadIconId: Int
    ) : DialogsListScreenItems() {

        val hasUnread: Boolean
            get() = unreadCount != 0
    }

    data class SearchItem(
        val avatarUrl: String?,
        val title: String,
        val message: String,
        val buttonAction: ButtonAction
    ) : DialogsListScreenItems() {

        enum class ButtonAction(@DrawableRes val iconId: Int) {
            REMOVE(R.drawable.ic_cross_dark),
            NEXT(R.drawable.ic_arrow_right_dark)
        }
    }

    object NotFound : DialogsListScreenItems()
}
