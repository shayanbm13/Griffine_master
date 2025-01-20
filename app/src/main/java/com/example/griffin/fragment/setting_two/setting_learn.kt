package com.example.griffin.fragment.setting_two

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.griffin.R
import com.example.griffin.mudels.SoundManager
import com.example.griffin.setting

class setting_learn : Fragment() {
     
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MediaPlayer.create(context, R.raw.zapsplat_multimedia_button_click_bright_003_92100)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_learn, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val learn_local = view.findViewById<Button>(R.id.learn_local)
        val learn_security: Button =  view.findViewById(R.id.learn_security)
        val learn_sensor: Button =  view.findViewById(R.id.learn_sensor)
        val learn_camera: Button =  view.findViewById(R.id.learn_camera)
        val learn_door: Button =  view.findViewById(R.id.learn_door)
        val learn_ir: Button =  view.findViewById(R.id.IR)
        learn_local.setBackgroundResource(R.drawable.local_off)
        learn_door.setBackgroundResource(R.drawable.dor_off)
        learn_ir.setBackgroundResource(R.drawable.ir_center_off)
        learn_camera.setBackgroundResource(R.drawable.camera_off)
        learn_sensor.setBackgroundResource(R.drawable.sensor_off)
        learn_security.setBackgroundResource(R.drawable.security_off)



//        viewPager = view.findViewById(R.id.setting_one)
//        pagerAdapter = MyPagerAdapter(supportFragmentManager)
//        viewPager.adapter = pagerAdapter
//
//
         
        val chooser_btns_list = listOf(learn_local, learn_security, learn_sensor,learn_camera,learn_door,learn_ir)
        fun selectButton(selectedButton: Button) {
            SoundManager.playSound()
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    SoundManager.playSound()
                    if (button==learn_local){
                        button.setBackgroundResource(R.drawable.local_on)
                    }
                    if (button==learn_security){
                        button.setBackgroundResource(R.drawable.security_on)
                    }
                    if (button==learn_sensor){
                        button.setBackgroundResource(R.drawable.sensor_on)
                    }
                    if (button==learn_camera){
                        button.setBackgroundResource(R.drawable.camera_on)
                    }
                    if (button==learn_door){
                        button.setBackgroundResource(R.drawable.dor_on)
                    }
                    if (button==learn_ir){
                        button.setBackgroundResource(R.drawable.ir_center_on)
                    }


                } else {
//                    SoundManager.playSound()
                    button.isSelected = false
                    if (button==learn_local){
                        button.setBackgroundResource(R.drawable.local_off)

                    }
                    if (button==learn_security){
                        button.setBackgroundResource(R.drawable.security_off)


                    }
                    if (button==learn_sensor){
                        button.setBackgroundResource(R.drawable.sensor_off)
                    }
                    if (button==learn_camera){
                        button.setBackgroundResource(R.drawable.camera_off)
                    }
                    if (button==learn_door){
                        button.setBackgroundResource(R.drawable.dor_off)
                    }
                    if (button==learn_ir){
                        button.setBackgroundResource(R.drawable.ir_center_off)
                    }
                }
            }
        }

        val settingactivity= requireActivity() as setting
        learn_local.setOnClickListener {
            selectButton(learn_local)
            settingactivity.changeViewPagerPage_three(2)
            settingactivity.changeViewPagerPage_four(2)
        }

        learn_security.setOnClickListener {
            selectButton(learn_security)
            settingactivity.changeViewPagerPage_four(3)
            settingactivity.changeViewPagerPage_three(3)
        }

        learn_sensor.setOnClickListener {
            selectButton(learn_sensor)
            settingactivity.changeViewPagerPage_four(5)
            settingactivity.changeViewPagerPage_three(5)
        }

        learn_camera.setOnClickListener {
            selectButton(learn_camera)
            settingactivity.changeViewPagerPage_four(4)
            settingactivity.changeViewPagerPage_three(4)
        }

        learn_door.setOnClickListener {
            selectButton(learn_door)
            settingactivity.changeViewPagerPage_four(6)
            settingactivity.changeViewPagerPage_three(6)
        }
        learn_ir.setOnClickListener {
            selectButton(learn_ir)
            settingactivity.changeViewPagerPage_four(13)
            settingactivity.changeViewPagerPage_three(10)
        }


    }

    override fun onResume() {
        super.onResume()

        val learn_local:Button = requireView().findViewById(R.id.learn_local)
        val learn_security: Button =  requireView().findViewById(R.id.learn_security)
        val learn_sensor: Button =  requireView().findViewById(R.id.learn_sensor)
        val learn_camera: Button =  requireView().findViewById(R.id.learn_camera)
        val learn_door: Button =  requireView().findViewById(R.id.learn_door)
        learn_local.setBackgroundResource(R.drawable.local_off)
        learn_door.setBackgroundResource(R.drawable.dor_off)
        learn_camera.setBackgroundResource(R.drawable.camera_off)
        learn_sensor.setBackgroundResource(R.drawable.sensor_off)
        learn_security.setBackgroundResource(R.drawable.security_off)


    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }


}