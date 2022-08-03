package com.eratart.baseui.view.button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.StringRes
import com.eratart.baseui.R
import com.google.android.material.textview.MaterialTextView

class OutlineButton(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    private val view = inflate(context, R.layout.view_button_outline, this)

    private val ivBtnIcon: ImageView by lazy { view.findViewById(R.id.ivBtnIcon) }
    private val tvBtnTitle: MaterialTextView by lazy { view.findViewById(R.id.tvBtnTitle) }

    private val attributes by lazy {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.OutlineButton, 0, 0)
    }
    private var attrText: String? = null
    private var attrIcon: Drawable? = null

    init {
        try {
            attrText = attributes.getString(R.styleable.OutlineButton_text)
            attrIcon = attributes.getDrawable(R.styleable.OutlineButton_icon)
        } finally {
            attributes.recycle()
        }

        setText(attrText)

        if (attrIcon != null) {
            ivBtnIcon.setImageDrawable(attrIcon)
        }
    }

    fun setText(@StringRes res: Int) {
        setText(context.getString(res))
    }

    fun setText(string: String?) {
        tvBtnTitle.text = string
    }
}