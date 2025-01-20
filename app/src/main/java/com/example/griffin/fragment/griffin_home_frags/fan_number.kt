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
import com.example.griffin.database.fan_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.fan
import com.example.griffin.mudels.SharedViewModel


class fan_number : Fragment() {
    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fan_number, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val fan_number = view.findViewById<TextView>(R.id.fan_number)
            val closeButton = view.findViewById<Button>(R.id.closeButton_fan)
            val more_fan = view.findViewById<Button>(R.id.more_fan)
            val less_fan = view.findViewById<Button>(R.id.less_fan)

            val datbaseHelper= fan_db.getInstance(requireContext())

            fan_number.text= 0.toString()


            more_fan.setOnClickListener {
                val current_num = fan_number.text.toString().toInt()
                if (current_num < 16) {
                    fan_number.text = (current_num + 1).toString()
                }
            }

            less_fan.setOnClickListener {
                val current_num = fan_number.text.toString().toInt()
                if (current_num > 0) {
                    fan_number.text = (current_num - 1).toString()
                }
            }
            closeButton.setOnClickListener {
                val fan_num = fan_number.text.toString().toInt()
                val activity =requireActivity() as griffin_home

//            SharedViewModel.update_number_of_lernLight(current_num)

                val datbaseHelper1= fan_db.getInstance(requireContext())
                val all_count= datbaseHelper1.getAllfans()
                val uniqueMacModels = all_count.mapNotNull { it?.mac }.toSet()

                // تعداد مدل‌های منحصر به فرد
                val numberOfUniqueMacModels = uniqueMacModels.count() +1

                if (fan_num>0){

                    for(num in (1) .. (fan_num)){


                        val fan= fan()

                        fan.room_name=room!!.room_name
                        fan.Fname="$numberOfUniqueMacModels : $fan_num pole : $num"

                        fan.mac="13"
                        datbaseHelper1.set_to_db_fan(fan)
                    }
                    val current_items=datbaseHelper1.getfansByRoomName(room!!.room_name)

                    SharedViewModel.update_fan_to_learn_list(current_items)
                    activity.viewPager.currentItem=17
                    fan_number.text="0"

                }

            }



        })


    }


}