package com.example.griffin.fragment.setting_two

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.adapters.my_stylesAdapter
import com.example.griffin.adapters.stylesAdapter
import com.example.griffin.database.rooms_db
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.setting


class setting_homestyle : Fragment() {


        private  val SharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_homestyle, container, false)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homestyle_addrome_menu = view.findViewById<ImageButton>(R.id.homestyle_addrome_menu)
        val recyclerView = view.findViewById<RecyclerView>(R.id.style_recyclerview)



        val databaseHelper=rooms_db.getInstance(requireContext())

        val roomNamesLiveData = databaseHelper.getAllRoomNames()
        val adapter = my_stylesAdapter(roomNamesLiveData) { buttonText ->
            var selected_room=databaseHelper.getRoomByRoomNamePrefix(buttonText)
            val setting_activity = requireActivity() as setting


            setting_activity.changeViewPagerPage_three(9)


            if (selected_room != null) {
                SharedViewModel.update_selected_rooms(selected_room)
                SharedViewModel.update_shared_style_image_rec(selected_room.room_image.toString()+"2")
            }



        }
        recyclerView.adapter = adapter

        SharedViewModel.rooms.observe(viewLifecycleOwner) { roomNamesList ->
            val adapter = my_stylesAdapter(roomNamesList) { buttonText ->
                var selected_room=databaseHelper.getRoomByRoomNamePrefix(buttonText)
                val setting_activity = requireActivity() as setting
                setting_activity.changeViewPagerPage_three(9)
                if (selected_room != null) {
                    SharedViewModel.update_selected_rooms(selected_room)
                    SharedViewModel.update_shared_style_image_rec(selected_room.room_image.toString()+"2")
                }
            }

            recyclerView.adapter = adapter
        }





        homestyle_addrome_menu.setOnClickListener {

            val popupMenu = PopupMenu(context, homestyle_addrome_menu)
            popupMenu.gravity = Gravity.TOP or Gravity.END
            popupMenu.menuInflater.inflate(R.menu.homestyle_addrome_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId){
                    R.id.add_rome -> {
                        Toast.makeText(context,"add roome", Toast.LENGTH_SHORT).show()
                        val setting_activity = requireActivity() as setting
                        SharedViewModel.update_style_update("creat")
                        setting_activity.changeViewPagerPage_four(12)
                        setting_activity.changeViewPagerPage_three(0)

                    }



                }
                true
            }
            popupMenu.show()

        }







    }



}