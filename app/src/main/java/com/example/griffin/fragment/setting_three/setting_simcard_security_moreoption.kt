package com.example.griffin.fragment.setting_three

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.example.griffin.R
import com.example.griffin.database.setting_network_db
import com.example.griffin.database.setting_simcard_accountsecurity_db
import com.example.griffin.database.setting_simcard_messageresponse_db
import com.example.griffin.database.setting_simcard_security_db
import com.example.griffin.fragment.setting_four.setting_simcard_security
import com.example.griffin.mudels.simcard_accountsecurity
import com.example.griffin.mudels.simcard_security


class setting_simcard_security_moreoption : Fragment() {
    private lateinit var simcard_security_Db: setting_simcard_security_db
    private lateinit var simcard_accountsecurity_Db: setting_simcard_accountsecurity_db





        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(
            R.layout.fragment_setting_simcard_security_moreoption,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sim_username_info=view.findViewById<TextView>(R.id.sim_username_info)
        val sim_password_info=view.findViewById<TextView>(R.id.sim_password_info)
        val sim_number_info=view.findViewById<TextView>(R.id.sim_number_info)
        simcard_accountsecurity_Db = setting_simcard_accountsecurity_db.getInstance(requireContext().applicationContext)
        simcard_security_Db = setting_simcard_security_db.getInstance(requireContext().applicationContext)
        if (!simcard_security_Db.isEmpty_simcard_security_tabale()){

            val current_sim_info =simcard_security_Db.get_from_db_simcard_security(1)
            sim_username_info.setText("Username : ${current_sim_info?.username}")
            sim_password_info.setText("Modem Password : ${current_sim_info?.password}")
        }
        if (!simcard_accountsecurity_Db.isEmpty_simcard_accountsecurity_tabale()){
            val current_sim_num_info =simcard_accountsecurity_Db.get_from_db_simcard_accountsecurity(1)
            sim_number_info.setText("Number : ${current_sim_num_info?.admin_number}")
        }

        val simcard_security_db_handler = setting_simcard_accountsecurity_db.getInstance(requireContext())
        val simcard_securityLiveData = simcard_security_Db.getsimcard_LiveData()
        val simcard_accountsecurityLiveData = simcard_accountsecurity_Db.getsimcard_accountsecurity_LiveData()
//        sim_number_info.setText("Number:  ${simcard_accountsecurity?.admin_number}")
//        sim_username_info.setText("Username: ${simcard_security?.username}")
//        sim_password_info.setText("Modem Password: ${simcard_security?.password}")
        simcard_accountsecurityLiveData.observe(viewLifecycleOwner) { simcard_accountsecurity ->
            sim_number_info.setText("Number: ${simcard_accountsecurity?.admin_number}")
        }
        simcard_securityLiveData.observe(viewLifecycleOwner) { simcard_security ->

            sim_username_info.setText("Username : ${simcard_security?.username}")
            sim_password_info.setText("Modem Password : ${simcard_security?.password}")
//            sim_number_info.text = "Phone:${simcard_security_db_handler.get_from_db_simcard_accountsecurity(1)?.admin_number}"

        }



    }








}