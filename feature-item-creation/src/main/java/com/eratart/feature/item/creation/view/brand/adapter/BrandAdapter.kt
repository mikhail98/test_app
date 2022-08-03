package com.eratart.feature.item.creation.view.brand.adapter

import android.view.ViewGroup
import com.eratart.baseui.base.recycler.BaseRecyclerAdapter
import com.eratart.feature.item.creation.databinding.ListItemBrandBinding
import com.eratart.feature.item.creation.databinding.ListItemBrandHeaderBinding
import com.eratart.feature.item.creation.view.brand.entity.BrandEntity

class BrandAdapter(
    private val viewModels: MutableList<BrandEntity>,
    private val listener: IBrandListener,
) :
    BaseRecyclerAdapter<BrandEntity>(viewModels) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_BRAND = 1
    }

    override fun getBindingViewHolder(viewType: Int, parent: ViewGroup): BaseBrandHolder<*> {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = ListItemBrandHeaderBinding.inflate(getInflater(parent), parent, false)
                BrandHeaderHolder(view)
            }
            VIEW_TYPE_BRAND -> {
                val view = ListItemBrandBinding.inflate(getInflater(parent), parent, false)
                BrandHolder(view, listener)
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (viewModels[position].title.isNullOrEmpty()) {
            VIEW_TYPE_BRAND
        } else {
            VIEW_TYPE_HEADER
        }
    }

    interface IBrandListener : BrandHolder.IListener
}