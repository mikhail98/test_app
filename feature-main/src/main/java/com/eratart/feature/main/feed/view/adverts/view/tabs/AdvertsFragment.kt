package com.eratart.feature.main.feed.view.adverts.view.tabs

import com.eratart.feature.main.R
import com.eratart.feature.main.feed.view.adverts.entity.AdvertTab
import com.eratart.feature.main.feed.view.adverts.view.tabs.base.BaseAdvertsFragment

class AdvertsFragment : BaseAdvertsFragment() {

    companion object {
        fun newInstance(): AdvertsFragment {
            return AdvertsFragment()
        }
    }

    override val tab = AdvertTab.ADVERTS
    override val shopChips = true

    override val emptyTitleRes: Int =  R.string.feature_main_feed_feed_empty_title
    override val emptyDescriptionRes: Int = R.string.feature_main_feed_feed_empty_description
    override val emptyIconRes: Int = R.drawable.ic_empty_feed
}