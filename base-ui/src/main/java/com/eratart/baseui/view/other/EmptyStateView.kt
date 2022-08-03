package com.eratart.baseui.view.other

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.eratart.baseui.R
import com.eratart.baseui.extensions.loadImageWithGlide
import com.google.android.material.textview.MaterialTextView

class EmptyStateView(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    private val view = inflate(context, R.layout.view_empty_state, this)

    private val ivIcon: ImageView by lazy { view.findViewById(R.id.ivIcon) }
    private val tvEmptyTile: MaterialTextView by lazy { view.findViewById(R.id.tvEmptyTile) }
    private val tvEmptyDescription: MaterialTextView by lazy { view.findViewById(R.id.tvEmptyDescription) }

    private val attributes by lazy {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.EmptyState, 0, 0)
    }
    private var attrIcon: Drawable? = null
    private var attrTitle: String? = null
    private var attrDescription: String? = null

    init {
        try {
            attrIcon = attributes.getDrawable(R.styleable.SelectableItem_icon)
            attrTitle = attributes.getString(R.styleable.EmptyState_title)
            attrDescription = attributes.getString(R.styleable.EmptyState_description)

        } finally {
            attributes.recycle()
        }

        setTitle(attrTitle)
        setDescription(attrTitle)

        setIcon(attrIcon)
    }

    fun setIcon(@DrawableRes drawableRes: Int?) {
        val res = drawableRes ?: return
        val drawable = ContextCompat.getDrawable(context, res)
        setIcon(drawable)
    }

    fun setIcon(drawable: Drawable?) {
        ivIcon.loadImageWithGlide(drawable)
    }

    fun setTitle(@StringRes res: Int) {
        setTitle(context.getString(res))
    }

    fun setTitle(string: String?) {
        tvEmptyTile.text = string
    }

    fun setDescription(@StringRes res: Int) {
        setDescription(context.getString(res))
    }

    fun setDescription(string: String?) {
        tvEmptyDescription.text = string
    }


}