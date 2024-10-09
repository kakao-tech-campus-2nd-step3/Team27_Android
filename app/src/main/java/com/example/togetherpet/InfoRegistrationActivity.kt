package com.example.togetherpet

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoRegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_registration)
        enableEdgeToEdge()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_reg) as NavHostFragment
        val navController = navHostFragment.navController


    }
}