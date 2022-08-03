package com.eratart.data.network.api

import com.eratart.data.model.response.UserResponse
import com.eratart.data.model.response.UsernameTakenResponse
import com.eratart.data.network.IRetrofitBuilder.Companion.AUTHORIZATION
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UsersApi {

    @GET("users/token")
    suspend fun getUserByToken(@Header(AUTHORIZATION) bearer: String): UserResponse

    @POST("users")
    suspend fun createUser(@Body user: UserResponse): UserResponse

    @PATCH("users/{userId}")
    suspend fun updateUser(
        @Header(AUTHORIZATION) bearer: String,
        @Path("userId") userId: Long,
        @Body user: UserResponse,
    ): UserResponse

    @GET("users/{userId}")
    suspend fun getUserById(
        @Header(AUTHORIZATION) bearer: String,
        @Path("userId") userId: Long,
    ): UserResponse

    @GET("/users/username/availability")
    suspend fun usernameTaken(
        @Header(AUTHORIZATION) bearer: String,
        @Query("username") username: String,
    ): UsernameTakenResponse
}