package com.example.griffin.fragment.setting_four

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.griffin.R
import com.example.griffin.adapters.FragmentPagerAdapter_select_work
import com.example.griffin.adapters.FragmentPagerAdapter_sensor_selectwork
import com.example.griffin.mudels.NonSwipeableViewPager
import com.example.griffin.mudels.PagerChangeListener
import com.example.griffin.mudels.SoundManager

class setting_learn_sensor : Fragment(), PagerChangeListener {
     
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewPager: NonSwipeableViewPager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MediaPlayer.create(context, R.raw.zapsplat_multimedia_button_click_bright_003_92100)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_select_work_sensor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sw_sensor_light = view.findViewById<Button>(R.id.sw_sensor_light)
        val sw_sensor_plug: Button = view.findViewById(R.id.sw_sensor_plug)
        val sw_sensor_tempreture: Button = view.findViewById(R.id.sw_sensor_tempreture)
        val sw_sensor_curtain: Button = view.findViewById(R.id.sw_sensor_curtain)
        val sw_sensor_valve: Button = view.findViewById(R.id.sw_sensor_valve)


         
        val chooser_btns_list = listOf(

            sw_sensor_light,
            sw_sensor_plug,
            sw_sensor_tempreture,sw_sensor_curtain,sw_sensor_valve

            )

        fun selectButton(selectedButton: Button) {
            SoundManager.playSound()
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    SoundManager.playSound()

                    if (button==sw_sensor_light){
                        button.setBackgroundResource(R.drawable.light_sensor_on)
                    }
                    if (button==sw_sensor_plug){
                        button.setBackgroundResource(R.drawable.plug_sensor_on)
                    }
                    if (button==sw_sensor_tempreture){
                        button.setBackgroundResource(R.drawable.temperature_sensor_on)
                    }
                    if (button==sw_sensor_curtain){
                        button.setBackgroundResource(R.drawable.curtain_sensor_on)
                    }
                    if (button==sw_sensor_valve){
                        button.setBackgroundResource(R.drawable.valve_sensor_on)
                    }


                } else {
//                    SoundManager.playSound()
                    button.isSelected = false

                    if (button==sw_sensor_light){
                        button.setBackgroundResource(R.drawable.light_sensor_off)
                    }
                    if (button==sw_sensor_plug){
                        button.setBackgroundResource(R.drawable.plug_sensor_off)
                    }
                    if (button==sw_sensor_tempreture){
                        button.setBackgroundResource(R.drawable.temperature_sensor__off)
                    }
                    if (button==sw_sensor_curtain){
                        button.setBackgroundResource(R.drawable.curtain_sensor_off)
                    }
                    if (button==sw_sensor_valve){
                        button.setBackgroundResource(R.drawable.valve_sensor_off)
                    }


                }
            }
        }

        viewPager = view.findViewById(R.id.select_work_sensor_viewpager)
        val pagerAdapter: FragmentPagerAdapter_sensor_selectwork = FragmentPagerAdapter_sensor_selectwork(childFragmentManager)
        viewPager.adapter = pagerAdapter

        sw_sensor_light.setOnClickListener {
            selectButton(sw_sensor_light)
            viewPager.currentItem=1



        }

        sw_sensor_plug.setOnClickListener {
            selectButton(sw_sensor_plug)
            viewPager.currentItem=1
        }

        sw_sensor_tempreture.setOnClickListener {
            selectButton(sw_sensor_tempreture)
            viewPager.currentItem=1
        }
        sw_sensor_curtain.setOnClickListener {
            selectButton(sw_sensor_curtain)
            viewPager.currentItem=2
        }
        sw_sensor_valve.setOnClickListener {
            selectButton(sw_sensor_valve)
            viewPager.currentItem=1
        }


    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }


    override fun onPagerItemChanged(position: Int) {
        viewPager.currentItem = position    }


}