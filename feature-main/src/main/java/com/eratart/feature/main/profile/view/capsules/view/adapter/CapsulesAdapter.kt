package com.eratart.feature.main.profile.view.capsules.view.adapter

import android.view.ViewGroup
import com.eratart.baseui.base.recycler.BaseRecyclerAdapter
import com.eratart.domain.model.domain.Capsule
import com.eratart.feature.main.databinding.ListItemCapsuleBinding

class CapsulesAdapter(
    viewModels: MutableList<Capsule>,
    private val itemCardHeight: Float,
    private val listener: IListener,
) :
    BaseRecyclerAdapter<Capsule>(viewModels) {


    override fun getBindingViewHolder(
        viewType: Int, parent: ViewGroup,
    ): CapsuleHolder {
        val view = ListItemCapsuleBinding.inflate(getInflater(parent), parent, false)
        return CapsuleHolder(view, itemCardHeight, listener)
    }

    interface IListener : CapsuleHolder.ICapsuleListener
}