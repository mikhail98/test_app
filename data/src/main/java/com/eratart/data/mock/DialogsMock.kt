package com.eratart.data.mock

import com.eratart.data.model.response.DialogResponse
import com.eratart.data.model.response.DialogsListResponse
import com.eratart.domain.model.domain.dialogs.SearchedItem

class DialogsMock {

    companion object {
        private const val AVATAR_URL_STUB =
            "https://static.vecteezy.com/packs/media/components/global/search-explore-nav/img/vectors/term-bg-1-666de2d941529c25aa511dc18d727160.jpg"
    }

    fun getDialogs(): DialogsListResponse {

        val dialogs = mutableListOf<DialogResponse>()

        val dialogResponse = DialogResponse(
            title = "Dropp Support",
            lastMessageDate = "25.06.2022:12.47",
            unreadCount = 0,
            avatarUrl = "",
            isYouLastMessageAuthor = true,
            lastMessageText = "Now i'll show you from where blah blah",
            isLastMessageRead = true
        )

        for (i in 0..10) {
            val newItem = dialogResponse.copy(
                title = if (i.isOdd) "This or That Guy" else dialogResponse.title,
                lastMessageDate = if (i.isOdd) "25.06.2022:12.4".plus((i % 10).toString()) else dialogResponse.lastMessageDate,
                unreadCount = if (i > 7) i else 0,
                isYouLastMessageAuthor = !(i > 7 || i.isOdd),
                avatarUrl = if (i < 2) null else AVATAR_URL_STUB,
                isLastMessageRead = !(i > 4 || i.isOdd)
            )
            dialogs.add(newItem)
        }

        return DialogsListResponse(dialogs.toList())
    }

    fun getRecentlySearched(): List<SearchedItem> {
        val items = mutableListOf<SearchedItem>()
        val item = SearchedItem(
            avatarUrl = AVATAR_URL_STUB,
            title = "Mikhail Yarashevich",
            message = "User",
            actionType = SearchedItem.ActionType.REMOVE
        )

        for (i in 0..5) {
            val newItem = if (i > 3) {
                item.copy(
                    avatarUrl = null,
                    title = "Message $i",
                    message = "Message"
                )
            } else item
            items.add(newItem)
        }
        return items
    }

    fun getSearched(): List<SearchedItem> {
        val items = mutableListOf<SearchedItem>()
        items.add(createSearchItem())
        items.add(createSearchItem())
        items.add(
            createSearchItem(
                avatarUrl = null,
                title = "Death book",
                message = "Item"
            )
        )
        items.add(createSearchItem("Гулькин Семен", "User"))
        items.add(createSearchItem("Саня Пушник", "User"))
        items.add(createSearchItem("Собака", "User"))
        items.add(createSearchItem(avatarUrl = null, title = "Pants for 40 UAH", message = "Item"))
        items.add(createSearchItem(avatarUrl = null, title = "One more item", message = "Item"))
        items.add(createSearchItem(avatarUrl = null, title = "Nice item", message = "Сообщение"))

        return items
    }

    private fun createSearchItem(
        title: String = "Somebody Doe",
        message: String = "User",
        avatarUrl: String? = AVATAR_URL_STUB,
        actionType: SearchedItem.ActionType = SearchedItem.ActionType.NEXT
    ) = SearchedItem(
        avatarUrl = avatarUrl,
        title = title,
        message = message,
        actionType = actionType
    )

    private val Int.isOdd: Boolean
        get() = this % 2 == 0
}