package com.example.griffin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.griffin.fragment.griffin_home_frags.*
import com.example.griffin.fragment.music_player_frag
import com.example.griffin.fragment.setting_two.empty

class FragmentPagerAdapter_selected_devices (fm: FragmentManager) : FragmentStatePagerAdapter(fm){


    companion object {
        private const val NUM_PAGES = 26
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> empty()
            1 -> lights()
            2 -> lights_learn()
            3 -> lights_number()
            4 -> temp_number()
            5 -> temp_learn()
            6 -> temp()
            7 -> curtain_number()
            8 -> curtain_learn()
            9 -> curtain_frag()
            10 -> plug_number()
            11 -> plug_learn()
            12 -> plug_frag()
            13 -> valve_number()
            14 -> valve_learn()
            15 -> valve_frag()
            16 -> fan_number()
            17 -> fan_learn()
            18 -> fan_frag()
            19 -> fragment_light_addscenario()
            20 -> fragment_plug_addscenario()
            21 -> fragment_valve_addscenario()
            22 -> fragment_curtain_addscenario()
            23 -> fragment_thermostat_addscenario()
            24 -> fragment_fan_addscenario()
            25 -> music_player_frag()


//            2 -> select_work_sensor_curtain()

            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }
}