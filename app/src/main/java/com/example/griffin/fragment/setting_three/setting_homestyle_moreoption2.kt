package com.example.griffin.fragment.setting_three

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.manager.Lifecycle
import com.example.griffin.R
import com.example.griffin.database.*
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.rooms
import com.example.griffin.setting


class setting_homestyle_moreoption2 : Fragment() {

    private  val SharedViewModel: SharedViewModel by activityViewModels()
    private var isFirstChange = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_homestyle_moreoption2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val style_imageview_more2=view.findViewById<ImageView>(R.id.style_imageview_more2)
        val style_name=view.findViewById<TextView>(R.id.style_name)
        val room_name_input=view.findViewById<EditText>(R.id.room_name_input)
        val room_type =view.findViewById<TextView>(R.id.room_type)
        val room_name =view.findViewById<TextView>(R.id.room_name)
        val ok_room_name_moew2=view.findViewById<Button>(R.id.ok_room_name_moew2)

        var previousText = ""
        SharedViewModel.selected_rooms.observe(viewLifecycleOwner) { value ->
            val imageName = value.room_image + "2"
            println(imageName)
            imageName?.let {
                val imageResource =
                    resources.getIdentifier(it, "drawable", requireActivity().packageName)
                if (imageResource != 0) {
                    Glide.with(requireContext())
                        .load(imageResource)
                        .into(style_imageview_more2)
                }
            }

            println(value.room_image!!.substring(0, 2))
            when (value.room_image!!.substring(0, 2)) {
                "s1" -> style_name.text = "Style 1"
                "s2" -> style_name.text = "Style 2"
                "s3" -> style_name.text = "Style 3"
                "of" -> style_name.text = "Office style"
            }
            room_type.text = value.room_type!!.dropLast(1)
            room_name_input.setText(value.room_name)
            room_name.text = value.room_name

            previousText = value.room_name ?: "" // اختصاص دادن مقدار به previousText
            fun updateButtonText(newText: String) {
                if (newText == previousText) {
                    ok_room_name_moew2.text = "Delete"
                } else {
                    ok_room_name_moew2.text = "Ok"
                }
            }

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    updateButtonText(s.toString())
                }
            }


            room_name_input.addTextChangedListener(textWatcher)
            updateButtonText(room_name_input.text.toString())

            val databaseHelper=rooms_db.getInstance(requireContext())

            SharedViewModel.is_image_changed.observe(viewLifecycleOwner, Observer { cmd ->
                println(cmd)

                if (cmd == "yes"){
                    ok_room_name_moew2.text = "Ok"


                }




            })

            var old_name = room_name_input.text.toString()
            ok_room_name_moew2.setOnClickListener {
                val curtainDb=curtain_db.getInstance(requireContext())
                val fanDb=fan_db.getInstance(requireContext())
                val lightDb=light_db.getInstance(requireContext())
                val plugtDb=plug_db.getInstance(requireContext())
                val tempDb=Temperature_db.getInstance(requireContext())
                val valveDb=valve_db.getInstance(requireContext())
                val sixcDB=six_workert_db.getInstance(requireContext())


                println("clicked")
                room_name_input.clearFocus()

                if (ok_room_name_moew2.text == "Ok"){
                    println(old_name)
                    SharedViewModel.update_style_update("changed")
                    SharedViewModel.update_is_image_changed("done")


                    SharedViewModel.shared_style_image_rec.observe(viewLifecycleOwner, Observer { value1 ->

                        println(value1)
                        var chamged_room=rooms()
                        chamged_room.room_name= room_name_input.text.toString()


                        chamged_room.room_image=value1.dropLast(1)
                        chamged_room.room_type=value.room_type
                        var id =value.id
                        databaseHelper.updateRoomById(id,chamged_room)
                        SharedViewModel.update_rooms(databaseHelper.getAllRoomNames())
                        databaseHelper.getAllRoomNames()
                        Toast.makeText(requireContext(), "Updated", Toast.LENGTH_SHORT).show()
                        room_name.text=room_name_input.text.toString()

                        curtainDb.updateCurtainName(old_name,room_name_input.text.toString())
                        fanDb.updateFanName(old_name,room_name_input.text.toString())
                        lightDb.updateLightName(old_name,room_name_input.text.toString())
                        plugtDb.updatePlugName(old_name,room_name_input.text.toString())
                        tempDb.updateTemperatureName(old_name,room_name_input.text.toString())
                        valveDb.updateValveName(old_name,room_name_input.text.toString())


                        val setting_activity = requireActivity() as setting
                        setting_activity.changeViewPagerPage_four(0)
                        setting_activity.changeViewPagerPage_three(0)
                    })



                }else{
                    databaseHelper.delete_from_db_rooms(value.id)
                    SharedViewModel.update_rooms(databaseHelper.getAllRoomNames())

                    val lights = lightDb.getAllLightsByRoomName(value.room_name)
                    val curtains = curtainDb.getAllcurtainsByRoomName(value.room_name)
                    val plugs = plugtDb.getPlugsByRoomName(value.room_name)
                    val valve = valveDb.getvalvesByRoomName(value.room_name)
                    val temps  = tempDb.getThermostatsByRoomName(value.room_name)
                    val sixc = sixcDB.getAllsix_workerts()
                    for (light in lights){

                        if (light != null) {
                            lightDb.delete_from_db_light(light.id)
                        }
                        for (six in sixc){
                            if (six!!.mac == light!!.mac){
                                sixcDB.delete_from_db_six_workert(six.id)
                            }
                        }

                    }
                    for(curtain in curtains){
                        if (curtain != null) {
                            curtainDb.delete_from_db_curtain(curtain.id)
                        }
                    }
                    for (plug in plugs){
                        if (plug != null) {
                            plugtDb.delete_from_db_Plug(plug.id)
                        }
                    }

                    for (valve in valve){
                        if (valve != null) {
                            valveDb.delete_from_db_valve(valve.id)
                        }
                    }
                    for (temp in temps){
                        if (temp != null) {
                            tempDb.delete_from_db_Temprature(temp.id)
                        }
                    }




                    room_type.text = null
                    room_name_input.setText(null)
                    room_name.text =null
                    style_name.text=null
                    Glide.with(requireContext())
                        .load(0)
                        .into(style_imageview_more2)
                    val setting_activity = requireActivity() as setting
                    setting_activity.changeViewPagerPage_three(0)
                }


            }


            SharedViewModel.shared_style_image_rec.observe(viewLifecycleOwner, Observer { value1 ->


                val imageName=value1.dropLast(1)+"2"
                imageName?.let {
                    val imageResource = resources.getIdentifier(it, "drawable", requireActivity().packageName)
                    if (imageResource != 0) {
                        Glide.with(requireContext())
                            .load(imageResource)
                            .into(style_imageview_more2)
                    }

                }



            })







        }


        style_imageview_more2.setOnClickListener {
            val setting_activity = requireActivity() as setting
            setting_activity.changeViewPagerPage_four(12)
            SharedViewModel.update_style_update("up")
        }

    }

}