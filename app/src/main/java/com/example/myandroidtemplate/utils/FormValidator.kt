package com.example.myandroidtemplate.utils

import android.util.Patterns
import com.example.myandroidtemplate.ui.common.FormError

object FormValidator {

    fun validateLogin(
        email: String,
        password: String
    ): FormError {
        return FormError(
            email = when {
                email.isBlank() -> "Email is required"
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    "Invalid email address"
                else -> null
            },
            password = when {
                password.isBlank() -> "Password is required"
                password.length < 6 ->
                    "Password must be at least 6 characters"
                else -> null
            }
        )
    }

    fun validateSignup(
        name: String,
        email: String,
        password: String
    ): FormError {
        return FormError(
            name = when {
                name.isBlank() -> "Name is required"
                name.length < 2 -> "Name is too short"
                else -> null
            },
            email = when {
                email.isBlank() -> "Email is required"
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    "Invalid email address"
                else -> null
            },
            password = when {
                password.isBlank() -> "Password is required"
                password.length < 6 ->
                    "Password must be at least 6 characters"
                else -> null
            }
        )
    }
}