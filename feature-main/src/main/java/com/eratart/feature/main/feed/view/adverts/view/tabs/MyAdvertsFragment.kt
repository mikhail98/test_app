package com.eratart.feature.main.feed.view.adverts.view.tabs

import com.eratart.feature.main.R
import com.eratart.feature.main.feed.view.adverts.entity.AdvertTab
import com.eratart.feature.main.feed.view.adverts.view.tabs.base.BaseAdvertsFragment

class MyAdvertsFragment : BaseAdvertsFragment() {

    companion object {
        fun newInstance(): MyAdvertsFragment {
            return MyAdvertsFragment()
        }
    }

    override val tab = AdvertTab.MY_ADVERTS
    override val shopChips = false

    override val emptyTitleRes: Int = R.string.feature_main_feed_my_items_empty_title
    override val emptyDescriptionRes: Int = R.string.feature_main_feed_my_items_empty_description
    override val emptyIconRes: Int = R.drawable.ic_empty_my_items
}