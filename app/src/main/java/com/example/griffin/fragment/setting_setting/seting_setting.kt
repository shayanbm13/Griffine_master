package com.example.griffin.fragment.setting_setting

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.viewpager.widget.ViewPager
import com.example.griffin.R
import com.example.griffin.adapters.MyPagerAdapter
import com.example.griffin.mudels.SoundManager
import com.example.griffin.setting


class seting_setting : Fragment() {
     
    // TODO: Rename and change types of parameters
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MediaPlayer.create(context, R.raw.zapsplat_multimedia_button_click_bright_003_92100)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seting_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        lateinit var viewPager: ViewPager
//        lateinit var pagerAdapter: MyPagerAdapter
//
        val setting_network = view.findViewById<Button>(R.id.setting_network)
        val setting_learn: Button =  view.findViewById(R.id.setting_learn)
        val setting_simcard: Button =  view.findViewById(R.id.setting_simcard)
        val setting_homestyle: Button =  view.findViewById(R.id.setting_homestyle)
        val setting_synchornalization: Button =  view.findViewById(R.id.setting_synchornalization)
//        viewPager = view.findViewById(R.id.setting_one)
//        pagerAdapter = MyPagerAdapter(supportFragmentManager)
//        viewPager.adapter = pagerAdapter
//
//
         
        val chooser_btns_list = listOf(setting_network, setting_learn, setting_simcard, setting_homestyle,setting_synchornalization)
        fun selectButton(selectedButton: Button) {
            SoundManager.playSound()
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    SoundManager.playSound()
                    if (button==setting_network){
                        button.setBackgroundResource(R.drawable.network_on)
                    }
                    if (button==setting_learn){
                        button.setBackgroundResource(R.drawable.learn_on)
                    }
                    if (button==setting_simcard){
                        button.setBackgroundResource(R.drawable.simcard_on)
                    }
                    if (button==setting_homestyle){
                        button.setBackgroundResource(R.drawable.homestyle_on)
                    }
                    if (button==setting_synchornalization){
                        button.setBackgroundResource(R.drawable.synchonization_on)
                    }

                } else {
//                    SoundManager.playSound()
                    button.isSelected = false
                    if (button==setting_network){
                        button.setBackgroundResource(R.drawable.network_off)
                    }
                    if (button==setting_learn){
                        button.setBackgroundResource(R.drawable.learn_off)
                    }
                    if (button==setting_simcard){
                        button.setBackgroundResource(R.drawable.simcard_off)
                    }
                    if (button==setting_homestyle){
                        button.setBackgroundResource(R.drawable.homestyle_off)
                    }
                    if (button==setting_synchornalization){
                        button.setBackgroundResource(R.drawable.synchonization_off)
                    }

                }
            }
        }
//
//
//
        val setting_activity = requireActivity() as setting
//
        setting_network.setOnClickListener {
            selectButton(setting_network)

            setting_activity.changeViewPagerPage_two(1)
            setting_activity.changeViewPagerPage_three(1)
            setting_activity.changeViewPagerPage_four(1)

        }

        setting_learn.setOnClickListener {
            selectButton(setting_learn)
            setting_activity.changeViewPagerPage_two(2)
            setting_activity.changeViewPagerPage_three(0)
            setting_activity.changeViewPagerPage_four(0)

        }

        setting_simcard.setOnClickListener {
            selectButton(setting_simcard)
            setting_activity.changeViewPagerPage_two(3)
            setting_activity.changeViewPagerPage_three(0)
            setting_activity.changeViewPagerPage_four(0)
        }

        setting_homestyle.setOnClickListener {
            selectButton(setting_homestyle)
            setting_activity.changeViewPagerPage_two(4)
            setting_activity.changeViewPagerPage_three(0)
            setting_activity.changeViewPagerPage_four(0)
        }
        setting_synchornalization.setOnClickListener {
            selectButton(setting_synchornalization)
            setting_activity.changeViewPagerPage_two(5)
            setting_activity.changeViewPagerPage_three(0)
            setting_activity.changeViewPagerPage_four(0)

        }
//
    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }



}