package com.eratart.feature.item.creation.view.size.adapter

import android.view.ViewGroup
import com.eratart.baseui.base.recycler.BaseRecyclerAdapter
import com.eratart.domain.model.domain.Size
import com.eratart.feature.item.creation.databinding.ListItemSizeBinding

class SizeAdapter(viewModels: MutableList<Size>) :
    BaseRecyclerAdapter<Size>(viewModels) {

    override fun getBindingViewHolder(viewType: Int, parent: ViewGroup): SizeHolder {
        val view = ListItemSizeBinding.inflate(getInflater(parent), parent, false)
        return SizeHolder(view)
    }
}