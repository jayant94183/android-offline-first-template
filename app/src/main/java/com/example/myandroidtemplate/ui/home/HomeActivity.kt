package com.example.myandroidtemplate.ui.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myandroidtemplate.R
import com.example.myandroidtemplate.databinding.ActivityHomeBinding
import com.example.myandroidtemplate.ui.login.LoginActivity
import com.example.myandroidtemplate.ui.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeUser()
        setupLogout()
    }

    private fun observeUser() {
        lifecycleScope.launchWhenStarted {
            viewModel.user.collectLatest { user ->
                binding.tvWelcome.text =
                    "Welcome ${user?.name ?: user?.email ?: "Guest"}"
            }
        }
    }

    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch {
                //  wait until Room is cleared
                viewModel.logout()

//                // then restart Splash
//                startActivity(
//                    Intent(this@HomeActivity, SplashActivity::class.java)
//                        .addFlags(
//                            Intent.FLAG_ACTIVITY_NEW_TASK or
//                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        )
//                )
            }
        }
    }
}
