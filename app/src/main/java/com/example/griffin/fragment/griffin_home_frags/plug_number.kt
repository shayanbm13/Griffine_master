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
import com.example.griffin.database.plug_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.Light
import com.example.griffin.mudels.Plug
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.Thermostst


class plug_number : Fragment() {
    private  val SharedViewModel: SharedViewModel by activityViewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plug_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val plug_number = view.findViewById<TextView>(R.id.plug_number)
            val closeButton = view.findViewById<Button>(R.id.closeButton_plug)
            val more_plug = view.findViewById<Button>(R.id.more_plug)
            val less_plug = view.findViewById<Button>(R.id.less_plug)

            val datbaseHelper= plug_db.getInstance(requireContext())

            plug_number.text= 0.toString()


            more_plug.setOnClickListener {
                val current_num = plug_number.text.toString().toInt()
                if (current_num < 16) {
                    plug_number.text = (current_num + 1).toString()
                }
            }

            less_plug.setOnClickListener {
                val current_num = plug_number.text.toString().toInt()
                if (current_num > 0) {
                    plug_number.text = (current_num - 1).toString()
                }
            }
            closeButton.setOnClickListener {
                val plug_num = plug_number.text.toString().toInt()
                val activity =requireActivity() as griffin_home

//            SharedViewModel.update_number_of_lernLight(current_num)

                val datbaseHelper1= plug_db.getInstance(requireContext())
                val all_count= datbaseHelper1.getAllPlugs()
                val uniqueMacModels = all_count.mapNotNull { it?.mac }.toSet()

                // تعداد مدل‌های منحصر به فرد
                val numberOfUniqueMacModels = uniqueMacModels.count() +1



                if (plug_num>0){

                    for(num in (1) .. (plug_num)){


                        val plug= Plug()

                        plug.room_name=room!!.room_name
                        plug.Pname="$numberOfUniqueMacModels : $plug_num pole : $num"

                        plug.mac="13"
                        datbaseHelper1.set_to_db_plug(plug)
                    }
                    val current_items=datbaseHelper1.getPlugsByRoomName(room!!.room_name)

                    SharedViewModel.update_plug_to_learn_list(current_items)
                    activity.viewPager.currentItem=11
                    plug_number.text="0"

                }

            }



        })


    }


}