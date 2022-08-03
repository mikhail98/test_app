package com.eratart.data.repository

import com.eratart.data.exception.ThrowableTransformer
import com.eratart.data.model.mapper.BrandsMapper
import com.eratart.data.model.mapper.CategoriesMapper
import com.eratart.data.network.api.OtherApi
import com.eratart.data.repository.base.BaseRepository
import com.eratart.domain.model.domain.Brand
import com.eratart.domain.model.domain.Categories
import com.eratart.domain.repository.IOtherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class OtherRepository(
    private val categoriesApi: OtherApi,
) : BaseRepository(), IOtherRepository {

    private val categoriesMapper by lazy { CategoriesMapper() }
    private val brandsMapper by lazy { BrandsMapper() }

    override fun getCategories(): Flow<List<Categories>> {
        return flow { emit(categoriesApi.getCategories()) }
            .map { categoriesMapper.mapFrom(it) }
            .catch { throw ThrowableTransformer.transform(it) }
    }

    override fun getBrands(): Flow<List<Brand>> {
        return flow { emit(categoriesApi.getBrands()) }
            .map { brandsMapper.mapFrom(it) }
            .catch { throw ThrowableTransformer.transform(it) }
    }
}