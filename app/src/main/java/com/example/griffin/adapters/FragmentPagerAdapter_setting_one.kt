package com.example.griffin.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.example.griffin.fragment.setting_setting.seting_setting
import com.example.griffin.fragment.setting_setting.setting_admin

class FragmentPagerAdapter_setting_one(fm: FragmentManager) : FragmentPagerAdapter(fm)  {
    companion object {
        private const val NUM_PAGES = 2 // تعداد صفحات مورد نظر خود را قرار دهید
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> setting_admin()
            1 -> seting_setting()

            else -> Fragment()
        }
    }

    override fun getCount(): Int {
        return NUM_PAGES
    }
}