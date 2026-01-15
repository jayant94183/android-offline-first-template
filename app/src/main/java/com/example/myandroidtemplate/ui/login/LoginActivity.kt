package com.example.myandroidtemplate.ui.login
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myandroidtemplate.databinding.ActivityLoginBinding
import com.example.myandroidtemplate.ui.common.UiState
import com.example.myandroidtemplate.ui.home.HomeActivity
import com.example.myandroidtemplate.ui.signup.SignupActivity
import com.example.myandroidtemplate.utils.clearErrorOnType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUi()
        observeState()
    }

    private fun setupUi() {
        binding.tilEmail.clearErrorOnType()
        binding.tilPassword.clearErrorOnType()

        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etEmail.text.toString().trim(),
                binding.etPassword.text.toString().trim()
            )
        }

        binding.tvSignup.setOnClickListener {
            startActivity(
                Intent(this, SignupActivity::class.java)
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
                        binding.tilEmail.error = state.formError.email
                        binding.tilPassword.error = state.formError.password
                    }

                    is UiState.Error -> {
                        showIdle()
                        Toast.makeText(
                            this@LoginActivity,
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
        binding.btnLogin.isEnabled = false
    }

    private fun showIdle() {
        binding.progressBar.visibility = View.GONE
        binding.btnLogin.isEnabled = true
    }

    private fun navigateToHome() {
        startActivity(
            Intent(this, HomeActivity::class.java)
        )
        finish() // prevent back navigation to login
    }
}
