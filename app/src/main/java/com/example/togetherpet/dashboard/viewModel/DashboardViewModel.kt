package com.example.togetherpet.dashboard.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.togetherpet.R
import com.example.togetherpet.fragment.CommunityFragment
import com.example.togetherpet.fragment.DiaryFragment
import com.example.togetherpet.home.view.HomeFragment
import com.example.togetherpet.searching.SearchingPetFragment
import com.example.togetherpet.fragment.WalkingPetFragment

class DashboardViewModel : ViewModel() {
    private val _selectedFragment = MutableLiveData<Fragment>()
    val selectedFragment: LiveData<Fragment> get() = _selectedFragment

    fun selectFragmentToShow(itemId: Int) {
        val fragment = when (itemId) {
            R.id.bottom_home -> HomeFragment()
            R.id.bottom_community -> CommunityFragment()
            R.id.bottom_searching -> SearchingPetFragment()
            R.id.bottom_diary -> DiaryFragment()
            R.id.bottom_walking -> WalkingPetFragment()
            else -> HomeFragment()
        }
        _selectedFragment.value = fragment
    }
}