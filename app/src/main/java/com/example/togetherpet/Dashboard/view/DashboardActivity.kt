package com.example.togetherpet.dashboard.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.togetherpet.dashboard.viewModel.DashboardViewModel
import com.example.togetherpet.R
import com.example.togetherpet.databinding.ActivityDashboardBinding
import com.example.togetherpet.home.view.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        handleItemClick()   //사용자가 클릭한 메뉴 정보를 viewModel에 전달
        setFragment(HomeFragment()) // 디폴트 Fragment 설정

        //viewModel이 선택한 Fragment 나타내기
        dashboardViewModel.selectedFragment.observe(this) { fragment ->
            setFragment(fragment)
        }
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_frameLayout, fragment)
            .commit()
    }

    private fun handleItemClick() {
        binding.homeBottomNavigationView.setOnItemSelectedListener { item ->
            dashboardViewModel.selectFragmentToShow(item.itemId)
            true
        }
    }
}