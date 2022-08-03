package com.eratart.feature.item.creation.entity

import com.eratart.domain.model.domain.Brand
import com.eratart.domain.model.domain.Categories
import com.eratart.domain.model.domain.Style

data class ParamsEntity(
    val categories: List<Categories> = arrayListOf(),
    val brands: List<Brand> = arrayListOf(),
    val styles: List<Style> = arrayListOf()
)