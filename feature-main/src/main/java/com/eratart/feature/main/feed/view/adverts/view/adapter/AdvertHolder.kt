package com.eratart.feature.main.feed.view.adverts.view.adapter

import com.eratart.baseui.base.recycler.BaseRecyclerViewHolder
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.loadImageWithGlide
import com.eratart.baseui.extensions.setHeight
import com.eratart.baseui.extensions.visible
import com.eratart.core.constants.StringConstants
import com.eratart.domain.model.domain.Advert
import com.eratart.domain.model.enums.MyAdvertState
import com.eratart.feature.main.R
import com.eratart.feature.main.databinding.ListItemAdvertBinding

class AdvertHolder(
    private val binding: ListItemAdvertBinding,
    private val itemCardHeight: Float,
    private val listener: IAdvertListener,
) :
    BaseRecyclerViewHolder<Advert, ListItemAdvertBinding>(binding) {

    private val cvImage by lazy { binding.cvImage }
    private val ivAdvert by lazy { binding.ivAdvert }
    private val btnFavorite by lazy { binding.btnFavorite }
    private val tvPrice by lazy { binding.tvPrice }
    private val clMyAdvertOverlay by lazy { binding.clMyAdvertOverlay }
    private val ivMyAdvertState by lazy { binding.ivMyAdvertState }
    private val tvMyAdvertState by lazy { binding.tvMyAdvertState }
    private val tvTitle by lazy { binding.tvTitle }
    private val tvCategory by lazy { binding.tvCategory }
    private val btnMore by lazy { binding.btnMore }

    override fun bindItem(item: Advert) {
        super.bindItem(item)

        cvImage.setHeight(itemCardHeight)
        ivAdvert.loadImageWithGlide(item.item.photos.firstOrNull())

        val favoriteImageRes = if (item.isFavorite) {
            R.drawable.ic_fav_active
        } else {
            R.drawable.ic_fav_inactive
        }
        btnFavorite.loadImageWithGlide(favoriteImageRes)
        btnFavorite.setOnClickListener {
            listener.onFavoriteClick(item, newState = !item.isFavorite)
        }

        tvPrice.text =
            item.price.amount.toString().plus(StringConstants.SPACE).plus(item.price.currency.name)

        tvTitle.text = item.item.title
        tvCategory.text = item.item.category

        btnMore.setOnClickListener {
            listener.onMoreAdvertClick(item)
        }

        val myAdvertState = item.myAdvertState
        if (myAdvertState != null) {
            btnFavorite.gone()
            tvPrice.gone()
            clMyAdvertOverlay.visible()

            val bgRes = when (myAdvertState) {
                MyAdvertState.MODERATION -> com.eratart.baseui.R.color.blacked_40
                MyAdvertState.PUBLISHED -> com.eratart.baseui.R.color.accent_40
                MyAdvertState.REJECTED -> com.eratart.baseui.R.color.error_40
            }

            clMyAdvertOverlay.setBackgroundResource(bgRes)

            val textRes = when (myAdvertState) {
                MyAdvertState.MODERATION -> R.string.feature_main_feed_my_advert_on_moderation
                MyAdvertState.PUBLISHED -> R.string.feature_main_feed_my_advert_published
                MyAdvertState.REJECTED -> R.string.feature_main_feed_my_advert_rejected
            }
            tvMyAdvertState.setText(textRes)

            val imageRes = when (myAdvertState) {
                MyAdvertState.MODERATION -> R.drawable.ic_on_moderation
                MyAdvertState.PUBLISHED -> R.drawable.ic_published
                MyAdvertState.REJECTED -> R.drawable.ic_rejected
            }
            ivMyAdvertState.loadImageWithGlide(imageRes)

        } else {
            btnFavorite.visible()
            tvPrice.visible()
            clMyAdvertOverlay.gone()
        }
    }

    interface IAdvertListener {
        fun onMoreAdvertClick(item: Advert)
        fun onFavoriteClick(item: Advert, newState: Boolean)
    }
}