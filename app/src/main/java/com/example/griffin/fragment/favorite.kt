package com.example.griffin.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.*
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.adapters.FavoriteAdapter
import com.example.griffin.adapters.LightAdapter
import com.example.griffin.database.*
import com.example.griffin.mudels.*
import java.util.*
import kotlin.concurrent.thread

class favorite : Fragment() {
     
    val SharedViewModel : SharedViewModel by activityViewModels()
    var Favorite_side= "user"
    private val handler = Handler()
    private val delayMillis = 2000

    private val requestQueue3: Queue<Pair<curtain, Button>> = LinkedList()
    private var isProcessing3 = false
    private val handler3 = Handler(Looper.getMainLooper())


    private val requestQueue: Queue<Pair<Light, SwitchCompat>> = LinkedList()
    private var isProcessing = false
    private val handler2 = Handler(Looper.getMainLooper())
    private val delayMillis2: Long = 400
    // کد‌ها و لازمه‌های مربوط به فرگمنت اول

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_favorite, container, false) // یا layout مورد نظر خود را قرار دهید
        // مراحل مربوط به نمایش و کنترل‌های فرگمنت اول

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         
    }

    @SuppressLint("MissingInflatedId", "CutPasteId")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        fun processQueue() {
            if (requestQueue.isEmpty()) {
                isProcessing = false
                return
            }
            val list = requestQueue.toList().toMutableList()

            if (list.size > 1) {
                // حذف همه آیتم‌ها به جز آخری
                list.subList(0, list.size - 1).clear()
            }

// پاک کردن صف و اضافه کردن آیتم باقی‌مانده از لیست
            requestQueue.clear()
            requestQueue.addAll(list)
            isProcessing = true
            val (light, switch) = requestQueue.poll()
            val previousStatus = light.status

            if (switch.isChecked) {
                light_db.getInstance(requireContext()).updateStatusById(light.id, "off")
            } else {
                light_db.getInstance(requireContext()).updateStatusById(light.id, "on")
            }

            Thread {
                try {
                    println("sended")
                    val res = udp_light(requireContext(), light)
                    println("2")
                    println(res)
                    requireActivity().runOnUiThread {
                        // Update UI if needed
                    }
                } catch (e: Exception) {
                    println(e)
                    print("lights page")
                } finally {
                    handler.postDelayed({
                        processQueue()
                    }, delayMillis2)
                }
            }.start()
        }


        fun View.setButtonScaleOnTouchListener() {
            setOnTouchListener(fun(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.animate().scaleX(0.8f).scaleY(0.8f).setDuration(150).start()
                         SoundManager.playSound()
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                    }
                }
                return false
            })
        }
        val Favorite_db_handler = favorite_db.Favorite_db.getInstance(requireContext())
        SharedViewModel.update_Favorite_list(Favorite_db_handler.getAllFavorite())



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




//          popup change status

        val inflater3 = LayoutInflater.from(requireContext())

        val customPopupView3: View = inflater3.inflate(R.layout.favorite_popup_curtain, null)



        val popupWidth3 = 650
        val popupHeight3 = 220
        val popupWindow3 = PopupWindow(customPopupView3, popupWidth3, popupHeight3, true)

        val alertDialogBuilder3 = AlertDialog.Builder(requireContext())
//        alertDialogBuilder3.setView(popupView3)

        val alertDialog3 = alertDialogBuilder3.create()
        alertDialog3.setCanceledOnTouchOutside(false)

        val curtain_open=customPopupView3.findViewById<Button>(R.id.curtain_open)
        val curtain_mid=customPopupView3.findViewById<Button>(R.id.curtain_mid)
        val curtain_close=customPopupView3.findViewById<Button>(R.id.curtain_close)
        val temp_disconnected=customPopupView3.findViewById<TextView>(R.id.temp_disconnected)
        var curtain_layout =customPopupView3.findViewById<ConstraintLayout>(R.id.curtain_layout)




        //  popup thermostat


        val inflater4 = LayoutInflater.from(requireContext())

        val customPopupView4: View = inflater4.inflate(R.layout.favorite_popup_temp, null)



        val popupWidth4 = 500
        val popupHeight4 = 600
        val popupWindow4 = PopupWindow(customPopupView4, popupWidth4, popupHeight4, true)

        val alertDialogBuilder4 = AlertDialog.Builder(requireContext())
//        alertDialogBuilder3.setView(popupView3)

        val alertDialog4 = alertDialogBuilder4.create()
        alertDialog4.setCanceledOnTouchOutside(false)


        //  popup light


        val inflater5 = LayoutInflater.from(requireContext())

        val customPopupView5: View = inflater5.inflate(R.layout.fvorite_popup_light, null)



        val popupWidth5 = 540
        val popupHeight5 = 240
        val popupWindow5 = PopupWindow(customPopupView5, popupWidth5, popupHeight5, true)

        val alertDialogBuilder5 = AlertDialog.Builder(requireContext())
//        alertDialogBuilder3.setView(popupView3)

        val alertDialog5 = alertDialogBuilder5.create()
        alertDialog5.setCanceledOnTouchOutside(false)
        val recyclerView2: RecyclerView = customPopupView5.findViewById(R.id.lights_recyclerview)
        val spanCount = 3
        val layoutManager2 = GridLayoutManager(requireContext(), 3)
        recyclerView2.layoutManager = layoutManager2
        val light_disconnected=customPopupView5.findViewById<TextView>(R.id.temp_disconnected)



        val yes_delete = customPopupView2.findViewById<Button>(R.id.yes_delete)
        val cancel_delete = customPopupView2.findViewById<Button>(R.id.cancel_delete)
        var favorite_item_menu=view.findViewById<ImageButton>(R.id.favorite_item_menu)

        val recyclerView: RecyclerView = view.findViewById(R.id.favorite_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager

        val plug_db_handler= plug_db.getInstance(requireContext())
        val fan_db_handler= fan_db.getInstance(requireContext())
        val valve_db_handler= valve_db.getInstance(requireContext())
        val curtain_db_handler= curtain_db.getInstance(requireContext())
        val therostat_db_handler= Temperature_db.getInstance(requireContext())
        val light_db_handler= light_db.getInstance(requireContext())

        SharedViewModel.Favorite_list.observe(viewLifecycleOwner, Observer { newlist ->
            SharedViewModel.is_doing.observe(viewLifecycleOwner, Observer { status ->

                val adapter = FavoriteAdapter(newlist) { selectedItem ->


                    if (Favorite_side=="user"){


                        try {

                            fun isConnectedToWifi(context: Context): Boolean {
                                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                                return networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
                            }
                            val wifiManager = requireContext().applicationContext.getSystemService(
                                Context.WIFI_SERVICE) as WifiManager
                            val wifiInfo = wifiManager.connectionInfo
                            val ssid = wifiInfo.ssid
                            val db_ssid = setting_network_db.getInstance(requireContext()).get_from_db_network_manual(1)?.modem_ssid

                            if (isConnectedToWifi(requireContext()) && ssid.replace("\"", "") ==db_ssid ){
                                Thread{



                                    if (selectedItem.type=="plug"){
                                        try {

                                            val current_plug=plug_db_handler.getPlugByCname(selectedItem.name)
                                            var list= arrayListOf<Plug>()
                                            if (current_plug != null) {
                                                list.add(current_plug)
                                            }


                                            try {
                                                UdpListener8089.pause()
                                                val ref_status=refresh_plug_for_scenario(requireContext(),list)
                                                if (ref_status.count()>0){
                                                    udp_plug(requireContext(),current_plug)
                                                }
                                                UdpListener8089.resume()
                                            }catch (e:Exception){
                                                println(e)

                                            }finally {
                                                UdpListener8089.resume()
                                            }
                                        }catch (e:Exception){

                                            println(e)
                                        }


                                    }else if (selectedItem.type=="fan"){
                                        try {
                                            val current_fan=fan_db_handler.getfanByCname(selectedItem.name)
                                            var list= arrayListOf<fan>()
                                            if (current_fan != null) {
                                                list.add(current_fan)
                                            }
                                            try {
                                                UdpListener8089.pause()
                                                val ref_status=refresh_fan_for_scenario(this,list)
                                                if (ref_status.count()>0){
                                                    udp_fan(requireContext(),current_fan)
                                                }
                                            }catch (e:Exception){
                                                println(e)
                                            }finally {
                                                UdpListener8089.resume()
                                            }

                                        }catch (e:Exception){

                                            println(e)
                                        }



                                    }else if (selectedItem.type=="valve"){
                                        try {
                                            val current_fan=valve_db_handler.getvalveByCname(selectedItem.name)
                                            var list= arrayListOf<valve>()
                                            if (current_fan != null) {
                                                list.add(current_fan)
                                            }

                                            try {
                                                UdpListener8089.pause()
                                                val ref_status=refresh_valve_for_scenario(requireContext(),list)
                                                if (ref_status.count()>0){
                                                    udp_valve(requireContext(),current_fan)
                                                }

                                            }catch (e:Exception){
                                                println(e)
                                            }finally {
                                                UdpListener8089.resume()
                                            }

                                        }catch (e:Exception){

                                            println(e)
                                        }

                                    }else if (selectedItem.type=="curtain"){

                                        try {
                                            val current_fan= selectedItem.name?.let {
                                                curtain_db_handler.getCurtainByCname(
                                                    it
                                                )
                                            }
                                            requireActivity().runOnUiThread{

                                                popupWindow3.showAtLocation(view, Gravity.CENTER, 0, 0)
                                            }




                                            var is_ok = current_fan?.let { refresh_curtain(this, it) }
                                            requireActivity().runOnUiThread{



                                                if (is_ok == true){
                                                    temp_disconnected.visibility=View.GONE
                                                    curtain_layout.alpha=0f
                                                    curtain_layout.visibility=View.VISIBLE
                                                    curtain_layout.animate().alpha(1f).setDuration(400).setListener(null)

                                                }else{

                                                    temp_disconnected.alpha=0f
                                                    temp_disconnected.visibility=View.VISIBLE
                                                    temp_disconnected.animate().alpha(1f).setDuration(400).setListener(null)
                                                }


                                                val database1= curtain_db.getInstance(requireContext())
                                                var status=database1.get_from_db_curtain(current_fan?.id)!!.status

//                    try {
////                        udp_light(this,selectedItem)
//                    }catch (e:Exception){
//                        println(e)
////
//                    }


                                                fun open(){
                                                    curtain_open.setBackgroundResource(R.drawable.curtain_open_on)
                                                    curtain_mid.setBackgroundResource(R.drawable.curtain_mid_off)
                                                    curtain_close.setBackgroundResource(R.drawable.curtain_close_off)
                                                    status="00"

                                                }
                                                fun mid(){
                                                    curtain_open.setBackgroundResource(R.drawable.curtain_open_off)
                                                    curtain_mid.setBackgroundResource(R.drawable.curtain_mid_on)
                                                    curtain_close.setBackgroundResource(R.drawable.curtain_close_off)
                                                    status="50"
                                                }
                                                fun close(){
                                                    curtain_open.setBackgroundResource(R.drawable.curtain_open_off)
                                                    curtain_mid.setBackgroundResource(R.drawable.curtain_mid_off)
                                                    curtain_close.setBackgroundResource(R.drawable.curtain_close_on)
                                                    status="99"
                                                }

                                                fun processQueue3() {
                                                    if (requestQueue3.isEmpty()) {
                                                        isProcessing3 = false
                                                        return
                                                    }
                                                    val list = requestQueue3.toList().toMutableList()

                                                    if (list.size > 1) {
                                                        // حذف همه آیتم‌ها به جز آخری
                                                        list.subList(0, list.size - 1).clear()
                                                    }

// پاک کردن صف و اضافه کردن آیتم باقی‌مانده از لیست
                                                    requestQueue3.clear()
                                                    requestQueue3.addAll(list)
                                                    isProcessing3 = true
                                                    val (curtain, button) = requestQueue3.poll()
                                                    val previousStatus = curtain.status
                                                    var status =when(button.id){
                                                        R.id.curtain_mid-> "50"
                                                        R.id.curtain_close-> "99"
                                                        R.id.curtain_open-> "00"


                                                        else -> {""}
                                                    }

                                                    Thread {
                                                        try {
                                                            val isSuccessful = udp_curtain(requireContext(), curtain,status)
                                                            requireActivity().runOnUiThread {
                                                                if (isSuccessful) {
                                                                    // Update button state based on new status
                                                                    if (status=="50"){
                                                                        mid()

                                                                    }else if (status=="99"){

                                                                        close()

                                                                    }else if (status=="00"){

                                                                        open()
                                                                    }
                                                                    println("suseesssssssssss")
//                                                        button.setBackgroundResource(if (curtain.status == "1") R.drawable.coler_on else R.drawable.coler_off)

                                                                } else {
                                                                    // Handle failure case
                                                                }
                                                            }
                                                        } catch (e: Exception) {
                                                            e.printStackTrace()
                                                        } finally {
                                                            handler3.postDelayed({
                                                                processQueue3()
                                                            }, delayMillis2)
                                                        }
                                                    }.start()
                                                }



                                                curtain_open.setOnClickListener {
                                                    requestQueue3.add(Pair(current_fan, curtain_open) as Pair<curtain, Button>?)
//                                            udp_curtain(requireContext(), selectedItem, "00")


                                                    if (!isProcessing3) {
                                                        processQueue3()
                                                    }
                                                }

                                                curtain_mid.setOnClickListener {
                                                    requestQueue3.add(Pair(current_fan, curtain_mid) as Pair<curtain, Button>?)
//                                            udp_curtain(requireContext(), selectedItem, "00")


                                                    if (!isProcessing3) {
                                                        processQueue3()
                                                    }
                                                }

                                                curtain_close.setOnClickListener {
                                                    requestQueue3.add(Pair(current_fan, curtain_close) as Pair<curtain, Button>?)
//                                            udp_curtain(requireContext(), selectedItem, "00")


                                                    if (!isProcessing3) {
                                                        processQueue3()
                                                    }
                                                }



                                                when(status){
                                                    "00"-> open()
                                                    "50"-> mid()
                                                    "99"-> close()
                                                }




                                            }


                                            requireActivity().runOnUiThread{
                                                popupWindow2.dismiss()
                                            }


                                        }catch (e:Exception){

                                            println(e)
                                        }



                                    }else if (selectedItem.type == "thermostat"){

                                        try {
                                            val current_fan=therostat_db_handler.getThermostatByName(selectedItem.name)




                                            var temp_layout = customPopupView4.findViewById<ConstraintLayout>(R.id.temp_layout)
//                    refresh_thermostat(this,selectedItem)
//                    refresh_thermostat(this,selectedItem,selectedItem!!.mac,"1","15","0","0",selectedItem.ip)
//                    handler.post{
//
//
//                    }
                                            val temp_db = Temperature_db.getInstance(requireContext())




                                            val coler_on_off = customPopupView4.findViewById<CheckBox>(R.id.coler_on_off)
                                            var current_temperature_textViwe=customPopupView4.findViewById<TextView>(R.id.current_temperature)

                                            val change_fan_status = customPopupView4.findViewById<Button>(R.id.change_fan_status)
                                            var fanstatus_num=0
                                            val status1 = customPopupView4.findViewById<ImageView>(R.id.status1)
                                            val status2 = customPopupView4.findViewById<ImageView>(R.id.status2)
                                            val status3 = customPopupView4.findViewById<ImageView>(R.id.status3)
                                            val status4 = customPopupView4.findViewById<TextView>(R.id.status4)
                                            val circularSeekBar=customPopupView4.findViewById<CircularSeekBar>(R.id.circularSeekBar)

                                            val radioGroup = customPopupView4.findViewById<RadioGroup>(R.id.radioGroup)
                                            val winterside = customPopupView4.findViewById<ImageButton>(R.id.radioOption1)
                                            val summerside = customPopupView4.findViewById<ImageButton>(R.id.radioOption2)
                                            val temp_disconnected = customPopupView4.findViewById<TextView>(R.id.temp_disconnected)

                                            var current_thermostat=temp_db.get_from_db_Temprature(current_fan?.id)
                                            var current_seekbar_temp=current_thermostat!!.temperature
                                            var on_off_status=current_thermostat.on_off
                                            var current_temperature=current_thermostat.current_temperature
                                            var fanstatus=current_thermostat.fan_status
                                            var mood=current_thermostat.mood

                                            var isRefreshing = false

// Inside your adapter click listener or wherever you call `refresh_thermostat`:


                                            var handler= Handler(Looper.getMainLooper())




                                            requireActivity().runOnUiThread{

                                                popupWindow4.showAtLocation(view, Gravity.CENTER, 0, 0)
                                            }


                                            if (current_fan != null) {
                                                therostat_db_handler.get_from_db_Temprature(current_fan.id)
                                            }

                                            fun fanstatus1(){
                                                fanstatus="1"
                                                if (status1.isVisible){
                                                    null

                                                }else{
                                                    status1.alpha = 0f
                                                    status1.visibility = View.VISIBLE

                                                    status1.animate()
                                                        .alpha(1f)
                                                        .setDuration(1000)
                                                        .setListener(null)

                                                }
                                                if (status2.isVisible){
                                                    status2.alpha = 1f
                                                    status2.visibility = View.GONE

                                                    status2.animate()
                                                        .alpha(0f)
                                                        .setDuration(1000)
                                                        .setListener(null)

                                                }

                                                if (status3.isVisible){

                                                    status3.alpha = 1f
                                                    status3.visibility = View.GONE

                                                    status4.animate()
                                                        .alpha(0f)
                                                        .setDuration(1000)
                                                        .setListener(null)
                                                }

                                                if (status4.isVisible){
                                                    status4.alpha = 1f
                                                    status4.visibility = View.GONE

                                                    status4.animate()
                                                        .alpha(0f)
                                                        .setDuration(1000)
                                                        .setListener(null)

                                                }

                                            }
                                            fun fanstatus2() {
                                                fanstatus="2"
                                                if (status1.isVisible){
                                                    null

                                                }else{
                                                    status1.alpha = 0f
                                                    status1.visibility = View.VISIBLE

                                                    status1.animate()
                                                        .alpha(1f)
                                                        .setDuration(1000)
                                                        .setListener(null)

                                                }
                                                if (status2.isVisible){
                                                    null
                                                }else{

                                                    status2.alpha = 0f
                                                    status2.visibility = View.VISIBLE

                                                    status2.animate()
                                                        .alpha(1f)
                                                        .setDuration(1000)
                                                        .setListener(null)
                                                }

                                                if (status3.isVisible){
                                                    status3.alpha = 1f
                                                    status3.visibility = View.GONE

                                                    status3.animate()
                                                        .alpha(0f)
                                                        .setDuration(1000)
                                                        .setListener(null)
                                                }

                                                if (status4.isVisible){
                                                    status4.alpha = 1f
                                                    status4.visibility = View.GONE

                                                    status4.animate()
                                                        .alpha(0f)
                                                        .setDuration(1000)
                                                        .setListener(null)
                                                }

                                            }
                                            fun fanstatus3(){
                                                fanstatus="3"
                                                if (status1.isVisible){
                                                    null

                                                }else{
                                                    status1.alpha = 0f
                                                    status1.visibility = View.VISIBLE

                                                    status1.animate()
                                                        .alpha(1f)
                                                        .setDuration(1000)
                                                        .setListener(null)

                                                }
                                                if (status2.isVisible){
                                                    null
                                                }else{

                                                    status2.alpha = 0f
                                                    status2.visibility = View.VISIBLE

                                                    status2.animate()
                                                        .alpha(1f)
                                                        .setDuration(1000)
                                                        .setListener(null)
                                                }
                                                if (status3.isVisible){
                                                    null
                                                }else{

                                                    status3.alpha = 0f
                                                    status3.visibility = View.VISIBLE

                                                    status3.animate()
                                                        .alpha(1f)
                                                        .setDuration(1000)
                                                        .setListener(null)

                                                }

                                                if (status4.isVisible){
                                                    status4.alpha = 1f
                                                    status4.visibility = View.GONE

                                                    status4.animate()
                                                        .alpha(0f)
                                                        .setDuration(1000)
                                                        .setListener(null)
                                                }
                                            }
                                            fun fanstatus4(){
                                                fanstatus="0"
                                                if (status1.isVisible){
                                                    status1.alpha = 1f
                                                    status1.visibility = View.GONE

                                                    status1.animate()
                                                        .alpha(0f)
                                                        .setDuration(1000)
                                                        .setListener(null)
                                                }
                                                if (status2.isVisible){
                                                    status2.alpha = 1f
                                                    status2.visibility = View.GONE

                                                    status2.animate()
                                                        .alpha(0f)
                                                        .setDuration(1000)
                                                        .setListener(null)


                                                }

                                                if (status3.isVisible){

                                                    status3.alpha = 1f
                                                    status3.visibility = View.GONE

                                                    status3.animate()
                                                        .alpha(0f)
                                                        .setDuration(1000)
                                                        .setListener(null)

                                                }


                                                if (status4.isVisible){
                                                    null
                                                }else{
                                                    status4.alpha = 0f
                                                    status4.visibility = View.VISIBLE

                                                    status4.animate()
                                                        .alpha(1f)
                                                        .setDuration(1000)
                                                        .setListener(null)

                                                }

                                            }
                                            fun update_statuses_ui(){
                                                try {
                                                    var new_db=Temperature_db.getInstance(requireContext())
                                                    var new_s = new_db.get_from_db_Temprature(current_fan?.id)
                                                    on_off_status=new_s!!.on_off
                                                    fanstatus=new_s!!.fan_status
                                                    current_temperature=new_s!!.current_temperature!!.toInt().toString()
                                                    mood=new_s!!.mood
                                                    current_seekbar_temp=new_s!!.temperature

                                                    circularSeekBar.setProgress(new_s!!.temperature!!.toInt())

                                                    current_temperature_textViwe.setText(current_temperature)

                                                    if (mood=="0"){
                                                        radioGroup.check(R.id.radioOption1)
                                                        winterside.setBackgroundResource(R.drawable.winter_side_on);
                                                        summerside.setBackgroundResource(R.drawable.summer_side_off);

                                                    }else{
                                                        radioGroup.check(R.id.radioOption2)
                                                        winterside.setBackgroundResource(R.drawable.winter_side_off);
                                                        summerside.setBackgroundResource(R.drawable.summer_side_on);
                                                    }
                                                    if (fanstatus=="0"){
                                                        fanstatus4()
                                                        fanstatus_num=0
                                                    }else if (fanstatus=="1"){
                                                        fanstatus1()
                                                        fanstatus_num=1

                                                    }else if (fanstatus=="2"){
                                                        fanstatus2()
                                                        fanstatus_num=2

                                                    }else if (fanstatus=="3"){
                                                        fanstatus3()
                                                        fanstatus_num=3

                                                    }

                                                    if (on_off_status=="0"){
                                                        coler_on_off.isChecked=false
                                                        coler_on_off.setBackgroundResource(R.drawable.coler_off)
                                                    }else if (on_off_status=="1"){
                                                        coler_on_off.isChecked=true
                                                        coler_on_off.setBackgroundResource(R.drawable.coler_on)
                                                    }


                                                }catch (e:Exception){
                                                    println(e)

                                                }


                                            }




                                            if (!isRefreshing) {
                                                isRefreshing = true
                                                thread {
                                                    // Your refresh_thermostat function logic here
                                                    var is_ok = refresh_thermostat(this, current_fan)

                                                    isRefreshing = false
                                                    handler.post{
                                                        if(is_ok){

                                                            update_statuses_ui()
                                                            temp_disconnected.visibility=View.GONE

                                                            temp_layout.alpha = 0f
                                                            temp_layout.visibility = View.VISIBLE

                                                            temp_layout.animate()
                                                                .alpha(1f)
                                                                .setDuration(1200)
                                                                .setListener(null)
                                                        }else{
                                                            temp_disconnected.alpha = 0f
                                                            temp_disconnected.visibility = View.VISIBLE

                                                            temp_disconnected.animate()
                                                                .alpha(1f)
                                                                .setDuration(1200)
                                                                .setListener(null)


                                                        }
//

                                                    }
//                            update_statuses_ui()
                                                }
                                            }







                                            update_statuses_ui()

                                            fun set_all_status(newMood:String?,newFanStatus:String?,newOnOffStatus:String?,newCurrentSeekbarTemp:String?){
                                                newMood?.let { mood = it }
                                                newFanStatus?.let { fanstatus = it }
                                                newOnOffStatus?.let { on_off_status = it }
                                                newCurrentSeekbarTemp?.let { current_seekbar_temp = it }

                                            }

                                            //                mood= termostats_in_room[0].mood.toString()
                                            //                fanstatus= termostats_in_room[0].fan_status.toString()
                                            //                on_off_status= termostats_in_room[0].on_off.toString()









                                            current_seekbar_temp=circularSeekBar.currentSelectedIndex






                                            fun SeekBarStatus():String{
                                                return circularSeekBar.currentSelectedIndex
                                            }







                                            val timerRunnable = Runnable {
                                                // اینجا کدی را اجرا کنید که می‌خواهید پس از 6 ثانیه از آخرین تغییر متغیرها انجام شود
                                                // مثلا:

                                                println(SeekBarStatus())
                                                println(on_off_status)
                                                println(fanstatus)
                                                println(mood)

                                                Thread{
                                                    udp_thermostat(this,current_fan,current_fan!!.mac,mood,SeekBarStatus(),fanstatus,on_off_status,current_fan.ip)
                                                    handler.post{
                                                        Thread.sleep(2000)

                                                        update_statuses_ui()
                                                    }
                                                }.start()



//                                    update_statuses_ui()


                                            }
                                            fun resetTimer() {
                                                // حذف هر تایمر قبلی اگر وجود داشته باشد
                                                handler.removeCallbacks(timerRunnable)

                                                // اضافه کردن تایمر جدید برای اجرای کد پس از 6 ثانیه
                                                handler.postDelayed(timerRunnable, delayMillis.toLong())
                                            }










                                            winterside.setOnClickListener{
                                                println("mod  changed 0 ")
                                                // اگر گزینه 1 انتخاب شد

                                                // تغییر تصویر زمینه به تصویر مرتبط با گزینه 1
                                                winterside.setBackgroundResource(R.drawable.winter_side_on);
                                                summerside.setBackgroundResource(R.drawable.summer_side_off);
                                                mood="0"
                                                resetTimer()
                                                println("A")

                                            }
                                            summerside.setOnClickListener{
                                                println("mod  changed 1 ")
                                                // اگر گزینه 2 انتخاب شد
                                                // تغییر تصویر زمینه به تصویر مرتبط با گزینه 2
                                                mood="1"
                                                resetTimer()
                                                println("B")
                                                summerside.setBackgroundResource(R.drawable.summer_side_on);
                                                winterside.setBackgroundResource(R.drawable.winter_side_off);
                                            }

                                            // تعیین عملکرد برای رادیو باتن‌ها
//                                        radioGroup.setOnCheckedChangeListener { group, checkedId ->
//                                            if (checkedId == R.id.radioOption1) {
//                                                // اگر گزینه 1 انتخاب شد
//
//                                                // تغییر تصویر زمینه به تصویر مرتبط با گزینه 1
//                                                winterside.setBackgroundResource(R.drawable.winter_side_on);
//                                                summerside.setBackgroundResource(R.drawable.summer_side_off);
//                                                mood="0"
//                                                resetTimer()
//                                            } else if (checkedId == R.id.radioOption2) {
//                                                // اگر گزینه 2 انتخاب شد
//                                                // تغییر تصویر زمینه به تصویر مرتبط با گزینه 2
//                                                mood="1"
//                                                resetTimer()
//                                                summerside.setBackgroundResource(R.drawable.summer_side_on);
//                                                winterside.setBackgroundResource(R.drawable.winter_side_off);
//                                            }
//                                        }



                                            coler_on_off.setOnClickListener {
                                                if (coler_on_off.isChecked){
                                                    coler_on_off.setBackgroundResource(R.drawable.coler_on)
                                                    on_off_status="1"
                                                    resetTimer()


                                                }else{
                                                    coler_on_off.setBackgroundResource(R.drawable.coler_off)
                                                    on_off_status="0"
                                                    resetTimer()


                                                }
                                            }




                                            change_fan_status.setOnClickListener {


                                                when(fanstatus_num ){
                                                    0-> {
                                                        fanstatus1()
                                                        resetTimer()
                                                        fanstatus_num+=1
                                                    }
                                                    1 ->{
                                                        fanstatus2()
                                                        resetTimer()
                                                        fanstatus_num+=1
                                                    }
                                                    2->{
                                                        fanstatus3()
                                                        resetTimer()
                                                        fanstatus_num+=1
                                                    }
                                                    3->{
                                                        fanstatus4()
                                                        resetTimer()
                                                        fanstatus_num=0
                                                    }
                                                }
                                            }



                                            circularSeekBar.setOnCircularSeekBarChangeListener(object :
                                                CircularSeekBar.OnCircularSeekBarChangeListener {
                                                override fun onProgressChanged(seekBar: CircularSeekBar?, progress: Int, fromUser: Boolean) {
                                                    // اینجا کدی که می‌خواهید هنگام تغییر مقدار سیکبار اجرا شود را قرار دهید.
                                                    // مثلاً اگر می‌خواهید مقدار جدید را نمایش دهید:


                                                    // اگر می‌خواهید کدی اجرا کنید که به وسایل هوشمند ارتباط برقرار کند:
                                                    // تابعی را اینجا فراخوانی کنید.
                                                    // به عنوان مثال: sendToSmartDevices(progress)
                                                }

                                                override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {
                                                    // وقتی کاربر شروع به جابجایی سیکبار می‌کند، این متد فراخوانی می‌شود.
                                                }

                                                override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
                                                    // وقتی کاربر پایان دادن به جابجایی سیکبار می‌دهد، این متد فراخوانی می‌شود.
                                                    current_seekbar_temp=circularSeekBar.currentSelectedIndex
                                                    resetTimer()

                                                }
                                            })



                                        }catch (e:Exception){

                                            println(e)
                                        }






                                    }else if (selectedItem.type=="light"){
                                        try {
                                            var current_fan=light_db_handler.getLightsByLname(selectedItem.name)




                                            var list= arrayListOf<Light>()
                                            if (current_fan != null) {
                                                list.add(current_fan)
                                            }

                                            val light_list=light_db_handler.getLightsByMacAddress(current_fan.mac)
                                            if (light_list.count() <= 3){
                                                requireActivity().runOnUiThread{
                                                    popupWindow5.showAtLocation(view, Gravity.CENTER, 0, 0)

                                                }
//                                    UdpListener8089.pause()
                                                val ref_status=refresh_light_for_scenario(requireContext(),list)
//                                    UdpListener8089.resume()
                                                if (ref_status.count()>0){

                                                    Thread{

                                                        requireActivity().runOnUiThread {
                                                            light_disconnected.visibility=View.GONE
                                                            recyclerView2.alpha=0f
                                                            recyclerView2.visibility=View.VISIBLE
                                                            recyclerView2.animate().alpha(1f).setDuration(400).setListener(null)

                                                        }


                                                    }.start()



                                                    requireActivity().runOnUiThread{

                                                        println(light_list)
                                                        SharedViewModel.update_light_to_learn_list(light_list)
                                                        SharedViewModel.light_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->


                                                            val adapter2 = LightAdapter(newlist) { selectedItem , switch ->

//                                                            val switch=view.findViewById<Switch>(R.id.light_on_of)
                                                                val faild_to_send= mutableListOf<Light>()
                                                                Thread{
                                                                    try {


                                                                        requestQueue.add(Pair(selectedItem, switch))
                                                                        if (!isProcessing) {
                                                                            processQueue()
                                                                        }
                                                                    }catch (e:Exception){
                                                                        println(e)
                                                                        //
                                                                    }
                                                                }.start()


                                                            }
                                                            recyclerView2.adapter = adapter2
                                                            adapter2.setItems(newlist as List<Light>)

                                                        })

                                                    }






                                                }else{


                                                    Thread{
                                                        requireActivity().runOnUiThread{

                                                            recyclerView2.visibility=View.GONE
                                                            light_disconnected.alpha=0f
                                                            light_disconnected.visibility=View.VISIBLE
                                                            light_disconnected.animate().alpha(1f).setDuration(400).setListener(null)
                                                        }

                                                    }.start()



                                                }




                                            }else{


                                                Thread{
                                                    try {

//                                    UdpListener8089.pause()
                                                        var refresh_light=refresh_light_2(requireContext(),list)
//                                    UdpListener8089.resume()
                                                        if (refresh_light){
//                                            Thread.sleep(200)
                                                            current_fan=light_db_handler.getLightsByLname(selectedItem.name)
                                                            println(current_fan.status)



                                                            udp_light(requireContext(),current_fan)

                                                        }



                                                    }catch (e:Exception){
                                                        println(e)
                                                        //
                                                    }
                                                }.start()
//                                            try {
//
//
//                                            }catch (e:Exception){
//
//                                                println(e)
//
//                                            }



                                            }




                                        }catch (e:Exception){
                                            println(e)

                                        }






                                    }





                                }.start()


                            }else{
                                requireActivity().runOnUiThread{
                                    Toast.makeText(requireContext(), "Connect to a Griffin Network", Toast.LENGTH_SHORT).show()

                                }
                            }

                        }catch (e:Exception){
                            println("favorite_ page : " + e)
                        }




//





                    }else if (Favorite_side=="delete"){

                        customPopupView2.findViewById<TextView>(R.id.text_msg).setText("Are you sure you want to delete ${selectedItem.name} ?")
                        popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                        yes_delete.setOnClickListener {

                            Favorite_db_handler.delete_from_db_Favorite(selectedItem.id)
                            popupWindow2.dismiss()
                            Toast.makeText(requireContext(),"Favorite ${selectedItem.name} Deleted",Toast.LENGTH_LONG).show()
                            SharedViewModel.update_Favorite_list(Favorite_db_handler.getAllFavorite())

                        }
                        cancel_delete.setOnClickListener {

                            popupWindow2.dismiss()
                            Toast.makeText(requireContext(),"Canceled",Toast.LENGTH_LONG).show()

                        }






                    }






                }

                recyclerView.adapter = adapter
                adapter.setItems(newlist)
                
                

            })
            
            
            
            
        })






            favorite_item_menu.setOnClickListener{
                println(Favorite_side)


                val popupMenu = PopupMenu(context, favorite_item_menu)
                popupMenu.gravity = Gravity.TOP or Gravity.END
                popupMenu.menuInflater.inflate(R.menu.favorite_menu, popupMenu.menu)
                if (Favorite_side== "delete"){
                    popupMenu.menu.findItem(R.id.delete_favorite).setTitle("cancel delete scenario")

                }else if(Favorite_side=="user"){
                    popupMenu.menu.findItem(R.id.delete_favorite).setTitle("delete favorite")

                }
                popupMenu.setOnMenuItemClickListener { menuItem ->


                    when(menuItem.itemId){


                        R.id.delete_favorite ->{
                            Toast.makeText(context,"delete Favorite is Active",Toast.LENGTH_SHORT).show()
                            if (Favorite_side== "delete"){
                                Favorite_side="user"

                                popupMenu.menu.findItem(R.id.delete_favorite).setTitle("delete favorite")
                                view.findViewById<TextView>(R.id.choosed_title).setText("Choose \nyour Favorite")

                                view.findViewById<TextView>(R.id.choosed_title).setTextColor(
                                    ColorStateList.valueOf(Color.WHITE)
                                )


                            }else if (Favorite_side=="user"){
                                println(Favorite_side)
                                popupMenu.menu.findItem(R.id.delete_favorite).setTitle("Cancel delete favorite")
                                view.findViewById<TextView>(R.id.choosed_title).setText("Delete \nyour Favorite")
                                Favorite_side="delete"

                                view.findViewById<TextView>(R.id.choosed_title).setTextColor(
                                    ColorStateList.valueOf(Color.RED)
                                )

                            }


                        }


                    }
                    true
                }
                popupMenu.show()
            }




    }

    override fun onDestroy() {
        super.onDestroy()
         
         
    }
    override fun onPause() {
        super.onPause()
         
    }


}

// فرگمنت‌های دیگر را هم بصورت مشابه پیاده‌سازی کنید
