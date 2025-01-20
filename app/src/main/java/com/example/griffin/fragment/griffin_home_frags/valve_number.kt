package com.example.griffin.fragment.griffin_home_frags


import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.adapters.LearnvalveAdadpter
import com.example.griffin.database.alarm_handeler_db
import com.example.griffin.database.valve_db

import com.example.griffin.database.setting_network_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.*
import com.example.griffin.myBroadcastReceiverr


class valve_number : Fragment() {
    private  val SharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_valve_number, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val valve_number = view.findViewById<TextView>(R.id.valve_number)
            val closeButton = view.findViewById<Button>(R.id.closeButton_valve)
            val more_valve = view.findViewById<Button>(R.id.more_valve)
            val less_valve = view.findViewById<Button>(R.id.less_valve)

            val datbaseHelper= valve_db.getInstance(requireContext())

            valve_number.text= 0.toString()


            more_valve.setOnClickListener {
                val current_num = valve_number.text.toString().toInt()
                if (current_num < 16) {
                    valve_number.text = (current_num + 1).toString()
                }
            }

            less_valve.setOnClickListener {
                val current_num = valve_number.text.toString().toInt()
                if (current_num > 0) {
                    valve_number.text = (current_num - 1).toString()
                }
            }
            closeButton.setOnClickListener {
                val valve_num = valve_number.text.toString().toInt()
                val activity =requireActivity() as griffin_home

//            SharedViewModel.update_number_of_lernLight(current_num)

                val datbaseHelper1= valve_db.getInstance(requireContext())
                val all_count= datbaseHelper1.getAllvalves()
                val uniqueMacModels = all_count.mapNotNull { it?.mac }.toSet()
                val numberOfUniqueMacModels = uniqueMacModels.count() +1


                if (valve_num>0){

                    for(num in (1) .. (valve_num)){


                        val valve= valve()

                        valve.room_name=room!!.room_name
                        valve.Vname="$numberOfUniqueMacModels : $valve_num pole : $num"

                        valve.mac="13"
                        datbaseHelper1.set_to_db_valve(valve)
                    }
                    val current_items=datbaseHelper1.getvalvesByRoomName(room!!.room_name)

                    SharedViewModel.update_valve_to_learn_list(current_items)
                    activity.viewPager.currentItem=14
                    valve_number.text="0"

                }

            }



        })


    }
}