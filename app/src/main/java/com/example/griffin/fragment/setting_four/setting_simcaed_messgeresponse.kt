package com.example.griffin.fragment.setting_four

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.griffin.R
import com.example.griffin.database.setting_simcard_messageresponse_db
import com.example.griffin.mudels.simcard_messageresponse
import com.example.griffin.mudels.simcard_security

class setting_simcaed_messgeresponse : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_simcaed_messgeresponse, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var scenario_response_input=view.findViewById<EditText>(R.id.scenario_response_input)
        var module_response_input=view.findViewById<EditText>(R.id.module_response_input)
        var sensor_response_input=view.findViewById<EditText>(R.id.sensor_response_input)
        var security_response_input=view.findViewById<EditText>(R.id.security_response_input)
        var message_response_ok=view.findViewById<Button>(R.id.message_response_ok)
        var message_response_cancel=view.findViewById<Button>(R.id.message_response_cancel)


        val databasehelper=setting_simcard_messageresponse_db.getInstance(requireContext())


        if (!databasehelper.isEmpty_simcard_message_response_tabale()){
            val current_db=databasehelper.get_from_db_simcard_message_response(1)
            scenario_response_input.setText(current_db!!.scenario_r)
            module_response_input.setText(current_db!!.module_r)
            sensor_response_input.setText(current_db!!.sensor_r)
            security_response_input.setText(current_db!!.security_r)


        }

        message_response_cancel.setOnClickListener {

            if (!databasehelper.isEmpty_simcard_message_response_tabale()){
                val current_db=databasehelper.get_from_db_simcard_message_response(1)
                scenario_response_input.setText(current_db!!.scenario_r)
                module_response_input.setText(current_db!!.module_r)
                sensor_response_input.setText(current_db!!.sensor_r)
                security_response_input.setText(current_db!!.security_r)


            }else{
                scenario_response_input.setText(null)
                module_response_input.setText(null)
                sensor_response_input.setText(null)
                security_response_input.setText(null)

            }

        }


        message_response_ok.setOnClickListener{
            println(scenario_response_input.text.toString().trim())
            println(module_response_input.text.toString().trim())
            println(sensor_response_input.text.toString().trim())
            println(security_response_input.text.toString().trim())
            if (databasehelper.isEmpty_simcard_message_response_tabale()){

                if (scenario_response_input.text.toString().isNotEmpty()&& module_response_input.text.toString().isNotEmpty()&& sensor_response_input.text.toString().isNotEmpty()&& security_response_input.text.toString().isNotEmpty()){


                    val simcard_messageresponse= simcard_messageresponse()
                    simcard_messageresponse.scenario_r=scenario_response_input.text.toString().trim()
                    simcard_messageresponse.module_r=module_response_input.text.toString().trim()
                    simcard_messageresponse.sensor_r=sensor_response_input.text.toString().trim()
                    simcard_messageresponse.security_r=security_response_input.text.toString().trim()
                    databasehelper.set_to_db_simcard_message_response(simcard_messageresponse)
                    Toast.makeText(requireContext(), "seted...", Toast.LENGTH_SHORT).show()
                }


            }else{
                val simcard_messageresponse= simcard_messageresponse()
                simcard_messageresponse.scenario_r=scenario_response_input.text.toString().trim()
                simcard_messageresponse.module_r=module_response_input.text.toString().trim()
                simcard_messageresponse.sensor_r=sensor_response_input.text.toString().trim()
                simcard_messageresponse.security_r=security_response_input.text.toString().trim()
                simcard_messageresponse.id=1

                databasehelper.update_db_simcard_message_response(simcard_messageresponse)
                Toast.makeText(requireContext(), "changes seted...", Toast.LENGTH_SHORT).show()

            }



        }
    }
}