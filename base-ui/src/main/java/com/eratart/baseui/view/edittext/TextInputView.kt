package com.eratart.baseui.view.edittext

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.eratart.baseui.R
import com.eratart.baseui.extensions.dpToPx
import com.eratart.baseui.extensions.gone
import com.eratart.baseui.extensions.setOnTextChangedListener
import com.eratart.baseui.extensions.showKeyboardFrom
import com.eratart.baseui.extensions.visible
import com.eratart.baseui.view.edittext.entity.TextInputViewState
import com.eratart.core.constants.FloatConstants
import com.eratart.core.constants.IntConstants
import com.google.android.material.textview.MaterialTextView

class TextInputView(context: Context, attributeSet: AttributeSet? = null) :
    FrameLayout(context, attributeSet) {

    companion object {
        private const val ANIM_DURATION = 150L
    }

    private val view = inflate(context, R.layout.view_text_input_view, this)
    private val tvHint: MaterialTextView by lazy { view.findViewById(R.id.tvHint) }
    private val tvError: MaterialTextView by lazy { view.findViewById(R.id.tvError) }
    private val etText: BackPressedEditText by lazy { view.findViewById(R.id.etText) }
    private val clBackground: ConstraintLayout by lazy { view.findViewById(R.id.clBackground) }

    private val currentState = TextInputViewState()

    private val colorWhite = ContextCompat.getColor(context, R.color.eminem)
    private val colorSecondary = ContextCompat.getColor(context, R.color.gray_80)
    private val colorErrorPrimary = ContextCompat.getColor(context, R.color.red_error)
    private val colorErrorSecondary = ContextCompat.getColor(context, R.color.red_error_secondary)

    private val attributes by lazy {
        context.theme.obtainStyledAttributes(attributeSet, R.styleable.TextInput, 0, 0)
    }
    private var attrHint: String? = null

    init {
        try {
            attrHint = attributes.getString(R.styleable.TextInput_hint)
        } finally {
            attributes.recycle()
        }
        setHint(attrHint)

        etText.setOnTextChangedListener {
            updateViewWithNewState(currentState.copy(hasText = it.isNotEmpty()))
        }
        etText.setOnBackPressedListener {
            etText.clearFocus()
        }
        etText.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                updateViewWithNewState(currentState.copy(isActive = false))
            }
        }
        setOnClickListener {
            updateViewWithNewState(currentState.copy(isActive = true))
        }
    }

    private fun animateToNoTextNoErrorInactive() {
        tvHint.animateTextColor(colorSecondary, colorWhite)
        tvHint.animateTextSize(12f, 16f)
        animateHintPosition(0f)
        etText.gone()
    }

    private fun animateToTextNoError() {
        tvHint.animateTextColor(colorWhite, colorSecondary)
        tvHint.animateTextSize(16f, 12f)
        val offset = -8f - 1f.dpToPx()
        animateHintPosition(offset)
        etText.visible()
    }

    private fun TextView.animateTextColor(fromColor: Int, toColor: Int) {
        val animator = ObjectAnimator.ofInt(this, "textColor", fromColor, toColor)
        animator.setEvaluator(ArgbEvaluator())
        animator.duration = ANIM_DURATION
        animator.start()
    }

    private fun TextView.animateTextSize(fromSize: Float, toSize: Float) {
        val animator = ObjectAnimator.ofFloat(this, "textSize", fromSize, toSize)
        animator.duration = ANIM_DURATION
        animator.start()
    }

    private fun animateHintPosition(offset: Float) {
        val deltaStart = FloatConstants.ZERO

        val positionAnim = TranslateAnimation(
            deltaStart, deltaStart, deltaStart, offset.dpToPx()
        )
        positionAnim.fillAfter = true
        positionAnim.duration = ANIM_DURATION
        tvHint.startAnimation(positionAnim)
    }

    private fun updateViewWithNewState(newState: TextInputViewState) {
        if (currentState == newState) return

        if (currentState.isActive && !newState.isActive) {
            if (!newState.hasText) {
                animateToNoTextNoErrorInactive()
            }
            etText.clearFocus()
        }
        if (!currentState.isActive && newState.isActive) {
            if (!newState.hasText) {
                animateToTextNoError()
            }
            showKeyboardWithDelay()
        }

        if (currentState.error != null && newState.error == null) {
            if (newState.hasText) {
                clBackground.setBackgroundResource(R.drawable.bg_text_input_view_filled)
            } else {
                clBackground.setBackgroundResource(R.drawable.bg_text_input_view)
            }
            tvError.text = null
            tvError.gone()
            if (newState.hasText) {
                tvHint.animateTextColor(colorErrorSecondary, colorSecondary)
                etText.animateTextColor(colorErrorPrimary, colorWhite)
            } else {
                tvHint.animateTextColor(colorErrorPrimary, colorWhite)
                etText.animateTextColor(colorErrorPrimary, colorWhite)
            }
        }

        if (currentState.error == null && newState.error != null) {
            clBackground.setBackgroundResource(R.drawable.bg_text_input_view_error)
            tvError.text = newState.error
            tvError.visible()
            if (newState.hasText) {
                tvHint.animateTextColor(colorSecondary, colorErrorSecondary)
                etText.animateTextColor(colorWhite, colorErrorPrimary)
            } else {
                tvHint.animateTextColor(colorWhite, colorErrorPrimary)
                etText.animateTextColor(colorWhite, colorErrorPrimary)
            }
        }

        if (currentState.hasText && !newState.hasText) {
            if (!currentState.isActive) {
                animateToNoTextNoErrorInactive()
            }
            clBackground.setBackgroundResource(R.drawable.bg_text_input_view)
        }
        if (!currentState.hasText && newState.hasText) {
            if (!currentState.isActive) {
                animateToTextNoError()
            }
            clBackground.setBackgroundResource(R.drawable.bg_text_input_view_filled)
        }

        currentState.hasText = newState.hasText
        currentState.error = newState.error
        currentState.isActive = newState.isActive
    }

    private fun showKeyboardWithDelay() {
        etText.requestFocus()
        etText.showKeyboardFrom()
        etText.setSelection(etText.text?.length ?: IntConstants.ZERO)
    }

    fun setHint(@StringRes textId: Int) {
        tvHint.setText(textId)
    }

    fun setHint(text: String?) {
        tvHint.text = text
    }

    fun setText(@StringRes textId: Int) {
        etText.setText(textId)
    }

    fun setText(text: String?) {
        etText.setText(text)
    }

    fun getText() = etText.text.toString()

    fun getError() = tvError.text.toString()

    fun getEditText() = etText

    fun setErrorRes(@StringRes textId: Int?) {
        if (textId == null) {
            updateViewWithNewState(currentState.copy(error = null))
        } else {
            setError(context.getString(textId))
        }
    }

    fun setErrorRes(@StringRes textId: Int) {
        setError(context.getString(textId))
    }

    fun setError(text: String?) {
        updateViewWithNewState(currentState.copy(error = text))
    }

}