package com.eratart.data.model.mapper

import com.eratart.data.model.response.CategoriesResponse
import com.eratart.data.model.response.CategoryResponse
import com.eratart.domain.model.domain.Categories
import com.eratart.domain.model.domain.Category

class CategoriesMapper : IMapper<List<CategoriesResponse>, List<Categories>> {

    private val genderMapper by lazy { GenderMapper() }

    override fun mapFrom(item: List<CategoriesResponse>): List<Categories> {
        return item.map { categoriesResponse ->
            val categories = categoriesResponse.categories.map { mapCategory(it) }
            Categories(genderMapper.mapFrom(categoriesResponse.gender), categories)
        }
    }

    private fun mapCategory(categoryResponse: CategoryResponse): Category {
        return with(categoryResponse) {
            val mappedCategories =
                (childCategories ?: emptyList()).map { mapCategory(it) }
            Category(id, title, key, level, genderMapper.mapFrom(gender), mappedCategories)
        }
    }

    override fun mapTo(item: List<Categories>): List<CategoriesResponse> {
        return emptyList()
    }
}