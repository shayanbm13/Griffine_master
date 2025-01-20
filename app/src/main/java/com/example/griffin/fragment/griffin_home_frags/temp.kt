package com.example.griffin.fragment.griffin_home_frags

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.griffin.R
import com.example.griffin.adapters.LearnThermostatAdadpter
import com.example.griffin.adapters.LightAdapter
import com.example.griffin.database.Temperature_db
import com.example.griffin.database.light_db
import com.example.griffin.database.setting_network_db
import com.example.griffin.mudels.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.concurrent.thread


class temp : Fragment() {

    private val handler = Handler()
    private val delayMillis = 2000
    private val SharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_temperature, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView: RecyclerView = view.findViewById(R.id.themp_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager

        val database = Temperature_db.getInstance(requireContext())

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val database1 = Temperature_db.getInstance(requireContext())
            SharedViewModel.update_temp_to_learn_list(database1.getThermostatsByRoomName(room!!.room_name))

            SharedViewModel.temp_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->

                var is_doing = false
                var temp_layout = view.findViewById<ConstraintLayout>(R.id.temp_layout)
                val adapter = LearnThermostatAdadpter(newlist) { selectedItem ->
                    temp_layout.visibility = View.GONE
                    fun go_to_termostat(selectedItem: Thermostst){
                        println(selectedItem.name)

                        try {



                            val temp_db = Temperature_db.getInstance(requireContext())


                            var termostats_in_room=temp_db.getThermostatsByRoomName(room!!.room_name)

                            val coler_on_off = view.findViewById<CheckBox>(R.id.coler_on_off)
                            var current_temperature_textViwe=view.findViewById<TextView>(R.id.current_temperature)

                            val change_fan_status = view.findViewById<Button>(R.id.change_fan_status)
                            var fanstatus_num=0
                            val status1 = view.findViewById<ImageView>(R.id.status1)
                            val status2 = view.findViewById<ImageView>(R.id.status2)
                            val status3 = view.findViewById<ImageView>(R.id.status3)
                            val status4 = view.findViewById<TextView>(R.id.status4)
                            val circularSeekBar=view.findViewById<CircularSeekBar>(R.id.circularSeekBar)

                            val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
                            val winterside = view.findViewById<ImageButton>(R.id.radioOption1)
                            val summerside = view.findViewById<ImageButton>(R.id.radioOption2)
                            val temp_disconnected = view.findViewById<TextView>(R.id.temp_disconnected)

                            var current_thermostat=temp_db.get_from_db_Temprature(selectedItem.id)
                            var current_seekbar_temp=current_thermostat!!.temperature
                            var on_off_status=current_thermostat.on_off
                            var current_temperature=current_thermostat.current_temperature
                            var fanstatus=current_thermostat.fan_status
                            var mood=current_thermostat.mood

                            var isRefreshing = false




                            var handler= Handler(Looper.getMainLooper())







                            database1.get_from_db_Temprature(selectedItem.id)

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
                                    var new_s = new_db.get_from_db_Temprature(selectedItem.id)
                                    on_off_status=new_s!!.on_off
                                    println(new_s!!.on_off)
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
                                            var is_ok = refresh_thermostat(this, selectedItem)

                                            println(is_ok)
                                            isRefreshing = false
                                            handler.post{
                                                if(is_ok){
                                                    requireActivity().runOnUiThread {
                                                        update_statuses_ui()
                                                    }

//                                                    Thread{
//                                                        update_statuses_ui()
//
//                                                    }
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


                                        }else{
                                            requireActivity().runOnUiThread{

                                                Toast.makeText(requireContext(), "Connect to a Griffin Network", Toast.LENGTH_SHORT).show()
                                            }
                                        }


                                    }catch (e:Exception){
                                        println(e)
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
                                    try {
                                        udp_thermostat(this,selectedItem,selectedItem.mac,mood,SeekBarStatus(),fanstatus,on_off_status,termostats_in_room[0].ip)

                                    }catch (e:Exception){
                                        println()
                                    }finally {

                                        requireActivity().runOnUiThread {

                                            update_statuses_ui()
                                        }
//                                        handler.post{
////                                    Thread.sleep(2000)
//
//                                            update_statuses_ui()
//                                        }
                                        is_doing = false


                                    }
                                }.start()



//                        update_statuses_ui()


                            }
                            fun resetTimer() {
                                // حذف هر تایمر قبلی اگر وجود داشته باشد
                                is_doing=true
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
//                            radioGroup.setOnCheckedChangeListener { group, checkedId ->
//                                if (checkedId == R.id.radioOption1) {
//                                    println("mod  changed 0 ")
//                                    // اگر گزینه 1 انتخاب شد
//
//                                    // تغییر تصویر زمینه به تصویر مرتبط با گزینه 1
//                                    winterside.setBackgroundResource(R.drawable.winter_side_on);
//                                    summerside.setBackgroundResource(R.drawable.summer_side_off);
//                                    mood="0"
//                                    resetTimer()
//                                } else if (checkedId == R.id.radioOption2) {
//                                    println("mod  changed 1 ")
//                                    // اگر گزینه 2 انتخاب شد
//                                    // تغییر تصویر زمینه به تصویر مرتبط با گزینه 2
//                                    mood="1"
//                                    resetTimer()
//                                    summerside.setBackgroundResource(R.drawable.summer_side_on);
//                                    winterside.setBackgroundResource(R.drawable.winter_side_off);
//                                }
//                            }
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



                    }



                    if (!is_doing){
                        try {
                            var temp_layout = view.findViewById<ConstraintLayout>(R.id.temp_layout)

                            val temp_db = Temperature_db.getInstance(requireContext())


                            var termostats_in_room=temp_db.getThermostatsByRoomName(room!!.room_name)

                            val coler_on_off = view.findViewById<CheckBox>(R.id.coler_on_off)
                            var current_temperature_textViwe=view.findViewById<TextView>(R.id.current_temperature)

                            val change_fan_status = view.findViewById<Button>(R.id.change_fan_status)
                            var fanstatus_num=0
                            val status1 = view.findViewById<ImageView>(R.id.status1)
                            val status2 = view.findViewById<ImageView>(R.id.status2)
                            val status3 = view.findViewById<ImageView>(R.id.status3)
                            val status4 = view.findViewById<TextView>(R.id.status4)
                            val circularSeekBar=view.findViewById<CircularSeekBar>(R.id.circularSeekBar)

                            val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)

                            val winterside = view.findViewById<ImageButton>(R.id.radioOption1)
                            val summerside = view.findViewById<ImageButton>(R.id.radioOption2)
                            val temp_disconnected = view.findViewById<TextView>(R.id.temp_disconnected)

                            var current_thermostat=temp_db.get_from_db_Temprature(selectedItem.id)
                            var current_seekbar_temp=current_thermostat!!.temperature
                            var on_off_status=current_thermostat.on_off
                            var current_temperature=current_thermostat.current_temperature
                            var fanstatus=current_thermostat.fan_status
                            var mood=current_thermostat.mood

                            var isRefreshing = false




                            var handler= Handler(Looper.getMainLooper())







                            database1.get_from_db_Temprature(selectedItem.id)

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
                                    var new_s = new_db.get_from_db_Temprature(selectedItem.id)
                                    on_off_status=new_s!!.on_off
                                    println(new_s!!.on_off)
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
                                            var is_ok = refresh_thermostat(this, selectedItem)

                                            println(is_ok)
                                            isRefreshing = false
                                            handler.post{
                                                if(is_ok){
                                                    requireActivity().runOnUiThread {
                                                        update_statuses_ui()
                                                    }

//                                                    Thread{
//                                                        update_statuses_ui()
//
//                                                    }
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


                                        }else{
                                            requireActivity().runOnUiThread{

                                                Toast.makeText(requireContext(), "Connect to a Griffin Network", Toast.LENGTH_SHORT).show()
                                            }
                                        }


                                    }catch (e:Exception){
                                        println(e)
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
                                    try {
                                        udp_thermostat(this,selectedItem,selectedItem.mac,mood,SeekBarStatus(),fanstatus,on_off_status,termostats_in_room[0].ip)

                                    }catch (e:Exception){
                                        println()
                                    }finally {

                                        requireActivity().runOnUiThread {

                                            update_statuses_ui()
                                        }
//                                        handler.post{
////                                    Thread.sleep(2000)
//
//                                            update_statuses_ui()
//                                        }
                                        is_doing = false


                                    }
                                }.start()



//                        update_statuses_ui()


                            }
                            fun resetTimer() {

                                // حذف هر تایمر قبلی اگر وجود داشته باشد
                                is_doing=true
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
//                            radioGroup.setOnCheckedChangeListener { group, checkedId ->
//                                if (checkedId == R.id.radioOption1) {
//                                    println("mod  changed 0 ")
//                                    // اگر گزینه 1 انتخاب شد
//
//                                    // تغییر تصویر زمینه به تصویر مرتبط با گزینه 1
//                                    winterside.setBackgroundResource(R.drawable.winter_side_on);
//                                    summerside.setBackgroundResource(R.drawable.summer_side_off);
//                                    mood="0"
//                                    resetTimer()
//                                    println("A")
//                                } else if (checkedId == R.id.radioOption2) {
//                                    println("mod  changed 1 ")
//                                    // اگر گزینه 2 انتخاب شد
//                                    // تغییر تصویر زمینه به تصویر مرتبط با گزینه 2
//                                    mood="1"
//                                    resetTimer()
//                                    println("B")
//                                    summerside.setBackgroundResource(R.drawable.summer_side_on);
//                                    winterside.setBackgroundResource(R.drawable.winter_side_off);
//                                }
//                            }
                            coler_on_off.setOnClickListener {
                                if (coler_on_off.isChecked){
                                    coler_on_off.setBackgroundResource(R.drawable.coler_on)
                                    on_off_status="1"
                                    resetTimer()
                                    println("C")

                                }else{
                                    coler_on_off.setBackgroundResource(R.drawable.coler_off)
                                    on_off_status="0"
                                    resetTimer()
                                    println("D")


                                }
                            }




                            change_fan_status.setOnClickListener {

                                when(fanstatus_num ){
                                    0-> {
                                        fanstatus1()
                                        resetTimer()
                                        println("E")
                                        fanstatus_num+=1
                                    }
                                    1 ->{
                                        fanstatus2()
                                        resetTimer()
                                        println("F")
                                        fanstatus_num+=1
                                    }
                                    2->{
                                        fanstatus3()
                                        resetTimer()
                                        println("G")
                                        fanstatus_num+=1
                                    }
                                    3->{
                                        fanstatus4()
                                        resetTimer()
                                        println("H")
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
                                    println("F")

                                }
                            })







                        }catch (e:Exception){
                            println(e)
                        }


                    }else{
                        Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_LONG).show()
                        var temp_layout = view.findViewById<ConstraintLayout>(R.id.temp_layout)
                        temp_layout.visibility = View.GONE

                        viewLifecycleOwner.lifecycleScope.launch {


                            // شبیه‌سازی تغییر مقدار به false بعد از 4 ثانیه

                            // تایم‌اوت 6 ثانیه برای چک کردن مقدار
                            withTimeoutOrNull(6000) {
                                while (is_doing) {
                                    delay(100) // چک کردن مقدار با یک تاخیر کوتاه
                                }
                                // اگر مقدار false شود، این بخش اجرا می‌شود

                                println("sssssssssssssssssss")
                                requireActivity().runOnUiThread{

                                    go_to_termostat(selectedItem)
                                }

                            } ?: run {
                                println("timeout")
                                // اگر مقدار در 6 ثانیه همچنان true باقی بماند، این بخش اجرا می‌شود

                            }
                        }


                    }
//                    println(selectedItem.id)






                }

                recyclerView.adapter = adapter
                adapter.setItems(newlist)


            })


        })
    }


}


