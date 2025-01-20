package com.example.griffin.fragment.griffin_home_frags

import android.app.AlertDialog
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.adapters.LearnThermostatAdadpter
import com.example.griffin.adapters.changeroom_adapter
import com.example.griffin.database.*
import com.example.griffin.griffin_home
import com.example.griffin.mudels.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.net.SocketTimeoutException


class temp_learn : Fragment() {
    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    fun rename_item_in_scenario(e_d:String , old_item_name:String,new_item_name:String,type:String){
        var scenario_db_handler = scenario_db.Scenario_db.getInstance(requireContext())
        val favorite_db_handler = favorite_db.Favorite_db.getInstance(requireContext())
        val all_favorite = favorite_db_handler.getAllFavorite()

        var all_scenario = scenario_db_handler.getAllScenario()
        if (e_d=="e"){
            if (type=="light"){
                for (scenario in all_scenario){
                    println(scenario.light)
                    println(old_item_name)
                    if (scenario.light!!.contains(old_item_name) ){
                        println("bood")
                        println(scenario.light)
                        scenario.light=scenario.light!!.replace(old_item_name,new_item_name)
                        println(scenario.id)
                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){

                    if (favorite.name==old_item_name && favorite.type=="light"){
                        favorite.name=new_item_name
                        favorite_db_handler.updateFavoriteNameById(favorite.id,favorite.name)

                    }
                }


            }else if (type=="curtain"){
                for (scenario in all_scenario){
                    if (scenario.curtain!!.contains(old_item_name) ){
                        scenario.curtain=scenario.curtain!!.replace(old_item_name,new_item_name)
                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){

                    if (favorite.name==old_item_name && favorite.type=="curtain"){
                        favorite.name=new_item_name
                        favorite_db_handler.updateFavoriteNameById(favorite.id,favorite.name)

                    }
                }

            }else if (type=="fan"){
                for (scenario in all_scenario){
                    if (scenario.fan!!.contains(old_item_name) ){
                        scenario.fan=scenario.fan!!.replace(old_item_name,new_item_name)
                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){



                    if (favorite.name==old_item_name && favorite.type=="fan"){
                        favorite.name=new_item_name
                        favorite_db_handler.updateFavoriteNameById(favorite.id,favorite.name)

                    }
                }

            }else if (type=="plug"){
                for (scenario in all_scenario){
                    if (scenario.plug!!.contains(old_item_name) ){
                        scenario.plug=scenario.plug!!.replace(old_item_name,new_item_name)
                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }

            }else if (type=="temp"){
                for (scenario in all_scenario){
                    if (scenario.thermostat!!.contains(old_item_name) ){
                        scenario.thermostat=scenario.thermostat!!.replace(old_item_name,new_item_name)
                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){

                    if (favorite.name==old_item_name && favorite.type=="thermostat"){
                        favorite.name=new_item_name
                        favorite_db_handler.updateFavoriteNameById(favorite.id,favorite.name)

                    }
                }

            }else if (type=="valve"){
                for (scenario in all_scenario){
                    if (scenario.valve!!.contains(old_item_name) ){
                        scenario.valve=scenario.valve!!.replace(old_item_name,new_item_name)
                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){

                    if (favorite.name==old_item_name && favorite.type=="valve"){
                        favorite.name=new_item_name
                        favorite_db_handler.updateFavoriteNameById(favorite.id,favorite.name)

                    }
                }

            }

        }else{
            if (type=="light"){
                for (scenario in all_scenario){
                    if (scenario.light!!.contains(old_item_name) ){
                        var my_list = scenario.light!!.split(",").toMutableList()
                        for (item in my_list){
                            if (item.contains(old_item_name)){
                                val my_index = my_list.indexOf(item)
                                my_list.removeAt(my_index)
                                break


                            }

                        }

                        scenario.light=my_list.joinToString(",")

                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){

                    if (favorite.name==old_item_name && favorite.type=="light"){
                        favorite.name=new_item_name
                        favorite_db_handler.delete_from_db_Favorite(favorite.id)

                    }
                }
            }else if (type=="curtain"){
                for (scenario in all_scenario){
                    if (scenario.curtain!!.contains(old_item_name) ){
                        var my_list = scenario.curtain!!.split(",").toMutableList()
                        for (item in my_list){
                            if (item.contains(old_item_name)){
                                val my_index = my_list.indexOf(item)
                                my_list.removeAt(my_index)
                                break


                            }

                        }

                        scenario.curtain=my_list.joinToString(",")

                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){

                    if (favorite.name==old_item_name && favorite.type=="curtain"){
                        favorite.name=new_item_name
                        favorite_db_handler.delete_from_db_Favorite(favorite.id)

                    }
                }
            }else if (type=="fan"){
                for (scenario in all_scenario){
                    if (scenario.fan!!.contains(old_item_name) ){
                        var my_list = scenario.fan!!.split(",").toMutableList()
                        for (item in my_list){
                            if (item.contains(old_item_name)){
                                val my_index = my_list.indexOf(item)
                                my_list.removeAt(my_index)
                                break


                            }

                        }

                        scenario.fan=my_list.joinToString(",")

                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){

                    if (favorite.name==old_item_name && favorite.type=="fan"){
                        favorite.name=new_item_name
                        favorite_db_handler.delete_from_db_Favorite(favorite.id)

                    }
                }
            }else if (type=="plug"){
                for (scenario in all_scenario){
                    if (scenario.plug!!.contains(old_item_name) ){
                        var my_list = scenario.plug!!.split(",").toMutableList()
                        for (item in my_list){
                            if (item.contains(old_item_name)){
                                val my_index = my_list.indexOf(item)
                                my_list.removeAt(my_index)
                                break


                            }

                        }

                        scenario.plug=my_list.joinToString(",")

                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){

                    if (favorite.name==old_item_name && favorite.type=="plug"){
                        favorite.name=new_item_name
                        favorite_db_handler.delete_from_db_Favorite(favorite.id)

                    }
                }
            }else if (type=="temp"){
                for (scenario in all_scenario){
                    if (scenario.thermostat!!.contains(old_item_name) ){
                        var my_list = scenario.thermostat!!.split(",").toMutableList()
                        for (item in my_list){
                            if (item.contains(old_item_name)){
                                val my_index = my_list.indexOf(item)
                                my_list.removeAt(my_index)
                                break


                            }

                        }

                        scenario.thermostat=my_list.joinToString(",")

                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){

                    if (favorite.name==old_item_name && favorite.type=="thermostat"){
                        favorite.name=new_item_name
                        favorite_db_handler.delete_from_db_Favorite(favorite.id)

                    }
                }
            }else if (type=="valve"){
                for (scenario in all_scenario){
                    if (scenario.valve!!.contains(old_item_name) ){
                        var my_list = scenario.valve!!.split(",").toMutableList()
                        for (item in my_list){
                            if (item.contains(old_item_name)){
                                val my_index = my_list.indexOf(item)
                                my_list.removeAt(my_index)
                                break


                            }

                        }

                        scenario.valve=my_list.joinToString(",")

                        scenario_db_handler.updateScenarioById(scenario.id,scenario)
                    }
                }
                for (favorite in all_favorite){

                    if (favorite.name==old_item_name && favorite.type=="thermostat"){
                        favorite.name=new_item_name
                        favorite_db_handler.delete_from_db_Favorite(favorite.id)

                    }
                }
            }


        }



    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temperature_learn, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val addlightitem_menu = view.findViewById<ImageButton>(R.id.addthermostat_menu)
        val recyclerView: RecyclerView = view.findViewById(R.id.themp_learn_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager
        val database= Temperature_db.getInstance(requireContext())


        var favorite_db_handeler= favorite_db.Favorite_db.getInstance(requireContext())


        val inflater4 = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customPopupView4: View = inflater4.inflate(R.layout.popup_change_room, null)
        var recyclerView2 = customPopupView4.findViewById<RecyclerView>(R.id.rooms_names_recycler)



        val room_db_handler =  rooms_db.getInstance(requireContext())
        var rooms= room_db_handler.getAllRooms()
        var rooms_name = arrayListOf<String?>()
        for (room in rooms){
            rooms_name.add(room?.room_name)

        }
        rooms_name.add("Favorite")


        val popupWidth4 = 450
        val popupHeight4 = 500
        val popupWindow4 = PopupWindow(customPopupView4, popupWidth4, popupHeight4, true)
        popupWindow4.isFocusable = true

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->
            val database1= Temperature_db.getInstance(requireContext())
            println(room!!.room_name)
            database1.deleteRowsWithNullOrEmptySubTtype()
//            println(database.getThermostatsByRoomName(room!!.room_name))
            SharedViewModel.update_temp_to_learn_list( database.getThermostatsByRoomName(room!!.room_name))
//            println(database.getThermostatsByRoomName(room!!.room_name))


            SharedViewModel.temp_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->



                val adapter = LearnThermostatAdadpter(newlist) { selectedItem ->

                    val inflater = LayoutInflater.from(requireContext())

                    val customPopupView: View = inflater.inflate(R.layout.popup_addlight, null)
                    val popupView: View = inflater.inflate(R.layout.popup_addlight, null)



                    val edit_light_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
                    val ok_name_light_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
                    val learn_light_pupop = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
                    val delete_light_pupop = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
                    val on_off_test_light_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)
                    val send_to_pupop = customPopupView.findViewById<Button>(R.id.send_to)
                    on_off_test_light_pupop.visibility=View.VISIBLE
                    on_off_test_light_pupop.isEnabled=false
                    val popupWidth = 400
                    val popupHeight = 490

                    // ایجاد لایه‌ی کاستوم


                    // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                    val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)

                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setView(popupView)

                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.setCanceledOnTouchOutside(false)




                    edit_light_name_pupop.setText(selectedItem.name.toString())

//            edit_light_name_pupop.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    edit_light_name_pupop.setOnClickListener{

                        edit_light_name_pupop.selectAll()
                    }


                    learn_light_pupop.setOnClickListener {

                        UdpListener8089.pause()


                        val wifiManager = requireContext().applicationContext.getSystemService(
                            Context.WIFI_SERVICE) as WifiManager


                        if (wifiManager.isWifiEnabled) {

                            val wifiInfo = wifiManager.connectionInfo
                            val ssid = wifiInfo.ssid.trim('"')
                            Toast.makeText(requireContext(), ssid, Toast.LENGTH_SHORT).show()
                            if (ssid == "Griffin"){
                                Toast.makeText(requireContext(), "Please Use Send Modem Setting ", Toast.LENGTH_SHORT).show()
                            }else if (ssid == "Griffin_Plus") {
                                try {

                                    val network_db=setting_network_db.getInstance(requireContext())
                                    val network=network_db.get_from_db_network_manual(1)


//                                    Toast.makeText(requireContext(), "ssid sent ", Toast.LENGTH_SHORT).show()




                                    Thread{
                                        UdpListener8089.pause()
                                        var myip= checkIP(requireContext())

                                        send_modem_setting(network!!.modem_ssid,network.modem_password,myip)


                                        receiveUdpMessage({ receivedMessage ->
                                            if (receivedMessage=="failed") {
                                                println("broadcast failed..")
                                                requireActivity().runOnUiThread{
                                                    Toast.makeText(requireContext(), "Failed.. ", Toast.LENGTH_SHORT).show()
                                                }
                                            }else{

                                                println(receivedMessage)
                                                requireActivity().runOnUiThread {
                                                    var receivedmessage_decoded = extract_response(receivedMessage)
                                                    var macip = receivedmessage_decoded[0]
                                                    var type = receivedmessage_decoded[1]
                                                    var pole_num = receivedmessage_decoded[2]

//                                                Toast.makeText(requireContext(), macip, Toast.LENGTH_SHORT)
//                                                    .show()
//                                                Toast.makeText(requireContext(), type, Toast.LENGTH_SHORT)
//                                                    .show()
//                                                Toast.makeText(requireContext(), pole_num, Toast.LENGTH_SHORT)
//                                                    .show()
                                                    println(type)
                                                    println(pole_num)

                                                    if (type == "Tmpr") {


                                                        val temp_database = com.example.griffin.database.Temperature_db.getInstance(requireContext())
                                                        val thermostat = Thermostst()
                                                        thermostat.mac = macip
                                                        thermostat.name = selectedItem.name

                                                        thermostat.fan_status = "0"
                                                        thermostat.mood = "0"
                                                        thermostat.room_name = room!!.room_name
                                                        thermostat.on_off = "0"
                                                        thermostat.subtype = "0000"
                                                        thermostat.temperature = "16"
                                                        temp_database.updatefullThermostatById(database.getThermostatByName(selectedItem.name)!!.id,thermostat)




                                                        requireActivity().runOnUiThread{
                                                            Toast.makeText(requireContext(), "Learned", Toast.LENGTH_LONG).show()


                                                        }


                                                    }


                                                }

                                            }


                                            println("Received: $receivedMessage")
                                        }, 8089, 1000)
                                        UdpListener8089.resume()




                                    }.start()






//                                Toast.makeText(
//                                    requireContext(),
//                                    "Learned.. ",
//                                    Toast.LENGTH_SHORT
//                                ).show()



                                    popupWindow.dismiss()
                                }catch (e:Exception){
                                    println(e)
                                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                                }





                            }else if (ssid == "Griffin_V3") {

                                try {

                                    println("1111111111111111111111111")

                                    if (selectedItem.mac =="13" ){
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
                                                        requireActivity().runOnUiThread {
                                                            var receivedmessage_decoded = extract_response(response)
                                                            var macip = receivedmessage_decoded[0]
                                                            var type = receivedmessage_decoded[1]
                                                            var pole_num = receivedmessage_decoded[2]

//                                                Toast.makeText(requireContext(), macip, Toast.LENGTH_SHORT)
//                                                    .show()
//                                                Toast.makeText(requireContext(), type, Toast.LENGTH_SHORT)
//                                                    .show()
//                                                Toast.makeText(requireContext(), pole_num, Toast.LENGTH_SHORT)
//                                                    .show()
                                                            println(type)
                                                            println(pole_num)

                                                            if (type == "Tmpr") {


                                                                val temp_database = com.example.griffin.database.Temperature_db.getInstance(requireContext())
                                                                val thermostat = Thermostst()
                                                                thermostat.mac = macip
                                                                thermostat.name = selectedItem.name

                                                                thermostat.fan_status = "0"
                                                                thermostat.mood = "0"
                                                                thermostat.room_name = room!!.room_name
                                                                thermostat.on_off = "0"
                                                                thermostat.subtype = "0000"
                                                                thermostat.temperature = "16"
                                                                temp_database.updatefullThermostatById(database.getThermostatByName(selectedItem.name)!!.id,thermostat)




                                                                requireActivity().runOnUiThread{
                                                                    Toast.makeText(requireContext(), "Learned", Toast.LENGTH_LONG).show()


                                                                }


                                                            }else if (type == "Irce"){

                                                                val temp_database = com.example.griffin.database.Temperature_db.getInstance(requireContext())
                                                                val thermostat = Thermostst()
                                                                thermostat.mac = macip
                                                                thermostat.name = selectedItem.name

                                                                thermostat.fan_status = "0"
                                                                thermostat.mood = "0"
                                                                thermostat.room_name = room!!.room_name
                                                                thermostat.on_off = "0"
                                                                thermostat.subtype = "0000"
                                                                thermostat.temperature = "16"
                                                                temp_database.updatefullThermostatById(database.getThermostatByName(selectedItem.name)!!.id,thermostat)






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


                                    }else{
                                        requireActivity().runOnUiThread{

                                            Toast.makeText(
                                                requireContext(),
                                                "try a empty one",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }
                                    }



                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    println(e)
                                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                                    UdpListener8089.resume()
                                }




                            }else{


                                try {
                                    UdpListener8089.pause()
                                    Thread{
                                        val network_db=setting_network_db.getInstance(requireContext())
                                        val network=network_db.get_from_db_network_manual(1)
                                        if (ssid ==network?.modem_ssid ){

                                            var myip= checkIP(requireContext())

                                            get_mac_temp(requireContext(),myip)
                                            receiveUdpMessage({ receivedMessage ->
                                                if (receivedMessage=="failed") {
                                                    println("broadcast failed..")
                                                    requireActivity().runOnUiThread{
                                                        Toast.makeText(requireContext(), "Failed.. ", Toast.LENGTH_SHORT).show()
                                                    }
                                                }else{
                                                    println(receivedMessage)
                                                    requireActivity().runOnUiThread {
                                                        var receivedmessage_decoded=extract_response(receivedMessage)
                                                        var macip= receivedmessage_decoded[0]
                                                        var type=receivedmessage_decoded[4]
                                                        var pole_num=receivedmessage_decoded[1]
                                                        var ip=receivedmessage_decoded[3]

//                                                Toast.makeText(requireContext(), macip, Toast.LENGTH_SHORT).show()
//                                                Toast.makeText(requireContext(), type, Toast.LENGTH_SHORT).show()
//                                                Toast.makeText(requireContext(), pole_num, Toast.LENGTH_SHORT).show()
                                                        println(macip)
                                                        println(type)
                                                        println(pole_num)
                                                        println(ip)
//                                                if (((pole_num[2].toString()+pole_num[3].toString()).toString().toInt()) < 9){
//                                                    val new_num=(pole_num.toInt())+1
//                                                    pole_num="000$new_num"
//
//                                                }else if (((pole_num[2].toString()+pole_num[3].toString()).toString().toInt()) == 9){
//
//
//                                                    pole_num="0010"
//
//                                                }else{
//                                                    val new_num=(pole_num.toInt())+1
//                                                    pole_num="00$new_num"
//
//                                                }

                                                        if (type=="Tmpr"){


                                                            val updated_plug=Thermostst()
                                                            updated_plug.ip=ip
                                                            updated_plug.mac=macip
                                                            updated_plug.room_name=selectedItem.room_name

                                                            updated_plug.fan_status = "0"
                                                            updated_plug.mood = "0"
                                                            updated_plug.room_name = room!!.room_name
                                                            updated_plug.on_off = "0"
                                                            updated_plug.subtype = "0000"
                                                            updated_plug.temperature = "16"


                                                            updated_plug.subtype=pole_num


                                                            updated_plug.name="${selectedItem.name}"
                                                            database.updatefullThermostatById(selectedItem!!.id,updated_plug)

                                                            requireActivity().runOnUiThread{
                                                                Toast.makeText(requireContext(), "Learned", Toast.LENGTH_LONG).show()


                                                            }



                                                        }





                                                    }

                                                }


                                                println("Received: $receivedMessage")
                                            }, 8089, 1300)
                                            UdpListener8089.resume()
                                            requireActivity().runOnUiThread{





                                                popupWindow.dismiss()

                                            }




                                        }else{
                                            println("Connect to a Griffin Network")
                                        }



                                    }.start()


                                }catch (e:Exception){
                                    println(e)
                                    Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                                }


                            }
//                            else{
//                                println(database.getThermostatByName(selectedItem.name)!!.id)
//                                val temp_database = com.example.griffin.database.Temperature_db.getInstance(requireContext())
//                                val thermostat = Thermostst()
//                                thermostat.mac = "121354684443543"
//                                thermostat.name = selectedItem.name
//
//                                thermostat.fan_status = "0"
//                                thermostat.mood = "0"
//                                thermostat.room_name = room!!.room_name
//                                thermostat.on_off = "0"
//                                thermostat.subtype = "0000"
//                                thermostat.temperature = "16"
//                                temp_database.updatefullThermostatById(database.getThermostatByName(selectedItem.name)!!.id,thermostat)
//                                Toast.makeText(
//                                    requireContext(),
//                                    "Learned.. ",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }


                        }

//                        if ()

                        UdpListener8089.resume()
                    }

                    send_to_pupop.setOnClickListener {
                        popupWindow.dismiss()
                        popupWindow4.showAtLocation(view, Gravity.CENTER, 0, 0)

                        var adapter2 = changeroom_adapter(rooms_name){ newroomName ->
                            if (newroomName=="Favorite"){

                                val all_favorite=favorite_db_handeler.getAllFavorite()
                                var favorite_names= arrayListOf<String?>()
                                for (favorite in all_favorite){
                                    favorite_names.add(favorite.name)
                                }
                                if (selectedItem.name !in favorite_names){

                                    var favorite_item= Favorite()
                                    favorite_item.type="thermostat"
                                    favorite_item.name=selectedItem.name
                                    favorite_db_handeler.set_to_db_Favorite(favorite_item)
                                    Toast.makeText(requireContext(), "sent to ${newroomName}", Toast.LENGTH_SHORT).show()
                                    popupWindow4.dismiss()
                                }else{
                                    popupWindow4.dismiss()
                                    Toast.makeText(requireContext(), "${selectedItem.name} exist in Favorite", Toast.LENGTH_SHORT).show()

                                }




                            }else{
                                Toast.makeText(requireContext(), "sent to ${newroomName}", Toast.LENGTH_SHORT).show()



                                selectedItem.room_name=newroomName

                                database.updatefullThermostatById(selectedItem.id,selectedItem)
                                popupWindow4.dismiss()
                                SharedViewModel.update_temp_to_learn_list( database.getThermostatsByRoomName(room!!.room_name))
                            }




                        }
                        recyclerView2.adapter = adapter2
                        recyclerView2.layoutManager = LinearLayoutManager(requireContext())


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

                    customPopupView2.findViewById<TextView>(R.id.text_msg).setText("Are you sure you want to delete ?")



                    val old_name = edit_light_name_pupop.text.toString()
                    delete_light_pupop.setOnClickListener {
                        popupWindow.dismiss()
                        popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                        cancel_delete.setOnClickListener {

                            popupWindow2.dismiss()
                        }
                        yes_delete.setOnClickListener {

                            rename_item_in_scenario("d",old_name,old_name,"temp")
                            database.delete_from_db_Temprature(selectedItem.id)
//                        val alllight=database.getAllLights()
                            SharedViewModel.update_temp_to_learn_list(database.getThermostatsByRoomName(room!!.room_name))
                            if (database.getThermostatsByRoomName(room!!.room_name).count() == 0){
                                val activity =requireActivity() as griffin_home
                                activity.viewPager.currentItem=0

                            }
                            popupWindow.dismiss()
                            popupWindow2.dismiss()

                        }




                    }


                    ok_name_light_pupop.setOnClickListener {
                        if(old_name.trim() != edit_light_name_pupop.text.toString().trim()){



                            val same_counter = database.getNumberOfItemsByLname(edit_light_name_pupop.text.toString().trim()).toInt()
                            if ( same_counter!= 0 ){

                                rename_item_in_scenario("d",old_name,edit_light_name_pupop.text.toString().trim(),"temp")
                                database.updateNameById(selectedItem.id,edit_light_name_pupop.text.toString().trim()+ " (${same_counter +1})")

                            }else{
                                rename_item_in_scenario("d",old_name,edit_light_name_pupop.text.toString().trim(),"temp")
                                database.updateNameById(selectedItem.id,edit_light_name_pupop.text.toString().trim())
                            }


                            SharedViewModel.update_temp_to_learn_list(database.getAllThermostats())
                        }

                        popupWindow.dismiss()

                    }
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)


                }

                recyclerView.adapter = adapter
                adapter.setItems(newlist)
            })






        })

        addlightitem_menu.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), addlightitem_menu)
            popupMenu.gravity = Gravity.TOP or Gravity.END
            popupMenu.menuInflater.inflate(R.menu.add_thermostat_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menuItem ->

                when (menuItem.itemId) {
                    R.id.add_thermostat -> {
                        val activity = requireActivity() as griffin_home
                        activity.viewPager.currentItem=4
// نمایش پاپ‌آپ

//                        hideNavigationBar()
                    }
                }
                true
            }
            popupMenu.show()
        }
    }


}