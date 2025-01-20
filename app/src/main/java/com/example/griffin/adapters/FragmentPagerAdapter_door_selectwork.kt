package com.example.griffin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.griffin.fragment.setting_four.choose_door_work.select_work_coding
import com.example.griffin.fragment.setting_four.choose_door_work.select_work_intercom
import com.example.griffin.fragment.setting_four.choose_door_work.select_work_opener
import com.example.griffin.fragment.setting_four.choose_sensor_work.select_work_sensor_curtain
import com.example.griffin.fragment.setting_four.choose_sensor_work.select_work_sensor_lptv
import com.example.griffin.fragment.setting_two.empty

class FragmentPagerAdapter_door_selectwork (fm: FragmentManager) : FragmentPagerAdapter(fm){


    companion object {
        private const val NUM_PAGES = 4
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> empty()
            1 -> select_work_intercom()
            2 -> select_work_coding()
            3 -> select_work_opener()

            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }
}