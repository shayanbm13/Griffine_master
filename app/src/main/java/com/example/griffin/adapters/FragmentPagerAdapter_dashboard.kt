package com.example.griffin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.griffin.fragment.*

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    companion object {
        private const val NUM_PAGES = 5 // تعداد صفحات مورد نظر خود را قرار دهید
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> scenario()
            1 -> favorite()
            2 -> security()
            3 -> energy()
            4 -> music_player_frag()
            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }
}
