package com.example.myandroidtemplate.ui.signup

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
class SignupViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val uiState: StateFlow<UiState<Unit>> = _uiState.asStateFlow()

    fun signup(
        name: String,
        email: String,
        password: String
    ) {
        //  Form validation
        val formError = FormValidator.validateSignup(
            name = name,
            email = email,
            password = password
        )

        if (formError.hasErrors()) {
            _uiState.value = UiState.ValidationError(formError)
            return
        }

        //  Async signup
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.signup(
                    name = name,
                    email = email,
                    password = password
                )

                //  CRITICAL: emit SUCCESS
                _uiState.value = UiState.Success(Unit)

            } catch (t: Throwable) {
                _uiState.value = UiState.Error(
                    ErrorMapper.map(t)
                )
            }
        }
    }
}
