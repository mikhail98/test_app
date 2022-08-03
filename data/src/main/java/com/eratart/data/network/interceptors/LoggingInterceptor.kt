package com.eratart.data.network.interceptors

import com.eratart.core.constants.StringConstants
import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptor : Interceptor {

    companion object {
        private const val HTTPS = "HTTPS"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val request = response.request
        val http = if (request.isHttps) {
            HTTPS.plus(StringConstants.SPACE)
        } else {
            StringConstants.EMPTY
        }
        val message = if (response.message.isNotEmpty()) {
            response.message.plus(StringConstants.SPACE)
        } else {
            StringConstants.EMPTY
        }
        println("RE:: $http${request.method} $message${response.code} ${request.url}")
        return response
    }
}