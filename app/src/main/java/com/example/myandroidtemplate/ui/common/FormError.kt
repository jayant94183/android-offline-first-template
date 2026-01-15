package com.example.myandroidtemplate.ui.common

data class FormError(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null
) {
    fun hasErrors(): Boolean =
        name != null || email != null || password != null
}