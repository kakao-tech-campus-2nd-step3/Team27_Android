package com.jnu.togetherpet.ui.activity.dashboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jnu.togetherpet.R
import com.jnu.togetherpet.databinding.ActivityDashboardBinding
import com.jnu.togetherpet.ui.fragment.home.HomeFragment
import com.jnu.togetherpet.ui.fragment.common.CustomToast
import com.jnu.togetherpet.ui.viewmodel.DashboardViewModel
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

        binding.boardAlarmBtn.setOnClickListener {
            CustomToast.displayToast(this, "현재 준비 중인 서비스 입니다")
        }

        binding.boardSettingBtn.setOnClickListener {
            CustomToast.displayToast(this, "현재 준비 중인 서비스 입니다")
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