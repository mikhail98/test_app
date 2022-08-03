package com.eratart.domain.model.other

class TokenResult(
    val isSuccess: Boolean,
    val token: String?,
    val exception: Exception?
)