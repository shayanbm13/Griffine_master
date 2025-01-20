package com.example.griffin.fragment.setting_three

import android.content.Context
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
import com.example.griffin.R
import com.example.griffin.database.setting_network_db

class fragment_setting_modem_info : Fragment() {

    private lateinit var networkDb: setting_network_db

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            checkIP()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting_modem_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val modem_name_info = view.findViewById<TextView>(R.id.modem_name_info)
        val modem_password_info = view.findViewById<TextView>(R.id.modem_password_info)
        val modem_ip_info = view.findViewById<TextView>(R.id.modem_ip_info)

        networkDb = setting_network_db.getInstance(requireContext().applicationContext)
        val networkManualLiveData = networkDb.getNetworkManualLiveData()

        networkManualLiveData.observe(viewLifecycleOwner, { networkManual ->
            modem_name_info.text = "Modem Name: ${networkManual?.modem_ssid}"
            modem_password_info.text = "Modem Password: ${networkManual?.modem_password}"
        })

        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    @Suppress("DEPRECATION")
    private fun checkIP() {
        val wifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (wifiManager.isWifiEnabled) {
            val wifiInfo = wifiManager.connectionInfo
            val ipAddress = wifiInfo.ipAddress
            val ipString = String.format(
                "%d.%d.%d.%d",
                ipAddress and 0xff,
                ipAddress shr 8 and 0xff,
                ipAddress shr 16 and 0xff,
                ipAddress shr 24 and 0xff
            )

            requireActivity().runOnUiThread {
                val modem_ip_info = view?.findViewById<TextView>(R.id.modem_ip_info)
                modem_ip_info?.text = "IP Address: $ipString"
            }
        }
    }
}
