package com.eratart.feature.dialogs.list.view.recycler.holders

import androidx.core.view.isVisible
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.feature.dialogs.list.databinding.ItemDialogBinding
import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems
import com.eratart.feature.dialogs.list.view.model.DialogsListScreenItems.DefaultDialog
import com.eratart.feature.dialogs.list.view.recycler.OnDialogClickListener

class DialogHolder(
    binding: ItemDialogBinding,
    val clickListener: OnDialogClickListener
) : DialogsListHolder<ItemDialogBinding>(binding) {

    private val ivAvatar by lazy { binding.ivAvatar }
    private val cvAvatar by lazy { binding.cvAvatar }
    private val tvTitle by lazy { binding.tvName }
    private val tvMessage by lazy { binding.tvMessage }
    private val ivUnreadMessage by lazy { binding.ivReadMessage }
    private val tvTime by lazy { binding.tvTime }
    private val tvUnreadMessages by lazy { binding.tvUnreadMessages }
    private val guideline by lazy { binding.guideline }


    override fun bindItem(item: DialogsListScreenItems) {
        super.bindItem(item)
        with(item as DefaultDialog) {
            ivAvatar.loadImageWithGlide(avatarUrl)

            val isAvatarVisible = avatarUrl != null
            cvAvatar.isVisible = isAvatarVisible
            guideline.isVisible = isAvatarVisible

            tvTitle.text = title
            tvMessage.text = message
            tvTime.text = time

            tvUnreadMessages.isVisible = hasUnread
            tvUnreadMessages.text = unreadCount.toString()

            ivUnreadMessage.setImageResource(readUnreadIconId)
            ivUnreadMessage.isVisible = isReadUnreadIconVisible
        }
        setOnItemClick { defaultDialog, _ ->
            clickListener.onDialogClick(defaultDialog as DefaultDialog)
        }
    }
}