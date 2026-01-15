package com.example.myandroidtemplate.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myandroidtemplate.data.repository.UserRepository
import com.example.myandroidtemplate.ui.common.UiState
import com.example.myandroidtemplate.utils.ErrorMapper
import com.example.myandroidtemplate.utils.FormValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        // 1️ Form validation (sync, before coroutine)
        val formError = FormValidator.validateLogin(email, password)
        if (formError.hasErrors()) {
            _uiState.value = UiState.ValidationError(formError)
            return
        }

        // 2⃣ Async login
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.login(email, password)

                //  emit SUCCESS
                _uiState.value = UiState.Success(Unit)

            } catch (t: Throwable) {
                _uiState.value = UiState.Error(
                    ErrorMapper.map(t)
                )
            }
        }
    }
}
