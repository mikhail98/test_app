package com.eratart.feature.item.creation.view.style.adapter

import android.view.ViewGroup
import com.eratart.baseui.base.recycler.BaseRecyclerAdapter
import com.eratart.feature.item.creation.databinding.ListItemStyleBinding
import com.eratart.feature.item.creation.view.style.entity.StyleEntity

class StyleAdapter(viewModels: MutableList<StyleEntity>) :
    BaseRecyclerAdapter<StyleEntity>(viewModels) {

    override fun getBindingViewHolder(viewType: Int, parent: ViewGroup): StyleHolder {
        val view = ListItemStyleBinding.inflate(getInflater(parent), parent, false)
        return StyleHolder(view)
    }
}