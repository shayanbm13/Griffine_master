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


class setting_synchronization : Fragment() {
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
        return inflater.inflate(R.layout.fragment_synchronization, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val Synchronization_send = view.findViewById<Button>(R.id.Synchronization_send)
        val Synchronization_recive: Button =  view.findViewById(R.id.Synchronization_recive)




         
        val chooser_btns_list = listOf(Synchronization_send, Synchronization_recive)
        fun selectButton(selectedButton: Button) {
            SoundManager.playSound()
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    SoundManager.playSound()
                    if (button==Synchronization_send){
                        button.setBackgroundResource(R.drawable.send_on)
                    }
                    if (button==Synchronization_recive){
                        button.setBackgroundResource(R.drawable.recive_on)
                    }



                } else {
//                    SoundManager.playSound()
                    button.isSelected = false
                    if (button==Synchronization_send){
                        button.setBackgroundResource(R.drawable.send_off)
                    }
                    if (button==Synchronization_recive){
                        button.setBackgroundResource(R.drawable.recive_off)
                    }


                }
            }
        }
//
//
//
//
        val setting_activity = requireActivity() as setting

        Synchronization_send.setOnClickListener {
            selectButton(Synchronization_send)
            setting_activity.changeViewPagerPage_four(10)
            setting_activity.changeViewPagerPage_three(1)


        }

        Synchronization_recive.setOnClickListener {
            selectButton(Synchronization_recive)
            setting_activity.changeViewPagerPage_four(11)
            setting_activity.changeViewPagerPage_three(1)
        }









    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }
    override fun onPause() {
        super.onPause()
         
    }




}