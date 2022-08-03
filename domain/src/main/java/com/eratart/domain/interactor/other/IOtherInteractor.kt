package com.eratart.domain.interactor.other

import com.eratart.domain.model.domain.Brand
import com.eratart.domain.model.domain.Categories
import com.eratart.domain.model.domain.Style
import kotlinx.coroutines.flow.Flow

interface IOtherInteractor {
    fun getCategories(): Flow<List<Categories>>
    fun getBrands(): Flow<List<Brand>>
    fun getStyles(): Flow<List<Style>>
}