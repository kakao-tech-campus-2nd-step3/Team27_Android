package com.example.togetherpet.Dashboard.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.togetherpet.R
import com.example.togetherpet.databinding.ActivityDashboardBinding
import com.example.togetherpet.fragment.CommunityFragment
import com.example.togetherpet.fragment.DiaryFragment
import com.example.togetherpet.fragment.HomeFragment
import com.example.togetherpet.fragment.SearchingPetFragment
import com.example.togetherpet.fragment.WalkingPetFragment

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setNaviClickListener()
        setFragment(HomeFragment()) // 디폴트 Fragment 설정
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.home_frameLayout, fragment)
            .commit()
    }

    private fun setNaviClickListener() {
        binding.homeBottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> {
                    setFragment(HomeFragment())
                    true
                }

                R.id.bottom_community -> {
                    setFragment(CommunityFragment())
                    true
                }

                R.id.bottom_searching -> {
                    setFragment(SearchingPetFragment())
                    true
                }

                R.id.bottom_diary -> {
                    setFragment(DiaryFragment())
                    true
                }

                R.id.bottom_walking -> {
                    setFragment(WalkingPetFragment())
                    true
                }

                else -> false
            }
        }
    }
}