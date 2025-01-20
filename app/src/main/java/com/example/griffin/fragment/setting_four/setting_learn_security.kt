package com.example.griffin.fragment.setting_four

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.griffin.R
import com.example.griffin.database.security_db
import com.example.griffin.mudels.security

class setting_learn_security : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_learn_security, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val alarm_duration= view.findViewById<EditText>(R.id.alarm_duration)
        val arm_active_deley= view.findViewById<EditText>(R.id.arm_active_deley)
        val alarm_trigger_deley= view.findViewById<EditText>(R.id.alarm_trigger_deley)
        val active_scenario= view.findViewById<EditText>(R.id.active_security)
        val password_security= view.findViewById<EditText>(R.id.password_security)
        val set_to_db= view.findViewById<Button>(R.id.ok_btn)

        var security_db_handler= security_db.getInstance(requireContext())
        var is_empty = security_db_handler.isEmptysecurity_tabale()
         if (!is_empty){
             val current_db = security_db_handler.get_from_db_security(1)
             alarm_duration.setText(current_db!!.alarm_duration)
             arm_active_deley.setText(current_db.arm_active_deley)
             alarm_trigger_deley.setText(current_db.alarm_triger_deley)
             active_scenario.setText(current_db.active_scenario)
             password_security.setText(current_db.password_security)


             set_to_db.setOnClickListener {
                 current_db.alarm_duration=alarm_duration.text.toString()
                 current_db.arm_active_deley=arm_active_deley.text.toString()
                 current_db.alarm_triger_deley=alarm_trigger_deley.text.toString()
                 current_db.active_scenario=active_scenario.text.toString()
                 current_db.password_security=password_security.text.toString()
                 security_db_handler.update_db_security(current_db)



                 val security = security()
                 security.alarm_duration

             }


         }else{




         }


        set_to_db.setOnClickListener {
            if (is_empty){


                val security = security()

                security.alarm_duration=alarm_duration.text.toString()
                security.arm_active_deley=arm_active_deley.text.toString()
                security.alarm_triger_deley=alarm_trigger_deley.text.toString()
                security.active_scenario=active_scenario.text.toString()
                security.password_security=password_security.text.toString()
                security_db_handler.set_to_db_security(security)
            }else{
                val current_db = security_db_handler.get_from_db_security(1)
                current_db!!.alarm_duration=alarm_duration.text.toString()
                current_db.arm_active_deley=arm_active_deley.text.toString()
                current_db.alarm_triger_deley=alarm_trigger_deley.text.toString()
                current_db.active_scenario=active_scenario.text.toString()
                current_db.password_security=password_security.text.toString()
                println(current_db.alarm_duration)
                security_db_handler.update_db_security(current_db)
                println("updated")



                val security = security()
                security.alarm_duration
            }

        }





    }


}