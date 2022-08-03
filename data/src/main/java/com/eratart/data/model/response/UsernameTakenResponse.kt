package com.eratart.data.model.response

import com.google.gson.annotations.SerializedName

class UsernameTakenResponse(
    @SerializedName("taken")
    val taken: Boolean?
)