package com.eratart.baseui.view.other

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.eratart.baseui.R
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.visible
import com.eratart.core.constants.IntConstants
import com.google.android.material.textview.MaterialTextView

class SelectableColorView(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    private val view = inflate(context, R.layout.view_selectable_color, this)

    private val clBackground: ConstraintLayout by lazy { view.findViewById(R.id.clBackground) }
    private val ivIcon: ImageView by lazy { view.findViewById(R.id.ivIcon) }
    private val tvTitle: MaterialTextView by lazy { view.findViewById(R.id.tvTitle) }
    private val crb1: ColorRadioButton by lazy { view.findViewById(R.id.crb1) }
    private val crb2: ColorRadioButton by lazy { view.findViewById(R.id.crb2) }
    private val crb3: ColorRadioButton by lazy { view.findViewById(R.id.crb3) }
    private val cbList = listOf(crb1, crb2, crb3)

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

    fun setItems(list: List<Int>) {
        val backgroundRes = if (list.isNotEmpty()) {
            R.drawable.bg_text_input_view_filled
        } else {
            R.drawable.bg_text_input_view
        }
        clBackground.setBackgroundResource(backgroundRes)


        cbList.forEachIndexed { index, cb ->
            val color = list.getOrNull(index)
            if (color != null) {
                cb.visible()
                cb.setColor(color)
            } else {
                cb.gone()
            }
        }
    }
}