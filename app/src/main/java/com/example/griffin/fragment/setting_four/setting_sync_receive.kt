package com.example.griffin.fragment.setting_four

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.griffin.R
import com.example.griffin.database.*
import com.example.griffin.griffin_home
import com.example.griffin.mudels.*
import com.example.griffin.myBroadcastReceiverr

import com.example.griffin.setting
import kotlinx.coroutines.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import kotlin.concurrent.thread


class setting_sync_receive : Fragment() {

    var socket: DatagramSocket? = null

    var working = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_sync_receive, container, false)
    }

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ok_receive = requireView().findViewById<Button>(R.id.ok_receive)
        val cance_receive = requireView().findViewById<Button>(R.id.cance_receive)
        val sender_ip = requireView()!!.findViewById<TextView>(R.id.sender_ip)
        val receive_steps = requireView().findViewById<TextView>(R.id.receive_steps)
        val received_status = requireView()!!.findViewById<TextView>(R.id.received_status)
        val connect_status = requireView().findViewById<TextView>(R.id.connect_status)
        val textView9 = requireView()!!.findViewById<TextView>(R.id.textView9)

        try {

            connect_status.setText("Disconnected")
        }catch (e:Exception){
            println(e)
        }



        var working = false
        val griffin_setting = requireActivity() as setting

        cance_receive.setOnClickListener {



            socket?.close()
            socket=null
            working=false
            ok_receive.isSelected=false
            try {

                textView9.setText("Current Database Will \n Be Cleared , Proceed?")
                sender_ip.setText("")
                receive_steps.setText("")
                received_status.setText("Canceled")
                connect_status.setText("Disconnecteded")
            }catch (e:Exception){
                println(e)
            }
            UdpListener8089.resume()



            griffin_setting.changeViewPagerPage_four(0)
            griffin_setting.changeViewPagerPage_three(0)
        }
        fun isConnectedToWifi(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
        }

        val inflater2 = LayoutInflater.from(requireContext())

        val customPopupView2: View = inflater2.inflate(R.layout.popup_delete_timetable, null)
        val popupView2: View = inflater2.inflate(R.layout.popup_delete_timetable, null)


        val popupWidth2 = resources.getDimension(`in`.nouri.dynamicsizeslib.R.dimen._170mdp).toInt()
        val popupHeight2 = resources.getDimension(`in`.nouri.dynamicsizeslib.R.dimen._77mdp).toInt()
        val popupWindow2 = PopupWindow(customPopupView2, popupWidth2, popupHeight2, true)

        val alertDialogBuilder2 = AlertDialog.Builder(requireContext())
        alertDialogBuilder2.setView(popupView2)





        val alertDialog2 = alertDialogBuilder2.create()
        alertDialog2.setCanceledOnTouchOutside(false)

        val yes_delete = customPopupView2.findViewById<Button>(R.id.yes_delete)
        val cancel_delete = customPopupView2.findViewById<Button>(R.id.cancel_delete)

        customPopupView2.findViewById<TextView>(R.id.text_msg).setText("Are you sure ?")





        ok_receive.setOnClickListener {

            val wifiManager = requireContext().applicationContext.getSystemService(
                Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val ssid = wifiInfo.ssid
            val db_ssid = setting_network_db.getInstance(requireContext()).get_from_db_network_manual(1)?.modem_ssid

            if (isConnectedToWifi(requireContext()) && ssid.replace("\"", "") ==db_ssid ){
                popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                yes_delete.setOnClickListener {
                    popupWindow2.dismiss()


                    working=true
                    UdpListener8089.pause()
                    try {
                        connect_status.setText("Connecting...")

                    }catch (e:Exception){
                        println(e)
                    }
                    rooms_db.getInstance(requireContext()).clear_db_rooms()
                    Temperature_db.getInstance(requireContext()).clear_db_Temprature()
                    light_db.getInstance(requireContext()).clear_db_light()
                    fan_db.getInstance(requireContext()).clear_db_fan()
                    curtain_db.getInstance(requireContext()).clear_db_curtain()
                    plug_db.getInstance(requireContext()).clear_db_plug()
                    valve_db.getInstance(requireContext()).clear_db_valve()
                    rooms_db.getInstance(requireContext()).clear_db_rooms()
                    camera_db.getInstance(requireContext()).clear_db_camera()
                    scenario_db.Scenario_db.getInstance(requireContext()).clear_db_scenario()
                    favorite_db.Favorite_db.getInstance(requireContext()).clear_db_favorite()
                    alarm_handeler_db.getInstance(requireContext()).clear_db_alarm()
                    door_db.getInstance(requireContext()).clear_db_DOOR()
                    Elevator_db.getInstance(requireContext()).clear_db()
                    security_db.getInstance(requireContext()).clearsecurityTable()
//                setting_network_db.getInstance(requireContext()).clearNetworkTable()
                    setting_simcard_accountsecurity_db.getInstance(requireContext()).clear_db()
                    setting_simcard_messageresponse_db.getInstance(requireContext()).clear_db()
                    setting_simcard_security_db.getInstance(requireContext()).clear_db()
                    six_workert_db.getInstance(requireContext()).clear_db_six_workert()
                    Ir_db.getInstance(requireContext()).clear_db_Ir()

                    try {
                        val alarm_db= alarm_handeler_db.getInstance(requireContext())
                        val alarms =alarm_db.getAllalarms()
                        for (alarm in alarms){
                            val alarmIntent = Intent(context, myBroadcastReceiverr::class.java)
                            val pendingIntent = PendingIntent.getBroadcast(context, alarm!!.alarm_name.hashCode(), alarmIntent,
                                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                            val alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager

// کنسل کردن الارم
                            alarmMgr.cancel(pendingIntent)

                            alarm_db.delete_from_db_alarm(alarm.id)

                        }
                    }catch (e:Exception){
                        println()

                    }

                    ok_receive.isSelected=true
                    try {

                        textView9.setText("Press Cancel To Stop")
                    }catch (e:Exception){
                        println(e)
                    }

                    GlobalScope.launch(Dispatchers.IO) {
                        val bufferSize = 2048
                        val receiveData = ByteArray(bufferSize)


                        var previus_data =""

                        try {

                            while (working) {
                                try {
                                    if (socket == null || socket!!.isClosed) {
                                        socket = DatagramSocket(8089)
                                        println("Listenning to 8089..")
                                    }
                                }catch (e:Exception){
                                    println(e)
                                    println("111111111")
//                            socket?.close()
//                            socket=null
//                            working=false
//                            ok_receive.isSelected=false
//                            textView9.setText("Current Database Will \n Be Cleared , Proceed?")
//                            sender_ip.setText("")
//                            receive_steps.setText("")
//                            received_status.setText("Failed")
//                            connect_status.setText("Disconnecteded")
//                            UdpListener8089.resume()
                                }



                                val receivePacket = DatagramPacket(receiveData, receiveData.size)
                                socket!!.receive(receivePacket)

                                val receivedMessage = String(receivePacket.data, 0, receivePacket.length)
                                println("received messagefrom 8089 : $receivedMessage")

//                        println(receivedMessage)


                                try {
                                    connect_status.setText("Connected")

                                }catch (e:Exception){
                                    println(e)
                                }
                                val msg=receivedMessage.split("~>")
                                val target_ip = msg[(msg.count())-1 ]
                                var akid = msg[(msg.count())-2 ]
                                val my_ip= checkIP(requireContext())

                                val sy = msg[0]
                                if (msg[0]=="sygi"){
                                    akid="#"
                                    try {

                                        sender_ip.setText(msg[1])
                                    }catch (e:Exception){
                                        println(e)
                                    }

                                }else if (msg[0] == "sydn"){
                                    println("dnnnnnnnnnnnnnnnnnnnnnnnnnnnn")
                                    working=false
                                    val msg=receivedMessage.split("~>")
                                    val target_ip = msg[(msg.count())-1 ]
                                    var akid = msg[(msg.count())-2 ]
                                    try {

                                        connect_status.setText("Disconnected...")
                                    }catch (e:Exception){
                                        println(e)
                                    }

                                    val message = "syac~>$akid"
                                    try {
                                        receive_steps.setText("")
                                        received_status.setText("Completed")
                                        textView9.setText("Current Database Will \n Be Cleared , Proceed?")

                                        val lightDatabase = light_db.getInstance(requireContext())
                                        lightDatabase.removeDuplicates()
                                        val curtainDatabase = curtain_db.getInstance(requireContext())
                                        curtainDatabase.removeDuplicates()
                                        val fanDatabase = fan_db.getInstance(requireContext())
                                        fanDatabase.removeDuplicates()
                                        val plugDatabase = plug_db.getInstance(requireContext())
                                        plugDatabase.removeDuplicates()
                                        val roomsDatabase = rooms_db.getInstance(requireContext())
                                        roomsDatabase.removeDuplicates()
                                        val sixWorkertDatabase = six_workert_db.getInstance(requireContext())
                                        sixWorkertDatabase.removeDuplicates()
                                        val temperatureDatabase = Temperature_db.getInstance(requireContext())
                                        temperatureDatabase.removeDuplicates()
                                        val valveDatabase = valve_db.getInstance(requireContext())
                                        valveDatabase.removeDuplicates()

                                    }catch (e:Exception){
                                        println(e)
                                    }


                                    println(target_ip)
//                            val serverAddress = InetAddress.getByName(target_ip)
//                            val serverPort = 8089
//                            val sendData = message.toByteArray()
//                            val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, serverPort)
//                            socket!!.send(sendPacket)
//                            socket!!.close()
                                    // باید بگه به ال سی ئی که تموم شد بعد سوکت رو ببنده





                                }else{
                                    try {

                                        if (msg[0]=="syro"){
                                            receive_steps.setText("Rooms")
                                        }else if (msg[0]=="symo"){
                                            receive_steps.setText("Modules")
                                        }else if (msg[0]=="sysp"){
                                            receive_steps.setText("spilet")
                                        }else if (msg[0]=="sysx"){
                                            receive_steps.setText("Modules")
                                        }else if (msg[0]=="sysc"){
                                            receive_steps.setText("Scenario")
                                        }
                                    }catch (e:Exception){
                                        println(e)
                                    }

                                    println(previus_data)
                                    println(receivedMessage)
                                    if (receivedMessage != previus_data){

                                        sync_decoder(requireContext(),receivedMessage)
                                        previus_data= receivedMessage
                                    }

                                }



                                var message = "sysi~>$my_ip"
                                if (akid =="#"){
                                    message = "sysi~>$my_ip"


                                }else{
                                    message = "syac~>$akid"

                                }

                                println(message)
                                println(target_ip)
                                val serverAddress = InetAddress.getByName(target_ip)
                                val serverPort = 8089


                                val sendData = message.toByteArray()
                                val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, serverPort)
                                socket!!.send(sendPacket)
//                            sync_fidback(requireContext(),sy,akid,target_ip)
                                if (msg[0] == "sydn"){
//                            socket!!.close()
                                    try {

                                        connect_status.setText("Disconnected...")
                                    }catch (e:Exception){
                                        println(e)
                                    }
                                    var db =light_db.getInstance(requireContext())
                                    val a = db.getLightsWithSubType0000()
                                    println(a)
                                    val list = arrayListOf<List<Light?>>()
                                    for (item in a ){
                                        if (item != null) {
                                            list.add(db.getLightsByMacAddress(item.mac))
                                        }
                                    }

                                    socket!!.close()
                                    UdpListener8089.resume()
                                }





                            }


                        }catch (e:Exception){
                            println("issssssssssss")
                            println(e)
                            socket?.close()

                            socket=null
                            working=false
                            ok_receive.isSelected=false
                            try {
                                textView9.setText("Current Database Will \n Be Cleared , Proceed?")
                                sender_ip.setText("")
                                receive_steps.setText("")
                                received_status.setText("Failed")
                                connect_status.setText("Disconnecteded")

                            }catch (e:Exception){
                                println(e)
                            }

                            UdpListener8089.resume()





                        }

                    }


                }

                cancel_delete.setOnClickListener {

                    popupWindow2.dismiss()
                }


            }else{
                Toast.makeText(requireContext(), "Connect to a Griffin Network", Toast.LENGTH_SHORT).show()
            }










        }


    }

    override fun onPause() {
        super.onPause()
        try {
            println("sssssssssssssssssssssssssssssssssssssssssssssdd")
            socket?.close()
            socket=null
            working=false
            val ok_receive = requireView().findViewById<Button>(R.id.ok_receive)
            val cance_receive = requireView().findViewById<Button>(R.id.cance_receive)
            val sender_ip = requireView()!!.findViewById<TextView>(R.id.sender_ip)
            val receive_steps = requireView().findViewById<TextView>(R.id.receive_steps)
            val received_status = requireView()!!.findViewById<TextView>(R.id.received_status)
            val connect_status = requireView().findViewById<TextView>(R.id.connect_status)
            val textView9 = requireView()!!.findViewById<TextView>(R.id.textView9)
            ok_receive.isSelected=false
            try {

                textView9.setText("Current Database Will \n Be Cleared , Proceed?")
                sender_ip.setText("")
                receive_steps.setText("")
                received_status.setText("Canceled")
                connect_status.setText("Disconnecteded")
            }catch (e:Exception){
                println(e)
            }
        }catch (e:Exception){
            println(e)
        }

        UdpListener8089.resume()
    }

    override fun onStop() {
        super.onStop()
        try {
            println("sssssssssssssssssssssssssssssssssssssssssssssdd")
            socket?.close()
            socket=null
            working=false
            val ok_receive = requireView().findViewById<Button>(R.id.ok_receive)
            val cance_receive = requireView().findViewById<Button>(R.id.cance_receive)
            val sender_ip = requireView()!!.findViewById<TextView>(R.id.sender_ip)
            val receive_steps = requireView().findViewById<TextView>(R.id.receive_steps)
            val received_status = requireView()!!.findViewById<TextView>(R.id.received_status)
            val connect_status = requireView().findViewById<TextView>(R.id.connect_status)
            val textView9 = requireView()!!.findViewById<TextView>(R.id.textView9)
            ok_receive.isSelected=false
            try {

                textView9.setText("Current Database Will \n Be Cleared , Proceed?")
                sender_ip.setText("")
                receive_steps.setText("")
                received_status.setText("Canceled")
                connect_status.setText("Disconnecteded")
            }catch (e:Exception){
                println(e)
            }
        }catch (e:Exception){
            println(e)
        }

        UdpListener8089.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            println("sssssssssssssssssssssssssssssssssssssssssssssdd")
            socket?.close()
            socket=null
            working=false
            val ok_receive = requireView().findViewById<Button>(R.id.ok_receive)
            val cance_receive = requireView().findViewById<Button>(R.id.cance_receive)
            val sender_ip = requireView()!!.findViewById<TextView>(R.id.sender_ip)
            val receive_steps = requireView().findViewById<TextView>(R.id.receive_steps)
            val received_status = requireView()!!.findViewById<TextView>(R.id.received_status)
            val connect_status = requireView().findViewById<TextView>(R.id.connect_status)
            val textView9 = requireView()!!.findViewById<TextView>(R.id.textView9)
            ok_receive.isSelected=false
            try {

                textView9.setText("Current Database Will \n Be Cleared , Proceed?")
                sender_ip.setText("")
                receive_steps.setText("")
                received_status.setText("Canceled")
                connect_status.setText("Disconnecteded")
            }catch (e:Exception){
                println(e)
            }
        }catch (e:Exception){
            println(e)
        }

        UdpListener8089.resume()
    }
}