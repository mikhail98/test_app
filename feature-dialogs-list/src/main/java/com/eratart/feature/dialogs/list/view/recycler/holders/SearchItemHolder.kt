package com.eratart.feature.dialogs.list.view.recycler.holders

import androidx.core.view.isVisible
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.feature.dialogs.list.databinding.ItemDialogSearchBinding
import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems
import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems.SearchItem
import com.eratart.feature.dialogs.list.view.recycler.SearchItemActionClickListener

class SearchItemHolder(
    binding: ItemDialogSearchBinding,
    private val clickListener: SearchItemActionClickListener
) : DialogsListHolder<ItemDialogSearchBinding>(binding) {

    private val ivAvatar by lazy { binding.ivAvatar }
    private val cvAvatar by lazy { binding.cvAvatar }
    private val tvMessage by lazy { binding.tvMessage }
    private val ivAction by lazy { binding.ivAction }
    private val tvTitle by lazy { binding.tvName }
    private val guideline by lazy { binding.guideline }

    override fun bindItem(item: DialogsListScreenItems) {
        with(item as SearchItem) {
            ivAvatar.loadImageWithGlide(avatarUrl)

            val isAvatarVisible = avatarUrl != null
            cvAvatar.isVisible = isAvatarVisible
            guideline.isVisible = isAvatarVisible

            tvMessage.text = message
            tvTitle.text = title

            ivAction.setImageResource(buttonAction.iconId)
            ivAction.setOnClickListener { clickListener.onClick(item) }
        }
    }
}