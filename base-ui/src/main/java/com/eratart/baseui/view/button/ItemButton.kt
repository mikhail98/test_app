package com.eratart.baseui.view.button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.StringRes
import com.eratart.baseui.R
import com.google.android.material.textview.MaterialTextView

class ItemButton(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    private val view = inflate(context, R.layout.view_item_button, this)

    private val ivMenuItem: ImageView by lazy { view.findViewById(R.id.ivMenuItem) }
    private val tvMenuItem: MaterialTextView by lazy { view.findViewById(R.id.tvMenuItem) }

    private val attributes by lazy {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.MenuItem, 0, 0)
    }
    private var attrText: String? = null
    private var attrIcon: Drawable? = null

    init {
        try {
            attrText = attributes.getString(R.styleable.MenuItem_text)
            attrIcon = attributes.getDrawable(R.styleable.MenuItem_icon)
        } finally {
            attributes.recycle()
        }

        setText(attrText)

        if (attrIcon != null) {
            ivMenuItem.setImageDrawable(attrIcon)
        }
    }

    fun setText(@StringRes res: Int) {
        setText(context.getString(res))
    }

    fun setText(string: String?) {
        tvMenuItem.text = string
    }
}