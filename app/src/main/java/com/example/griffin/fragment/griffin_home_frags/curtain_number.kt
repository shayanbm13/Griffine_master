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
import com.example.griffin.database.curtain_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.curtain
import com.example.griffin.mudels.SharedViewModel


class curtain_number : Fragment() {
    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curtain_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val curtain_number = view.findViewById<TextView>(R.id.curtain_number)
            val closeButton = view.findViewById<Button>(R.id.closeButton_curtain)
            val more_curtain = view.findViewById<Button>(R.id.more_curtain)
            val less_curtain = view.findViewById<Button>(R.id.less_curtain)

            val datbaseHelper= curtain_db.getInstance(requireContext())

            curtain_number.text= 0.toString()


            more_curtain.setOnClickListener {
                val current_num = curtain_number.text.toString().toInt()
                if (current_num < 18) {
                    curtain_number.text = (current_num + 1).toString()
                }
            }

            less_curtain.setOnClickListener {
                val current_num = curtain_number.text.toString().toInt()
                if (current_num > 0) {
                    curtain_number.text = (current_num - 1).toString()
                }
            }
            closeButton.setOnClickListener {
                val current_num = curtain_number.text.toString().toInt()
                val activity =requireActivity() as griffin_home

//            SharedViewModel.update_number_of_lerncurtain(current_num)


                val datbaseHelper= curtain_db.getInstance(requireContext())
                var current_items=datbaseHelper.getAllcurtains()
                var current_items_number=datbaseHelper.getAllcurtains()
                val uniqueMacModels = current_items_number.mapNotNull { it?.mac }.toSet()
                val numberOfUniqueMacModels = uniqueMacModels.count() +1
                if (current_num>0){

                    for(num in (1) .. (current_num)){


                        var curtain= curtain()

                        curtain.room_name=room!!.room_name
                        curtain.Cname="$numberOfUniqueMacModels : ${datbaseHelper.getAllcurtainsByRoomName(room.room_name).count()+1}"
                        curtain.mac="13"
                        datbaseHelper.set_to_db_curtain(curtain)


                    }
                    val current_items=datbaseHelper.getAllcurtainsByRoomName(room!!.room_name)
                    SharedViewModel.update_curtain_to_learn_list(current_items)
                    activity.viewPager.currentItem=8
                    curtain_number.text="0"

                }

            }



        })


    }


}