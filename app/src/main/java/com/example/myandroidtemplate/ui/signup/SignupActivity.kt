package com.example.myandroidtemplate.ui.signup
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myandroidtemplate.databinding.ActivitySignupBinding
import com.example.myandroidtemplate.ui.common.UiState
import com.example.myandroidtemplate.ui.home.HomeActivity
import com.example.myandroidtemplate.utils.clearErrorOnType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        observeState()
    }

    private fun setupUi() {
        binding.tilName.clearErrorOnType()
        binding.tilEmail.clearErrorOnType()
        binding.tilPassword.clearErrorOnType()

        binding.btnSignup.setOnClickListener {
            viewModel.signup(
                name = binding.etName.text.toString().trim(),
                email = binding.etEmail.text.toString().trim(),
                password = binding.etPassword.text.toString().trim()
            )
        }
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collectLatest { state ->
                when (state) {

                    UiState.Idle -> showIdle()

                    UiState.Loading -> showLoading()

                    is UiState.Success -> {
                        showIdle()
                        navigateToHome()
                    }

                    is UiState.ValidationError -> {
                        showIdle()
                        binding.tilName.error = state.formError.name
                        binding.tilEmail.error = state.formError.email
                        binding.tilPassword.error = state.formError.password
                    }

                    is UiState.Error -> {
                        showIdle()
                        Toast.makeText(
                            this@SignupActivity,
                            state.error.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSignup.isEnabled = false
    }

    private fun showIdle() {
        binding.progressBar.visibility = View.GONE
        binding.btnSignup.isEnabled = true
    }

    private fun navigateToHome() {
        startActivity(
            Intent(this, HomeActivity::class.java)
        )
        finish() // prevent going back to signup
    }
}
