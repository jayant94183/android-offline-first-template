package com.example.myandroidtemplate.ui.common

sealed class UiState<out T> {

    object Idle : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<T>(
        val data: T
    ) : UiState<T>()

    data class Error(
        val error: UiError
    ) : UiState<Nothing>()

    data class ValidationError(
        val formError: FormError
    ) : UiState<Nothing>()
}