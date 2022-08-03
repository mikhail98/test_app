package com.eratart.baseui.view.edittext.entity

data class TextInputViewState(
    var isActive: Boolean = false,
    var hasText: Boolean = false,
    var error: String? = null
)