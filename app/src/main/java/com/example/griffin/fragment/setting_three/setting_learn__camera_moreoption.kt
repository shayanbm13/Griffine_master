package com.example.griffin.fragment.setting_three

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.griffin.R
import com.example.griffin.database.camera_db
import com.example.griffin.fragment.setting_four.setting_learn_camera
import com.example.griffin.griffin_home
import com.example.griffin.mudels.SharedViewModel


class setting_learn__camera_moreoption : Fragment() {
    private val sharedViewModel:SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_setting_learn__camera_moreoption,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val set_camera=view.findViewById<Button>(R.id.set_camera)
        val rename=view.findViewById<Button>(R.id.rename)
        val delete_camera=view.findViewById<Button>(R.id.more_option_dalate)


        val inflater = LayoutInflater.from(requireContext())

        val customPopupView: View = inflater.inflate(R.layout.popup_addlight, null)
        val popupView: View = inflater.inflate(R.layout.popup_addlight, null)



        val edit_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
        val ok_name_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
        val learn_light_pupop = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
        val delete_light_pupop = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
        val on_off_test_light_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)
        val send_to = customPopupView.findViewById<Button>(R.id.send_to)

        learn_light_pupop.visibility=View.INVISIBLE
        on_off_test_light_pupop.visibility=View.INVISIBLE
        delete_light_pupop.visibility=View.INVISIBLE
        send_to.visibility=View.INVISIBLE
        val popupWidth = 490
        val popupHeight = 490
        edit_name_pupop.setText("")
        // ایجاد لایه‌ی کاستوم


        // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
        val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(popupView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.setCanceledOnTouchOutside(false)





        sharedViewModel.current_camera.observe(viewLifecycleOwner, Observer { camera ->


            println(camera!!.id)
            set_camera.setOnClickListener {


                println(camera.CAMname)
                println(camera.id)
                println(camera)
                val my_camera_db=camera_db.getInstance(requireContext())
                if (camera!!.id.toString()=="null"){
                    my_camera_db.set_to_db_camera(camera)
                    sharedViewModel.update_cam_update("2")
                    Toast.makeText(requireContext(), "Camera Saved", Toast.LENGTH_SHORT).show()

                }else{
                    my_camera_db.updatecameraById(camera.id,camera)
                    Toast.makeText(requireContext(), "Camera Updated", Toast.LENGTH_SHORT).show()
                }


            }


            rename.setOnClickListener {
                edit_name_pupop.setText("")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                val my_camera_db=camera_db.getInstance(requireContext())

                if (camera!!.id.toString()=="null" ){
                    camera!!.CAMname=edit_name_pupop.text.toString()

                }else{
                    camera!!.CAMname=edit_name_pupop.text.toString()
                    my_camera_db.updatecameraById(camera.id,camera)


                }




            }
            ok_name_pupop.setOnClickListener {
                val my_camera_db=camera_db.getInstance(requireContext())
                camera!!.CAMname=edit_name_pupop.text.toString()
                my_camera_db.updatecameraById(camera.id,camera)
                sharedViewModel.update_cam_update("1")
                Toast.makeText(requireContext(), "Camera Renamed", Toast.LENGTH_SHORT).show()
                popupWindow.dismiss()

            }

            delete_camera.setOnClickListener {

                val my_camera_db=camera_db.getInstance(requireContext())
                if (camera!!.id.toString()=="null"){

                }else{
                    my_camera_db.delete_from_db_camera(camera!!.id)
                    sharedViewModel.update_cam_update("1")
                    Toast.makeText(requireContext(), "Camera Deleted", Toast.LENGTH_SHORT).show()

                }





            }



        })















    }

}