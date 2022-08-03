package com.eratart.baseui.view.other

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.tabs.TabLayout

class TabLayoutSingleListener(context: Context, attributeSet: AttributeSet?) :
    TabLayout(context, attributeSet) {

    private var singleListener: OnTabSelectedListener? = null

    fun initTabLayout(names: List<String>, onTabChangedListener: (Int) -> Unit) {
        names.forEach {
            addTab(newTab().setText(it))
        }
        val listener = object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab) {
                onTabChangedListener.invoke(tab.position)
            }

            override fun onTabUnselected(tab: Tab) {
            }

            override fun onTabReselected(tab: Tab) {
            }
        }
        singleListener = listener
        val listenerToSet = singleListener ?: return
        addOnTabSelectedListener(listenerToSet)
    }

    fun selectTabWithoutTrigger(index: Int) {
        val listener = singleListener ?: return
        removeOnTabSelectedListener(listener)
        selectTab(getTabAt(index))
        addOnTabSelectedListener(listener)
    }
}