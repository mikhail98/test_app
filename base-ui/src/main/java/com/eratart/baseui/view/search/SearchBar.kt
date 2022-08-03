package com.eratart.baseui.view.search

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.eratart.baseui.R
import com.eratart.baseui.extensions.hideKeyboardFrom
import com.eratart.baseui.extensions.setOnImeActionSearchClickListener
import com.eratart.baseui.extensions.showKeyboardFrom
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce

class SearchBar(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    init {
        inflate(context, R.layout.view_search_bar, this)
    }

    private val etSearch by lazy { findViewById<EditText>(R.id.etSearch) }
    private val ivClear by lazy { findViewById<ImageView>(R.id.ivClear) }

    private val clearClickListener by lazy {
        OnClickListener { etSearch.text.clear() }
    }

    var isActive: Boolean = false
        set(value) {
            etSearch.requestFocus()
            if (value) etSearch.showKeyboardFrom() else etSearch.hideKeyboardFrom()
            field = value
        }

    fun subscribeTextUpdated(debounceDelayMills: Long): Flow<String> {
        ivClear.setOnClickListener(clearClickListener)
        return callbackFlow {
            etSearch.addTextChangedListener { editable ->
                ivClear.isVisible = !editable.isNullOrBlank()
                trySend(editable.toString())
            }
            awaitClose()
        }.debounce(debounceDelayMills)
    }

    fun setOnSearchClickListener(action: (String) -> Unit) {
        etSearch.setOnImeActionSearchClickListener(action)
    }
}