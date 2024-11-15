package com.jnu.togetherpet.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jnu.togetherpet.ui.activity.login.LoginActivity
import com.jnu.togetherpet.R
import com.jnu.togetherpet.ui.viewmodel.SplashActivityViewModel
import com.jnu.togetherpet.ui.activity.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var splashScreen: SplashScreen
    private val viewModel: SplashActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        viewModel.isLoggedIn()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            repeatOnLifecycle((Lifecycle.State.STARTED)) {
                viewModel.userLoginState.collectLatest {
                    delay(3000)
                    if (it) navigateToHome()
                    else navigateToLogin()
                }
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHome() {
        val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}