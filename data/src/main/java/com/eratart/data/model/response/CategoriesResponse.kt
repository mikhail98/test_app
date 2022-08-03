package com.eratart.data.model.response

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
    @SerializedName("gender")
    val gender: Int,
    @SerializedName("categories")
    val categories: List<CategoryResponse>,
)

data class CategoryResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("key")
    val key: String,
    @SerializedName("level")
    val level: Int,
    @SerializedName("gender")
    val gender: Int?,
    @SerializedName("childCategories")
    val childCategories: List<CategoryResponse>?,
)