package com.eratart.data.network

import com.eratart.core.BuildConfig
import com.eratart.data.network.api.OtherApi
import com.eratart.data.network.api.UsersApi
import com.eratart.data.network.interceptors.LoggingInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitBuilder() : IRetrofitBuilder {

    companion object {
        private const val CONNECTION_TIMEOUT = 60L
        private const val WRITE_TIMEOUT = 60L
        private const val READ_TIMEOUT = 60L

        private const val CONTENT_TYPE_PARAMETER = "content-type"
        private const val CONTENT_TYPE_VALUE = "application/json"

        private const val BASE_URL = "https://devapi.justdropp.com/"
    }

    private fun contentTypeHeaderParam() = Pair(CONTENT_TYPE_PARAMETER, CONTENT_TYPE_VALUE)

    private fun jsonHeader() = listOf(contentTypeHeaderParam())

    private fun <T> buildRetrofit(client: OkHttpClient, clazz: Class<T>) = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(clazz)

    private fun buildClient(headers: List<Pair<String, String>>) = with(OkHttpClient.Builder()) {
        connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)

        addInterceptor { chain -> makeHeaders(chain, headers) }
        makeLoggingInterceptor()?.run { addInterceptor(this) }
        makeLoggingInterceptor2()?.run { addInterceptor(this) }
        build()
    }

    private fun makeHeaders(
        chain: Interceptor.Chain, headers: List<Pair<String, String>>,
    ): Response {
        val requestBuilder = chain.request().newBuilder()
        headers.forEach { header ->
            requestBuilder.addHeader(header.first, header.second)
        }
        return chain.proceed(requestBuilder.build())
    }

    private fun makeLoggingInterceptor(): Interceptor? {
        return if (isDebug()) LoggingInterceptor() else null
    }

    private fun makeLoggingInterceptor2(): Interceptor? {
        return if (isDebug()) {
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        } else null
    }

    private fun isDebug(): Boolean = BuildConfig.DEBUG

    override fun getUsersApi(): UsersApi = getApi(UsersApi::class.java, jsonHeader())

    override fun getOtherApi(): OtherApi = getApi(OtherApi::class.java, jsonHeader())

    private fun <T> getApi(clazz: Class<T>, headers: List<Pair<String, String>>): T {
        return buildRetrofit(buildClient(headers), clazz)
    }
}