package com.eratart.baseui.base.recycler

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseRecyclerViewHolder<VM, VB : ViewBinding>(
    private val binding: VB
) :
    RecyclerView.ViewHolder(binding.root) {

    protected val containerView by lazy { binding.root }
    protected val context: Context by lazy { containerView.context }

    private var onItemClick: ((VM, Int) -> Unit)? = null
    fun setOnItemClick(listener: ((VM, Int) -> Unit)?) {
        this.onItemClick = listener
    }

    open fun bindItem(item: VM) {
        containerView.setOnClickListener {
            onItemClick?.invoke(item, bindingAdapterPosition)
        }
    }
}