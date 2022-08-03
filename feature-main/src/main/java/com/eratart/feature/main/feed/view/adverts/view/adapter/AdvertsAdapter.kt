package com.eratart.feature.main.feed.view.adverts.view.adapter

import android.view.ViewGroup
import com.eratart.baseui.base.recycler.BaseRecyclerAdapter
import com.eratart.domain.model.domain.Advert
import com.eratart.feature.main.databinding.ListItemAdvertBinding

class AdvertsAdapter(
    viewModels: MutableList<Advert>,
    private val itemCardHeight: Float,
    private val listener: IListener,
) :
    BaseRecyclerAdapter<Advert>(viewModels) {

    override fun getBindingViewHolder(viewType: Int, parent: ViewGroup): AdvertHolder {
        val view = ListItemAdvertBinding.inflate(getInflater(parent), parent, false)
        return AdvertHolder(view, itemCardHeight, listener)
    }

    interface IListener : AdvertHolder.IAdvertListener
}