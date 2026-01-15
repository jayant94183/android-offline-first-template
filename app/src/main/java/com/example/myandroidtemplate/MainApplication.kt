package com.example.myandroidtemplate

import android.app.Application
import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.myandroidtemplate.data.repository.UserRepository
import com.example.myandroidtemplate.ui.splash.SplashActivity
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var userRepository: UserRepository

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var splashLaunched = false

    override fun onCreate() {
        super.onCreate()

        // Observe auth state continuously
        appScope.launch {
            userRepository.observeUser()
                .collect { user ->
                    if (user == null && !splashLaunched) {
                        splashLaunched = true
                        restartToSplash()
                    }

                    if (user != null) {
                        splashLaunched = false
                    }
                }
        }

        //  Still observe foreground events
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            object : DefaultLifecycleObserver {
                override fun onStart(owner: LifecycleOwner) {
                    appScope.launch {
                        val user = userRepository.observeUser().first()
                        if (user == null && !splashLaunched) {
                            splashLaunched = true
                            restartToSplash()
                        }
                    }
                }
            }
        )
    }

    private fun restartToSplash() {
        val intent = Intent(this, SplashActivity::class.java).apply {
            addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
            )
        }
        startActivity(intent)
    }
}
