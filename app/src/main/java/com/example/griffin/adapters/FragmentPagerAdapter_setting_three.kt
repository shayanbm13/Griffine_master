package com.example.griffin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.griffin.fragment.setting_setting.seting_setting
import com.example.griffin.fragment.setting_setting.setting_admin
import com.example.griffin.fragment.setting_three.*
import com.example.griffin.fragment.setting_two.empty

class FragmentPagerAdapter_setting_three(fm: FragmentManager) : FragmentPagerAdapter(fm)  {
    companion object {
        private const val NUM_PAGES = 11
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> empty()
            1 -> fragment_setting_modem_info()
            2 -> setting_local_moreoption()
            3 -> setting_security_moreoption()
            4 -> setting_learn__camera_moreoption()
            5 -> setting_learn_sensor_moreoption()
            6 -> setting_learn_door_moreoption()
            7 -> setting_simcard_security_moreoption()
            8 -> setting_homestyle_moreoption()
            9 -> setting_homestyle_moreoption2()
            10 -> setting_learn_ir_moreoption()

            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }
}