package com.eratart.data.model.response

import com.google.gson.annotations.SerializedName

data class DialogsListResponse(
    @SerializedName("dialogs")
    val dialogs: List<DialogResponse>
)

data class DialogResponse(
    @SerializedName("title")
    val title: String,
    @SerializedName("lastMessageDate")
    val lastMessageDate: String,
    @SerializedName("unreadCount")
    val unreadCount: Int,
    @SerializedName("avatarUrl")
    val avatarUrl: String?,
    @SerializedName("isYouLastMessageAuthor")
    val isYouLastMessageAuthor: Boolean,
    @SerializedName("lastMessageText")
    val lastMessageText: String,
    @SerializedName("isLastMessageRead")
    val isLastMessageRead: Boolean
)