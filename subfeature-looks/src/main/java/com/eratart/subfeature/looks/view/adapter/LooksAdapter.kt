package com.eratart.subfeature.looks.view.adapter

import android.view.ViewGroup
import com.eratart.baseui.base.recycler.BaseRecyclerAdapter
import com.eratart.domain.model.domain.Look
import com.eratart.subfeature.looks.databinding.ListItemLookBinding

class LooksAdapter(
    viewModels: MutableList<Look>,
    private val itemCardHeight: Float,
    private val itemCardWidth: Float,
    private val listener: IListener,
) :
    BaseRecyclerAdapter<Look>(viewModels) {

    override fun getBindingViewHolder(
        viewType: Int, parent: ViewGroup,
    ): LookHolder {
        val view = ListItemLookBinding.inflate(getInflater(parent), parent, false)
        return LookHolder(view, itemCardHeight, itemCardWidth, listener)
    }


    interface IListener : LookHolder.IItemListener

}