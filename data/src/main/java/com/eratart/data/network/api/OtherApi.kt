package com.eratart.data.network.api

import com.eratart.data.model.response.BrandResponse
import com.eratart.data.model.response.CategoriesResponse
import retrofit2.http.GET

interface OtherApi {

    @GET("categories/tree")
    suspend fun getCategories(): List<CategoriesResponse>

    @GET("brands")
    suspend fun getBrands(): List<BrandResponse>
}