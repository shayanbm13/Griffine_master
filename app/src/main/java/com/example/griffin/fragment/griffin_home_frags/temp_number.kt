package com.example.griffin.fragment.griffin_home_frags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.griffin.R
import com.example.griffin.database.Temperature_db
import com.example.griffin.database.light_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.Light
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.Thermostst


class temp_number : Fragment() {

    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      
        
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temp_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val temp_number = view.findViewById<TextView>(R.id.thermo_number)
            val closeButton = view.findViewById<Button>(R.id.closeButton_thermo)
            val more_temp = view.findViewById<Button>(R.id.more_thermo)
            val less_temp = view.findViewById<Button>(R.id.less_thermo)

            val datbaseHelper= Temperature_db.getInstance(requireContext())

            temp_number.text= 0.toString()


            more_temp.setOnClickListener {
                val current_num = temp_number.text.toString().toInt()
                if (current_num < 3) {
                    temp_number.text = (current_num + 1).toString()
                }
            }

            less_temp.setOnClickListener {
                val current_num = temp_number.text.toString().toInt()
                if (current_num > 0) {
                    temp_number.text = (current_num - 1).toString()
                }
            }
            closeButton.setOnClickListener {
                val current_num = temp_number.text.toString().toInt()
                val activity =requireActivity() as griffin_home

//            SharedViewModel.update_number_of_lernLight(current_num)

                val datbaseHelper1=Temperature_db.getInstance(requireContext())
//                val all_count=datbaseHelper1.getAllThermostats().count()



                if (current_num>0){

                    for(num in (1) .. (current_num)){


                        val thermostst= Thermostst()

                        thermostst.room_name=room!!.room_name
                        thermostst.name="Thermostat ${datbaseHelper1.getAllThermostats().count()+1}"

                        thermostst.mac="13"
                        datbaseHelper1.set_to_db_Temprature(thermostst)
                    }
                    val current_items=datbaseHelper1.getThermostatsByRoomName(room!!.room_name)

                    SharedViewModel.update_temp_to_learn_list(current_items)
                    activity.viewPager.currentItem=5
                    temp_number.text="0"

                }

            }



        })


    }

}