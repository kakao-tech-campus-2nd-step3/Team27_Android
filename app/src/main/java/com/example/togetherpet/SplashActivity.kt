package com.example.togetherpet

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.togetherpet.Login.LoginActivity
import com.example.togetherpet.dashboard.view.DashboardActivity
import com.example.togetherpet.fragment.WalkingPetViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var splashScreen: SplashScreen
    private val viewModel : SplashActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashScreen = installSplashScreen()
        viewModel.isLoggedIn()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            repeatOnLifecycle((Lifecycle.State.STARTED)){
                viewModel.userLoginState.collectLatest{
                    delay(3000)
                    if (it) navigateToLogin()
                    else navigateToHome()
                }
            }
        }
    }

    fun navigateToLogin(){
        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun navigateToHome(){
        val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}