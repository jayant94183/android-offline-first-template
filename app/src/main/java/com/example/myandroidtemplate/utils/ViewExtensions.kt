package com.example.myandroidtemplate.utils

import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.clearErrorOnType() {
    editText?.addTextChangedListener {
        if (error != null) {
            error = null
        }
    }
}