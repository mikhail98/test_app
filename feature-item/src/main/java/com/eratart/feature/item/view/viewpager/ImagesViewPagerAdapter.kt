package com.eratart.feature.item.view.viewpager

import androidx.fragment.app.FragmentActivity
import com.eratart.baseui.view.viewpager.BaseViewPager2Adapter
import com.eratart.feature.item.view.viewpager.fragment.view.ImagesFragment

class ImagesViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val imagesList: List<String>,
) : BaseViewPager2Adapter(fragmentActivity) {

    override val fragments by lazy { imagesList.map { ImagesFragment.newInstance(it) } }
}