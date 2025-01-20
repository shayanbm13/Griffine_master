package com.example.griffin.fragment.setting_four

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import com.example.griffin.R
import com.example.griffin.database.setting_simcard_accountsecurity_db
import com.example.griffin.mudels.simcard_accountsecurity
import com.example.griffin.mudels.simcard_messageresponse


class setting_simcard_accountsecurity : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_simcard_accountsecurity, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sms_answer_on_off=view.findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.sms_answer_on_off)
        val admin_phone_number_input=view.findViewById<EditText>(R.id.admin_phone_number_input)
        val backup1_phone_number_input=view.findViewById<EditText>(R.id.backup1_phone_number_input)
        val backup2_phone_number_input=view.findViewById<EditText>(R.id.backup2_phone_number_input)
        val backup3_phone_number_input=view.findViewById<EditText>(R.id.backup3_phone_number_input)
        val backup4_phone_number_input=view.findViewById<EditText>(R.id.backup4_phone_number_input)
        val account_security_cancel=view.findViewById<Button>(R.id.account_security_cancel)
        val account_security_ok=view.findViewById<Button>(R.id.account_security_ok)



        var databasehelper=setting_simcard_accountsecurity_db.getInstance(requireContext())

        if (!databasehelper.isEmpty_simcard_accountsecurity_tabale()){
            val current_db=databasehelper.get_from_db_simcard_accountsecurity(1)
            if (current_db!!.smsanswer_on_off=="on"){

                sms_answer_on_off.isChecked=true
            }else{
                sms_answer_on_off.isChecked=false


            }
            admin_phone_number_input.setText(current_db!!.admin_number)
            backup1_phone_number_input.setText(current_db!!.backup_number1)
            backup2_phone_number_input.setText(current_db!!.backup_number2)
            backup3_phone_number_input.setText(current_db!!.backup_number3)
            backup4_phone_number_input.setText(current_db!!.backup_number4)
        }

        account_security_cancel.setOnClickListener {

            if (!databasehelper.isEmpty_simcard_accountsecurity_tabale()){
                val current_db=databasehelper.get_from_db_simcard_accountsecurity(1)
                admin_phone_number_input.setText(current_db!!.admin_number)
                backup1_phone_number_input.setText(current_db!!.backup_number1)
                backup2_phone_number_input.setText(current_db!!.backup_number2)
                backup3_phone_number_input.setText(current_db!!.backup_number3)
                backup4_phone_number_input.setText(current_db!!.backup_number4)
                if (current_db!!.smsanswer_on_off=="on"){
                    sms_answer_on_off.isChecked=true
                }else{
                    sms_answer_on_off.isChecked=false
                }
            }else{
                admin_phone_number_input.setText(null)
                backup1_phone_number_input.setText(null)
                backup2_phone_number_input.setText(null)
                backup3_phone_number_input.setText(null)
                backup4_phone_number_input.setText(null)
                sms_answer_on_off.isChecked=false
            }
        }

        account_security_ok.setOnClickListener {

            if (databasehelper.isEmpty_simcard_accountsecurity_tabale()){

                if (admin_phone_number_input.text.toString().isNotEmpty()){

                    val simcard_accountsecurity= simcard_accountsecurity()
                    if (sms_answer_on_off.isChecked){
                        simcard_accountsecurity.smsanswer_on_off="on"
                    }else{
                        simcard_accountsecurity.smsanswer_on_off="off"
                    }
                    simcard_accountsecurity.admin_number=admin_phone_number_input.text.toString()
                    simcard_accountsecurity.backup_number1=backup1_phone_number_input.text.toString()
                    simcard_accountsecurity.backup_number2=backup2_phone_number_input.text.toString()
                    simcard_accountsecurity.backup_number3=backup3_phone_number_input.text.toString()
                    simcard_accountsecurity.backup_number4=backup4_phone_number_input.text.toString()
                    databasehelper.set_to_db_simcard_accountsecurity(simcard_accountsecurity)
                    Toast.makeText(requireContext(), "seted...", Toast.LENGTH_SHORT).show()
                }

            }else{
                val simcard_accountsecurity= simcard_accountsecurity()
                if (sms_answer_on_off.isChecked){
                    simcard_accountsecurity.smsanswer_on_off="on"
                }else{
                    simcard_accountsecurity.smsanswer_on_off="off"
                }
                simcard_accountsecurity.admin_number=admin_phone_number_input.text.toString()
                simcard_accountsecurity.backup_number1=backup1_phone_number_input.text.toString()
                simcard_accountsecurity.backup_number2=backup2_phone_number_input.text.toString()
                simcard_accountsecurity.backup_number3=backup3_phone_number_input.text.toString()
                simcard_accountsecurity.backup_number4=backup4_phone_number_input.text.toString()
                simcard_accountsecurity.id=1

                databasehelper.update_db_simcard_accountsecurity(simcard_accountsecurity)
                Toast.makeText(requireContext(), "changes seted...", Toast.LENGTH_SHORT).show()

            }


        }


    }

}