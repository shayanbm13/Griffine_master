package com.example.griffin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.griffin.fragment.setting_two.*

class FragmentPagerAdapter_setting_two (fm: FragmentManager) : FragmentPagerAdapter(fm)  {
    companion object {
        private const val NUM_PAGES = 6
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> empty()
            1 -> setting_network()
            2 -> setting_learn()
            3 -> setting_simcard()
            4 -> setting_homestyle()
            5 -> setting_synchronization()

            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }
}