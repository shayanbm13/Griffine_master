package com.example.griffin.fragment.setting_four

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.adapters.stylesAdapter
import com.example.griffin.database.rooms_db
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.rooms
import com.example.griffin.setting

class styles_page : Fragment(), stylesAdapter.OnItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: stylesAdapter
    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_styles_page, container, false)



        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val next_style=view.findViewById<ImageButton>(R.id.next_style)
        val privios_style=view.findViewById<ImageButton>(R.id.privios_style)
        val style_textview=view.findViewById<TextView>(R.id.style_textview)
//        val ok_btn_style=view.findViewById<Button>(R.id.ok_btn_style)

        val image_style1= listOf(R.drawable.s1_bathroom1_1,R.drawable.s1_bathroom2_1,R.drawable.s1_bathroom3_1,R.drawable.s1_dining_room_1,R.drawable.s1_gust_room_1,R.drawable.s1_kids_room_1,R.drawable.s1_kitchen_1,R.drawable.s1_kitchen2_1,R.drawable.s1_living_room_1,R.drawable.s1_master_room_1,R.drawable.s1_room_1,R.drawable.s1_tv_room_1,R.drawable.s1_yard_1,)
        val image_style2= listOf(R.drawable.s2_bathroom1_1,R.drawable.s2_bathroom2_1,R.drawable.s2_bathroom3_1,R.drawable.s2_dining_room_1,R.drawable.s2_gust_room_1,R.drawable.s2_kids_room_1,R.drawable.s2_kitchen_1,R.drawable.s2_kitchen2_1,R.drawable.s2_living_room_1,R.drawable.s2_master_room_1,R.drawable.s2_room_1,R.drawable.s2_tv_room_1,R.drawable.s2_yard_1,)
        val image_style3= listOf(R.drawable.s3_bathroom1_1,R.drawable.s3_bathroom2_1,R.drawable.s3_bathroom3_1,R.drawable.s3_dining_room_1,R.drawable.s3_gust_room_1,R.drawable.s3_kids_room_1,R.drawable.s3_kitchen_1,R.drawable.s3_kitchen2_1,R.drawable.s3_living_room_1,R.drawable.s3_master_room_1,R.drawable.s3_room_1,R.drawable.s3_tv_room_1,R.drawable.s3_yard_1,)
        val image_officestyle= listOf(R.drawable.of_confrance_room_1,R.drawable.of_kitchen_room_1,R.drawable.of_managment_room_1,R.drawable.of_managment2_room_1,R.drawable.of_office_room_1,R.drawable.of_office_room2_1,R.drawable.of_wc_1)


        recyclerView = view.findViewById(R.id.style_recyclerview)




        fun change_style(image_style:List<Int>?){


            recyclerView.layoutManager = GridLayoutManager(activity, 2)
            imageAdapter = image_style?.let { stylesAdapter(it,this) }!!
            recyclerView.adapter = imageAdapter
        }

        if (style_textview.text=="Style 1"){

            change_style(image_style1)

        }

        next_style.setOnClickListener {
            if (style_textview.text=="Style 1"){
                style_textview.text="Style 2"
                change_style(image_style2)
            }
            else if (style_textview.text=="Style 2"){
                style_textview.text="Style 3"
                change_style(image_style3)
            }else if (style_textview.text=="Style 3"){
                style_textview.text="Office Style"
                change_style(image_officestyle)
            }


        }
        privios_style.setOnClickListener {
            if (style_textview.text=="Office Style"){
                style_textview.text="Style 3"
                change_style(image_style3)

            }
            else if (style_textview.text=="Style 3"){
                style_textview.text="Style 2"
                change_style(image_style2)

            }
            else if (style_textview.text=="Style 2"){
                style_textview.text="Style 1"
                change_style(image_style1)

            }


        }
        val databaseHelper=rooms_db.getInstance(requireContext())

//
//        ok_btn_style.setOnClickListener {
//
//            val rooms=rooms()
//            SharedViewModel.shared_style_image_rec.observe(viewLifecycleOwner, Observer { value ->
//
//                rooms.room_type=when(value.toString().trim('"').substring(3,7)){
//                    "bath" -> "bathroom"
//                    "dini" -> "diningroom"
//                    "gust" -> "gustroom"
//                    "kids" -> "kidsroom"
//                    "kitc" -> "kitchen"
//                    "livi" -> "livingroom"
//                    "mast" -> "masterroom"
//                    "tv_r" -> "tvroom"
//                    "yard" -> "yard"
//                    "room" -> "room"
//                    else -> {"no sync"}
//                }
//                })
//           SharedViewModel.custom_room_name.observe(viewLifecycleOwner, Observer { value ->
//               rooms.room_name = value.toString().trim('"')
//
//
//           })
//
//            if(rooms.room_name?.isNotBlank() == true){
//
//                if (databaseHelper.checkRoomNameExists(rooms.room_name.toString())){
//                    Toast.makeText(requireContext(), " Duplicate Name", Toast.LENGTH_SHORT).show()
//
//                }else{
//
//                    if (databaseHelper.checkRoomTypeExists(rooms.room_type.toString())){
//                        var last_room_type =rooms.room_type.toString()
//                        var room_type_num= databaseHelper.getRoomTypeCount(last_room_type)+1
//                        var new_room_type="$last_room_type $room_type_num"
//
//
//
//                        rooms.room_type=new_room_type
//
//                        databaseHelper.set_to_db_rooms(rooms)
//
//
//                        SharedViewModel.update_rooms(databaseHelper.getAllRoomNames())
//
//
//
//                    }else{
//                        var last_room_type =rooms.room_type.toString()
//                        var room_type_num= databaseHelper.getRoomTypeCount(last_room_type)+1
//                        var new_room_type="$last_room_type $room_type_num"
//
//
//
//                        rooms.room_type=new_room_type
//
//                        databaseHelper.set_to_db_rooms(rooms)
//                        SharedViewModel.update_rooms(databaseHelper.getAllRoomNames())
//
//                    }
//
//                }
//
//
//
//
//
//
//            }else {
//
//                Toast.makeText(requireContext(), "  enter a name and press ok", Toast.LENGTH_SHORT).show()
//            }
//
//
//
//        }






    }
    override fun onItemClick(resourceName: String) {

        SharedViewModel.style_update.observe(viewLifecycleOwner, Observer { cmd ->
            println(cmd)
            if (cmd == "up"){

                SharedViewModel.update_shared_style_image_rec(resourceName)
                val setting_activity = requireActivity() as setting
//                setting_activity.changeViewPagerPage_three(9)



                SharedViewModel.update_is_image_changed("yes")
            }else if (cmd == "creat"){

                SharedViewModel.update_shared_style_image_rec(resourceName)
                val setting_activity = requireActivity() as setting
                setting_activity.changeViewPagerPage_three(8)

            }


        })



    }
}
