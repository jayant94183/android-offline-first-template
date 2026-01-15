package com.example.myandroidtemplate.ui.common

sealed class UiError(val message: String) {

    data class Network(
        val reason: String = "No internet connection. Please try again."
    ) : UiError(reason)

    object Unauthorized :
        UiError("Your session has expired. Please log in again.")

    object Server :
        UiError("Server error. Please try again later.")

    object Timeout :
        UiError("Request timed out. Check your connection.")

    data class Validation(val reason: String) :
        UiError(reason)

    object Unknown :
        UiError("Something went wrong. Please try again.")
}