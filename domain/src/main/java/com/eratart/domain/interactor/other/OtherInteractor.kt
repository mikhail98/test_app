package com.eratart.domain.interactor.other

import com.eratart.core.coroutines.asFlow
import com.eratart.domain.model.domain.Brand
import com.eratart.domain.model.domain.Categories
import com.eratart.domain.model.domain.Style
import com.eratart.domain.repository.IOtherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class OtherInteractor(private val otherRepository: IOtherRepository) :
    IOtherInteractor {

    override fun getCategories(): Flow<List<Categories>> {
        return otherRepository.getCategories().flowOn(Dispatchers.IO)
    }

    override fun getBrands(): Flow<List<Brand>> {
        return otherRepository.getBrands().flowOn(Dispatchers.IO)
    }

    override fun getStyles(): Flow<List<Style>> {
        return MockUtil.getStyles().asFlow()
    }
}