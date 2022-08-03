package com.eratart.domain.model.domain.dialogs

class Dialog(
    val title: String,
    val unreadCount: Int,
    val avatarUrl: String?,
    val lastMessage: LastMessage
)

class LastMessage(
    val isYouAuthor: Boolean,
    val date: String,
    val text: String,
    val isRead: Boolean
)