package com.example.griffin.fragment.setting_four

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.griffin.R
import com.example.griffin.database.setting_network_db
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.SoundManager
import com.example.griffin.mudels.network_manual

class setting_network_manual : Fragment() {

     
    private val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MediaPlayer.create(context, R.raw.zapsplat_multimedia_button_click_bright_003_92100)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_network_manual, container, false)



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val setting_manual_modem_ssid = view.findViewById<EditText>(R.id.setting_manual_modem_ssid)
        val setting_manual_modem_password = view.findViewById<EditText>(R.id.setting_manual_modem_password)
        val setting_manual_api_key = view.findViewById<EditText>(R.id.setting_manual_api_key)
        val setting_manual_cityname = view.findViewById<EditText>(R.id.setting_manual_cityname)
        val cancel_btn_manual = view.findViewById<Button>(R.id.cancel_btn_manual)
        val ok_btn_manual = view.findViewById<Button>(R.id.ok_btn_manual)



         
        val chooser_btns_list = listOf(cancel_btn_manual, ok_btn_manual)
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

        val databaseHelper = setting_network_db.getInstance(requireContext())



        if (!databaseHelper.isEmptynetwork_tabale()){
            var current_db: network_manual? = databaseHelper.get_from_db_network_manual(1)
            setting_manual_modem_ssid.setText(current_db!!.modem_ssid.toString())
            setting_manual_modem_password.setText(current_db.modem_password.toString())
            setting_manual_api_key.setText(current_db.api_key.toString())
            setting_manual_cityname.setText(current_db.city_name.toString())


        }








        SharedViewModel.shared_ssid.observe(viewLifecycleOwner, { text ->

            setting_manual_modem_ssid.setText(text.trim('"'))
        })




        cancel_btn_manual!!.setOnClickListener{

            if (!databaseHelper.isEmptynetwork_tabale()){
                var current_db: network_manual? = databaseHelper.get_from_db_network_manual(1)
                setting_manual_modem_ssid.setText(current_db!!.modem_ssid.toString())
                setting_manual_modem_password.setText(current_db.modem_password.toString())
                setting_manual_api_key.setText(current_db.api_key.toString())
                setting_manual_cityname.setText(current_db.city_name.toString())


            }else{
                setting_manual_modem_ssid.setText(null)
                setting_manual_modem_password.setText(null)
                setting_manual_api_key.setText(null)
                setting_manual_cityname.setText(null)

            }


            selectButton(cancel_btn_manual)

        }
        ok_btn_manual!!.setOnClickListener{
            selectButton(ok_btn_manual)
//            var cheker = false
//            try {
//                databaseHelper.get_from_db_network_manual(1)
//                cheker= true
//            }catch (e:Exception){
//                cheker= false
//
//            }
//            println(cheker)
            val databaseHelper = setting_network_db.getInstance(requireContext())
            if (databaseHelper.isEmptynetwork_tabale()) {
                var network_manual = network_manual()
                network_manual.modem_ssid = setting_manual_modem_ssid.text.toString()
                network_manual.modem_password = setting_manual_modem_password.text.toString()
                network_manual.api_key = setting_manual_api_key.text.toString()
                network_manual.city_name = setting_manual_cityname.text.toString()
                databaseHelper.set_to_db_network_manual(network_manual)
                Toast.makeText(requireContext(), "seted...", Toast.LENGTH_SHORT).show()
//                if (setting_manual_modem_ssid.text.toString().isNotEmpty() &&
//                    setting_manual_modem_password.text.toString().isNotEmpty() &&
//                    setting_manual_api_key.text.toString().isNotEmpty() &&
//                    setting_manual_cityname.text.toString().isNotEmpty()
//                ) {
//
//                }
            } else {
                var network_manual = network_manual()
                network_manual.modem_ssid = setting_manual_modem_ssid.text.toString()
                network_manual.modem_password = setting_manual_modem_password.text.toString()
                network_manual.api_key = setting_manual_api_key.text.toString()
                network_manual.city_name = setting_manual_cityname.text.toString()
                network_manual.id=1
                databaseHelper.update_db_network_manual(network_manual)
                Toast.makeText(requireContext(), "changes seted...", Toast.LENGTH_SHORT).show()
            }




        }
    }
    override fun onDestroy() {
        super.onDestroy()
         
    }
    override fun onPause() {
        super.onPause()
         
    }


}