package com.eratart.feature.item.creation.view.categories.adapter

import android.view.ViewGroup
import com.eratart.baseui.base.recycler.BaseRecyclerAdapter
import com.eratart.feature.item.creation.databinding.ListItemCategoryBinding
import com.eratart.feature.item.creation.view.categories.entity.CategoryEntity

class CategoriesAdapter(viewModels: MutableList<CategoryEntity>) :
    BaseRecyclerAdapter<CategoryEntity>(viewModels) {

    override fun getBindingViewHolder(viewType: Int, parent: ViewGroup): CategoriesHolder {
        val view = ListItemCategoryBinding.inflate(getInflater(parent), parent, false)
        return CategoriesHolder(view)
    }
}