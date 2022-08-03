package com.eratart.baseui.view.viewpager

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

abstract class BaseViewPager2Adapter(activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    abstract val fragments: List<Fragment>

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun getViewAtPosition(position: Int): View? {
        return fragments[position].view
    }
}