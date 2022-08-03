package com.eratart.data.repository.base

import com.eratart.data.network.IRetrofitBuilder.Companion.AUTH_BEARER
import com.eratart.domain.exception.UnauthorizedException
import com.eratart.tools.auth.IAuthTool
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import java.net.HttpURLConnection

abstract class BaseRepository {

    protected fun getTokenFlow(authTool: IAuthTool): Flow<String> {
        return authTool.getToken().flatMapConcat {
            val token = it.token
            if (it.isSuccess && token != null) {
                flow { emit(AUTH_BEARER.plus(token)) }
            } else {
                throw UnauthorizedException(HttpURLConnection.HTTP_UNAUTHORIZED)
            }
        }
    }
}