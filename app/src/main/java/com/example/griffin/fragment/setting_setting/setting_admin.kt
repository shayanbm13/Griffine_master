package com.example.griffin.fragment.setting_setting

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.griffin.R
import com.example.griffin.dashboard
import com.example.griffin.mudels.SoundManager
import com.example.griffin.setting


class setting_admin : Fragment() {

     

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MediaPlayer.create(context, R.raw.zapsplat_multimedia_button_click_bright_003_92100)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_admin_inter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val setting_password= view?.findViewById<EditText>(R.id.setting_password)
        val cancel_btn= view?.findViewById<Button>(R.id.cancel_btn)
        val ok_btn= view?.findViewById<Button>(R.id.ok_btn)



         
        val chooser_btns_list = listOf(cancel_btn, ok_btn)
        fun selectButton(selectedButton: Button) {
            SoundManager.playSound()
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    SoundManager.playSound()



                } else {
//                    SoundManager.playSound()
                    button!!.isSelected = false



                }
            }
        }
//
//
//
//
        cancel_btn!!.setOnClickListener {
            selectButton(cancel_btn)


        }


        ok_btn!!.setOnClickListener {
            selectButton(ok_btn)
            setting_password?.clearFocus()
            if (setting_password!!.text.toString()=="admin"){
                val setting_activity = requireActivity() as setting
                setting_activity.changeViewPagerPage_one(2)

            }
            else{
                Toast.makeText(context,"incorrect password",Toast.LENGTH_SHORT).show()

            }



        }






    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }








}