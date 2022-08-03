package com.eratart.feature.main.feed.view.adverts.view.tabs

import com.eratart.feature.main.R
import com.eratart.feature.main.feed.view.adverts.entity.AdvertTab
import com.eratart.feature.main.feed.view.adverts.view.tabs.base.BaseAdvertsFragment

class FavoritesFragment : BaseAdvertsFragment() {

    companion object {
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }

    override val tab = AdvertTab.FAVORITES
    override val shopChips = false

    override val emptyTitleRes: Int =  R.string.feature_main_feed_favorite_empty_title
    override val emptyDescriptionRes: Int = R.string.feature_main_feed_favorite_empty_description
    override val emptyIconRes: Int = R.drawable.ic_empty_favorites
}