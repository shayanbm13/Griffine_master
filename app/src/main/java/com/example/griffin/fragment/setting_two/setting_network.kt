package com.example.griffin.fragment.setting_two

import android.content.Context
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.griffin.R
import com.example.griffin.fragment.setting_four.setting_network_manual
import com.example.griffin.setting
import androidx.fragment.app.viewModels
import com.example.griffin.database.Ir_db
import com.example.griffin.database.Master_slave_db
import com.example.griffin.database.setting_network_db
import com.example.griffin.database.six_workert_db
import com.example.griffin.mudels.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.net.SocketTimeoutException

class setting_network : Fragment() {

    private  val SharedViewModel: SharedViewModel by activityViewModels()

     
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MediaPlayer.create(context, R.raw.zapsplat_multimedia_button_click_bright_003_92100)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting_network, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val network_manual = view.findViewById<Button>(R.id.network_manual)
        val network_get_ssid: Button =  view.findViewById(R.id.network_get_ssid)
        val network_send_modem_seting: Button =  view.findViewById(R.id.network_send_modem_seting)




         
        val chooser_btns_list = listOf(network_manual)




//
//
//
//

        val master_slave_db_handler = Master_slave_db.getInstance(requireContext())
        network_manual.setOnClickListener {
            SoundManager.playSound()

            if (master_slave_db_handler.getStatusById(1) == "master"){
                network_manual.setBackgroundResource(R.drawable.slave_btn1)
                master_slave_db_handler.updateStatusbyId(1 , "slave")

            }else if(master_slave_db_handler.getStatusById(1) == "slave") {
                network_manual.setBackgroundResource(R.drawable.master_btn1)
                master_slave_db_handler.updateStatusbyId(1 , "master")
            }

            val setting_activity = requireActivity() as setting
            setting_activity.changeViewPagerPage_three(1)
            setting_activity.changeViewPagerPage_four(1)


        }

        network_get_ssid.setOnClickListener {
            SoundManager.playSound()
            try {
                val wifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager


                if (wifiManager.isWifiEnabled) {

                    val wifiInfo = wifiManager.connectionInfo
                    val ssid = wifiInfo.ssid
                    if (ssid != "Griffin"){
                        SharedViewModel.update_shared_ssid(ssid)

                    }else{
                        Toast.makeText(requireContext(), "Wrong ssid connected", Toast.LENGTH_SHORT).show()

                    }

                }
            }catch (e:Exception){
                println("network_get_ssid - setting_network")
                println(e)
            }




        }

        network_send_modem_seting.setOnClickListener {
            UdpListener8089.pause()
            try {
                SoundManager.playSound()
                val wifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager


                if (wifiManager.isWifiEnabled) {

                    println("enable")
                    val wifiInfo = wifiManager.connectionInfo
                    var ssid = wifiInfo.ssid
                    ssid= ssid.substring(1,(ssid.length)-1)
                    println(ssid)
                    if (ssid == "Griffin"){
                        println("griffin")

                        val network_db= setting_network_db.getInstance(requireContext())
                        val network=network_db.get_from_db_network_manual(1)

                        var myip= checkIP(requireContext())
                        Thread {

                            send_modem_setting_old1(network!!.modem_ssid)
                            Thread.sleep(850)
                            send_modem_setting_old2(network!!.modem_password)


                        }.start()



                    }else if (ssid == "Griffin_Plus"){

                        val network_db=setting_network_db.getInstance(requireContext())
                        val network= try {
                            network_db.get_from_db_network_manual(1)

                        }catch (e:Exception){
                            null
                        }
                        if (network!=null){
                            var myip= checkIP(requireContext())






                            Thread{
                                send_modem_setting(network!!.modem_ssid,network.modem_password,myip)

//                                requireActivity().runOnUiThread{
//
//                                    Toast.makeText(requireContext(), "ssid sent ", Toast.LENGTH_SHORT).show()
//
//                                }
                                UdpListener8089.pause()
                                receiveUdpMessage({ receivedMessage ->
                                    if (receivedMessage=="failed") {
                                        println("broadcast failed..")
                                    }else{

                                        println(receivedMessage)
                                        requireActivity().runOnUiThread {
                                            var receivedmessage_decoded = extract_response(receivedMessage)
                                            var macip = receivedmessage_decoded[0]

                                            var type = receivedmessage_decoded[1]
                                            var pole_num = receivedmessage_decoded[2]
                                            requireActivity().runOnUiThread{

                                                Toast.makeText(requireContext(), macip, Toast.LENGTH_SHORT)
                                                    .show()
                                                Toast.makeText(requireContext(), type, Toast.LENGTH_SHORT)
                                                    .show()
                                                Toast.makeText(requireContext(), pole_num, Toast.LENGTH_SHORT)
                                                    .show()

                                            }
                                            println(type)
                                            println(pole_num)

                                            if (type == "Elev") {


                                                val elevator_database = com.example.griffin.database.Elevator_db.getInstance(requireContext())
                                                val elevator = Elevator()
                                                elevator.mac = macip





                                                elevator_database.deleteRowsWithNullOrEmptySubTtype()
                                                elevator_database.set_to_db_Elevator(elevator)




                                                requireActivity().runOnUiThread {

                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Learned.. ",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }


                                            }else if (type == "SixC"){
                                                val six_workert_db=six_workert_db.getInstance(requireContext())
                                                six_workert_db.deleteRowsWithNullOrNewLocalName()
                                                val added_six_worker =six_workert()
                                                val six_worker_count = six_workert_db.getAllsix_workerts().count()
                                                added_six_worker.name="new Local " + ((six_worker_count)+1)
                                                added_six_worker.pole_num=pole_num.toInt().toString()

                                                added_six_worker.sub_type=",,,,,"
                                                added_six_worker.type=",,,,,"
                                                added_six_worker.status=",,,,,"
                                                added_six_worker.ip="192.168.1.1"
                                                added_six_worker.mac=macip

                                                added_six_worker.work_name=",,,,,"

                                                six_workert_db.set_to_db_six_workert(added_six_worker)
                                                SharedViewModel.update_six_worker_list(six_workert_db.getAllsix_workerts())

                                                requireActivity().runOnUiThread {

                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Learned.. ",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }


                                        }

                                    }


                                    println("Received: $receivedMessage")
                                }, 8089, 1000)
                                UdpListener8089.resume()




                            }.start()






//                            Toast.makeText(
//                                requireContext(),
//                                "Learned.. ",
//                                Toast.LENGTH_SHORT
//                            ).show()


                        }else{
                            Toast.makeText(requireContext(), "please enter your SSID and Password in setting", Toast.LENGTH_SHORT).show()

                        }




                    }else if (ssid == "Griffin_V3") {

                        try {

                            println("1111111111111111111111111")

                            Thread{
                                try {
                                    UdpListener8089.pause()
                                    // اتصال به سرور
                                    val ip ="192.168.4.1"
                                    val port = 80
                                    val network_db=setting_network_db.getInstance(requireContext())
                                    val network=network_db.get_from_db_network_manual(1)
                                    var step = 0

                                    var myip= checkIP(requireContext())
                                    println("22222222222222")


                                    val socket = Socket(ip, port)
                                    println("Connected to server.")

                                    val timeout = 5000 // 5 ثانیه زمان انتظار برای دریافت پاسخ
                                    val retryDelay = 1000L // 1 ثانیه تأخیر برای تلاش مجدد

                                    try {
                                        // تنظیم timeout برای انتظار دریافت پاسخ
                                        socket.soTimeout = timeout

                                        // ارسال پیام به سرور
                                        val output = OutputStreamWriter(socket.getOutputStream())
                                        val message = "ssid:${network!!.modem_ssid}~>pswd:${network!!.modem_password}~>$myip" // پیام خود را وارد کن
                                        Thread.sleep(300)
                                        output.write("$message\n")
                                        output.flush()
                                        println("Message sent: $message")

                                        // گوش دادن برای دریافت پاسخ
                                        val input = BufferedReader(InputStreamReader(socket.getInputStream()))
                                        val response = input.readLine()
                                        println(response)

                                        if (response != null) {
                                            println("Received response: $response")
                                            socket.close()

                                            //  main code

                                            var receivedmessage_decoded = extract_response(response)
                                            var macip = receivedmessage_decoded[0]

                                            var type = receivedmessage_decoded[1]
                                            var pole_num = receivedmessage_decoded[2]
                                            requireActivity().runOnUiThread{

//                                                Toast.makeText(requireContext(), macip, Toast.LENGTH_SHORT)
//                                                    .show()
//                                                Toast.makeText(requireContext(), type, Toast.LENGTH_SHORT)
//                                                    .show()
//                                                Toast.makeText(requireContext(), pole_num, Toast.LENGTH_SHORT)
//                                                    .show()

                                            }
                                            println(type)
                                            println(pole_num)

                                            if (type == "Elev") {


                                                val elevator_database = com.example.griffin.database.Elevator_db.getInstance(requireContext())
                                                val elevator = Elevator()
                                                elevator.mac = macip





                                                elevator_database.deleteRowsWithNullOrEmptySubTtype()
                                                elevator_database.set_to_db_Elevator(elevator)



                                                requireActivity().runOnUiThread{

                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Learned.. ",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }



                                            }else if (type == "SixC") {
                                                val six_workert_db =
                                                    six_workert_db.getInstance(requireContext())
                                                six_workert_db.deleteRowsWithNullOrNewLocalName()
                                                val added_six_worker = six_workert()
                                                val six_worker_count =
                                                    six_workert_db.getAllsix_workerts().count()
                                                added_six_worker.name =
                                                    "new Local " + ((six_worker_count) + 1)
                                                added_six_worker.pole_num =
                                                    pole_num.toInt().toString()

                                                added_six_worker.sub_type = ",,,,,"
                                                added_six_worker.type = ",,,,,"
                                                added_six_worker.status = ",,,,,"
                                                added_six_worker.ip = "192.168.1.1"
                                                added_six_worker.mac = macip

                                                added_six_worker.work_name = ",,,,,"

                                                six_workert_db.set_to_db_six_workert(
                                                    added_six_worker
                                                )
                                                requireActivity().runOnUiThread {
                                                    SharedViewModel.update_six_worker_list(
                                                        six_workert_db.getAllsix_workerts()
                                                    )

                                                    Toast.makeText(
                                                        requireContext(),
                                                        "Learned.. ",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }else if (type == "Irce"){




                                                val ir_database= Ir_db.getInstance(requireContext())
                                                val ir_count = ir_database.getAllIrs().count()
                                                val ir = IR()
                                                ir.name="Ir Center : ${ir_count+1}"
                                                ir.mac= macip


                                                ir_database.set_to_db_Ir(ir)

                                                requireActivity().runOnUiThread{
                                                    Toast.makeText(requireContext(), "Learned", Toast.LENGTH_LONG).show()


                                                }
                                            }

                                            UdpListener8089.resume()


                                        }else{
                                            requireActivity().runOnUiThread {
                                                UdpListener8089.resume()
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Failed.. ",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }

                                        }

                                    } catch (e: SocketTimeoutException) {
                                        println("Timeout reached, no response received for message.")
                                        requireActivity().runOnUiThread{

                                            Toast.makeText(requireContext(), "Failed.. ", Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        requireActivity().runOnUiThread{

                                            Toast.makeText(requireContext(), "Failed.. ", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    // بستن ارتباط
                                    socket.close()
                                    UdpListener8089.resume()
                                }catch (e:Exception){
                                    println(e)
                                    UdpListener8089.resume()
                                }

                            }.start()




                        } catch (e: Exception) {
                            e.printStackTrace()
                            println(e)
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                            UdpListener8089.resume()
                        }




                    }

                }
            }catch (e:Exception){
                println(  println("network_send_modem_seting - setting_network"))
                println(e)

            }








            UdpListener8089.resume()
        }








    }

    override fun onResume() {
        super.onResume()

        val master_slave_db_handler = Master_slave_db.getInstance(requireContext())
        val network_manual = requireView().findViewById<Button>(R.id.network_manual)
        if (master_slave_db_handler.getStatusById(1) == "master"){
            network_manual.setBackgroundResource(R.drawable.master_btn1)

        }else if (master_slave_db_handler.getStatusById(1) == "slave"){
            network_manual.setBackgroundResource(R.drawable.slave_btn1)

        }




    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }
    override fun onPause() {
        super.onPause()
         
    }
}