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
import com.example.griffin.database.light_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.Light
import com.example.griffin.mudels.SharedViewModel


class lights_number : Fragment() {
    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lights_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val light_number = view.findViewById<TextView>(R.id.light_number)
            val closeButton = view.findViewById<Button>(R.id.closeButton)
            val more_light = view.findViewById<Button>(R.id.more_light)
            val less_light = view.findViewById<Button>(R.id.less_light)

            val datbaseHelper=light_db.getInstance(requireContext())

            light_number.text= 0.toString()


            more_light.setOnClickListener {
                val current_num = light_number.text.toString().toInt()
                if (current_num < 18) {
                    light_number.text = (current_num + 1).toString()
                }
            }

            less_light.setOnClickListener {
                val current_num = light_number.text.toString().toInt()
                if (current_num > 0) {
                    light_number.text = (current_num - 1).toString()
                }
            }
            closeButton.setOnClickListener {
                val current_num = light_number.text.toString().toInt()
                val activity =requireActivity() as griffin_home

//            SharedViewModel.update_number_of_lernLight(current_num)


                val datbaseHelper=light_db.getInstance(requireContext())
                var current_items=datbaseHelper.getAllLights()
                var current_items_number=datbaseHelper.getAllLights()
                val uniqueMacModels = current_items_number.mapNotNull { it?.mac }.toSet()

                // تعداد مدل‌های منحصر به فرد
                val numberOfUniqueMacModels = uniqueMacModels.count() +1


                if (current_num>0){

                    for(num in (1) .. (current_num)){


                        var light=Light()

                        light.room_name=room!!.room_name
                            light.Lname="$numberOfUniqueMacModels: $current_num pole: $num"
                            light.mac="13"
                        datbaseHelper.set_to_db_light(light)


                    }
                    val current_items=datbaseHelper.getAllLightsByRoomName(room!!.room_name)
                    SharedViewModel.update_light_to_learn_list(current_items)
                    activity.viewPager.currentItem=2
                    light_number.text=datbaseHelper.getAllLightsByRoomName(room!!.room_name).count().toString()

                }

            }



        })
    }

}
