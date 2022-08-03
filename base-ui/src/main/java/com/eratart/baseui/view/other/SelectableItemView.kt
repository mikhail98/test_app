package com.eratart.baseui.view.other

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.eratart.baseui.R
import com.eratart.core.constants.IntConstants
import com.google.android.material.textview.MaterialTextView

class SelectableItemView(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    private val view = inflate(context, R.layout.view_selectable_item, this)

    private val clBackground: ConstraintLayout by lazy { view.findViewById(R.id.clBackground) }
    private val ivIcon: ImageView by lazy { view.findViewById(R.id.ivIcon) }
    private val tvTitle: MaterialTextView by lazy { view.findViewById(R.id.tvTitle) }
    private val tvItem: MaterialTextView by lazy { view.findViewById(R.id.tvItem) }

    private val attributes by lazy {
        context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.SelectableItem, IntConstants.ZERO, IntConstants.ZERO
        )
    }
    private var attrTitle: String? = null
    private var attrIcon: Drawable? = null

    init {
        try {
            attrTitle = attributes.getString(R.styleable.SelectableItem_title)
            attrIcon = attributes.getDrawable(R.styleable.SelectableItem_icon)
        } finally {
            attributes.recycle()
        }

        setTitle(attrTitle)

        if (attrIcon != null) {
            ivIcon.setImageDrawable(attrIcon)
        }
    }

    fun setTitle(@StringRes res: Int) {
        setTitle(context.getString(res))
    }

    fun setTitle(string: String?) {
        tvTitle.text = string
    }

    fun setItem(@StringRes res: Int) {
        setItem(context.getString(res))
    }

    fun setItem(string: String?) {
        tvItem.text = string
        val backgroundRes = if (string != null) {
            R.drawable.bg_text_input_view_filled
        } else {
            R.drawable.bg_text_input_view
        }
        clBackground.setBackgroundResource(backgroundRes)
    }

    fun getItem() = tvItem.text.toString()
}