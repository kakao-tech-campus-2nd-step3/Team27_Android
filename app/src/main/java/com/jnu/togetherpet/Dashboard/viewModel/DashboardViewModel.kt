package com.jnu.togetherpet.dashboard.viewModel

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jnu.togetherpet.R
import com.jnu.togetherpet.fragment.CommunityFragment
import com.jnu.togetherpet.fragment.DiaryFragment
import com.jnu.togetherpet.home.view.HomeFragment
import com.jnu.togetherpet.fragment.WalkingPetFragment
import com.jnu.togetherpet.searching.searchingHome.view.SearchingPetFragment

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