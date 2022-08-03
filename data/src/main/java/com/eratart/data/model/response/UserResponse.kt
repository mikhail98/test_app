package com.eratart.data.model.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    var _id: Long?,
    @SerializedName("firstName")
    var firstName: String?,
    @SerializedName("lastName")
    var lastName: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("username")
    var username: String?,
    @SerializedName("photo")
    var photo: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("authId")
    var authId: String?,
    @SerializedName("gender")
    var gender: Int?,
    @SerializedName("createdAt")
    var createdAt: String?,
    @SerializedName("updatedAt")
    var updatedAt: String?
)