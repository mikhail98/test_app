package com.eratart.data.exception

import com.eratart.domain.exception.BackendException
import com.eratart.domain.exception.NotFoundException
import com.eratart.domain.exception.OtherApiException
import com.eratart.domain.exception.TimeoutException
import com.eratart.domain.exception.UnauthorizedException
import retrofit2.HttpException
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED
import java.net.SocketTimeoutException

object ThrowableTransformer {
    fun transform(e: Throwable): Throwable {
        return when (e) {
            is HttpException -> {
                when (val code = e.code()) {
                    HTTP_UNAUTHORIZED -> UnauthorizedException(code)
                    HTTP_NOT_FOUND -> NotFoundException(code)
                    else -> BackendException(code, e.message())
                }
            }
            is SocketTimeoutException -> TimeoutException()
            else -> OtherApiException(e.message.orEmpty())
        }
    }
}