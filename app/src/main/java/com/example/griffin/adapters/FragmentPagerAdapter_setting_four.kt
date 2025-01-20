package com.example.griffin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.griffin.fragment.setting_four.*
import com.example.griffin.fragment.setting_setting.seting_setting
import com.example.griffin.fragment.setting_setting.setting_admin
import com.example.griffin.fragment.setting_two.empty

class FragmentPagerAdapter_setting_four (fm: FragmentManager) : FragmentPagerAdapter(fm)  {
    companion object {
        private const val NUM_PAGES = 14
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> empty()
            1 -> setting_network_manual()
            2 -> setting_learn_local()
            3-> setting_learn_security()
            4-> setting_learn_camera()
            5-> setting_learn_sensor()
            6-> setting_learn_door()
            7-> setting_simcard_security()
            8-> setting_simcaed_messgeresponse()
            9-> setting_simcard_accountsecurity()
            10-> setting_sync_send()
            11-> setting_sync_receive()
            12-> styles_page()
            13-> setting_learn_ir()

            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }
}