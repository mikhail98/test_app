package com.eratart.feature.item.creation.view.categories.adapter

import androidx.core.view.isVisible
import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.feature.item.creation.databinding.ListItemCategoryBinding
import com.eratart.feature.item.creation.view.categories.entity.CategoryEntity

class CategoriesHolder(
    private val binding: ListItemCategoryBinding,
) :
    BaseRecyclerViewHolder<CategoryEntity, ListItemCategoryBinding>(binding) {

    private val tvCategoryTitle by lazy { binding.tvCategoryTitle }
    private val ivCategorySelected by lazy { binding.ivCategorySelected }

    override fun bindItem(item: CategoryEntity) {
        super.bindItem(item)
        tvCategoryTitle.text = item.category.title
        ivCategorySelected.isVisible = item.isSelected
    }
}