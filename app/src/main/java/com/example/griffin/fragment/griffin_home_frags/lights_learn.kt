package com.example.griffin.fragment.griffin_home_frags

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.griffin.R
import com.example.griffin.adapters.LearnLightAdapter
import com.example.griffin.adapters.changeroom_adapter
import com.example.griffin.database.*
import com.example.griffin.griffin_home
import com.example.griffin.mudels.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket
import java.net.SocketTimeoutException

class lights_learn : Fragment() {
    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lights_learn, container, false)
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
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val addlightitem_menu = view.findViewById<ImageButton>(R.id.addlightitem_menu)
        val recyclerView: RecyclerView = view.findViewById(R.id.light_learn_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager
        val database=light_db.getInstance(requireContext())



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
            println(room!!.room_name)

            val learn_light_db=light_db.getInstance(requireContext())
            learn_light_db.deleteRowsWithNullOrEmptySubTtype()
            SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(room!!.room_name))

            SharedViewModel.light_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->





                val adapter = LearnLightAdapter(newlist) { selectedItem ->

                    val inflater = LayoutInflater.from(requireContext())

                    val customPopupView: View = inflater.inflate(R.layout.popup_addlight, null)
                    val popupView: View = inflater.inflate(R.layout.popup_addlight, null)



                    val edit_light_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
                    val ok_name_light_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
                    val learn_light_pupop = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
                    val delete_light_pupop = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
                    val on_off_test_light_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)
                    val send_to_pupop = customPopupView.findViewById<Button>(R.id.send_to)

                    val popupWidth = 400
                    val popupHeight = 490

                    // ایجاد لایه‌ی کاستوم


                    // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                    val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)

                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setView(popupView)

                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.setCanceledOnTouchOutside(false)


                    on_off_test_light_pupop.isEnabled = selectedItem.mac != "13" && (selectedItem.mac !=null)
                    if (!on_off_test_light_pupop.isEnabled){
                        on_off_test_light_pupop.alpha=0.5f


                    }


                    edit_light_name_pupop.setText(selectedItem.Lname.toString())

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

                                    var myip= checkIP(requireContext())
//                                    UdpListener8089.pause()
//                                    Thread{
//
//                                        UdpListener8089.resume()
//                                    }.start()

//                                    Toast.makeText(requireContext(), "ssid sent ", Toast.LENGTH_SHORT).show()
                                    Thread{
                                        UdpListener8089.pause()


                                        if (selectedItem.mac =="13" ){

                                            send_modem_setting(network!!.modem_ssid,network.modem_password,myip)

                                        }else{
                                            requireActivity().runOnUiThread{

                                                Toast.makeText(
                                                    requireContext(),
                                                    "try a empty one",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            }
                                        }
                                        receiveUdpMessage({ receivedMessage ->
                                            if (receivedMessage=="failed") {
                                                requireActivity().runOnUiThread{
                                                    Toast.makeText(requireContext(), "Failed.. ", Toast.LENGTH_SHORT).show()
                                                }
                                                println("broadcast failed..")
                                            }else{
                                                println(receivedMessage)
                                                requireActivity().runOnUiThread {
                                                    var receivedmessage_decoded=extract_response(receivedMessage)
                                                    var macip= receivedmessage_decoded[0]
                                                    var type=receivedmessage_decoded[1]
                                                    var pole_num=receivedmessage_decoded[2]

//                                                Toast.makeText(requireContext(), macip, Toast.LENGTH_SHORT).show()
//                                                Toast.makeText(requireContext(), type, Toast.LENGTH_SHORT).show()
//                                                Toast.makeText(requireContext(), pole_num, Toast.LENGTH_SHORT).show()
                                                    println(type)
                                                    println(pole_num)

                                                    if (type=="Lght"){


                                                        val same_mac_to_learn= selectedItem.mac?.let { it1 ->
                                                            database.getLightsByMacAddress(
                                                                it1
                                                            )
                                                        }
                                                        val same_mac_count=same_mac_to_learn!!.count()

                                                        if (same_mac_to_learn != null) {
                                                            var counted=0
                                                            for (light in same_mac_to_learn.subList(0,pole_num.toInt()) ){


                                                                val updated_light=Light()
                                                                updated_light.ip=light!!.ip
                                                                updated_light.mac=macip
                                                                updated_light.room_name=selectedItem.room_name
                                                                updated_light.status="off"
                                                                println(counted)
                                                                if (counted<9){

                                                                    updated_light.sub_type="000${counted+1}"
                                                                }else if (counted==9 ){
                                                                    updated_light.sub_type="0010"

                                                                }else if (counted>9 ){
                                                                    updated_light.sub_type="00${counted+1}"

                                                                }

                                                                updated_light.Lname=light.Lname
                                                                database.updateLightById(light!!.id,updated_light)
                                                                counted=counted+1
                                                                println(updated_light.Lname)
                                                                println(updated_light.sub_type)
                                                            }
                                                            requireActivity().runOnUiThread{

                                                                Toast.makeText(requireContext(), "Learned.. ", Toast.LENGTH_SHORT).show()
                                                                val learn_light_db=light_db.getInstance(requireContext())
                                                                SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(room!!.room_name))
                                                                on_off_test_light_pupop.isEnabled = true
                                                                selectedItem.mac=macip
                                                                on_off_test_light_pupop.alpha=1f
                                                                popupWindow.dismiss()
                                                            }




                                                        }



                                                    }else if (type=="Lsix"){


                                                        val same_mac_to_learn= selectedItem.mac?.let { it1 ->
                                                            database.getLightsByMacAddress(
                                                                it1
                                                            )
                                                        }
                                                        val same_mac_count=same_mac_to_learn!!.count()

                                                        if (same_mac_to_learn != null) {
                                                            var counted=0
                                                            for (light in same_mac_to_learn.subList(0,pole_num.toInt()) ){


                                                                val updated_light=Light()
                                                                updated_light.ip=light!!.ip
                                                                updated_light.mac=macip
                                                                updated_light.room_name=selectedItem.room_name
                                                                updated_light.status="off"
                                                                if (counted<9){

                                                                    updated_light.sub_type="000${counted+1}"
                                                                }else if (counted==9 ){
                                                                    updated_light.sub_type="0010"

                                                                }else if (counted>9 ){
                                                                    updated_light.sub_type="00${counted+1}"

                                                                }

                                                                updated_light.Lname=light.Lname
                                                                database.updateLightById(light!!.id,updated_light)
                                                                counted=counted+1
                                                            }
                                                            val six_workert_db= six_workert_db.getInstance(requireContext())
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

                                                            requireActivity().runOnUiThread{
                                                                Toast.makeText(requireContext(), "Learned", Toast.LENGTH_LONG).show()
                                                                val learn_light_db=light_db.getInstance(requireContext())
                                                                SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(room!!.room_name))
                                                                on_off_test_light_pupop.isEnabled = true
                                                                selectedItem.mac=macip
                                                                on_off_test_light_pupop.alpha=1f
                                                                popupWindow.dismiss()
                                                            }




                                                        }



                                                    }else if (type == "Fano") {
                                                        if (pole_num.toInt() == 2){
                                                            val same_mac_to_learn= selectedItem.mac?.let { it1 ->
                                                                database.getLightsByMacAddress(
                                                                    it1
                                                                )
                                                            }
                                                            val same_mac_count=same_mac_to_learn!!.count()

                                                            val updated_light=Light()
                                                            updated_light.ip=same_mac_to_learn[0]!!.ip
                                                            updated_light.mac=macip
                                                            updated_light.room_name=selectedItem.room_name
                                                            updated_light.status="off"
                                                            updated_light.sub_type="0001"
                                                            updated_light.Lname= same_mac_to_learn[0]?.Lname
                                                            database.updateLightById(same_mac_to_learn[0]!!.id,updated_light)

                                                            val database2= fan_db.getInstance(requireContext())
                                                            val updated_fan= fan()
                                                            updated_fan.ip=same_mac_to_learn[1]!!.ip
                                                            updated_fan.mac=macip
                                                            updated_fan.room_name=selectedItem.room_name
                                                            updated_fan.status="0"
                                                            updated_fan.subtype="0002"

                                                            updated_fan.Fname= same_mac_to_learn[1]?.Lname
                                                            database2.set_to_db_fan(updated_fan)


                                                        }else if (pole_num.toInt() == 3){
                                                            val same_mac_to_learn= selectedItem.mac?.let { it1 ->
                                                                database.getLightsByMacAddress(
                                                                    it1
                                                                )
                                                            }
                                                            val same_mac_count=same_mac_to_learn!!.count()



                                                            val updated_light=Light()
                                                            updated_light.ip=same_mac_to_learn[0]!!.ip
                                                            updated_light.mac=macip
                                                            updated_light.room_name=selectedItem.room_name
                                                            updated_light.status="off"
                                                            updated_light.sub_type="0001"
                                                            updated_light.Lname= same_mac_to_learn[0]?.Lname
                                                            database.updateLightById(same_mac_to_learn[0]!!.id,updated_light)

                                                            val updated_light2=Light()
                                                            updated_light2.ip=same_mac_to_learn[1]!!.ip
                                                            updated_light2.mac=macip
                                                            updated_light2.room_name=selectedItem.room_name
                                                            updated_light2.status="off"
                                                            updated_light2.sub_type="0002"
                                                            updated_light2.Lname= same_mac_to_learn[1]?.Lname
                                                            database.updateLightById(same_mac_to_learn[1]!!.id,updated_light2)


                                                            val database2= fan_db.getInstance(requireContext())
                                                            val updated_fan= fan()
                                                            updated_fan.ip=same_mac_to_learn[2]!!.ip
                                                            updated_fan.mac=macip
                                                            updated_fan.room_name=selectedItem.room_name
                                                            updated_fan.status="0"
                                                            updated_fan.subtype="0003"

                                                            updated_fan.Fname= same_mac_to_learn[2]?.Lname
                                                            database2.set_to_db_fan(updated_fan)


                                                        }else if (pole_num.toInt() == 4){
                                                            val same_mac_to_learn= selectedItem.mac?.let { it1 ->
                                                                database.getLightsByMacAddress(
                                                                    it1
                                                                )
                                                            }
                                                            val same_mac_count=same_mac_to_learn!!.count()



                                                            val updated_light=Light()
                                                            updated_light.ip=same_mac_to_learn[0]!!.ip
                                                            updated_light.mac=macip
                                                            updated_light.room_name=selectedItem.room_name
                                                            updated_light.status="off"
                                                            updated_light.sub_type="0001"
                                                            updated_light.Lname= same_mac_to_learn[0]?.Lname
                                                            database.updateLightById(same_mac_to_learn[0]!!.id,updated_light)

                                                            val updated_light2=Light()
                                                            updated_light2.ip=same_mac_to_learn[1]!!.ip
                                                            updated_light2.mac=macip
                                                            updated_light2.room_name=selectedItem.room_name
                                                            updated_light2.status="off"
                                                            updated_light2.sub_type="0002"
                                                            updated_light2.Lname= same_mac_to_learn[1]?.Lname
                                                            database.updateLightById(same_mac_to_learn[1]!!.id,updated_light2)

                                                            val updated_light3=Light()
                                                            updated_light3.ip=same_mac_to_learn[2]!!.ip
                                                            updated_light3.mac=macip
                                                            updated_light3.room_name=selectedItem.room_name
                                                            updated_light3.status="off"
                                                            updated_light3.sub_type="0003"
                                                            updated_light3.Lname= same_mac_to_learn[2]?.Lname
                                                            database.updateLightById(same_mac_to_learn[2]!!.id,updated_light3)


                                                            val database2= fan_db.getInstance(requireContext())
                                                            val updated_fan= fan()
                                                            updated_fan.ip=same_mac_to_learn[3]!!.ip
                                                            updated_fan.mac=macip
                                                            updated_fan.room_name=selectedItem.room_name
                                                            updated_fan.status="0"
                                                            updated_fan.subtype="0004"

                                                            updated_fan.Fname= same_mac_to_learn[3]?.Lname
                                                            database2.set_to_db_fan(updated_fan)


                                                        }


                                                        requireActivity().runOnUiThread{

                                                            Toast.makeText(requireContext(), "Learned.. ", Toast.LENGTH_SHORT).show()
                                                            val learn_light_db=light_db.getInstance(requireContext())
                                                            SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(room!!.room_name))
                                                            on_off_test_light_pupop.isEnabled = true
                                                            selectedItem.mac=macip
                                                            on_off_test_light_pupop.alpha=1f
                                                            popupWindow.dismiss()
                                                        }

                                                    }

                                                }

                                            }


                                            println("Received: $receivedMessage")
                                        }, 8089, 1000)
                                        UdpListener8089.resume()
                                        requireActivity().runOnUiThread{
                                            on_off_test_light_pupop.isEnabled = true
                                            on_off_test_light_pupop.alpha=1f




                                            popupWindow.dismiss()

                                        }



                                    }.start()

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

                                                            var receivedmessage_decoded=extract_response(response)
                                                            var macip= receivedmessage_decoded[0]
                                                            var type=receivedmessage_decoded[1]
                                                            var pole_num=receivedmessage_decoded[2]

//                                                Toast.makeText(requireContext(), macip, Toast.LENGTH_SHORT).show()
//                                                Toast.makeText(requireContext(), type, Toast.LENGTH_SHORT).show()
//                                                Toast.makeText(requireContext(), pole_num, Toast.LENGTH_SHORT).show()
                                                            println(type)
                                                            println(pole_num)

                                                            if (type=="Lght"){


                                                                val same_mac_to_learn= selectedItem.mac?.let { it1 ->
                                                                    database.getLightsByMacAddress(
                                                                        it1
                                                                    )
                                                                }
                                                                val same_mac_count=same_mac_to_learn!!.count()

                                                                if (same_mac_to_learn != null) {
                                                                    var counted=0
                                                                    for (light in same_mac_to_learn.subList(0,pole_num.toInt()) ){


                                                                        val updated_light=Light()
                                                                        updated_light.ip=light!!.ip
                                                                        updated_light.mac=macip
                                                                        updated_light.room_name=selectedItem.room_name
                                                                        updated_light.status="off"
                                                                        println(counted)
                                                                        if (counted<9){

                                                                            updated_light.sub_type="000${counted+1}"
                                                                        }else if (counted==9 ){
                                                                            updated_light.sub_type="0010"

                                                                        }else if (counted>9 ){
                                                                            updated_light.sub_type="00${counted+1}"

                                                                        }

                                                                        updated_light.Lname=light.Lname
                                                                        database.updateLightById(light!!.id,updated_light)
                                                                        counted=counted+1
                                                                        println(updated_light.Lname)
                                                                        println(updated_light.sub_type)
                                                                    }
                                                                    requireActivity().runOnUiThread{

                                                                        Toast.makeText(requireContext(), "Learned.. ", Toast.LENGTH_SHORT).show()
                                                                        val learn_light_db=light_db.getInstance(requireContext())
                                                                        SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(room!!.room_name))
                                                                        on_off_test_light_pupop.isEnabled = true
                                                                        selectedItem.mac=macip
                                                                        on_off_test_light_pupop.alpha=1f
                                                                        popupWindow.dismiss()
                                                                    }




                                                                }



                                                            }else if (type=="Lsix"){


                                                                val same_mac_to_learn= selectedItem.mac?.let { it1 ->
                                                                    database.getLightsByMacAddress(
                                                                        it1
                                                                    )
                                                                }
                                                                val same_mac_count=same_mac_to_learn!!.count()

                                                                if (same_mac_to_learn != null) {
                                                                    var counted=0
                                                                    for (light in same_mac_to_learn.subList(0,pole_num.toInt()) ){


                                                                        val updated_light=Light()
                                                                        updated_light.ip=light!!.ip
                                                                        updated_light.mac=macip
                                                                        updated_light.room_name=selectedItem.room_name
                                                                        updated_light.status="off"
                                                                        if (counted<9){

                                                                            updated_light.sub_type="000${counted+1}"
                                                                        }else if (counted==9 ){
                                                                            updated_light.sub_type="0010"

                                                                        }else if (counted>9 ){
                                                                            updated_light.sub_type="00${counted+1}"

                                                                        }

                                                                        updated_light.Lname=light.Lname
                                                                        database.updateLightById(light!!.id,updated_light)
                                                                        counted=counted+1
                                                                    }
                                                                    val six_workert_db= six_workert_db.getInstance(requireContext())
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

                                                                    requireActivity().runOnUiThread{
                                                                        Toast.makeText(requireContext(), "Learned", Toast.LENGTH_LONG).show()
                                                                        val learn_light_db=light_db.getInstance(requireContext())
                                                                        SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(room!!.room_name))
                                                                        on_off_test_light_pupop.isEnabled = true
                                                                        selectedItem.mac=macip
                                                                        on_off_test_light_pupop.alpha=1f
                                                                        popupWindow.dismiss()
                                                                    }




                                                                }



                                                            }else if (type == "Fano") {
                                                                if (pole_num.toInt() == 2){
                                                                    val same_mac_to_learn= selectedItem.mac?.let { it1 ->
                                                                        database.getLightsByMacAddress(
                                                                            it1
                                                                        )
                                                                    }
                                                                    val same_mac_count=same_mac_to_learn!!.count()

                                                                    val updated_light=Light()
                                                                    updated_light.ip=same_mac_to_learn[0]!!.ip
                                                                    updated_light.mac=macip
                                                                    updated_light.room_name=selectedItem.room_name
                                                                    updated_light.status="off"
                                                                    updated_light.sub_type="0001"
                                                                    updated_light.Lname= same_mac_to_learn[0]?.Lname
                                                                    database.updateLightById(same_mac_to_learn[0]!!.id,updated_light)

                                                                    val database2= fan_db.getInstance(requireContext())
                                                                    val updated_fan= fan()
                                                                    updated_fan.ip=same_mac_to_learn[1]!!.ip
                                                                    updated_fan.mac=macip
                                                                    updated_fan.room_name=selectedItem.room_name
                                                                    updated_fan.status="0"
                                                                    updated_fan.subtype="0002"

                                                                    updated_fan.Fname= same_mac_to_learn[1]?.Lname
                                                                    database2.set_to_db_fan(updated_fan)


                                                                }else if (pole_num.toInt() == 3){
                                                                    val same_mac_to_learn= selectedItem.mac?.let { it1 ->
                                                                        database.getLightsByMacAddress(
                                                                            it1
                                                                        )
                                                                    }
                                                                    val same_mac_count=same_mac_to_learn!!.count()



                                                                    val updated_light=Light()
                                                                    updated_light.ip=same_mac_to_learn[0]!!.ip
                                                                    updated_light.mac=macip
                                                                    updated_light.room_name=selectedItem.room_name
                                                                    updated_light.status="off"
                                                                    updated_light.sub_type="0001"
                                                                    updated_light.Lname= same_mac_to_learn[0]?.Lname
                                                                    database.updateLightById(same_mac_to_learn[0]!!.id,updated_light)

                                                                    val updated_light2=Light()
                                                                    updated_light2.ip=same_mac_to_learn[1]!!.ip
                                                                    updated_light2.mac=macip
                                                                    updated_light2.room_name=selectedItem.room_name
                                                                    updated_light2.status="off"
                                                                    updated_light2.sub_type="0002"
                                                                    updated_light2.Lname= same_mac_to_learn[1]?.Lname
                                                                    database.updateLightById(same_mac_to_learn[1]!!.id,updated_light2)


                                                                    val database2= fan_db.getInstance(requireContext())
                                                                    val updated_fan= fan()
                                                                    updated_fan.ip=same_mac_to_learn[2]!!.ip
                                                                    updated_fan.mac=macip
                                                                    updated_fan.room_name=selectedItem.room_name
                                                                    updated_fan.status="0"
                                                                    updated_fan.subtype="0003"

                                                                    updated_fan.Fname= same_mac_to_learn[2]?.Lname
                                                                    database2.set_to_db_fan(updated_fan)


                                                                }else if (pole_num.toInt() == 4){
                                                                    val same_mac_to_learn= selectedItem.mac?.let { it1 ->
                                                                        database.getLightsByMacAddress(
                                                                            it1
                                                                        )
                                                                    }
                                                                    val same_mac_count=same_mac_to_learn!!.count()



                                                                    val updated_light=Light()
                                                                    updated_light.ip=same_mac_to_learn[0]!!.ip
                                                                    updated_light.mac=macip
                                                                    updated_light.room_name=selectedItem.room_name
                                                                    updated_light.status="off"
                                                                    updated_light.sub_type="0001"
                                                                    updated_light.Lname= same_mac_to_learn[0]?.Lname
                                                                    database.updateLightById(same_mac_to_learn[0]!!.id,updated_light)

                                                                    val updated_light2=Light()
                                                                    updated_light2.ip=same_mac_to_learn[1]!!.ip
                                                                    updated_light2.mac=macip
                                                                    updated_light2.room_name=selectedItem.room_name
                                                                    updated_light2.status="off"
                                                                    updated_light2.sub_type="0002"
                                                                    updated_light2.Lname= same_mac_to_learn[1]?.Lname
                                                                    database.updateLightById(same_mac_to_learn[1]!!.id,updated_light2)

                                                                    val updated_light3=Light()
                                                                    updated_light3.ip=same_mac_to_learn[2]!!.ip
                                                                    updated_light3.mac=macip
                                                                    updated_light3.room_name=selectedItem.room_name
                                                                    updated_light3.status="off"
                                                                    updated_light3.sub_type="0003"
                                                                    updated_light3.Lname= same_mac_to_learn[2]?.Lname
                                                                    database.updateLightById(same_mac_to_learn[2]!!.id,updated_light3)


                                                                    val database2= fan_db.getInstance(requireContext())
                                                                    val updated_fan= fan()
                                                                    updated_fan.ip=same_mac_to_learn[3]!!.ip
                                                                    updated_fan.mac=macip
                                                                    updated_fan.room_name=selectedItem.room_name
                                                                    updated_fan.status="0"
                                                                    updated_fan.subtype="0004"

                                                                    updated_fan.Fname= same_mac_to_learn[3]?.Lname
                                                                    database2.set_to_db_fan(updated_fan)


                                                                }


                                                                requireActivity().runOnUiThread{

                                                                    Toast.makeText(requireContext(), "Learned.. ", Toast.LENGTH_SHORT).show()
                                                                    val learn_light_db=light_db.getInstance(requireContext())
                                                                    SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(room!!.room_name))
                                                                    on_off_test_light_pupop.isEnabled = true
                                                                    selectedItem.mac=macip
                                                                    on_off_test_light_pupop.alpha=1f
                                                                    popupWindow.dismiss()
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

                                    Thread{
                                        val network_db=setting_network_db.getInstance(requireContext())
                                        val network=network_db.get_from_db_network_manual(1)
                                        if (ssid ==network?.modem_ssid ){

                                            var myip= checkIP(requireContext())

                                            get_mac_light(requireContext(),myip)
                                            UdpListener8089.pause()
                                            receiveUdpMessage({ receivedMessage ->
                                                if (receivedMessage=="failed") {
                                                    requireActivity().runOnUiThread{
                                                        Toast.makeText(requireContext(), "Failed.. ", Toast.LENGTH_SHORT).show()
                                                    }
                                                    println("broadcast failed..")
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

                                                        if (type=="Lght"){


                                                            val updated_light=Light()
                                                            updated_light.ip=ip
                                                            updated_light.mac=macip
                                                            updated_light.room_name=selectedItem.room_name
                                                            updated_light.status="off"


                                                            updated_light.sub_type=pole_num


                                                            updated_light.Lname="${selectedItem.Lname}"
                                                            database.updateLightById(selectedItem!!.id,updated_light)



                                                            requireActivity().runOnUiThread{
                                                                Toast.makeText(requireContext(), "Learned", Toast.LENGTH_LONG).show()
                                                                val learn_light_db=light_db.getInstance(requireContext())
                                                                SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(room!!.room_name))
                                                                selectedItem.mac=macip
                                                                on_off_test_light_pupop.isEnabled = true
                                                                on_off_test_light_pupop.alpha=1f
                                                                popupWindow.dismiss()

                                                            }


                                                        }





                                                    }

                                                }


                                                println("Received: $receivedMessage")
                                            }, 8089, 1300)
                                            UdpListener8089.resume()
                                            requireActivity().runOnUiThread{
                                                on_off_test_light_pupop.isEnabled = true
                                                on_off_test_light_pupop.alpha=1f




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


                        }

//

                        UdpListener8089.resume()
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

                    val delete_msg = customPopupView2.findViewById<TextView>(R.id.text_msg)
                    delete_msg.setText("Delete all device poles or one pole of device")

                    val delete_all_poles = customPopupView2.findViewById<Button>(R.id.yes_delete)
                    delete_all_poles.setText("Delete all")

                    val delete_pole = customPopupView2.findViewById<Button>(R.id.cancel_delete)
                    delete_pole.setText("detele pole")



                    val old_name =edit_light_name_pupop.text.toString()
                    delete_light_pupop.setOnClickListener {

                        popupWindow.dismiss()
                        popupWindow.dismiss()

                        popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                        delete_pole.setOnClickListener{

                            val same_mac_count = learn_light_db.getLightsByMacAddress(selectedItem.mac).count()
                            if (same_mac_count ==1){
                                database.delete_from_db_light(selectedItem.id)
//                        val alllight=database.getAllLights()
                                rename_item_in_scenario("d",old_name,old_name,"light")
                                SharedViewModel.update_light_to_learn_list(learn_light_db.getAllLightsByRoomName(room!!.room_name))
                                if (learn_light_db.getAllLightsByRoomName(room!!.room_name).count() == 0){
                                    val activity =requireActivity() as griffin_home
                                    activity.viewPager.currentItem=0

                                }
                                val sixworkerdb = six_workert_db.getInstance(requireContext())
                                val sixworker_list = sixworkerdb.getsix_workertsByMacAddress(selectedItem.mac)
                                if (sixworker_list.count() >0 ){
                                    sixworkerdb.delete_from_db_six_workert(sixworker_list[0]?.id)
                                }

                            }else{
                                database.delete_from_db_light(selectedItem.id)
//                        val alllight=database.getAllLights()
                                rename_item_in_scenario("d",old_name,old_name,"light")
                                SharedViewModel.update_light_to_learn_list(learn_light_db.getAllLightsByRoomName(room!!.room_name))
                                val sixworkerdb = six_workert_db.getInstance(requireContext())
                                val sixworker_list = sixworkerdb.getsix_workertsByMacAddress(selectedItem.mac)
                                if (sixworker_list.count() >0 ){
                                    sixworkerdb.delete_from_db_six_workert(sixworker_list[0]?.id)
                                }
                                if (learn_light_db.getAllLightsByRoomName(room!!.room_name).count() == 0){
                                    val activity =requireActivity() as griffin_home
                                    activity.viewPager.currentItem=0

                                }

                            }
                            popupWindow.dismiss()
                        }
                        delete_all_poles.setOnClickListener{
                            println("=============")
                            val same_mac_lights = learn_light_db.getLightsByMacAddress(selectedItem.mac)
                            for (light in same_mac_lights){
                                database.delete_from_db_light(light!!.id)
                                if (light != null) {
                                    println(light.Lname)
                                    light.Lname?.let { it1 ->
                                        rename_item_in_scenario("d",
                                            it1,it1,"light")
                                    }
                                }

                            }
                            val sixworkerdb = six_workert_db.getInstance(requireContext())
                            val sixworker_list = sixworkerdb.getsix_workertsByMacAddress(selectedItem.mac)
                            if (sixworker_list.count() >0 ){
                                sixworkerdb.delete_from_db_six_workert(sixworker_list[0]?.id)
                            }
                            SharedViewModel.update_light_to_learn_list(learn_light_db.getAllLightsByRoomName(room!!.room_name))
                            popupWindow2.dismiss()
                            if (learn_light_db.getAllLightsByRoomName(room!!.room_name).count() == 0){
                                val activity =requireActivity() as griffin_home
                                activity.viewPager.currentItem=0

                            }
                        }


                    }
                    on_off_test_light_pupop.setOnClickListener {
                        try {
                            val wifiManager = requireContext().applicationContext.getSystemService(
                                Context.WIFI_SERVICE) as WifiManager
                            val wifiInfo = wifiManager.connectionInfo
                            val ssid = wifiInfo.ssid.trim('"')
                            val network_db=setting_network_db.getInstance(requireContext())
                            val network=network_db.get_from_db_network_manual(1)
                            if (ssid ==network?.modem_ssid ){


                                Thread{
                                    try {
                                        val current = light_db.getInstance(requireContext()).getLightsByid(selectedItem.id.toString())
                                        all_same_mac_on_on_test(this,current)
                                    }catch (e:Exception){
                                        println(e)

                                    }
                                }.start()
                            }else{
                                println("Connect to a Griffin Network")
                            }
                        }catch (e:Exception){
                            println(e)
                        }
//                        selectedItem



//                        udp_light(this,selectedItem)

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
                                if (selectedItem.Lname !in favorite_names){

                                    var favorite_item= Favorite()
                                    favorite_item.type="light"
                                    favorite_item.name=selectedItem.Lname
                                    favorite_db_handeler.set_to_db_Favorite(favorite_item)
                                    Toast.makeText(requireContext(), "sent to ${newroomName}", Toast.LENGTH_SHORT).show()
                                    popupWindow4.dismiss()
                                }else{
                                    popupWindow4.dismiss()
                                    Toast.makeText(requireContext(), "${selectedItem.Lname} exist in Favorite", Toast.LENGTH_SHORT).show()

                                }




                            }else{
                                Toast.makeText(requireContext(), "sent to ${newroomName}", Toast.LENGTH_SHORT).show()



                                selectedItem.room_name=newroomName

                                database.updateLightById(selectedItem.id,selectedItem)
                                popupWindow4.dismiss()
                                SharedViewModel.update_light_to_learn_list( database.getAllLightsByRoomName(room!!.room_name))
                            }




                        }
                        recyclerView2.adapter = adapter2
                        recyclerView2.layoutManager = LinearLayoutManager(requireContext())


                    }


                    ok_name_light_pupop.setOnClickListener {
                        if(old_name.trim() != edit_light_name_pupop.text.toString().trim()){



                            val light=Light()

                            val same_counter = database.getNumberOfItemsByLname(edit_light_name_pupop.text.toString().trim()).toInt()
                            if ( same_counter!= 0 ){


                                light.Lname=edit_light_name_pupop.text.toString().trim()+ " (${same_counter +1})"


                            }else{
                                light.Lname=edit_light_name_pupop.text.toString().trim()
                            }

                            light.mac=selectedItem.mac
                            light.status=selectedItem.status
                            light.room_name=selectedItem.room_name
                            light.sub_type=selectedItem.sub_type
                            light.ip=selectedItem.ip
                            database.updateLightById(selectedItem.id,light)
                            if (old_name != null) {
                                println("renameeeddd")
                                rename_item_in_scenario("e",old_name, light.Lname!!,"light")
                            }
                            SharedViewModel.update_light_to_learn_list(database.getAllLightsByRoomName(room.room_name))
                        }

                        popupWindow.dismiss()

                    }
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)


                }

                recyclerView.adapter = adapter
                adapter.setItems(newlist)
            })




        })










//
//        database.get_db_light_LiveData().observe(viewLifecycleOwner, Observer { newlist->
//            adapter.setItems(newlist)
//
//        })
//
//




        addlightitem_menu.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), addlightitem_menu)
            popupMenu.gravity = Gravity.TOP or Gravity.END
            popupMenu.menuInflater.inflate(R.menu.add_light_menu, popupMenu.menu)


            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.add_light -> {
                        val activity = requireActivity() as griffin_home
                        activity.viewPager.currentItem=3
// نمایش پاپ‌آپ

//                        hideNavigationBar()
                    }
                }
                true
            }
            popupMenu.show()
        }
    }


    override fun onResume() {
        super.onResume()

    }

}






