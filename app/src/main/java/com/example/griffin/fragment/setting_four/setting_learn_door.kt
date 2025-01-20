package com.example.griffin.fragment.setting_four

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.griffin.R
import com.example.griffin.adapters.FragmentPagerAdapter_door_selectwork
import com.example.griffin.adapters.FragmentPagerAdapter_select_work
import com.example.griffin.mudels.NonSwipeableViewPager
import com.example.griffin.mudels.SoundManager


class setting_learn_door : Fragment() {
    private lateinit var viewPager: NonSwipeableViewPager
     


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        MediaPlayer.create(context, R.raw.zapsplat_multimedia_button_click_bright_003_92100)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_learn_door, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intercom = view.findViewById<Button>(R.id.intercom)
        val coding_select: Button = view.findViewById(R.id.coding_select)
        val opener_select: Button = view.findViewById(R.id.opener_select)
        val coding_layout: ConstraintLayout = view.findViewById(R.id.coding_layout)
        val opener_layout: ConstraintLayout = view.findViewById(R.id.opener_layout)



//        viewPager = view.findViewById(R.id.setting_one)
//        pagerAdapter = MyPagerAdapter(supportFragmentManager)
//        viewPager.adapter = pagerAdapter
//
//
         
        val chooser_btns_list = listOf(
            intercom,
            coding_select,
            opener_select
        )

        fun selectButton(selectedButton: Button) {
            SoundManager.playSound()
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    SoundManager.playSound()
                    if (button == intercom) {
                        button.setBackgroundResource(R.drawable.select_back_on)
                    }
                    if (button == coding_select) {
                        coding_layout.setBackgroundResource(R.drawable.select_back_on)
                    }
                    if (button == opener_select) {
                        opener_layout.setBackgroundResource(R.drawable.select_back_on)
                    }



                } else {
//                    SoundManager.playSound()
                    button.isSelected = false
                    if (button == intercom) {
                        button.setBackgroundResource(R.drawable.select_back)
                    }
                    if (button == coding_select) {
                        coding_layout.setBackgroundResource(R.drawable.select_back)
                    }
                    if (button == opener_select) {
                        opener_layout.setBackgroundResource(R.drawable.select_back)
                    }


                }
            }
        }



        viewPager = view.findViewById(R.id.select_work_door_viewpager)
        val pagerAdapter: FragmentPagerAdapter_door_selectwork = FragmentPagerAdapter_door_selectwork(childFragmentManager)
        viewPager.adapter = pagerAdapter



        intercom.setOnClickListener {
            selectButton(intercom)
            viewPager.currentItem=1


        }

        coding_select.setOnClickListener {
            selectButton(coding_select)
            viewPager.currentItem=2
        }

        opener_select.setOnClickListener {
            selectButton(opener_select)
            viewPager.currentItem=3
        }
    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }


}