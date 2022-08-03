package com.eratart.feature.main.feed.view.feed

import androidx.fragment.app.FragmentActivity
import com.eratart.baseui.view.viewpager.BaseViewPager2Adapter
import com.eratart.feature.main.feed.view.adverts.view.tabs.AdvertsFragment
import com.eratart.feature.main.feed.view.adverts.view.tabs.FavoritesFragment
import com.eratart.feature.main.feed.view.adverts.view.tabs.MyAdvertsFragment

class FeedViewPagerAdapter(activity: FragmentActivity) : BaseViewPager2Adapter(activity) {

    override val fragments by lazy {
        arrayListOf(
            AdvertsFragment.newInstance(),
            FavoritesFragment.newInstance(),
            MyAdvertsFragment.newInstance(),
        )
    }
}