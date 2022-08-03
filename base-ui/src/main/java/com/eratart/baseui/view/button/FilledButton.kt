package com.eratart.baseui.view.button

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.eratart.baseui.R
import com.google.android.material.textview.MaterialTextView

class FilledButton(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    private val view = inflate(context, R.layout.view_button_filled, this)

    private val tvBtnTitle: MaterialTextView by lazy { view.findViewById(R.id.tvBtnTitle) }
    private val clBtnBackground: ConstraintLayout by lazy { view.findViewById(R.id.clBtnBackground) }

    private val attributes by lazy {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.FilledButton, 0, 0)
    }
    private var attrText: String? = null
    private var isChecked: Boolean? = null

    init {
        try {
            attrText = attributes.getString(R.styleable.FilledButton_text)
            isChecked = attributes.getBoolean(R.styleable.FilledButton_isActive, false)
        } finally {
            attributes.recycle()
        }

        setText(attrText)
        setActive(isChecked ?: false)
    }

    fun setText(@StringRes res: Int) {
        setText(context.getString(res))
    }

    fun setText(string: String?) {
        tvBtnTitle.text = string
    }

    private fun setInactive() {
        clBtnBackground.setBackgroundResource(R.drawable.bg_btn_filled_inactive)
        tvBtnTitle.setTextColor(ContextCompat.getColor(context, R.color.gray_80))
        isClickable = false
        isEnabled = false
    }

    private fun setActive() {
        clBtnBackground.setBackgroundResource(R.drawable.bg_btn_filled_active)
        tvBtnTitle.setTextColor(ContextCompat.getColor(context, R.color.eminem))
        isClickable = true
        isEnabled = true
    }

    fun setActive(isActive: Boolean) {
        if (isActive) setActive() else setInactive()
    }
}