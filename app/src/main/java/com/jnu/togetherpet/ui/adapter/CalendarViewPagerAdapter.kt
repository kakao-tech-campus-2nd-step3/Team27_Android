package com.jnu.togetherpet.ui.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jnu.togetherpet.ui.fragment.walking.CalendarWeekFragment

class CalendarViewPagerAdapter(
    fm: FragmentActivity
) : FragmentStateAdapter(fm) {

    override fun getItemCount(): Int {
        Log.d("testt", "getItemCount")
        return Int.MAX_VALUE
    }

    override fun createFragment(position: Int): Fragment {
        Log.d("testt", "createFragment, position = $position")
        return CalendarWeekFragment.newInstance(position)
    }

    companion object {
        const val START_POSITION = Int.MAX_VALUE / 2
    }
}


