package com.example.griffin.fragment.setting_two

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


class setting_simcard : Fragment() {

     
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MediaPlayer.create(context, R.raw.zapsplat_multimedia_button_click_bright_003_92100)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_simcard, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val simcard_security = view.findViewById<Button>(R.id.simcard_security)
        val simcard_message_response: Button =  view.findViewById(R.id.simcard_message_response)
        val simcard_account_security: Button =  view.findViewById(R.id.simcard_account_security)

//        viewPager = view.findViewById(R.id.setting_one)
//        pagerAdapter = MyPagerAdapter(supportFragmentManager)
//        viewPager.adapter = pagerAdapter
//
//
         
        val chooser_btns_list = listOf(simcard_security, simcard_message_response, simcard_account_security)
        fun selectButton(selectedButton: Button) {
            SoundManager.playSound()
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    SoundManager.playSound()
                    if (button==simcard_security){
                        button.setBackgroundResource(R.drawable.security_on)
                    }
                    if (button==simcard_message_response){
                        button.setBackgroundResource(R.drawable.message_response_on)
                    }
                    if (button==simcard_account_security){
                        button.setBackgroundResource(R.drawable.acc_security_on)
                    }


                } else {
//                    SoundManager.playSound()
                    button.isSelected = false
                    if (button==simcard_security){
                        button.setBackgroundResource(R.drawable.security_off)


                    }
                    if (button==simcard_message_response){
                        button.setBackgroundResource(R.drawable.message_response_off)
                    }
                    if (button==simcard_account_security){
                        button.setBackgroundResource(R.drawable.acc_security_off)
                    }

                }
            }
        }
//
//
//
//
        val setting_activity = requireActivity() as setting

        simcard_security.setOnClickListener {
            selectButton(simcard_security)
            setting_activity.changeViewPagerPage_four(7)
            setting_activity.changeViewPagerPage_three(7)


        }

        simcard_message_response.setOnClickListener {
            selectButton(simcard_message_response)
            setting_activity.changeViewPagerPage_four(8)
            setting_activity.changeViewPagerPage_three(7)

        }

        simcard_account_security.setOnClickListener {
            selectButton(simcard_account_security)
            setting_activity.changeViewPagerPage_four(9)
            setting_activity.changeViewPagerPage_three(7)

        }








    }

    override fun onResume() {
        super.onResume()
        val simcard_security = requireView().findViewById<Button>(R.id.simcard_security)
        val simcard_message_response: Button =  requireView().findViewById(R.id.simcard_message_response)
        val simcard_account_security: Button =  requireView().findViewById(R.id.simcard_account_security)
        simcard_security.setBackgroundResource(R.drawable.security_off)
        simcard_message_response.setBackgroundResource(R.drawable.message_response_off)
        simcard_account_security.setBackgroundResource(R.drawable.acc_security_off)
    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }



}