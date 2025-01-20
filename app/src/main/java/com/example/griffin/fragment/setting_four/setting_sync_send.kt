package com.example.griffin.fragment.setting_four

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.example.griffin.R
import com.example.griffin.database.*
import com.example.griffin.mudels.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import kotlin.concurrent.thread


class setting_sync_send : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var  socket :DatagramSocket?=null


    var rooms_counter=0
    var lights_counter=0
    var fans_counter=0
    var curtains_counter=0
    var valve_counter=0
    var temperature_counter=0
    var plugs_counter=0
    var cameras_counter=0
    var sixworker_counter=0
    var scenarios_counter=0
    var side = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_sync_send, container, false)
    }
    fun findDuplicateIndices(list: List<String>): List<List<Int>> {
        val indexMap = mutableMapOf<String, MutableList<Int>>()

        // ذخیره ایندکس هر آیتم در دیکشنری
        list.forEachIndexed { index, item ->
            if (indexMap.containsKey(item)) {
                indexMap[item]?.add(index)
            } else {
                indexMap[item] = mutableListOf(index)
            }
        }

        // فیلتر کردن آیتم‌هایی که ایندکس‌های تکراری دارند
        return indexMap.values.filter { it.size > 1 }
    }

    fun filterStarsBeforeTilde(input: String): String {
        var result = input
        while (true) {
            // پیدا کردن موقعیت ~
            val tildeIndex = result.indexOf('~')

            // اگر ~ پیدا نشد، از حلقه خارج می‌شود
            if (tildeIndex == -1) break

            // حذف تمام * هایی که مستقیماً قبل از ~ قرار دارند
            val filteredPart = result.substring(0, tildeIndex).trimEnd('*')
            // ترکیب رشته بعد از ~ با بخش فیلتر شده
            result = filteredPart + result.substring(tildeIndex)
        }

        return result
    }

    fun substringBetween(input: String, startChar: Char, endChar: Char): String? {
        val startIndex = input.indexOf(startChar)
        val endIndex = input.indexOf(endChar, startIndex + 1)

        // بررسی اینکه هر دو کاراکتر پیدا شوند
        if (startIndex != -1 && endIndex != -1) {
            return input.substring(startIndex + 1, endIndex)
        }
        return null
    }

    fun mergeGroupsOfIndices(input: String, groupsOfIndices: List<List<Int>>): List<String> {
        // تقسیم ورودی بر اساس "***"
        val list = input.split("***").toMutableList()

        // ایجاد یک کپی از لیست اصلی برای نگهداری تغییرات
        val newList = list.toMutableList()

        // پیمایش گروه‌های ایندکس برای ادغام
        groupsOfIndices.forEach { indices ->
            if (indices.isEmpty()) return@forEach

            // دریافت طول رشته اولین ایندکس
            val length = list[indices[0]].length

            // ایجاد یک آرایه کاراکتری با طول رشته و مقدار اولیه '-'
            val combined = CharArray(length) { '-' }

            // ادغام رشته‌ها در ایندکس‌های مشخص شده
            indices.forEach { index ->
                list[index].forEachIndexed { charIndex, char ->
                    // اگر مقدار '0' یا '1' در هر رشته یافت شد، آن را جایگزین کنید
                    if (char == '0' || char == '1') {
                        combined[charIndex] = if (combined[charIndex] == '-') char else combined[charIndex] + char.toInt()
                    }
                }
            }

            // قرار دادن رشته ادغام‌شده در اولین ایندکس از گروه
            newList[indices[0]] = String(combined)

            // پاک‌سازی ایندکس‌های دیگر گروه
            indices.drop(1).forEach { index ->
                newList[index] = newList[index].replace(Regex("[01]"), "-")
            }
        }

        // حذف رشته‌های خالی که فقط شامل '-' هستند
        return newList.filter { it.contains(Regex("[01]")) }
    }
    fun removeDuplicates(strings: List<String>): List<String> {
        val seen = mutableSetOf<String>() // مجموعه برای پیگیری رشته‌های دیده‌شده
        val result = mutableListOf<String>() // لیست برای ذخیره رشته‌های بدون تکرار

        strings.forEach { str ->
            if (str !in seen) {
                seen.add(str) // اضافه کردن رشته به مجموعه
                result.add(str) // اضافه کردن رشته به لیست نتیجه
            }
        }

        return result
    }
    fun retainFirstIndexOnly(strings: List<String>, groupsOfIndices: List<List<Int>>): List<String> {
        // لیستی برای ذخیره ایندکس‌های که باید حذف شوند
        val indicesToRemove = mutableSetOf<Int>()

        // پیمایش گروه‌های ایندکس
        groupsOfIndices.forEach { indices ->
            if (indices.isEmpty()) return@forEach

            // فقط اولین ایندکس را حفظ می‌کنیم و بقیه را برای حذف آماده می‌کنیم
            indices.drop(1).forEach { index ->
                indicesToRemove.add(index)
            }
        }

        // ایجاد لیست جدید بر اساس ایندکس‌های باقی‌مانده
        val newList = strings.indices.filter { index -> index !in indicesToRemove }
            .map { index -> strings[index] }

        return newList
    }

    override fun onResume() {
        super.onResume()

        var start_send_db = view?.findViewById<Button>(R.id.start_send_db)
        var connection_status_send = view?.findViewById<TextView>(R.id.connection_status_send)
        var target_ip_view = view?.findViewById<TextView>(R.id.target_ip)
        var sending_steps = view?.findViewById<TextView>(R.id.sending_steps)
        var learn_camera_ipadsddress = view?.findViewById<TextView>(R.id.learn_camera_ipadsddress)


        var light_db =  light_db.getInstance(requireContext())
        var fan_db =  fan_db.getInstance(requireContext())
        var plug_db =  plug_db.getInstance(requireContext())
        var curtain_db = curtain_db.getInstance(requireContext())
        var valve_db = valve_db.getInstance(requireContext())
        var Temperature_db = Temperature_db.getInstance(requireContext())
        var sixWorkertDb_db = six_workert_db.getInstance(requireContext())
        var room_db = rooms_db.getInstance(requireContext())
        var camera_db = camera_db.getInstance(requireContext())
        var scenario_db = scenario_db.Scenario_db.getInstance(requireContext())

        var lights =light_db.getAllLights().sortedBy { it?.room_name }


        var fans =fan_db.getAllfans().sortedBy { it.room_name }
        var plugs =plug_db.getAllPlugs().sortedBy { it?.room_name }
        var curtains =curtain_db.getAllcurtains().sortedBy { it?.room_name }
        var valve = valve_db.getAllvalves().sortedBy { it?.room_name }
        var temperature = Temperature_db.getAllThermostats().sortedBy { it?.room_name }
        var sixworker = sixWorkertDb_db.getAllsix_workerts()
        var rooms = room_db.getAllRooms().toMutableList()
        var room=rooms()
        room.room_name="Multifunctional"
        room.room_type="Security"
        rooms.add(room)
        var cameras= camera_db.getAllcameras()
        var scenarios= scenario_db.getAllScenario()






        fun room_encoder(room: rooms,akid:Int,ip:String):String{
            var room_type = room.room_type
            when {
                room_type!!.startsWith("living") -> {
                    room_type = "Hall"
                }
                room_type.startsWith("bathroom") -> {
                    room_type = "Bathroom"
                }
                room_type.startsWith("dining") -> {
                    room_type = "Hall"
                }
                room_type.startsWith("gust") -> {
                    room_type = "Bedroom"
                }
                room_type.startsWith("kid") -> {
                    room_type = "Bedroom"
                }
                room_type.startsWith("kitchen") -> {
                    room_type = "Kitchen"
                }
                room_type.startsWith("maste") -> {
                    room_type = "Bedroom"
                }
                room_type.startsWith("tv") -> {
                    room_type = "Hall"
                }
                room_type.startsWith("hayat") -> {
                    room_type = "Yard"
                }
                room_type.startsWith("room") -> {
                    room_type = "Bedroom"
                }
            }
            val msg = "syro~>Griffin~>${room.room_name}~>$room_type~>$akid~>$ip"


            return msg
        }
        fun decreaseString(inputString: String): String? {

            var num = inputString.toInt()
            // کاهش مقدار عددی در یک واحد
            num-=1
            if (num== -1){
                num=0
            }
            // تبدیل عدد به رشته و اضافه کردن صفرهای لازم در ابتدا
            return num.toString().padStart(inputString.length, '0')
        }
        fun state_coder(pole_num:String? , pole_state:String) :String {

            val state = arrayListOf<String>("-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-")
            if (pole_state == "on" || pole_state == "1"){
                state[pole_num?.toInt()!!]="1"


            }else if(pole_state =="off" || pole_state =="0" ){

                state[pole_num?.toInt()!!]="0"
            }


            return state.joinToString(separator = "")

        }



        fun light_encoder(light: Light , akid:Int,ip:String):String{

            val first = light_db.getLightsByMacAddress(light.mac).sortedBy {it!!.sub_type }[0]
            if (first!!.sub_type=="0001"){
                println(light!!.sub_type)
                println(light!!.Lname)
                println(light!!.room_name)

                light.sub_type=decreaseString(light.sub_type.toString())
                println(light.sub_type)
                println(light.Lname)
            }
            if (light.ip?.contains("~>") == true){
                val splited_light = light.ip!!.split("~>")
                light.ip=splited_light[1]

            }


            val msg = "symo~>Griffin~>${light.room_name}~>${light.Lname}~>Lght~>${light.sub_type}~>${"0000000000000000"}~>${light.mac}~>${light.ip}~>~>~>~>$akid~>$ip"

            println(msg)
            return msg
        }


        fun fan_encoder(fan: fan , akid:Int,ip:String):String{

            val first = fan_db.getfansByMacAddress(fan.mac).sortedBy {it!!.subtype }[0]
            if (first!!.subtype=="0001"){

                fan.subtype=decreaseString(fan.subtype.toString())
            }

            val msg = "symo~>Griffin~>${fan.room_name}~>${fan.Fname}~>Fano~>${fan.subtype}~>${"0000000000000000"}~>${fan.mac}~>${fan.ip}~>~>~>~>$akid~>$ip"

            return msg
        }

        fun plug_encoder(plug: Plug , akid:Int,ip:String):String{

            val first = plug_db.getPlugsByMacAddress(plug.mac).sortedBy { it?.subtype }[0]
            if (first != null) {
                if (first.subtype=="0001"){

                    plug.subtype=decreaseString(plug.subtype.toString())
                }
            }
//            when(plug.room_name) {
//                "livingroom" -> {
//                    plug.room_name = "Hall"
//                }
//                "bathroom1" -> {
//                    plug.room_name = "Bathroom"
//                }
//                "bathroom" -> {
//                    plug.room_name = "Bathroom"
//                }
//                "dining" -> {
//                    plug.room_name = "Hall"
//                }
//                "gust_room" -> {
//                    plug.room_name = "Bedroom"
//                }
//                "kids_room" -> {
//                    plug.room_name = "Bedroom"
//                }
//                "kitchen" -> {
//                    plug.room_name = "Kitchen"
//                }
//                "living_room" -> {
//                    plug.room_name = "Hall"
//                }
//                "master_room" -> {
//                    plug.room_name = "Bedroom"
//                }
//                "tv_room" -> {
//                    plug.room_name = "Hall"
//                }
//                "yard" -> {
//                    plug.room_name = "Yard"
//                }
//                "room" -> {
//                    plug.room_name = "Bedroom"
//                }
//            }
            println("vvvvvvvvvvvvvv ${plug.subtype}")
            val msg = "symo~>Griffin~>${plug.room_name}~>${plug.Pname}~>Plug~>${plug.subtype}~>${"0000000000000000"}~>${plug.mac}~>${plug.ip}~>~>~>~>$akid~>$ip"

            return msg
        }
        fun valve_encoder(valve: valve , akid:Int,ip:String):String{

            val first = valve_db.getvalvesByMacAddress(valve.mac).sortedBy { it?.subtype }[0]
            if (first != null) {
                if (first.subtype=="0001"){

                    valve.subtype=decreaseString(valve.subtype.toString())
                }
            }

//            when(valve.room_name) {
//                "livingroom" -> {
//                    valve.room_name = "Hall"
//                }
//                "bathroom1" -> {
//                    valve.room_name = "Bathroom"
//                }
//                "bathroom" -> {
//                    valve.room_name = "Bathroom"
//                }
//                "dining" -> {
//                    valve.room_name = "Hall"
//                }
//                "gust_room" -> {
//                    valve.room_name = "Bedroom"
//                }
//                "kids_room" -> {
//                    valve.room_name = "Bedroom"
//                }
//                "kitchen" -> {
//                    valve.room_name = "Kitchen"
//                }
//                "living_room" -> {
//                    valve.room_name = "Hall"
//                }
//                "master_room" -> {
//                    valve.room_name = "Bedroom"
//                }
//                "tv_room" -> {
//                    valve.room_name = "Hall"
//                }
//                "yard" -> {
//                    valve.room_name = "Yard"
//                }
//                "room" -> {
//                    valve.room_name = "Bedroom"
//                }
//            }
            val msg = "symo~>Griffin~>${valve.room_name}~>${valve.Vname}~>ElVa~>${valve.subtype}~>${"0000000000000000"}~>${valve.mac}~>${valve.ip}~>~>~>~>$akid~>$ip"

            return msg
        }
        fun curtain_encoder(curtain: curtain , akid:Int,ip:String):String{


            val first = curtain_db.getcurtainsByMacAddress(curtain.mac).sortedBy { it!!.sub_type }[0]
            if (first!!.sub_type=="0001"){

                curtain.sub_type=decreaseString(curtain.sub_type.toString())
            }

//            when(curtain.room_name) {
//                "livingroom" -> {
//                    curtain.room_name = "Hall"
//                }
//                "bathroom1" -> {
//                    curtain.room_name = "Bathroom"
//                }
//                "bathroom" -> {
//                    curtain.room_name = "Bathroom"
//                }
//                "dining" -> {
//                    curtain.room_name = "Hall"
//                }
//                "gust_room" -> {
//                    curtain.room_name = "Bedroom"
//                }
//                "kids_room" -> {
//                    curtain.room_name = "Bedroom"
//                }
//                "kitchen" -> {
//                    curtain.room_name = "Kitchen"
//                }
//                "living_room" -> {
//                    curtain.room_name = "Hall"
//                }
//                "master_room" -> {
//                    curtain.room_name = "Bedroom"
//                }
//                "tv_room" -> {
//                    curtain.room_name = "Hall"
//                }
//                "yard" -> {
//                    curtain.room_name = "Yard"
//                }
//                "room" -> {
//                    curtain.room_name = "Bedroom"
//                }
//            }
            val msg = "symo~>Griffin~>${curtain.room_name}~>${curtain.Cname}~>Crtn~>${curtain.sub_type}~>${"0000000000000000"}~>${curtain.mac}~>${curtain.ip}~>~>~>~>$akid~>$ip"

            return msg
        }
        fun thermostat_encoder(thermostst : Thermostst,akid:Int,ip:String):String{

            val first = thermostst.mac?.let { Temperature_db.getThermostatsByMac(it).sortedBy { it.subtype } }
                ?.get(0)
            if (first != null) {
                if (first.subtype=="0001"){

                    thermostst.subtype=decreaseString(thermostst.subtype.toString())
                }
            }


            val t = "25"
            val on_off=thermostst.on_off
            val temperature=thermostst.temperature
            val fan_status=thermostst.fan_status
            val mood=thermostst.mood
            val statuss = "$on_off$t$temperature$fan_status${mood}000000000"
            val msg = "symo~>Griffin~>${thermostst.room_name}~>${thermostst.name}~>Tmpr~>${thermostst.subtype}~>${statuss}~>${thermostst.mac}~>${thermostst.ip}~>~>~>~>$akid~>$ip"

            return msg
        }


        fun sixworker_encoder(sixworker : six_workert,akid:Int,ip:String):String{



            val empty = "----"


            val status = sixworker.type+"!"+sixworker.pole_num+"!"+sixworker.status+"!"+sixworker.work_name
            val msg = "symo~>Griffin~>${empty}~>${sixworker.name}~>SixC~>${sixworker.sub_type}~>${status}~>${sixworker.mac}~>${sixworker.ip}~>~>~>~>$akid~>$ip"

            return msg
        }
        fun camera_encoder(camera: camera,akid:Int,ip:String):String{



            val msg = "symo~>Griffin~>Multifunctional~>${camera.CAMname}~>Camr~>0000~>0000000000000000~>~>~>rtsp://${camera.user}:${camera.pass}@${camera.ip}:${camera.port}/cam/realmonitor?channel=${camera.chanel}&subtype=${camera.subtype}~>~>~>$akid~>$ip"

            return msg
        }


        fun scenario_encoder(scenario:scenario ,akid:Int , ip:String):String{

            val scenario_mac_lights= arrayListOf<String>()
            val scenario_status_lights= arrayListOf<String>()
            val scenario_room_name_lights= arrayListOf<String>()
            val scenario_mac_plugs= arrayListOf<String>()
            val scenario_status_plugs= arrayListOf<String>()
            val scenario_room_name_plugs= arrayListOf<String>()
            val scenario_mac_termostat= arrayListOf<String>()
            val scenario_status_termostat= arrayListOf<String>()
            val scenario_room_name_termostat= arrayListOf<String>()
            val scenario_mac_curtain= arrayListOf<String>()
            val scenario_status_curtain= arrayListOf<String>()
            val scenario_room_name_curtain= arrayListOf<String>()
            val scenario_mac_valve= arrayListOf<String>()
            val scenario_status_valve= arrayListOf<String>()
            val scenario_room_name_valve= arrayListOf<String>()
            val scenario_mac_fan= arrayListOf<String>()
            val scenario_status_fan= arrayListOf<String>()
            val scenario_room_name_fan= arrayListOf<String>()

            val scenario_lights = scenario.light?.split(",")
            val scenario_plugs = scenario.plug?.split(",")
            val scenario_termostat = scenario.thermostat?.split(",")
            println("##############"+ scenario.thermostat )
            println("##############"+ scenario_termostat )
            val scenario_curtain = scenario.curtain?.split(",")
            val scenario_valve = scenario.valve?.split(",")
            val scenario_fan = scenario.fan?.split(",")

            if (scenario_lights != null && (scenario.light!="" && scenario.light!=null ) ) {





                for (light in scenario_lights){
                    val new_light = light_db.getLightsByLname(light.split("#")[0])
                    println(new_light.Lname)
                    val lightss = light_db.getLightsByMacAddress(new_light.mac).sortedBy { it!!.sub_type }[0]
                    if (lightss!!.sub_type == "0001"){
                        val new_light_status =state_coder(new_light.sub_type?.let {
                            decreaseString(
                                it
                            )
                        }, light.split("#")[1])
                        new_light.mac?.let { scenario_mac_lights.add(it) }
                        new_light.room_name?.let { scenario_room_name_lights.add(it) }
                        scenario_status_lights.add(new_light_status)
                    }else {
                        val new_light_status = state_coder(new_light.sub_type, light.split("#")[1])
                        new_light.mac?.let { scenario_mac_lights.add(it) }
                        new_light.room_name?.let { scenario_room_name_lights.add(it) }
                        scenario_status_lights.add(new_light_status)
                    }
                }
            }
            if (scenario_plugs != null && (scenario.plug!="" && scenario.plug!=null ) ) {
                for (plug in scenario_plugs){
                    val new_plug = plug_db.getPlugByCname(plug.split("#")[0])
                    val plugss= plug_db.getPlugsByMacAddress(new_plug!!.mac).sortedBy { it!!.subtype }[0]
                    if (plugss!!.subtype == "0001"){
                        val new_plug_status =state_coder(new_plug?.subtype?.let { decreaseString(it) },plug.split("#")[1])
                        new_plug?.mac?.let { scenario_mac_plugs.add(it) }
                        new_plug?.room_name?.let { scenario_room_name_plugs.add(it) }
                        scenario_status_plugs.add(new_plug_status)

                    }else{

                        val new_plug_status =state_coder(new_plug?.subtype,plug.split("#")[1])
                        new_plug?.mac?.let { scenario_mac_plugs.add(it) }
                        new_plug?.room_name?.let { scenario_room_name_plugs.add(it) }
                        scenario_status_plugs.add(new_plug_status)

                    }

                }
            }
            if (scenario_termostat != null && (scenario.thermostat!="" && scenario.thermostat!=null ) ) {
                println("+++++++")
                for (termostat in scenario_termostat){
                    println(termostat.split("#")[0])
                    val new_termostat = Temperature_db.getThermostatByName(termostat.split("#")[0])
//                    val new_termostat_status = termostat.split("#")[1].split(Regex("[^0-9]")).joinToString(separator = "").padEnd(16, '0')

                    val status = termostat.split("#")[1].substringBefore("!")
                    val temperature = substringBetween(termostat.split("#")[1], '!', '$')
                    val mood = substringBetween(termostat.split("#")[1], '$', '@')
                    val fanStatus = termostat.split("#")[1].substringAfter("@")

                    val new_termostat_status ="${status}--${temperature}${mood}${fanStatus}000000000"

                    new_termostat?.mac?.let { scenario_mac_termostat.add(it) }
                    scenario_status_termostat.add(new_termostat_status)
                    new_termostat?.room_name?.let { scenario_room_name_termostat.add(it) }
                }
            }
            if (scenario_curtain != null && (scenario.curtain!="" && scenario.curtain!=null ) ) {
                for (curtain in scenario_curtain){
                    val new_curtain = curtain_db.getCurtainByCname(curtain.split("#")[0])
                    val newcurtain_status =curtain.split("#")[1]
                    new_curtain?.mac?.let { scenario_mac_curtain.add(it) }
                    new_curtain?.room_name?.let { scenario_room_name_curtain.add(it) }
                    scenario_status_curtain.add(newcurtain_status)
                }
            }
            if (scenario_valve != null && (scenario.curtain!="" && scenario.curtain!=null ) ) {
                for (valve in scenario_valve){
                    val new_valve = valve_db.getvalveByCname(valve.split("#")[0])
                    val valvess = valve_db.getvalvesByMacAddress(new_valve!!.mac).sortedBy { it!!.subtype }[0]
                    if (valvess!!.subtype == "0001"){
                        val new_valve_status=state_coder(new_valve?.subtype?.let { decreaseString(it) },valve.split("#")[1])
                        new_valve?.mac?.let { scenario_mac_valve.add(it) }
                        new_valve?.room_name?.let { scenario_room_name_valve.add(it) }
                        scenario_status_valve.add(new_valve_status)
                    }else{

                        val new_valve_status=state_coder(new_valve?.subtype,valve.split("#")[1])
                        new_valve?.mac?.let { scenario_mac_valve.add(it) }
                        new_valve?.room_name?.let { scenario_room_name_valve.add(it) }
                        scenario_status_valve.add(new_valve_status)
                    }

                }
            }
            if (scenario_fan != null && (scenario.fan!="" && scenario.fan!=null ) ) {
                for (fan in scenario_fan){
                    val new_fan = fan_db.get_from_db_fan_By_name(fan.split("#")[0])
                    val fanss = fan_db.getfansByMacAddress(new_fan!!.mac).sortedBy { it!!.subtype }[0]
                    val is_n = light_db.getLightsByMacAddress(new_fan.mac)
                    if (fanss!!.subtype == "0001" && is_n.count()==0 ){
                        val new_fan_status =state_coder(new_fan?.subtype?.let { decreaseString(it) },fan.split("#")[1])

                        new_fan?.mac?.let { scenario_mac_fan.add(it) }
                        new_fan?.room_name?.let { scenario_room_name_fan.add(it) }
                        scenario_status_fan.add(new_fan_status)
                    }else{
                        val new_fan_status =state_coder(new_fan?.subtype,fan.split("#")[1])

                        new_fan?.mac?.let { scenario_mac_fan.add(it) }
                        new_fan?.room_name?.let { scenario_room_name_fan.add(it) }
                        scenario_status_fan.add(new_fan_status)

                    }
                }
            }
            val my_senario_list2= mutableListOf<String>()
            my_senario_list2.add(scenario_room_name_lights.joinToString(separator = "***"))
            my_senario_list2.add(scenario_room_name_termostat.joinToString(separator = "***"))
            my_senario_list2.add(scenario_room_name_curtain.joinToString(separator = "***"))
            my_senario_list2.add(scenario_room_name_valve.joinToString(separator = "***"))
            my_senario_list2.add(scenario_room_name_fan.joinToString(separator = "***"))
            my_senario_list2.add(scenario_room_name_plugs.joinToString(separator = "***"))
            var final_room_names =my_senario_list2.joinToString(separator = "***").trimEnd('*')

            val my_senario_list_mac= mutableListOf<String>()
            my_senario_list_mac.add(scenario_mac_lights.joinToString (separator = "***"))
            my_senario_list_mac.add(scenario_mac_termostat.joinToString (separator = "***"))
            my_senario_list_mac.add(scenario_mac_curtain.joinToString (separator = "***"))
            my_senario_list_mac.add(scenario_mac_valve.joinToString (separator = "***"))
            my_senario_list_mac.add(scenario_mac_fan.joinToString (separator = "***"))
            my_senario_list_mac.add(scenario_mac_plugs.joinToString (separator = "***"))
            var final_mac=my_senario_list_mac.joinToString(separator = "***").trimEnd('*')
            println("final_mac : " + final_mac)

            val mysenario_list_status= mutableListOf<String>()
            mysenario_list_status.add(scenario_status_lights.joinToString (separator = "***"))
            mysenario_list_status.add(scenario_status_termostat.joinToString (separator = "***"))
            mysenario_list_status.add(scenario_status_curtain.joinToString (separator = "***"))
            my_senario_list_mac.add(scenario_mac_valve.joinToString (separator = "***"))
            mysenario_list_status.add(scenario_status_fan.joinToString (separator = "***"))
            mysenario_list_status.add(scenario_status_plugs.joinToString (separator = "***"))
            var final_status = mysenario_list_status.joinToString(separator = "***").trimEnd('*')

            println("sysc~>Griffin~>${scenario.scenario_name}~>${scenario.id}~>$final_room_names~>$final_mac~>$final_status~>~>~>~>~>$akid~>$ip")
            println("----------------------")
            val final_mac_dublicates = findDuplicateIndices(final_mac.split("***"))
            println(final_mac_dublicates)
            val new_status = mergeGroupsOfIndices(final_status,final_mac_dublicates)

            final_status=new_status.joinToString  (separator="***")

            final_mac = removeDuplicates(final_mac.split("***")).joinToString(separator = "***")

            final_room_names = retainFirstIndexOnly(final_room_names.split("***"),final_mac_dublicates).joinToString (separator = "***")


            var msg = "sysc~>Griffin~>${scenario.scenario_name}~>${scenario.id}~>$final_room_names~>$final_mac~>$final_status~>~>~>~>~>$akid~>$ip"




            println(msg)
            return msg
        }



        fun isConnectedToWifi(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            return networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
        }

        start_send_db?.setOnClickListener {




            lights =light_db.getAllLights().sortedBy { it?.room_name }


            fans =fan_db.getAllfans().sortedBy { it.room_name }
            plugs =plug_db.getAllPlugs().sortedBy { it?.room_name }
            curtains =curtain_db.getAllcurtains().sortedBy { it?.room_name }
            valve = valve_db.getAllvalves().sortedBy { it?.room_name }
            temperature = Temperature_db.getAllThermostats().sortedBy { it?.room_name }
            sixworker = sixWorkertDb_db.getAllsix_workerts()
            rooms = room_db.getAllRooms().toMutableList()
            var room=rooms()
            room.room_name="Multifunctional"
            room.room_type="Security"
            rooms.add(room)
            cameras= camera_db.getAllcameras()
            scenarios= scenario_db.getAllScenario()










            for (light in lights){
                println(light!!.Lname + "           " + light!!.sub_type)
            }


            val wifiManager = requireContext().applicationContext.getSystemService(
                Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val ssid = wifiInfo.ssid
            val db_ssid = setting_network_db.getInstance(requireContext()).get_from_db_network_manual(1)?.modem_ssid

            if (isConnectedToWifi(requireContext()) && ssid.replace("\"", "") ==db_ssid ){

                UdpListener8089.pause()
                if (side){
                    println("in")
                    socket?.close()
                    socket=null
                    UdpListener8089.resume()
                    try {

                        start_send_db.setText("Start")
                        target_ip_view!!.setText("")
                        learn_camera_ipadsddress!!.setText("")
                        sending_steps!!.setText("")

                        connection_status_send!!.setText("Disconnected")
                    }catch (e:Exception){
                        println(e)
                    }
                    side= false
                    UdpListener8089.resume()
                }else{
                    println("ese")
                    side = true

                    try {
                        try {

                            start_send_db.text="Please Wait..."
                        }catch (e:Exception){
                            println(e)
                        }
                        try {

                            connection_status_send?.setText("Connecting")
                        }catch (e:Exception){
                            println(e)
                        }

                        GlobalScope.launch(Dispatchers.IO) {
                            UdpListener8089.pause()
                            val bufferSize = 1024
                            val receiveData = ByteArray(bufferSize)


                            var is_finished =false
                            var target_ip = convertIpToBroadcast(requireContext())
                            var conection_status =false
                            var ak_id = 1
                            var send_level=1
                            val received_akid=1



                            while (side){
//                        UdpListener8089.pause()
                                println(conection_status)

                                try {
                                    if (socket == null || socket?.isClosed == true || socket?.isBound == true) {
//                            UdpListener8089.pause()
                                        socket = DatagramSocket(8089)
                                        println("Listenning to 8089..")
                                    }
                                    if (is_finished || side ==false){
                                        break
                                    }
                                }catch (e:Exception){

                                    socket?.close()
                                    socket=null
                                    UdpListener8089.resume()
                                    try {

                                        start_send_db.setText("Start")
                                        target_ip_view!!.setText("")
                                        learn_camera_ipadsddress!!.setText("")
                                        sending_steps!!.setText("")
                                        conection_status=false
                                        connection_status_send!!.setText("Disconnected")
                                    }catch (e:Exception){
                                        println(e)
                                    }
                                    side= false
                                    UdpListener8089.resume()
                                    break
                                }






                                var serverAddress = InetAddress.getByName(target_ip)
                                val serverPort = 8089

                                if (!conection_status ){
                                    println("connection test")

                                    val message = "sygi~>${checkIP(requireContext())}"
                                    val sendData = message.toByteArray()
                                    val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, serverPort)
                                    socket?.send(sendPacket)
                                    socket?.close()
                                    receiveUdpMessage({ receivedMessage ->
                                        println(receivedMessage)
                                        val listed_msg=receivedMessage.split("~>")

                                        if (listed_msg[0] == "sysi"){
                                            try {

                                                connection_status_send?.setText("Connected")
                                            }catch (e:Exception){
                                                println(e)
                                            }
                                            try {

                                                target_ip_view?.setText(listed_msg[1])
                                            }catch (e:Exception){
                                                println(e)
                                            }
                                            println("done")
                                            conection_status=true
                                            target_ip=listed_msg.reversed()[0]
                                            send_level=2

//                               return@receiveUdpMessagebreak
                                        }




                                    }, 8089, 1000)



                                }else{
                                    println(ak_id)
                                    println(rooms_counter)
                                    println(rooms.count())

                                    if( rooms_counter< rooms.count()){
                                        serverAddress = InetAddress.getByName(target_ip)
                                        try {

                                            sending_steps?.setText("Rooms")
                                        }catch (e:Exception){
                                            println(e)
                                        }
                                        val messagee =rooms[rooms_counter]?.let { it1 -> room_encoder(it1,ak_id,
                                            checkIP(requireContext())
                                        ) }
                                        val sendData = messagee?.toByteArray()
                                        val sendPacket = DatagramPacket(sendData, sendData!!.size, serverAddress, serverPort)
                                        socket?.send(sendPacket)
                                        socket?.close()

                                        receiveUdpMessage({ receivedMessage ->
                                            val listed_receivedMessage=receivedMessage.split("~>")
                                            if (listed_receivedMessage[0]=="syac"){
                                                if (listed_receivedMessage[1] == ak_id.toString()){
                                                    rooms_counter+=1
                                                    ak_id+=1

                                                }
                                            }
                                        }, 8089, 1000)

                                    }else if(lights_counter< lights.count()){
                                        serverAddress = InetAddress.getByName(target_ip)
                                        try {

                                            sending_steps?.setText("Lights")
                                        }catch (e:Exception){
                                            println(e)
                                        }
                                        val messagee =
                                            lights[lights_counter]?.let { it1 -> light_encoder(it1,ak_id, checkIP(requireContext())) }
                                        println(lights[lights_counter]!!.Lname +"       "+ lights[lights_counter]!!.sub_type)
                                        val sendData = messagee?.toByteArray()
                                        val sendPacket = DatagramPacket(sendData, sendData!!.size, serverAddress, serverPort)
                                        socket?.send(sendPacket)
                                        socket?.close()
                                        receiveUdpMessage({ receivedMessage ->
                                            val listed_receivedMessage=receivedMessage.split("~>")
                                            if (listed_receivedMessage[0]=="syac"){
                                                if (listed_receivedMessage[1] == ak_id.toString()){
                                                    lights_counter+=1
                                                    ak_id+=1

                                                }
                                            }
                                        }, 8089, 1000)



                                    }else if(fans_counter< fans.count()){
                                        serverAddress = InetAddress.getByName(target_ip)
                                        try {

                                            sending_steps?.setText("Fans")
                                        }catch (e:Exception){
                                            println(e)
                                        }
                                        val messagee =
                                            fans[fans_counter]?.let { it1 -> fan_encoder(it1,ak_id, checkIP(requireContext())) }
                                        val sendData = messagee?.toByteArray()
                                        val sendPacket = DatagramPacket(sendData, sendData!!.size, serverAddress, serverPort)
                                        socket?.send(sendPacket)
                                        socket?.close()
                                        receiveUdpMessage({ receivedMessage ->
                                            val listed_receivedMessage=receivedMessage.split("~>")
                                            if (listed_receivedMessage[0]=="syac"){
                                                if (listed_receivedMessage[1] == ak_id.toString()){
                                                    fans_counter+=1
                                                    ak_id+=1

                                                }
                                            }
                                        }, 8089, 1000)



                                    }else if(plugs_counter< plugs.count()){
                                        serverAddress = InetAddress.getByName(target_ip)
                                        try {

                                            sending_steps?.setText("Plugs")
                                        }catch (e:Exception){
                                            println(e)
                                        }
                                        val messagee =plug_encoder(plugs[plugs_counter],ak_id, checkIP(requireContext()))
                                        val sendData = messagee?.toByteArray()
                                        val sendPacket = DatagramPacket(sendData, sendData!!.size, serverAddress, serverPort)
                                        socket?.send(sendPacket)
                                        socket?.close()
                                        receiveUdpMessage({ receivedMessage ->
                                            val listed_receivedMessage=receivedMessage.split("~>")
                                            if (listed_receivedMessage[0]=="syac"){
                                                if (listed_receivedMessage[1] == ak_id.toString()){
                                                    plugs_counter+=1
                                                    ak_id+=1

                                                }
                                            }
                                        }, 8089, 1000)



                                    }else if(valve_counter< valve.count()){
                                        serverAddress = InetAddress.getByName(target_ip)
                                        try {

                                            sending_steps?.setText("Valve")
                                        }catch (e:Exception){
                                            println(e)
                                        }
                                        val messagee =valve_encoder(valve[valve_counter],ak_id, checkIP(requireContext()))
                                        val sendData = messagee?.toByteArray()
                                        val sendPacket = DatagramPacket(sendData, sendData!!.size, serverAddress, serverPort)
                                        socket?.send(sendPacket)
                                        socket?.close()
                                        receiveUdpMessage({ receivedMessage ->
                                            val listed_receivedMessage=receivedMessage.split("~>")
                                            if (listed_receivedMessage[0]=="syac"){
                                                if (listed_receivedMessage[1] == ak_id.toString()){
                                                    valve_counter+=1
                                                    ak_id+=1

                                                }
                                            }
                                        }, 8089, 1000)



                                    }else if(curtains_counter< curtains.count()){
                                        serverAddress = InetAddress.getByName(target_ip)
                                        try {

                                            sending_steps?.setText("Curtain")
                                        }catch (e:Exception){
                                            println(e)
                                        }
                                        val messagee = curtains[curtains_counter]?.let { it1 ->
                                            curtain_encoder(
                                                it1,ak_id, checkIP(requireContext()))
                                        }
                                        val sendData = messagee?.toByteArray()
                                        val sendPacket = DatagramPacket(sendData, sendData!!.size, serverAddress, serverPort)
                                        socket?.send(sendPacket)
                                        socket?.close()
                                        receiveUdpMessage({ receivedMessage ->
                                            val listed_receivedMessage=receivedMessage.split("~>")
                                            if (listed_receivedMessage[0]=="syac"){
                                                if (listed_receivedMessage[1] == ak_id.toString()){
                                                    curtains_counter+=1
                                                    ak_id+=1

                                                }
                                            }
                                        }, 8089, 1000)



                                    }else if(temperature_counter< temperature.count()){
                                        serverAddress = InetAddress.getByName(target_ip)
                                        try {

                                            sending_steps?.setText("Thermostat")
                                        }catch (e:Exception){
                                            println(e)
                                        }
                                        val messagee = temperature[temperature_counter]?.let { it1 ->
                                            thermostat_encoder(
                                                it1,ak_id, checkIP(requireContext()))
                                        }
                                        val sendData = messagee?.toByteArray()
                                        val sendPacket = DatagramPacket(sendData, sendData!!.size, serverAddress, serverPort)
                                        socket?.send(sendPacket)
                                        socket?.close()
                                        receiveUdpMessage({ receivedMessage ->
                                            val listed_receivedMessage=receivedMessage.split("~>")
                                            if (listed_receivedMessage[0]=="syac"){
                                                if (listed_receivedMessage[1] == ak_id.toString()){
                                                    temperature_counter+=1
                                                    ak_id+=1

                                                }
                                            }
                                        }, 8089, 1000)



                                    }else if(cameras_counter< cameras.count()){
                                        serverAddress = InetAddress.getByName(target_ip)
                                        try {

                                            sending_steps?.setText("Camera")
                                        }catch (e:Exception){
                                            println(e)
                                        }
                                        val messagee = cameras[cameras_counter]?.let { it1 ->
                                            camera_encoder(
                                                it1,ak_id, checkIP(requireContext()))
                                        }
                                        val sendData = messagee?.toByteArray()
                                        val sendPacket = DatagramPacket(sendData, sendData!!.size, serverAddress, serverPort)
                                        socket?.send(sendPacket)
                                        socket?.close()
                                        receiveUdpMessage({ receivedMessage ->
                                            val listed_receivedMessage=receivedMessage.split("~>")
                                            if (listed_receivedMessage[0]=="syac"){
                                                if (listed_receivedMessage[1] == ak_id.toString()){
                                                    cameras_counter+=1
                                                    ak_id+=1

                                                }
                                            }
                                        }, 8089, 1000)



                                        println("aaaaaaaaaaaaaaaaaaaaaaaaa  " + scenarios_counter + "        " + scenarios.count())
                                    }else if (sixworker_counter < sixworker.count()){
                                        serverAddress = InetAddress.getByName(target_ip)
                                        try {

                                            sending_steps?.setText("Six worker")
                                        }catch (e:Exception){
                                            println(e)
                                        }
                                        val messagee =sixworker[sixworker_counter]?.let { it1 ->
                                            sixworker_encoder(
                                                it1,ak_id, checkIP(requireContext()))
                                        }

                                        val sendData = messagee?.toByteArray()
                                        val sendPacket = DatagramPacket(sendData, sendData!!.size, serverAddress, serverPort)
                                        socket?.send(sendPacket)
                                        socket?.close()
                                        receiveUdpMessage({ receivedMessage ->
                                            val listed_receivedMessage=receivedMessage.split("~>")
                                            if (listed_receivedMessage[0]=="syac"){
                                                if (listed_receivedMessage[1] == ak_id.toString()){
                                                    sixworker_counter+=1
                                                    ak_id+=1

                                                }
                                            }
                                        }, 8089, 1000)



                                    }else if(scenarios_counter< scenarios.count()){

                                        println("                    " + scenarios.count())
                                        serverAddress = InetAddress.getByName(target_ip)
                                        try {

                                            sending_steps?.setText("Scenarios")
                                        }catch (e:Exception){
                                            println(e)
                                        }

                                        try {

                                            val messagee = scenarios[scenarios_counter].let { it1 ->
                                                scenario_encoder(
                                                    it1,ak_id, checkIP(requireContext()))
                                            }
                                            println(messagee)
                                            val sendData = messagee?.toByteArray()
                                            val sendPacket = DatagramPacket(sendData, sendData!!.size, serverAddress, serverPort)
                                            socket?.send(sendPacket)
                                            socket?.close()
                                            receiveUdpMessage({ receivedMessage ->
                                                val listed_receivedMessage=receivedMessage.split("~>")
                                                if (listed_receivedMessage[0]=="syac"){
                                                    if (listed_receivedMessage[1] == ak_id.toString()){
                                                        scenarios_counter+=1
                                                        ak_id+=1

                                                    }
                                                }
                                            }, 8089, 1000)



                                        }catch (e:Exception){
                                            println(e)
                                            scenarios_counter+=1
                                            socket?.close()
                                        }



                                    }else{
                                        println(serverAddress)
                                        val messagee = "sydn~>$ak_id~>${checkIP(requireContext())}"
                                        val sendData = messagee.toByteArray()
                                        val sendPackett = DatagramPacket(sendData, sendData.size, serverAddress, serverPort)
                                        socket?.send(sendPackett)
                                        socket?.close()

                                        receiveUdpMessage({ receivedMessage ->
                                            val listed_receivedMessage=receivedMessage.split("~>")
                                            if (listed_receivedMessage[0]=="syac" && (listed_receivedMessage[1] == ak_id.toString())){
                                                is_finished=true
                                                requireActivity().runOnUiThread {
                                                    try {
                                                        start_send_db.setText("Start")
                                                        sending_steps?.setText("")
                                                        learn_camera_ipadsddress!!.setText("Finished")
                                                        connection_status_send?.setText("Disconect")

                                                    }catch (e:Exception){
                                                        println(e)
                                                    }

                                                    rooms_counter=0
                                                    scenarios_counter=0
                                                    cameras_counter=0
                                                    sixworker_counter=0
                                                    temperature_counter=0
                                                    curtains_counter=0
                                                    valve_counter=0
                                                    plugs_counter=0
                                                    lights_counter=0
                                                    fans_counter=0


                                                }

                                            }
                                        }, 8089, 1000)

                                    }


                                }

                                Thread.sleep(800)
                            }
                            socket?.close()
                            UdpListener8089.resume()
                            side=false
                        }

                        rooms_counter=0
                        scenarios_counter=0
                        cameras_counter=0
                        sixworker_counter=0
                        temperature_counter=0
                        curtains_counter=0
                        valve_counter=0
                        plugs_counter=0
                        lights_counter=0
                        fans_counter=0

                    }catch (e:Exception){
                        socket?.close()
                        socket=null

                        UdpListener8089.resume()
                        try {

                            start_send_db.setText("Start")
                            target_ip_view!!.setText("")
                            learn_camera_ipadsddress!!.setText("")
                            sending_steps!!.setText("")
                            connection_status_send!!.setText("Disconnected")
                        }catch (e:Exception){
                            println(e)
                        }
                        side= false
                        rooms_counter=0
                        scenarios_counter=0
                        cameras_counter=0
                        sixworker_counter=0
                        temperature_counter=0
                        curtains_counter=0
                        valve_counter=0
                        plugs_counter=0
                        lights_counter=0
                        fans_counter=0
                        socket?.close()
                        println(e)


                        UdpListener8089.resume()
                    }

                }


            }else{
                Toast.makeText(requireContext(), "Connect to a Griffin Network", Toast.LENGTH_SHORT).show()
            }











        }






    }

    override fun onPause() {
        super.onPause()
        try {
            socket?.close()
            socket=null
            val start_send_db = view?.findViewById<Button>(R.id.start_send_db)
            val connection_status_send = view?.findViewById<TextView>(R.id.connection_status_send)
            val target_ip_view = view?.findViewById<EditText>(R.id.target_ip)
            val sending_steps = view?.findViewById<EditText>(R.id.sending_steps)
            val learn_camera_ipadsddress = view?.findViewById<EditText>(R.id.learn_camera_ipadsddress)
            try {
                start_send_db!!.setText("Start")
                target_ip_view!!.setText("")
                learn_camera_ipadsddress!!.setText("")
                sending_steps!!.setText("")

                connection_status_send!!.setText("Disconnected")

            }catch (e:Exception){
                println(e)
            }
            side= false
        }catch (e:Exception){
            println(e)
        }

        UdpListener8089.resume()


    }

    override fun onStop() {
        super.onStop()
        try {
            socket?.close()
            socket=null
            val start_send_db = view?.findViewById<Button>(R.id.start_send_db)
            val connection_status_send = view?.findViewById<TextView>(R.id.connection_status_send)
            val target_ip_view = view?.findViewById<EditText>(R.id.target_ip)
            val sending_steps = view?.findViewById<EditText>(R.id.sending_steps)
            val learn_camera_ipadsddress = view?.findViewById<EditText>(R.id.learn_camera_ipadsddress)
            try {

                start_send_db!!.setText("Start")
                target_ip_view!!.setText("")
                learn_camera_ipadsddress!!.setText("")
                sending_steps!!.setText("")

                connection_status_send!!.setText("Disconnected")
            }catch (e:Exception){
                println(e)
            }
            side= false
        }catch (e:Exception){
            println(e)
        }

        UdpListener8089.resume()


    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            socket?.close()
            socket=null
            val start_send_db = view?.findViewById<Button>(R.id.start_send_db)
            val connection_status_send = view?.findViewById<TextView>(R.id.connection_status_send)
            val target_ip_view = view?.findViewById<EditText>(R.id.target_ip)
            val sending_steps = view?.findViewById<EditText>(R.id.sending_steps)
            val learn_camera_ipadsddress = view?.findViewById<EditText>(R.id.learn_camera_ipadsddress)
            try {
                start_send_db!!.setText("Start")
                target_ip_view!!.setText("")
                learn_camera_ipadsddress!!.setText("")
                sending_steps!!.setText("")

                connection_status_send!!.setText("Disconnected")

            }catch (e:Exception){
                println(e)
            }
            side= false
        }catch (e:Exception){
            println(e)
        }

        UdpListener8089.resume()

    }

}