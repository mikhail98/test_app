package com.eratart.domain.model.domain.dialogs

data class SearchedItem(
    val avatarUrl: String?,
    val title: String,
    val message: String,
    val actionType: ActionType
) {
    enum class ActionType(val action: String) {
        REMOVE("remove"),
        NEXT("next")
    }
}