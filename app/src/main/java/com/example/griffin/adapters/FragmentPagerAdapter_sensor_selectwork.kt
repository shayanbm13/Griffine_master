package com.example.griffin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.griffin.fragment.setting_four.choose_lolcal_work.select_work_curtain
import com.example.griffin.fragment.setting_four.choose_lolcal_work.select_work_light
import com.example.griffin.fragment.setting_four.choose_lolcal_work.select_work_plug
import com.example.griffin.fragment.setting_four.choose_lolcal_work.select_work_scenario
import com.example.griffin.fragment.setting_four.choose_sensor_work.select_work_sensor_curtain
import com.example.griffin.fragment.setting_four.choose_sensor_work.select_work_sensor_lptv
import com.example.griffin.fragment.setting_four.select_work_local
import com.example.griffin.fragment.setting_two.empty

class FragmentPagerAdapter_sensor_selectwork (fm: FragmentManager) : FragmentPagerAdapter(fm){


    companion object {
        private const val NUM_PAGES = 3
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> empty()
            1 -> select_work_sensor_lptv()
            2 -> select_work_sensor_curtain()

            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }
}