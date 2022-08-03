package com.eratart.baseui.view.edittext.listeners

import android.text.Editable
import android.text.TextWatcher

class TextListener(private val textListener: (String) -> Unit) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(editable: Editable) {
        textListener.invoke(editable.toString())
    }
}