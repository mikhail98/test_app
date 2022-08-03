package com.eratart.data.network

import com.eratart.data.network.api.OtherApi
import com.eratart.data.network.api.UsersApi

interface IRetrofitBuilder {

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val AUTH_BEARER = "Bearer "
    }

    fun getUsersApi(): UsersApi

    fun getOtherApi(): OtherApi
}