package com.eratart.data.model.mapper

import com.eratart.data.model.response.BrandResponse
import com.eratart.domain.model.domain.Brand

class BrandsMapper : IMapper<List<BrandResponse>, List<Brand>> {

    override fun mapFrom(item: List<BrandResponse>): List<Brand> {
        return item.map { Brand(it.id, it.name) }
    }

    override fun mapTo(item: List<Brand>): List<BrandResponse> {
        return item.map { BrandResponse(it.id, it.name) }
    }
}