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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.griffin.R
import com.example.griffin.database.Ir_db
import com.example.griffin.mudels.SharedViewModel


class setting_learn_ir_moreoption : Fragment() {


    val Shared_view_model: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_learn_ir_moreoption, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rename = view.findViewById<Button>(R.id.rename)
        val delete = view.findViewById<Button>(R.id.more_option_dalate)
        val irDb= Ir_db.getInstance(requireContext())


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



        Shared_view_model.current_IR.observe(viewLifecycleOwner, Observer { current_ir ->

            ok_name_pupop.setOnClickListener{
                current_ir!!.name=edit_name_pupop.text.toString()
                irDb.updateIrById(current_ir!!.id,current_ir)
                Shared_view_model.update_IR_update("R")

                popupWindow.dismiss()
            }

            rename.setOnClickListener{
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                edit_name_pupop.setText(current_ir!!.name)

            }
            delete.setOnClickListener{
                irDb.delete_from_db_Ir(current_ir!!.id)
                Shared_view_model.update_IR_update("D")
                popupWindow.dismiss()
            }




        })


    }


}