package com.eratart.baseui.view.other

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.eratart.baseui.R

class DefaultFullscreenLoader(context: Context, attributeSet: AttributeSet? = null) :
    ConstraintLayout(context, attributeSet) {

    init {
        inflate(getContext(), R.layout.view_fullscreen_loader, this)
    }
}