package com.example.griffin.fragment.setting_three

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.griffin.R
import com.example.griffin.database.rooms_db
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.rooms
import com.example.griffin.setting


class setting_homestyle_moreoption : Fragment() {
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_setting_homestyle_moreoption, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val style_imageview_2=view.findViewById<ImageView>(R.id.style_imageview_2)
        val style_name=view.findViewById<TextView>(R.id.style_name)
        val room_name_input=view.findViewById<EditText>(R.id.room_name_input)
        val room_type =view.findViewById<TextView>(R.id.room_type)
        val ok_room_name=view.findViewById<Button>(R.id.ok_room_name)
        val databaseHelper=rooms_db.getInstance(requireContext())

        sharedViewModel.shared_style_image_rec.observe(viewLifecycleOwner, Observer { value1 ->


            val imageName=value1.dropLast(1)+"2"
            imageName?.let {
                val imageResource = resources.getIdentifier(it, "drawable", requireActivity().packageName)
                if (imageResource != 0) {
                    Glide.with(requireContext())
                        .load(imageResource)
                        .into(style_imageview_2)
                }

            }

            val imageName_start=imageName.substring(0,2)
            when (imageName_start){
                "s1" -> style_name.text= "Style 1"
                "s2" -> style_name.text= "Style 2"
                "s3" -> style_name.text= "Style 3"
                "of" -> style_name.text= "Office style"
            }
            when(imageName.trim('"').substring(3,7)){
                "bath" ->room_type.text= "Bathroom"
                "dini" -> room_type.text="Diningroom"
                "gust" -> room_type.text="gustroom"
                "kids" -> room_type.text="Kidsroom"
                "kitc" -> room_type.text="Kitchen"
                "livi" -> room_type.text="Livingroom"
                "mast" -> room_type.text="Masterroom"
                "tv_r" -> room_type.text="Tvroom"
                "yard" -> room_type.text="Yard"
                "room" -> room_type.text="Room"
                else -> {"no sync"}
            }



        ok_room_name.setOnClickListener {
            room_name_input.clearFocus()
            sharedViewModel.update_custom_room_name(room_name_input.text.toString())




            val rooms=rooms()
            sharedViewModel.shared_style_image_rec.observe(viewLifecycleOwner, Observer { value ->

                rooms.room_type=when(value.toString().trim('"').substring(3,7)){
                    "bath" -> "bathroom"
                    "dini" -> "diningroom"
                    "gust" -> "gustroom"
                    "kids" -> "kidsroom"
                    "kitc" -> "kitchen"
                    "livi" -> "livingroom"
                    "mast" -> "masterroom"
                    "tv_r" -> "tvroom"
                    "yard" -> "yard"
                    "room" -> "room"
                    else -> {"no sync"}
                }
            })
            sharedViewModel.custom_room_name.observe(viewLifecycleOwner, Observer { value ->
                rooms.room_name = value.toString().trim('"')


            })

            if(rooms.room_name?.isNotBlank() == true){

                if (databaseHelper.checkRoomNameExists(rooms.room_name.toString())){
                    Toast.makeText(requireContext(), " Duplicate Name", Toast.LENGTH_SHORT).show()

                }else{

                    if (databaseHelper.checkRoomTypeExists(rooms.room_type.toString())){
                        var last_room_type =rooms.room_type.toString()
                        var room_type_num= databaseHelper.getRoomTypeCount(last_room_type)+1
                        var new_room_type="$last_room_type $room_type_num"



                        rooms.room_type=new_room_type
                        rooms.room_image=value1.dropLast(1)


                        databaseHelper.set_to_db_rooms(rooms)
                        Toast.makeText(requireContext(),"Saved" , Toast.LENGTH_SHORT).show()
                        room_name_input.text=null
                        room_type.text = null
                        room_name_input.setText(null)
                        style_name.text=null
                        Glide.with(requireContext())
                            .load(0)
                            .into(style_imageview_2)

                        sharedViewModel.update_rooms(databaseHelper.getAllRoomNames())



                    }else{
                        var last_room_type =rooms.room_type.toString()
                        var room_type_num= databaseHelper.getRoomTypeCount(last_room_type)+1
                        var new_room_type="$last_room_type $room_type_num"



                        rooms.room_type=new_room_type
                        rooms.room_image=value1.dropLast(1)

                        databaseHelper.set_to_db_rooms(rooms)
                        Toast.makeText(requireContext(),"Saved" , Toast.LENGTH_SHORT).show()
                        room_name_input.text=null

                        room_name_input.text=null
                        room_type.text = null
                        room_name_input.setText(null)
                        style_name.text=null
                        Glide.with(requireContext())
                            .load(0)
                            .into(style_imageview_2)
                        sharedViewModel.update_rooms(databaseHelper.getAllRoomNames())
                        val setting_activity = requireActivity() as setting
                        setting_activity.changeViewPagerPage_three(0)
                        setting_activity.changeViewPagerPage_four(0)


                    }

                }






            }else {

                Toast.makeText(requireContext(), "  enter a name and press ok", Toast.LENGTH_SHORT).show()
            }









        }
        sharedViewModel.shared_style_image_rec.observe(viewLifecycleOwner, Observer { value ->

            room_name_input.setText(null)



        })



//            val databaseHelper=rooms_db.getInstance(requireContext().applicationContext)
//
//            val roomsLiveData=databaseHelper.get_db_rooms_LiveData()
//            roomsLiveData.observe(viewLifecycleOwner) { rooms ->
//                room_name.text = rooms!!.room_name
//
//            }


        })

    }
}