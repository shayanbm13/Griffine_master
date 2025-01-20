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
import com.example.griffin.database.setting_network_db
import com.example.griffin.database.setting_simcard_security_db
import com.example.griffin.mudels.simcard_security


class setting_simcard_security : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_simcard_security, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val simcard_security_username_input= view.findViewById<EditText>(R.id.simcard_security_user_input)
        val simcard_security_password_input= view.findViewById<EditText>(R.id.simcard_security_password_input)
        val simcard_security_cancel= view.findViewById<Button>(R.id.simcard_security_cancel)
        val simcard_security_ok= view.findViewById<Button>(R.id.simcard_security_ok)


        val databasehelper= setting_simcard_security_db.getInstance(requireContext())


        if (!databasehelper.isEmpty_simcard_security_tabale()){
            val current_db=databasehelper.get_from_db_simcard_security(1)
            simcard_security_username_input.setText(current_db!!.username)
            simcard_security_password_input.setText(current_db!!.password)


        }

        simcard_security_cancel.setOnClickListener {


            if (!databasehelper.isEmpty_simcard_security_tabale()){
                val current_db=databasehelper.get_from_db_simcard_security(1)
                simcard_security_username_input.setText(current_db!!.username)
                simcard_security_password_input.setText(current_db!!.password)


            }else{
                simcard_security_username_input.setText(null)
                simcard_security_password_input.setText(null)

            }
        }

        simcard_security_ok.setOnClickListener{
            if (databasehelper.isEmpty_simcard_security_tabale()){

                if (simcard_security_username_input.text.toString().isNotEmpty()&& simcard_security_password_input.text.toString().isNotEmpty()){

                    val simcardSecurity=simcard_security()
                    simcardSecurity.username=simcard_security_username_input.text.toString()
                    simcardSecurity.password=simcard_security_password_input.text.toString()
                    databasehelper.set_to_db_simcard_security(simcardSecurity)
                    Toast.makeText(requireContext(), "seted...", Toast.LENGTH_SHORT).show()



                }

            }else{
                val simcardSecurity=simcard_security()
                simcardSecurity.username=simcard_security_username_input.text.toString()
                simcardSecurity.password=simcard_security_password_input.text.toString()
                simcardSecurity.id=1

                databasehelper.update_db_simcard_security(simcardSecurity)
                Toast.makeText(requireContext(), "changes seted...", Toast.LENGTH_SHORT).show()

            }



        }


    }

}