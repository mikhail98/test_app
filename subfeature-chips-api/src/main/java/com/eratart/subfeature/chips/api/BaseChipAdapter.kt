package com.eratart.subfeature.chips.api

import android.view.ViewGroup
import com.eratart.baseui.base.recycler.BaseRecyclerAdapter
import com.eratart.subfeature.chips.api.databinding.ListItemChipsBinding

abstract class BaseChipAdapter<T : Any, VM : ChipItem<T>>(viewModels: MutableList<VM>) :
    BaseRecyclerAdapter<VM>(viewModels) {

    override fun getBindingViewHolder(
        viewType: Int, parent: ViewGroup,
    ): ChipHolder<T, VM> {
        val view = ListItemChipsBinding.inflate(getInflater(parent), parent, false)
        return ChipHolder(view)
    }

}