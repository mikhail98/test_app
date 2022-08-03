package com.eratart.domain.repository

import com.eratart.domain.model.domain.Brand
import com.eratart.domain.model.domain.Categories
import kotlinx.coroutines.flow.Flow

interface IOtherRepository {

    fun getCategories(): Flow<List<Categories>>

    fun getBrands(): Flow<List<Brand>>

}