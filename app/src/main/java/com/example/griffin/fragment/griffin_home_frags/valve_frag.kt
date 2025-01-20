package com.example.griffin.fragment.griffin_home_frags

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.util.Calendar
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.griffin.R
import com.example.griffin.adapters.LearnvalveAdadpter
import com.example.griffin.database.alarm_handeler_db
import com.example.griffin.database.setting_network_db
import com.example.griffin.database.valve_db
import com.example.griffin.mudels.*
import com.example.griffin.myBroadcastReceiverr
import java.util.*

class valve_frag : Fragment() {
    private  val SharedViewModel: SharedViewModel by activityViewModels()
    private val requestQueue: Queue<Pair<valve, Button>> = LinkedList()
    private var isProcessing = false
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 400
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private fun processQueue() {
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
        val (valve, button) = requestQueue.poll()
        val previousStatus = valve.status

        val valve_db = valve_db.getInstance(requireContext())
        Thread {
            try {
                val isSuccessful = udp_valve(requireContext(), valve)
                requireActivity().runOnUiThread {
                    if (isSuccessful) {
                        // Update button state based on new status
//                        valve.status = if (valve.status == "1") "0" else "1"
                        println("suseesssssssssss"+valve.status)
                        button.setBackgroundResource(if (valve_db.get_from_db_valve(valve.id)?.status == "1") R.drawable.coler_on else R.drawable.coler_off)

                    } else {
                        // Handle failure case
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                handler.postDelayed({
                    processQueue()
                }, delayMillis)
            }
        }.start()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_valve_frag, container, false)
    }



    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var valve_layout =view.findViewById<ConstraintLayout>(R.id.valve_layout)
        val recyclerView: RecyclerView = view.findViewById(R.id.valve_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager

        val database= valve_db.getInstance(requireContext())

        SharedViewModel.current_room.observe(viewLifecycleOwner, androidx.lifecycle.Observer { room ->
            valve_layout.visibility=View.GONE

            val learn_valve_db= valve_db.getInstance(requireContext())
//            learn_light_db.deleteRowsWithNullOrEmptyMac()
            SharedViewModel.update_valve_to_learn_list( learn_valve_db.getvalvesByRoomName(room!!.room_name))

            SharedViewModel.valve_to_learn_list.observe(viewLifecycleOwner, androidx.lifecycle.Observer { newlist ->
                val valve_status=view.findViewById<Button>(R.id.valve_status)



                val adapter = LearnvalveAdadpter(newlist) { selectedItem ->
//                    Toast.makeText(requireContext(), selectedItem.Lname, Toast.LENGTH_SHORT).show()

                    val set_timer_valve_item_menu=view.findViewById<ImageButton>(R.id.set_timer_valve_item_menu)
                    val timetable_1=view.findViewById<TextView>(R.id.timetable_1)
                    val timetable_2=view.findViewById<TextView>(R.id.timetable_2)
                    val timetable_3=view.findViewById<TextView>(R.id.timetable_3)
                    val timetable_4=view.findViewById<TextView>(R.id.timetable_4)
                    val temp_disconnected=view.findViewById<TextView>(R.id.temp_disconnected)

                    timetable_1.setText("")
                    timetable_2.setText("")
                    timetable_3.setText("")
                    timetable_4.setText("")

                    timetable_1.movementMethod = ScrollingMovementMethod()
                    timetable_2.movementMethod = ScrollingMovementMethod()
                    timetable_3.movementMethod = ScrollingMovementMethod()
                    timetable_4.movementMethod = ScrollingMovementMethod()


                    val timetabel_list= arrayListOf<TextView>()
                    timetabel_list.add(timetable_1)
                    timetabel_list.add(timetable_2)
                    timetabel_list.add(timetable_3)
                    timetabel_list.add(timetable_4)

                    Thread{
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
                                var is_ok = refresh_valve(this,selectedItem,room)
                                val handler = Handler(Looper.getMainLooper())
                                handler.post {
                                    if (is_ok){
                                        temp_disconnected.visibility=View.GONE
                                        valve_layout.alpha=0f
                                        valve_layout.visibility=View.VISIBLE
                                        valve_layout.animate().alpha(1f).setDuration(400).setListener(null)

                                    }else{
                                        temp_disconnected.alpha=0f
                                        temp_disconnected.visibility=View.VISIBLE
                                        temp_disconnected.animate().alpha(1f).setDuration(400).setListener(null)

                                    }


                                    val database1= valve_db.getInstance(requireContext())
                                    var status=database1.get_from_db_valve(selectedItem.id)!!.status

                                    val alarmHandelerDb= alarm_handeler_db.getInstance(requireContext())
                                    val alarm_list=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.Vname)

                                    var counterr1=0
                                    for (alarm in alarm_list){
                                        var days=""
                                        var text=""
                                        for (item in alarm){
                                            var dayas2=item.alarm_day + " / "
                                            if (!days.contains(dayas2)){
                                                days+=dayas2

                                            }


                                        }
                                        var my_indexes= arrayListOf<Int>(0,1)
                                        for (num in my_indexes){
                                            try {

                                                val next=when(alarm[num].next_status){
                                                    "1"->"on"
                                                    "0"->"off"
                                                    else -> {
                                                        "LOST"
                                                    }
                                                }
                                                text += "  ==>  " + alarm[num].alarm_tyme + " (" + next + ") "
                                            }catch (e:Exception){

                                            }

                                        }

                                        timetabel_list[counterr1].setText(days.uppercase(Locale.getDefault())+text)
                                        counterr1+=1


                                    }


                                    fun on(){
                                        valve_status.setBackgroundResource(R.drawable.coler_on)


                                        status="1"

                                    }
                                    fun off(){
                                        valve_status.setBackgroundResource(R.drawable.coler_off)
                                        status="0"
                                    }

                                    when(status){
                                        "1"-> on()
                                        "0"-> off()

                                    }

                                    valve_status.setOnClickListener {
                                        try {
                                            requestQueue.add(Pair(selectedItem, valve_status))
                                            if (!isProcessing) {
                                                processQueue()
                                            }
                                        }catch (e:Exception){
                                            println(e)
                                        }




                                    }




                                    val inflater2 = LayoutInflater.from(requireContext())

                                    val customPopupView2: View = inflater2.inflate(R.layout.popup_delete_timetable, null)
                                    val popupView2: View = inflater2.inflate(R.layout.popup_delete_timetable, null)



//                    val edit_light_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
//                    val ok_name_light_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
//                    val learn_light_pupop = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
//                    val delete_light_pupop = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
//                    val on_off_test_light_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)


                                    val popupWidth2 = resources.getDimension(`in`.nouri.dynamicsizeslib.R.dimen._170mdp).toInt()
                                    val popupHeight2 = resources.getDimension(`in`.nouri.dynamicsizeslib.R.dimen._77mdp).toInt()

                                    // ایجاد لایه‌ی کاستوم


                                    // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                                    val popupWindow2 = PopupWindow(customPopupView2, popupWidth2, popupHeight2, true)

                                    val alertDialogBuilder2 = AlertDialog.Builder(requireContext())
                                    alertDialogBuilder2.setView(popupView2)

                                    val alertDialog2 = alertDialogBuilder2.create()
                                    alertDialog2.setCanceledOnTouchOutside(false)

                                    val yes_delete = customPopupView2.findViewById<Button>(R.id.yes_delete)
                                    val cancel_delete = customPopupView2.findViewById<Button>(R.id.cancel_delete)














                                    val days_list= arrayListOf<Int>()

                                    val inflater = LayoutInflater.from(requireContext())

                                    val customPopupView: View = inflater.inflate(R.layout.popup_setalarm, null)
                                    val popupView: View = inflater.inflate(R.layout.popup_setalarm, null)



//                    val edit_light_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
//                    val ok_name_light_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
//                    val learn_light_pupop = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
//                    val delete_light_pupop = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
//                    val on_off_test_light_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)


                                    val popupWidth = 480
                                    val popupHeight = 480

                                    // ایجاد لایه‌ی کاستوم


                                    // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                                    val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)

                                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                                    alertDialogBuilder.setView(popupView)

                                    val alertDialog = alertDialogBuilder.create()
                                    alertDialog.setCanceledOnTouchOutside(false)

                                    val hourPicker_on = customPopupView.findViewById<NumberPicker>(R.id.hourPicker_on)
                                    val hourValuesOn = arrayOf("--", *Array(24) { it.toString() })
                                    hourPicker_on.minValue = 0
                                    hourPicker_on.maxValue = hourValuesOn.size - 1
                                    hourPicker_on.displayedValues = hourValuesOn
                                    hourPicker_on.value = 0  // پیش‌فرض روی "--"

                                    hourPicker_on.setOnScrollListener { _, _ ->
                                        val currentValue = hourPicker_on.value
                                        if (currentValue == 0) {
                                            // کاربر هنوز مقداری انتخاب نکرده
                                            return@setOnScrollListener
                                        }
                                        if (hourPicker_on.scrollY > 0 && currentValue < hourValuesOn.size - 1) {
                                            hourPicker_on.value = currentValue + 1
                                        } else if (hourPicker_on.scrollY < 0 && currentValue > 1) {
                                            hourPicker_on.value = currentValue - 1
                                        }
                                    }

// Minute Picker On
                                    val minutePicker_on = customPopupView.findViewById<NumberPicker>(R.id.minutePicker_on)
                                    val minuteValuesOn = arrayOf("--", *Array(60) { it.toString() })
                                    minutePicker_on.minValue = 0
                                    minutePicker_on.maxValue = minuteValuesOn.size - 1
                                    minutePicker_on.displayedValues = minuteValuesOn
                                    minutePicker_on.value = 0  // پیش‌فرض روی "--"

                                    minutePicker_on.setOnScrollListener { _, _ ->
                                        val currentValue = minutePicker_on.value
                                        if (currentValue == 0) {
                                            // کاربر هنوز مقداری انتخاب نکرده
                                            return@setOnScrollListener
                                        }
                                        if (minutePicker_on.scrollY > 0 && currentValue < minuteValuesOn.size - 1) {
                                            minutePicker_on.value = currentValue + 1
                                        } else if (minutePicker_on.scrollY < 0 && currentValue > 1) {
                                            minutePicker_on.value = currentValue - 1
                                        }
                                    }

// Hour Picker Off
                                    val hourPicker_off = customPopupView.findViewById<NumberPicker>(R.id.hourPicker_off)
                                    val hourValuesOff = arrayOf("--", *Array(24) { it.toString() })
                                    hourPicker_off.minValue = 0
                                    hourPicker_off.maxValue = hourValuesOff.size - 1
                                    hourPicker_off.displayedValues = hourValuesOff
                                    hourPicker_off.value = 0  // پیش‌فرض روی "--"

                                    hourPicker_off.setOnScrollListener { _, _ ->
                                        val currentValue = hourPicker_off.value
                                        if (currentValue == 0) {
                                            // کاربر هنوز مقداری انتخاب نکرده
                                            return@setOnScrollListener
                                        }
                                        if (hourPicker_off.scrollY > 0 && currentValue < hourValuesOff.size - 1) {
                                            hourPicker_off.value = currentValue + 1
                                        } else if (hourPicker_off.scrollY < 0 && currentValue > 1) {
                                            hourPicker_off.value = currentValue - 1
                                        }
                                    }

// Minute Picker Off
                                    val minutePicker_off = customPopupView.findViewById<NumberPicker>(R.id.minutePicker_off)
                                    val minuteValuesOff = arrayOf("--", *Array(60) { it.toString() })
                                    minutePicker_off.minValue = 0
                                    minutePicker_off.maxValue = minuteValuesOff.size - 1
                                    minutePicker_off.displayedValues = minuteValuesOff
                                    minutePicker_off.value = 0  // پیش‌فرض روی "--"

                                    minutePicker_off.setOnScrollListener { _, _ ->
                                        val currentValue = minutePicker_off.value
                                        if (currentValue == 0) {
                                            // کاربر هنوز مقداری انتخاب نکرده
                                            return@setOnScrollListener
                                        }
                                        if (minutePicker_off.scrollY > 0 && currentValue < minuteValuesOff.size - 1) {
                                            minutePicker_off.value = currentValue + 1
                                        } else if (minutePicker_off.scrollY < 0 && currentValue > 1) {
                                            minutePicker_off.value = currentValue - 1
                                        }
                                    }





                                    val day_sa =customPopupView.findViewById<CheckBox>(R.id.day_sa)
                                    val day_su =customPopupView.findViewById<CheckBox>(R.id.day_su)
                                    val day_mo =customPopupView.findViewById<CheckBox>(R.id.day_mo)
                                    val day_tu =customPopupView.findViewById<CheckBox>(R.id.day_tu)
                                    val day_we =customPopupView.findViewById<CheckBox>(R.id.day_we)
                                    val day_th =customPopupView.findViewById<CheckBox>(R.id.day_th)
                                    val day_fr =customPopupView.findViewById<CheckBox>(R.id.day_fr)

                                    val setalarm =customPopupView.findViewById<Button>(R.id.setalarm)

                                    day_sa.setOnCheckedChangeListener{ _, isChecked ->
                                        if (isChecked){
                                            day_sa.setTextColor(Color.RED)
                                            days_list.add(7)

                                        }else{
                                            day_sa.setTextColor(Color.WHITE)
                                            days_list.remove(7)
                                        }
                                    }
                                    day_su.setOnCheckedChangeListener{ _, isChecked ->
                                        if (isChecked){
                                            day_su.setTextColor(Color.RED)
                                            days_list.add(1)

                                        }else{
                                            day_su.setTextColor(Color.WHITE)
                                            days_list.remove(1)
                                        }
                                    }
                                    day_mo.setOnCheckedChangeListener{ _, isChecked ->
                                        if (isChecked){
                                            day_mo.setTextColor(Color.RED)
                                            days_list.add(2)

                                        }else{
                                            day_mo.setTextColor(Color.WHITE)
                                            days_list.remove(2)
                                        }
                                    }
                                    day_tu.setOnCheckedChangeListener{ _, isChecked ->
                                        if (isChecked){
                                            day_tu.setTextColor(Color.RED)
                                            days_list.add(3)

                                        }else{
                                            day_tu.setTextColor(Color.WHITE)
                                            days_list.remove(3)
                                        }
                                    }
                                    day_we.setOnCheckedChangeListener{ _, isChecked ->
                                        if (isChecked){
                                            day_we.setTextColor(Color.RED)
                                            days_list.add(4)

                                        }else{
                                            day_we.setTextColor(Color.WHITE)
                                            days_list.remove(4)
                                        }
                                    }
                                    day_th.setOnCheckedChangeListener{ _, isChecked ->
                                        if (isChecked){
                                            day_th.setTextColor(Color.RED)
                                            days_list.add(5)

                                        }else{
                                            day_th.setTextColor(Color.WHITE)
                                            days_list.remove(5)
                                        }
                                    }
                                    day_fr.setOnCheckedChangeListener{ _, isChecked ->
                                        if (isChecked){
                                            day_fr.setTextColor(Color.RED)
                                            days_list.add(6)

                                        }else{
                                            day_fr.setTextColor(Color.WHITE)
                                            days_list.remove(6)
                                        }
                                    }

                                    set_timer_valve_item_menu.setOnClickListener {

                                        println(Calendar.SATURDAY)
                                        println(Calendar.SUNDAY)

                                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)




                                    }

                                    val alarm_database= alarm_handeler_db.getInstance(requireContext())
                                    fun getDisplayedValue(picker: NumberPicker, values: Array<String>): String {
                                        return values[picker.value]
                                    }

                                    setalarm.setOnClickListener {
                                        println(days_list)
                                        val alarmHandelerDb= alarm_handeler_db.getInstance(requireContext())
                                        val alarm_list=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.Vname)

                                        if (alarm_list.count() < 4) {
                                            val groups=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.Vname)
                                            if (days_list.count() > 0) {
                                                for (day in days_list) {
                                                    println(day)
                                                    val alarmName = "valve_" + selectedItem.Vname+"("+day+")" + "_1"
                                                    val alarmIntent = Intent(context, myBroadcastReceiverr::class.java)



                                                    alarmIntent.putExtra("alarmName", alarmName)
                                                    alarmIntent.putExtra("alarmH", hourPicker_on.value-1)
                                                    alarmIntent.putExtra("alarmM", minutePicker_on.value-1)
                                                    alarmIntent.putExtra("alarmD", day)
                                                    val pendingIntent = PendingIntent.getBroadcast(
                                                        context,
                                                        alarmName.hashCode(),
                                                        alarmIntent,
                                                        PendingIntent.FLAG_CANCEL_CURRENT
                                                    )
                                                    val alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                                    val calendar = Calendar.getInstance()

                                                    calendar.set(Calendar.DAY_OF_WEEK, day)
                                                    calendar.set(Calendar.HOUR_OF_DAY, hourPicker_on.value-1)
                                                    calendar.set(Calendar.MINUTE, minutePicker_on.value-1)
                                                    calendar.set(Calendar.SECOND, 0)
                                                    calendar.set(Calendar.MILLISECOND, 0)
                                                    val dayy = when (day) {
                                                        1 -> "su"
                                                        2 -> "mo"
                                                        3 -> "tu"
                                                        4 -> "we"
                                                        5 -> "th"
                                                        6 -> "fr"
                                                        7 -> "sa"
                                                        else -> {
                                                            "unknown"
                                                        }
                                                    }
                                                    val alarm_on = alarm()
                                                    alarm_on.alarm_day = dayy
                                                    alarm_on.alarm_name = alarmName
                                                    alarm_on.device_name = selectedItem.Vname
                                                    alarm_on.alarm_tyme = (hourPicker_on.value-1).toString() + ":" + (minutePicker_on.value-1).toString()
                                                    alarm_on.next_status = "1"
                                                    alarm_on.grooup = groups.count().toString()
                                                    if ((getDisplayedValue(hourPicker_on, hourValuesOn) != "--" )&& (getDisplayedValue(minutePicker_on, minuteValuesOn) != "--")) {


                                                        alarm_database.set_to_db_alarm(alarm_on)


                                                        alarmMgr.setExactAndAllowWhileIdle(
                                                            AlarmManager.RTC_WAKEUP,
                                                            calendar.timeInMillis,
                                                            pendingIntent
                                                        )
                                                        println(alarmName + " seted")
                                                    }

                                                    val alarmName2 = "valve_" + selectedItem.Vname+"("+day+")"+"_0" // نام یا شناسه یکتا برای الارم
                                                    val alarmIntent2 = Intent(context, myBroadcastReceiverr::class.java)
                                                    alarmIntent2.putExtra("alarmName", alarmName2)
                                                    alarmIntent2.putExtra("alarmH", hourPicker_off.value-1)
                                                    alarmIntent2.putExtra("alarmM", minutePicker_off.value-1)
                                                    alarmIntent2.putExtra("alarmD", day)
                                                    val pendingIntent2 =
                                                        PendingIntent.getBroadcast(context, alarmName2.hashCode(), alarmIntent2, PendingIntent.FLAG_CANCEL_CURRENT)

                                                    val alarmMgr2 = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                                    val calendar2 = Calendar.getInstance()

                                                    calendar2.set(Calendar.DAY_OF_WEEK, day)
                                                    calendar2.set(Calendar.HOUR_OF_DAY, hourPicker_off.value-1)
                                                    calendar2.set(Calendar.MINUTE, minutePicker_off.value-1)
                                                    calendar2.set(Calendar.SECOND, 0)
                                                    calendar2.set(Calendar.MILLISECOND, 0)

                                                    val day2 = when (day) {
                                                        1 -> "su"
                                                        2 -> "mo"
                                                        3 -> "tu"
                                                        4 -> "we"
                                                        5 -> "th"
                                                        6 -> "fr"
                                                        7 -> "sa"
                                                        else -> {
                                                            "unknown"
                                                        }
                                                    }
                                                    val alarm_off = alarm()
                                                    alarm_off.alarm_day = day2
                                                    alarm_off.alarm_name = alarmName2
                                                    alarm_off.device_name = selectedItem.Vname
                                                    alarm_off.alarm_tyme = (hourPicker_off.value-1).toString() + ":" + (minutePicker_off.value-1).toString()
                                                    alarm_off.next_status = "0"
                                                    alarm_off.grooup = groups.count().toString()
                                                    if ((getDisplayedValue(hourPicker_off, hourValuesOff) != "--" )&& (getDisplayedValue(minutePicker_off, minuteValuesOff) != "--")) {

                                                        alarm_database.set_to_db_alarm(alarm_off)

                                                        alarmMgr2.setExactAndAllowWhileIdle(
                                                            AlarmManager.RTC_WAKEUP,
                                                            calendar2.timeInMillis,
                                                            pendingIntent2
                                                        )
                                                        println(alarmName2 + " seted")


                                                    }
                                                }

                                                val alarmHandelerDb= alarm_handeler_db.getInstance(requireContext())
                                                val alarm_list=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.Vname)

                                                var counterr2=0
                                                for (alarm in alarm_list){
                                                    var days=""
                                                    var text=""
                                                    for (item in alarm){
                                                        var dayas2=item.alarm_day + " / "
                                                        if (!days.contains(dayas2)){
                                                            days+=dayas2

                                                        }


                                                    }
                                                    var my_indexes2= arrayListOf<Int>(0,1)
                                                    for (num in my_indexes2){
                                                        try {
                                                            val next=when(alarm[num].next_status){
                                                                "1"->"on"
                                                                "0"->"off"
                                                                else -> {
                                                                    "LOST"
                                                                }
                                                            }
                                                            text += "  ==>  " + alarm[num].alarm_tyme + " (" + next + ") "

                                                        }catch (e:Exception){

                                                        }

                                                    }

                                                    timetabel_list[counterr2].setText(days.uppercase(Locale.getDefault()) +text)
                                                    counterr2+=1


                                                }

                                                popupWindow.dismiss()
                                            }
                                        } else {
                                            Toast.makeText(requireContext(), "timetable is full", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    val selected_timetabel= arrayListOf<TextView>()

                                    timetable_1.setOnClickListener {
                                        if (timetable_1.text !=null &&timetable_1.text !=""){
                                            popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                                            selected_timetabel.clear()
                                            selected_timetabel.add(timetable_1)



                                        }
                                    }
                                    timetable_2.setOnClickListener {
                                        if (timetable_2.text !=null && timetable_2.text !=""){
                                            popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                                            selected_timetabel.clear()
                                            selected_timetabel.add(timetable_2)



                                        }
                                    }
                                    timetable_3.setOnClickListener {
                                        if (timetable_3.text !=null &&timetable_3.text !=""){
                                            popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                                            selected_timetabel.clear()
                                            selected_timetabel.add(timetable_3)



                                        }
                                    }
                                    timetable_4.setOnClickListener {
                                        if (timetable_4.text !=null && timetable_4.text !=""){
                                            popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                                            selected_timetabel.clear()
                                            selected_timetabel.add(timetable_4)



                                        }
                                    }
                                    cancel_delete.setOnClickListener {
                                        popupWindow2.dismiss()
                                    }
                                    yes_delete.setOnClickListener {

                                        val alarmHandelerDb= alarm_handeler_db.getInstance(requireContext())
                                        val alarm_list=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.Vname)



                                        val alarm_db= alarm_handeler_db.getInstance(requireContext())



                                        if (alarm_list.count() > 0 ){


                                            for(alarmm in alarm_list[timetabel_list.indexOf(selected_timetabel[0])]){



                                                val alarmName =alarmm.alarm_name
                                                val alarmIntent = Intent(context, myBroadcastReceiverr::class.java)
                                                val pendingIntent = PendingIntent.getBroadcast(context, alarmName.hashCode(), alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
                                                val alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                                alarmMgr.cancel(pendingIntent)

                                                alarm_db.delete_from_db_alarm(alarmm.id)


                                            }
                                            for (textview in timetabel_list){
                                                textview.setText("")
                                            }
                                            val alarmHandelerDb= alarm_handeler_db.getInstance(requireContext())
                                            val alarm_list=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.Vname)

                                            var counterr3=0
                                            for (alarm in alarm_list){
                                                var days=""
                                                var text=""
                                                for (item in alarm){
                                                    var dayas2=item.alarm_day + " / "
                                                    if (!days.contains(dayas2)){
                                                        days+=dayas2
                                                    }
                                                }
                                                var my_indexes3= arrayListOf<Int>(0,1)
                                                for (num in my_indexes3){
                                                    try {

                                                        val next=when(alarm[num].next_status){
                                                            "1"->"on"
                                                            "0"->"off"
                                                            else -> {
                                                                "LOST"
                                                            }
                                                        }
                                                        text += "  ==>  " + alarm[num].alarm_tyme + " (" + next + ") "
                                                    }catch (e:Exception){

                                                    }

                                                }

                                                timetabel_list[counterr3].setText(days.uppercase(Locale.getDefault())+text)
                                                counterr3+=1


                                            }
//                            selected_timetabel[0].setText("")
                                            Toast.makeText(requireContext(), "Timetable deleted", Toast.LENGTH_SHORT).show()





                                            popupWindow2.dismiss()

                                        }

                                    }

                                }

                            }else{
                                requireActivity().runOnUiThread{

                                    Toast.makeText(requireContext(), "Connect to a Griffin Network", Toast.LENGTH_SHORT).show()
                                }
                            }




                        }catch (e:Exception){
                            println(e)

                        }
                    }.start()









                }

                recyclerView.adapter = adapter
                adapter.setItems(newlist)






            })




        })


    }


}