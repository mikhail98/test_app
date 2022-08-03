package com.eratart.baseui.view.other

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.eratart.baseui.R
import com.eratart.baseui.extensions.changeDrawableColor
import com.eratart.baseui.extensions.setHeightFrame
import com.eratart.baseui.extensions.setWidthFrame
import com.eratart.core.constants.IntConstants

class ColorRadioButton(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    private val view = inflate(context, R.layout.view_color_radio_button, this)

    private val ivColor: ImageView by lazy { view.findViewById(R.id.ivColor) }
    private val ivSelected: ImageView by lazy { view.findViewById(R.id.ivSelected) }
    private val clRoot: ConstraintLayout by lazy { view.findViewById(R.id.clRoot) }

    private val attributes by lazy {
        context.theme.obtainStyledAttributes(
            attributeSet, R.styleable.ColorRadioButton, IntConstants.ZERO, IntConstants.ZERO
        )
    }
    private var attrSelected: Boolean? = null
    private var attrColor: Int? = null
    private var attrSize: Int? = null

    private var isItemSelected: Boolean = false
    private var currentColor: Int = IntConstants.ZERO

    init {
        try {
            attrSelected = attributes.getBoolean(
                R.styleable.ColorRadioButton_selected, false
            )
            attrColor = attributes.getResourceId(
                R.styleable.ColorRadioButton_itemColor, IntConstants.ZERO
            )
            attrSize = attributes.getDimensionPixelSize(
                R.styleable.ColorRadioButton_itemSize, IntConstants.ZERO
            )
        } finally {
            attributes.recycle()
        }

        setItemSelected(attrSelected ?: false)
        setColor(attrColor ?: R.color.item_color_14)
        attrSize?.apply {
            setSize(this)
        }
    }

    fun setSize(size: Int) {
        clRoot.setHeightFrame(size)
        clRoot.setWidthFrame(size)
    }

    fun addOnCheckChangeListener(listener: ((isSelected: Boolean, color: Int) -> Unit)?) {
        if (listener != null) {
            setOnClickListener {
                setItemSelected(!isItemSelected)
                listener.invoke(isItemSelected, currentColor)
            }
        }
    }

    fun setItemSelected(isSelected: Boolean) {
        isItemSelected = isSelected
        ivSelected.isVisible = isSelected
    }

    fun setColor(color: Int) {
        this.currentColor = color
        if (currentColor != IntConstants.ZERO) {
            ivColor.setBackgroundColor(ContextCompat.getColor(context, color))
        }
        val colorsToOpposite = listOf(
            R.color.item_color_5,
            R.color.item_color_6,
            R.color.item_color_12,
            R.color.item_color_14,
        )
        val tintColor = if (colorsToOpposite.contains(color)) {
            R.color.blacked
        } else {
            R.color.eminem
        }
        ivSelected.changeDrawableColor(ContextCompat.getColor(context, tintColor))
    }

    fun getColor(): Int = currentColor

}