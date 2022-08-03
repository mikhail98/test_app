package com.eratart.baseui.view.button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import com.eratart.baseui.R

class SmallButton(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    private val view = inflate(context, R.layout.view_small_button, this)

    private val ivBtnIcon: ImageView by lazy { view.findViewById(R.id.ivBtnIcon) }

    private val attributes by lazy {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.SmallButton, 0, 0)
    }
    private var attrIcon: Drawable? = null

    init {
        try {
            attrIcon = attributes.getDrawable(R.styleable.SmallButton_icon)
        } finally {
            attributes.recycle()
        }

        if (attrIcon != null) {
            ivBtnIcon.setImageDrawable(attrIcon)
        }
    }
}