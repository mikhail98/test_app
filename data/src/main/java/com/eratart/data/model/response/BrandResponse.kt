package com.eratart.data.model.response

import com.google.gson.annotations.SerializedName

data class BrandResponse(
    @SerializedName("id")
    val id: Long,

    @SerializedName("name")
    val name: String
)