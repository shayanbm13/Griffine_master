import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import com.example.griffin.database.alarm_handeler_db
import com.example.griffin.database.fan_db
import com.example.griffin.mudels.udp_fan
import com.example.griffin.myBroadcastReceiverr

//import android.content.Context
//import android.view.View
//import android.widget.LinearLayout
//import androidx.fragment.app.Fragment
//import com.example.griffin.R
//import com.example.griffin.database.alarm_handeler_db
//import com.example.griffin.database.fan_db
//import com.example.griffin.database.fan_db
//import com.example.griffin.mudels.*
//import java.net.DatagramPacket
//import java.net.DatagramSocket
//import java.net.InetAddress
//
////import android.content.Context
////import androidx.fragment.app.Fragment
////import com.example.griffin.database.fan_db
////import com.example.griffin.database.fan_db
////import com.example.griffin.mudels.*
////import java.net.DatagramPacket
////import java.net.DatagramSocket
////import java.net.InetAddress
////
////
////fun fan_sub_type_coder(context: Context, mac: String?): String {
////    val fan_database = fan_db.getInstance(context)
////    val same_macs = mac?.let { fan_database.getfansByMacAddress(it) }
////    var key_status = "0000000000000000"
////
////    if (same_macs != null) {
////        var pole_count=0
////        for (fan in same_macs) {
////            val pole = fan?.subtype?.toInt() ?: 0
////            if (fan?.status == "1") {
////                println("on")
////                if (key_status.length >= pole) {
////                    val startIndex = pole-1
////                    val endIndex = startIndex + 1
////                    val newChar = "1"
////                    key_status = key_status.substring(0, startIndex) + newChar + key_status.substring(endIndex)
////                }
////            }else if (fan?.status == "0") {
////                println("off")
////                if (key_status.length >= pole) {
////                    val startIndex = pole-1
////                    val endIndex = startIndex + 1
////                    val newChar = "0"
////                    key_status = key_status.substring(0, startIndex) + newChar + key_status.substring(endIndex)
////                }
////            }
////            if (fan!!.subtype!!.toInt()>pole_count ){
////                pole_count=fan.subtype!!.toInt()
////
////            }
////
////        }
////        println(pole_count)
////        println(key_status)
////        key_status="${key_status.substring(0,pole_count)}${key_status.substring(pole_count).replace("0","-")}"
////
////    }
////    println(key_status)
////    return key_status
////}
////
////
////
////
////
////fun send_to_fan(mac:String?,subtype:String?,status:String?,myip:String,ip:String?){
////    var targetIP=ip
////
////    val message = "cmnd~>$mac~>Valv~>$subtype~>$status~>$myip"
////    val serverAddress = InetAddress.getByName(targetIP)
////    val serverPort = 8089
////
////    val socket = DatagramSocket()
////    val sendData = message.toByteArray()
////    val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, serverPort)
////    socket.send(sendPacket)
////    socket.close()
////
////}
////
////
////
////
////
////fun udp_fan (context: Context, fan: fan?){
////
////    var isResponseReceivedFirstTime = false
////
////
////    val timeoutMillis = 1000
////    var loopCount = 0
////
////    do {
////
////
////        if (loopCount == 0) {
////            if (!isResponseReceivedFirstTime){
////
////                val fan_database= fan_db.getInstance(context)
////                println(fan!!.status)
////                if(fan_database.getStatusById(fan!!.id)=="1"){
////
////                    fan_database.updateStatusbyId(fan.id, "0")
////
////                }else{
////                    fan_database.updateStatusbyId(fan.id, "1")
////
////                }
////            }
////
////            val fan_database= fan_db.getInstance(context)
////            send_to_fan(fan!!.mac,fan!!.subtype,
////                fan_sub_type_coder(context,fan.mac), checkIP(context),fan_database.get_from_db_fan(fan.id)!!.ip)
////            println("direct IP sent")
////
////
////
////
////            receiveUdpMessage({ receivedMessage ->
////                if (receivedMessage=="failed"){
////                    println("direct IP failed..")
////
////                }else{
////                    if ((!receivedMessage.startsWith("cmnd"))&&(!receivedMessage.startsWith("rfsh")) ){
////                        println("direct IP success..")
////                        isResponseReceivedFirstTime = true
////                        val fan_database2= fan_db.getInstance(context)
////                        var receivedmessage_decoded= extract_response(receivedMessage)
////                        var  macip=receivedmessage_decoded[0]
////                        var  pole_num=receivedmessage_decoded[1]
////                        var  key_status=receivedmessage_decoded[2]
////                        var ip =receivedmessage_decoded[3]
////
//////                    var statusList=light_sub_type_decoder(key_status,pole_num)
////
////                        val fan_database = fan_db.getInstance(context)
////                        val same_macs = fan_database.getfansByMacAddress(fan.mac).sortedBy { it!!.subtype!!.toInt() }
////                        var index=0
//////                    for (fan_same_mac in same_macs){
//////                        fan_database2.updateStatusbyId(fan_same_mac!!.id,statusList[index])
//////                        index+=1
//////
//////                    }
////
////                    }
////
////
////                }
////
////
////
////            }, 8089, 500)
////
////
////
////
////
////
////        }else if (loopCount == 1 && !isResponseReceivedFirstTime) {
////            println("broadcasting..")
////            val fan_database= fan_db.getInstance(context)
////            val broadcastIP= convertIpToBroadcast(context)
////            send_to_fan(fan!!.mac,fan.subtype,
////                fan_sub_type_coder(context,fan.mac), checkIP(context),broadcastIP)
////            println("broadcast sent")
////            var isrecived=false
////            receiveUdpMessage({ receivedMessage ->
////                if (receivedMessage=="failed"){
////                    println("broadcast failed..")
////                    if(fan_database.getStatusById(fan.id)=="1"){
////
////                        fan_database.updateStatusbyId(fan.id, "0")
////
////                    }else{
////                        fan_database.updateStatusbyId(fan.id, "1")
////
////                    }
////                    println("Broadcast failed..")
////
////                }else{
////
////                    if ((!receivedMessage.startsWith("cmnd"))&&(!receivedMessage.startsWith("rfsh")) ){
////                        println("success..")
////                        var receivedmessage_decoded= extract_response(receivedMessage)
////                        var  macip=receivedmessage_decoded[0]
////                        var  pole_num=receivedmessage_decoded[1]
////                        var  key_status=receivedmessage_decoded[2]
////                        var ip =receivedmessage_decoded[3]
////                        val light_database3= fan_db.getInstance(context)
////                        val same_macs = light_database3.getfansByMacAddress(fan.mac).sortedBy { it!!.subtype!!.toInt() }
////
//////                    var statusList=light_sub_type_decoder(key_status,pole_num)
//////                    var index=0
////                        for (light_same_mac3 in same_macs){
////                            light_database3.updatefanbyId(light_same_mac3!!.id , ip)
//////                        light_database3.updateStatusById(light_same_mac3!!.id,statusList[index])
//////                        index+=1
////                        }
////
////                    }else{
////                        println("Broadcast failed..")
////
////                    }
////
////                }
////
////
////            }, 8089, 500)
////
////
////        }
////
////        loopCount++
////
////        // تاخیر یک ثانیه قبل از ورود به دور جدید از حلقه
////        Thread.sleep(500)
////    } while (loopCount < 2) // تکرار حلقه تا دو بار اجرا
////
////
////
////}
////
////
////fun refresh_fan(context: Fragment, fan: fan, rooms: rooms?){
////
////    val fan_database1= fan_db.getInstance(context.requireContext())
////    val fan_list=fan_database1.getfansWithNonEmptyMacByRoomName2(rooms!!.room_name)
////    for (same_mac_list in fan_list) {
////        var isResponseReceivedFirstTime = false
////        var loopCount = 0
////
////        do {
////
////
////            if (loopCount == 0) {
////
////
////                send_refresh(fan.mac, checkIP(context.requireContext()),fan.ip)
////
////
////                println("direct IP sent")
////
////
////
////
////                receiveUdpMessage({ receivedMessage ->
////                    if (receivedMessage=="failed"){
////                        println("direct IP failed..")
////
////                    }else{
////                        if ((!receivedMessage.startsWith("cmnd"))&&(!receivedMessage.startsWith("rfsh")) ){
////                            println("direct IP success..")
////                            isResponseReceivedFirstTime = true
////                            val fan_db= fan_db.getInstance(context.requireContext())
////                            var receivedmessage_decoded= extract_response(receivedMessage)
////                            var  macip=receivedmessage_decoded[0]
////                            var  pole_num=receivedmessage_decoded[1]
////                            var  key_status=receivedmessage_decoded[2]
////                            var ip =receivedmessage_decoded[3]
////
////                            var statusList= fan_sub_type_decoder(key_status,fan_db.getfansByMacAddress(macip)[fan_db.getfansByMacAddress(macip).count()-1]!!.subtype.toString())
////
////
////                            val same_macs = fan_db.getfansByMacAddress(same_mac_list[0]!!.mac).sortedBy { it!!.subtype!!.toInt() }
////                            var index=0
////                            for (lightt in same_macs){
////                                fan_database1.updateStatusbyId(lightt!!.id,statusList[0+index])
////                                index+=1
////
////                            }
////
////
////
////
////                        }else{
////                            println("direct IP failed..")
////                        }
////
////
////
////                    }
////
////
////
////                }, 8089, 500)
////
////
////
////
////
////
////            }else if (loopCount == 1 && !isResponseReceivedFirstTime) {
////                println("broadcasting..")
////
////
////                val broadcastIP= convertIpToBroadcast(context.requireContext())
////                send_refresh(fan.mac, checkIP(context.requireContext()),broadcastIP)
////
////
////                println("broadcast sent")
////
////
////                receiveUdpMessage({ receivedMessage ->
////                    if (receivedMessage=="failed"){
////                        println("broadcast failed..")
////
////
////
////
////                    }else{
////                        if ((!receivedMessage.startsWith("cmnd"))&&(!receivedMessage.startsWith("rfsh")) ){
////
////                            println("success..")
////                            val fan_db= fan_db.getInstance(context.requireContext())
////                            var receivedmessage_decoded= extract_response(receivedMessage)
////                            var  macip=receivedmessage_decoded[0]
////                            var  pole_num=receivedmessage_decoded[1]
////                            var  key_status=receivedmessage_decoded[2]
////                            var new_ip =receivedmessage_decoded[3]
////
////
////
////                            var statusList= fan_sub_type_decoder(key_status,fan_db.getfansByMacAddress(macip)[fan_db.getfansByMacAddress(macip).count()-1]!!.subtype.toString())
////
////                            val same_macs = fan_db.getfansByMacAddress(same_mac_list[0]!!.mac).sortedBy { it!!.subtype!!.toInt() }
////                            var index=0
////                            for (lightt in same_macs){
////                                fan_database1.updateStatusAndIpById(lightt!!.id,statusList[0+index],new_ip)
////                                index+=1
////
////                            }
////
////
////                        }else{
////
////                            println("broadcast failed..")
////
////                        }
////
////
////                    }
////
////
////                }, 8089, 500)
////
////
////            }
////
////            loopCount++
////
////            // تاخیر یک ثانیه قبل از ورود به دور جدید از حلقه
////            Thread.sleep(500)
////        } while (loopCount < 2)
////
////
////
////    }
////
////
////    // تکرار حلقه تا دو بار اجرا
////
////
////
////}
//
//
//
//
//val fanDatabase= fan_db.getInstance(this)
//fanLiveData = fanDatabase.getfansWithNonEmptyMacByRoomName(curent_room!!.room_name)
//fanLiveData.observe(this, androidx.lifecycle.Observer { lights ->
//    val musicplayer_layout= findViewById<LinearLayout>(R.id.musicplayer_layout)
//    if (lights.count() > 0){
//
//        device_fan.isEnabled=true
//        device_fan.visibility= View.VISIBLE
//        device_fan.setOnClickListener {
//
//            if (receivedBundel!=null) {
//                val receivedMessage = receivedBundel!!.getString("mode")
//
//                if (receivedMessage == "admin") {
//                    viewPager.currentItem=11
//                    musicplayer_layout.visibility= View.VISIBLE
//
//
//                }else{
//
//                    viewPager.currentItem=12
//                    musicplayer_layout.visibility= View.GONE
//
//
//                }
//
//            }else{
//                viewPager.currentItem=12
//                musicplayer_layout.visibility= View.GONE
//
//            }
//        }
//
//
//
//    }else{
//        device_fan.isEnabled=false
//        device_fan.visibility= View.GONE
//        viewPager.currentItem=0
//    }
//
//
//
//
//
//
//
//})
//
//
//





//
//
////
////fun fan_sub_type_coder(context: Context, mac: String?): String {
////    val fan_database = fan_db.getInstance(context)
////    val same_macs = mac?.let { fan_database.getfansByMacAddress(it) }
////    var key_status = "0000000000000000"
////
////    if (same_macs != null) {
////        var pole_count=0
////        for (fan in same_macs) {
////            val pole = fan?.subtype?.toInt() ?: 0
////            if (fan?.status == "1") {
////                println("on")
////                if (key_status.length >= pole) {
////                    val startIndex = pole-1
////                    val endIndex = startIndex + 1
////                    val newChar = "1"
////                    key_status = key_status.substring(0, startIndex) + newChar + key_status.substring(endIndex)
////                }
////            }else if (fan?.status == "0") {
////                println("off")
////                if (key_status.length >= pole) {
////                    val startIndex = pole-1
////                    val endIndex = startIndex + 1
////                    val newChar = "0"
////                    key_status = key_status.substring(0, startIndex) + newChar + key_status.substring(endIndex)
////                }
////            }
////            if (fan!!.subtype!!.toInt()>pole_count ){
////                pole_count=fan.subtype!!.toInt()
////
////            }
////
////        }
////        println(pole_count)
////        println(key_status)
////        key_status="${key_status.substring(0,pole_count)}${key_status.substring(pole_count).replace("0","-")}"
////
////    }
////    println(key_status)
////    return key_status
////}
////
////
////
////
////
////fun send_to_fan(mac:String?,subtype:String?,status:String?,myip:String,ip:String?){
////    var targetIP=ip
////
////    val message = "cmnd~>$mac~>Fano~>$subtype~>$status~>$myip"
////    val serverAddress = InetAddress.getByName(targetIP)
////    val serverPort = 8089
////
////    val socket = DatagramSocket()
////    val sendData = message.toByteArray()
////    val sendPacket = DatagramPacket(sendData, sendData.size, serverAddress, serverPort)
////    socket.send(sendPacket)
////    socket.close()
////
////}
////
////
////
////
////
////fun udp_fan (context: Context, fan: fan?){
////
////    var isResponseReceivedFirstTime = false
////
////
////    val timeoutMillis = 1000
////    var loopCount = 0
////
////    do {
////
////
////        if (loopCount == 0) {
////            if (!isResponseReceivedFirstTime){
////
////                val fan_database= fan_db.getInstance(context)
////                println(fan!!.status)
////                if(fan_database.getStatusById(fan!!.id)=="1"){
////
////                    fan_database.updateStatusbyId(fan.id, "0")
////
////                }else{
////                    fan_database.updateStatusbyId(fan.id, "1")
////
////                }
////            }
////
////            val fan_database= fan_db.getInstance(context)
////            send_to_fan(fan!!.mac,fan!!.subtype,
////                fan_sub_type_coder(context,fan.mac), checkIP(context),fan_database.get_from_db_fan(fan.id)!!.ip)
////            println("direct IP sent")
////
////
////
////
////            receiveUdpMessage({ receivedMessage ->
////                if (receivedMessage=="failed"){
////                    println("direct IP failed..")
////
////                }else{
////                    if ((!receivedMessage.startsWith("cmnd"))&&(!receivedMessage.startsWith("rfsh")) ){
////                        println("direct IP success..")
////                        isResponseReceivedFirstTime = true
////                        val fan_database2= fan_db.getInstance(context)
////                        var receivedmessage_decoded= extract_response(receivedMessage)
////                        var  macip=receivedmessage_decoded[0]
////                        var  pole_num=receivedmessage_decoded[1]
////                        var  key_status=receivedmessage_decoded[2]
////                        var ip =receivedmessage_decoded[3]
////
//////                    var statusList=light_sub_type_decoder(key_status,pole_num)
////
////                        val fan_database = fan_db.getInstance(context)
////                        val same_macs = fan_database.getfansByMacAddress(fan.mac).sortedBy { it!!.subtype!!.toInt() }
////                        var index=0
//////                    for (fan_same_mac in same_macs){
//////                        fan_database2.updateStatusbyId(fan_same_mac!!.id,statusList[index])
//////                        index+=1
//////
//////                    }
////
////                    }
////
////
////                }
////
////
////
////            }, 8089, 500)
////
////
////
////
////
////
////        }else if (loopCount == 1 && !isResponseReceivedFirstTime) {
////            println("broadcasting..")
////            val fan_database= fan_db.getInstance(context)
////            val broadcastIP= convertIpToBroadcast(context)
////            send_to_fan(fan!!.mac,fan.subtype,
////                fan_sub_type_coder(context,fan.mac), checkIP(context),broadcastIP)
////            println("broadcast sent")
////            var isrecived=false
////            receiveUdpMessage({ receivedMessage ->
////                if (receivedMessage=="failed"){
////                    println("broadcast failed..")
////                    if(fan_database.getStatusById(fan.id)=="1"){
////
////                        fan_database.updateStatusbyId(fan.id, "0")
////
////                    }else{
////                        fan_database.updateStatusbyId(fan.id, "1")
////
////                    }
////                    println("Broadcast failed..")
////
////                }else{
////
////                    if ((!receivedMessage.startsWith("cmnd"))&&(!receivedMessage.startsWith("rfsh")) ){
////                        println("success..")
////                        var receivedmessage_decoded= extract_response(receivedMessage)
////                        var  macip=receivedmessage_decoded[0]
////                        var  pole_num=receivedmessage_decoded[1]
////                        var  key_status=receivedmessage_decoded[2]
////                        var ip =receivedmessage_decoded[3]
////                        val light_database3= fan_db.getInstance(context)
////                        val same_macs = light_database3.getfansByMacAddress(fan.mac).sortedBy { it!!.subtype!!.toInt() }
////
//////                    var statusList=light_sub_type_decoder(key_status,pole_num)
//////                    var index=0
////                        for (light_same_mac3 in same_macs){
////                            light_database3.updatefanbyId(light_same_mac3!!.id , ip)
//////                        light_database3.updateStatusById(light_same_mac3!!.id,statusList[index])
//////                        index+=1
////                        }
////
////                    }else{
////                        println("Broadcast failed..")
////
////                    }
////
////                }
////
////
////            }, 8089, 500)
////
////
////        }
////
////        loopCount++
////
////        // تاخیر یک ثانیه قبل از ورود به دور جدید از حلقه
////        Thread.sleep(500)
////    } while (loopCount < 2) // تکرار حلقه تا دو بار اجرا
////
////
////
////}
////
////
////fun refresh_fan(context: Fragment, fan: fan, rooms: rooms?){
////
////    val fan_database1= fan_db.getInstance(context.requireContext())
////    val fan_list=fan_database1.getfansWithNonEmptyMacByRoomName2(rooms!!.room_name)
////    for (same_mac_list in fan_list) {
////        var isResponseReceivedFirstTime = false
////        var loopCount = 0
////
////        do {
////
////
////            if (loopCount == 0) {
////
////
////                send_refresh(fan.mac, checkIP(context.requireContext()),fan.ip)
////
////
////                println("direct IP sent")
////
////
////
////
////                receiveUdpMessage({ receivedMessage ->
////                    if (receivedMessage=="failed"){
////                        println("direct IP failed..")
////
////                    }else{
////                        if ((!receivedMessage.startsWith("cmnd"))&&(!receivedMessage.startsWith("rfsh")) ){
////                            println("direct IP success..")
////                            isResponseReceivedFirstTime = true
////                            val fan_db= fan_db.getInstance(context.requireContext())
////                            var receivedmessage_decoded= extract_response(receivedMessage)
////                            var  macip=receivedmessage_decoded[0]
////                            var  pole_num=receivedmessage_decoded[1]
////                            var  key_status=receivedmessage_decoded[2]
////                            var ip =receivedmessage_decoded[3]
////
////                            var statusList= fan_sub_type_decoder(key_status,fan_db.getfansByMacAddress(macip)[fan_db.getfansByMacAddress(macip).count()-1]!!.subtype.toString())
////
////
////                            val same_macs = fan_db.getfansByMacAddress(same_mac_list[0]!!.mac).sortedBy { it!!.subtype!!.toInt() }
////                            var index=0
////                            for (lightt in same_macs){
////                                fan_database1.updateStatusbyId(lightt!!.id,statusList[0+index])
////                                index+=1
////
////                            }
////
////
////
////
////                        }else{
////                            println("direct IP failed..")
////                        }
////
////
////
////                    }
////
////
////
////                }, 8089, 500)
////
////
////
////
////
////
////            }else if (loopCount == 1 && !isResponseReceivedFirstTime) {
////                println("broadcasting..")
////
////
////                val broadcastIP= convertIpToBroadcast(context.requireContext())
////                send_refresh(fan.mac, checkIP(context.requireContext()),broadcastIP)
////
////
////                println("broadcast sent")
////
////
////                receiveUdpMessage({ receivedMessage ->
////                    if (receivedMessage=="failed"){
////                        println("broadcast failed..")
////
////
////
////
////                    }else{
////                        if ((!receivedMessage.startsWith("cmnd"))&&(!receivedMessage.startsWith("rfsh")) ){
////
////                            println("success..")
////                            val fan_db= fan_db.getInstance(context.requireContext())
////                            var receivedmessage_decoded= extract_response(receivedMessage)
////                            var  macip=receivedmessage_decoded[0]
////                            var  pole_num=receivedmessage_decoded[1]
////                            var  key_status=receivedmessage_decoded[2]
////                            var new_ip =receivedmessage_decoded[3]
////
////
////
////                            var statusList= fan_sub_type_decoder(key_status,fan_db.getfansByMacAddress(macip)[fan_db.getfansByMacAddress(macip).count()-1]!!.subtype.toString())
////
////                            val same_macs = fan_db.getfansByMacAddress(same_mac_list[0]!!.mac).sortedBy { it!!.subtype!!.toInt() }
////                            var index=0
////                            for (lightt in same_macs){
////                                fan_database1.updateStatusAndIpById(lightt!!.id,statusList[0+index],new_ip)
////                                index+=1
////
////                            }
////
////
////                        }else{
////
////                            println("broadcast failed..")
////
////                        }
////
////
////                    }
////
////
////                }, 8089, 500)
////
////
////            }
////
////            loopCount++
////
////            // تاخیر یک ثانیه قبل از ورود به دور جدید از حلقه
////            Thread.sleep(500)
////        } while (loopCount < 2)
////
////
////
////    }
////
////
////    // تکرار حلقه تا دو بار اجرا
////
////
////
////}
//
//
//
//
