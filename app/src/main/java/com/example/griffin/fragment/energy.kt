package com.example.griffin.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.griffin.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
class energy : Fragment() {
    // کد‌ها و لازمه‌های مربوط به فرگمنت اول

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_energy, container, false) // یا layout مورد نظر خود را قرار دهید
        // مراحل مربوط به نمایش و کنترل‌های فرگمنت اول

        return view
    }
}