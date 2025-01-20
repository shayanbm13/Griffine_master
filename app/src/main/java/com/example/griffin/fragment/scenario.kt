package com.example.griffin.fragment

//import com.example.griffin.mudels.CustomLoadingCircle
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.icu.util.Calendar
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.*
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.marginLeft
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.griffin.R
import com.example.griffin.adapters.CustomAdapter
import com.example.griffin.adapters.ScenarioAdapter
import com.example.griffin.dashboard
import com.example.griffin.database.*
import com.example.griffin.fragment.griffin_home_frags.temp
import com.example.griffin.griffin_home
import com.example.griffin.mudels.*
import com.example.griffin.myBroadcastReceiverr
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.util.*
import kotlin.collections.ArrayList
import com.example.griffin.mudels.scenario as scenario_mudel

class scenario : Fragment() {
     
    private lateinit var loadingIndicator: CircularProgressIndicator
    private lateinit var recyclerView2: RecyclerView
    private lateinit var adapter2: CustomAdapter
    var scenario_side= "user"


    fun taghsim(dividend: Double, divisor: Double): Double {
        val result = dividend / divisor // انجام تقسیم بدون ضرب در 100
        return String.format("%.4f", result).toDouble() // تبدیل نتیجه به چهار رقم اعشاری
    }


    fun calculatePercentage(part: Double, whole: Double): Double {
        // چک کردن اینکه کله صفر نباشد
        if (whole == 0.0) {
            throw IllegalArgumentException("عدد کله نمی‌تواند صفر باشد.")
        }
        // محاسبه درصد
        return (part / whole) * 100
    }

    fun getLightsGroupedBySameMac(lights: List<Light>): List<List<Light>> {
        val groupedLights = mutableMapOf<String, MutableList<Light>>()

        for (light in lights) {
            val mac = light.mac ?: continue // تایید کردن اینکه Mac خالی نباشد
            if (!groupedLights.containsKey(mac)) {
                groupedLights[mac] = mutableListOf()
            }
            groupedLights[mac]?.add(light)
        }

        // تبدیل Map به لیست از لیست‌ها
        return groupedLights.values.toList()
    }
    fun getfansGroupedBySameMac(lights: List<fan>): List<List<fan>> {
        val groupedLights = mutableMapOf<String, MutableList<fan>>()

        for (light in lights) {
            val mac = light.mac ?: continue // تایید کردن اینکه Mac خالی نباشد
            if (!groupedLights.containsKey(mac)) {
                groupedLights[mac] = mutableListOf()
            }
            groupedLights[mac]?.add(light)
        }

        // تبدیل Map به لیست از لیست‌ها
        return groupedLights.values.toList()
    }
    fun getcurtainGroupedBySameMac(curtains: List<curtain>): List<List<curtain>> {
        val groupedLights = mutableMapOf<String, MutableList<curtain>>()

        for (curtain in curtains) {
            val mac = curtain.mac ?: continue // تایید کردن اینکه Mac خالی نباشد
            if (!groupedLights.containsKey(mac)) {
                groupedLights[mac] = mutableListOf()
            }
            groupedLights[mac]?.add(curtain)
        }

        // تبدیل Map به لیست از لیست‌ها
        return groupedLights.values.toList()
    }
    fun getPlugsGroupedBySameMac(plugs: List<Plug>): List<List<Plug>> {
        val groupedLights = mutableMapOf<String, MutableList<Plug>>()

        for (plugg in plugs) {
            val mac = plugg.mac ?: continue // تایید کردن اینکه Mac خالی نباشد
            if (!groupedLights.containsKey(mac)) {
                groupedLights[mac] = mutableListOf()
            }
            groupedLights[mac]?.add(plugg)
        }

        // تبدیل Map به لیست از لیست‌ها
        return groupedLights.values.toList()
    }
    fun getvalvesGroupedBySameMac(valve: List<valve>): List<List<valve>> {
        val groupedLights = mutableMapOf<String, MutableList<valve>>()

        for (valvee in valve) {
            val mac = valvee.mac ?: continue // تایید کردن اینکه Mac خالی نباشد
            if (!groupedLights.containsKey(mac)) {
                groupedLights[mac] = mutableListOf()
            }
            groupedLights[mac]?.add(valvee)
        }

        // تبدیل Map به لیست از لیست‌ها
        return groupedLights.values.toList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         
    }

    private  val SharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_scenario, container, false)

        return view
    }


    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
         
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


        val activity =requireActivity() as dashboard
        val set_timer_plug_item_menu=activity.findViewById<ImageButton>(R.id.set_timer_plug_item_menu)
        val timetable_1=activity.findViewById<TextView>(R.id.timetable_1)
        val timetable_2=activity.findViewById<TextView>(R.id.timetable_2)
        val timetable_3=activity.findViewById<TextView>(R.id.timetable_3)
        val timetable_4=activity.findViewById<TextView>(R.id.timetable_4)
        val temp_disconnected=activity.findViewById<TextView>(R.id.temp_disconnected)

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



        var mode : String? = Config.mode

        val inflater = LayoutInflater.from(requireContext())

//        val customPopupView: View = inflater.inflate(R.layout.scenario_loading, null)
        val popupView: View = inflater.inflate(R.layout.scenario_loading, null)
        val popupWidth = 530
        val popupHeight = 530
        // ایجاد لایه‌ی کاستوم
        // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
//        val loadingView=popupView.findViewById<CustomLoadingCircle>(R.id.custom_loading_view)
        val popupWindow = PopupWindow(popupView, popupWidth, popupHeight, true)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(popupView)
        val alertDialog = alertDialogBuilder.create()
        val loading_view=popupView.findViewById<CustomLoadingLine>(R.id.loadingAnim)
//        loading_view.increaseProgress(20)


//        loadingView.startLoading()


//




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
        var scenario_item_menu=view.findViewById<ImageButton>(R.id.scenarioitem_menu)


        val scenario_db_handler_2 = scenario_db.Scenario_db.getInstance(requireContext())


        SharedViewModel.update_Scenario_list( scenario_db_handler_2.getAllScenario())




        val inflater3 = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customPopupView3: View = inflater3.inflate(R.layout.edit_scenario_popup, null)



        val popupWidth3 = 450
        val popupHeight3 = 450
        val popupWindow3 = PopupWindow(customPopupView3, popupWidth3, popupHeight3, true)
        popupWindow3.isFocusable = true
        val alertDialogBuilder3 = AlertDialog.Builder(requireContext())
        alertDialogBuilder2.setView(customPopupView3)

        val alertDialog3 = alertDialogBuilder3.create()
        alertDialog3.setCanceledOnTouchOutside(false)


        val inflater4 = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customPopupView4: View = inflater4.inflate(R.layout.scenario_undone, null)



        val popupWidth4 = 450
        val popupHeight4 = 500
        val popupWindow4 = PopupWindow(customPopupView4, popupWidth4, popupHeight4, true)
        popupWindow4.isFocusable = true

        customPopupView4.findViewById<Button>(R.id.ok_done).setOnClickListener {
            popupWindow4.dismiss()

        }
        val alertDialogBuilder4 = AlertDialog.Builder(requireContext())
        alertDialogBuilder2.setView(customPopupView4)

        val alertDialog4 = alertDialogBuilder4.create()
        alertDialog4.setCanceledOnTouchOutside(false)

        val recyclerView: RecyclerView = view.findViewById(R.id.scenario_recyclerview)
        val spanCount = 3
        val layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.layoutManager = layoutManager

        SharedViewModel.Scenario_list.observe(viewLifecycleOwner, Observer { newlist ->

            SharedViewModel.is_doing.observe(viewLifecycleOwner, Observer { status ->
                println(status)


                val adapter = ScenarioAdapter(newlist) { selectedItem ->

                    if (scenario_side=="user"){

                        println("userrrr")




                        if ((status != "bussy") || (status==null)){
                            Toast.makeText(requireContext(), selectedItem.scenario_name, Toast.LENGTH_SHORT).show()
                            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

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
                                    requireActivity().runOnUiThread {
                                        SharedViewModel.update_is_doing("bussy")
                                    }

                                    loading_view.increaseProgress(0.0)

                                    val light_database_handler=light_db.getInstance(requireContext())
                                    val thermostat_database_handler=Temperature_db.getInstance(requireContext())
                                    val curtain_database_handler=curtain_db.getInstance(requireContext())
                                    val valve_database_handler=valve_db.getInstance(requireContext())
                                    val fan_database_handler=fan_db.getInstance(requireContext())
                                    val plug_database_handler=plug_db.getInstance(requireContext())

                                    var next =true

                                    var all_scenario_to_do= ""
                                    var scenario_done= listOf<Any>()
                                    var light_scenario_done= ArrayList<String?>()
                                    var thermostat_scenario_done= ArrayList<String?>()
                                    var curtain_scenario_done= ArrayList<String?>()
                                    var valve_scenario_done= ArrayList<String?>()
                                    var fan_scenario_done= ArrayList<String?>()
                                    var plug_scenario_done= ArrayList<String?>()
                                    var music_scenario_done= ArrayList<String?>()



                                    val scenario_database_handler= scenario_db.Scenario_db.getInstance(requireContext())
                                    var light_scenario= emptyList<String>()
                                    if (selectedItem.light?.isEmpty() == false) {
                                        if (all_scenario_to_do!=""){
                                            all_scenario_to_do+=selectedItem.light
                                        }else{
                                            all_scenario_to_do+=","+selectedItem.light
                                        }

                                        light_scenario= selectedItem.light!!.split(",")


                                    }

                                    var thermostat_scenario=emptyList<String>()
                                    if (selectedItem.thermostat?.isEmpty() == false) {
                                        thermostat_scenario=selectedItem.thermostat!!.split(",")
                                        if (all_scenario_to_do!=""){
                                            all_scenario_to_do+=selectedItem.thermostat
                                        }else{
                                            all_scenario_to_do+=","+selectedItem.thermostat
                                        }
                                    }


                                    var curtain_scenario= emptyList<String>()
                                    if (selectedItem.curtain?.isEmpty() == false) {
                                        curtain_scenario=selectedItem.curtain!!.split(",")
                                        if (all_scenario_to_do!=""){
                                            all_scenario_to_do+=selectedItem.curtain
                                        }else{
                                            all_scenario_to_do+=","+selectedItem.curtain
                                        }
                                    }




                                    var valve_scenario= emptyList<String>()
                                    if (selectedItem.valve?.isEmpty() == false) {
                                        valve_scenario=selectedItem.valve!!.split(",")
                                        if (all_scenario_to_do!=""){
                                            all_scenario_to_do+=selectedItem.valve
                                        }else{
                                            all_scenario_to_do+=","+selectedItem.valve
                                        }
                                    }


                                    var fan_scenario= emptyList<String>()
                                    if (selectedItem.fan?.isEmpty() == false) {
                                        fan_scenario=selectedItem.fan!!.split(",")
                                        if (all_scenario_to_do!=""){
                                            all_scenario_to_do+=selectedItem.fan
                                        }else{
                                            all_scenario_to_do+=","+selectedItem.fan
                                        }
                                    }
                                    var plug_scenario =  emptyList<String>()
                                    if (selectedItem.plug?.isEmpty() == false) {
                                        plug_scenario=selectedItem.plug!!.split(",")
                                        if (all_scenario_to_do!=""){
                                            all_scenario_to_do+=selectedItem.plug
                                        }else{
                                            all_scenario_to_do+=","+selectedItem.plug
                                        }
                                    }

                                    var music_scenario =  emptyList<String>()
                                    if (selectedItem.music?.isEmpty() == false) {
                                        music_scenario=selectedItem.music!!.split(",")
                                        if (all_scenario_to_do!=""){
                                            all_scenario_to_do+=selectedItem.music
                                        }else{
                                            all_scenario_to_do+=","+selectedItem.music
                                        }
                                    }


                                    val all_scenario_count=plug_scenario.count()+fan_scenario.count()+valve_scenario.count()+curtain_scenario.count()+thermostat_scenario.count()+light_scenario.count()+music_scenario.count()




                                    if (music_scenario.size !=0 ){

                                        val playlist= mutableListOf<MusicModel>()

                                        val urls = mutableListOf<String>()
                                        for (music in music_scenario){
                                            urls.add(music)

                                        }
                                        for ( music in musicList2){
                                            if (music.audioUrl in urls ){
                                                playlist.add(music)


                                            }else{
                                                println(music.audioUrl)
                                            }
                                        }

                                        playlist[0].isplaying="true"
                                        musicList=playlist

//                        println(final_light_to_do)
                                        println(music_scenario)



                                        val musicPlayer = Music_player.getInstance(requireContext())
                                        musicPlayer.playMusic(musicList[0].audioUrl)






                                        Handler(Looper.getMainLooper()).post {
                                            loading_view.increaseProgress((taghsim(1.0 ,all_scenario_count.toDouble() ))* 100)
//
                                        }


                                        music_scenario_done.add("music")












                                    }






                                    if (light_scenario.size !=0 ){
//                        Handler(Looper.getMainLooper()).post {
////                            loadingIndicator =view.findViewById(R.id.loadingIndicator)
//                            loadPercentage.addPercentage(20) // for example
//                            updateLoadingIndicator()
//                        }

                                        var current_light_in_scenario= ArrayList<String>()
                                        for (light in light_scenario){
                                            current_light_in_scenario.add(light.split("#")[0])


                                        }
                                        println(current_light_in_scenario)
                                        println(light_scenario)
                                        val same_macs=light_database_handler.getLightsBySameMacForLnames(current_light_in_scenario)
                                        val final_same_mac= ArrayList<Light>()
                                        for (light in same_macs){
                                            final_same_mac.add(light[0])

                                        }
//                        println(same_macs)
//                        val refreshed_lights=refresh_light_for_scenario(requireContext(),final_same_mac)



//                        println("lights refreshed...")

                                        val current_light_to_change= arrayListOf<Light>()
                                        for (light in light_scenario){



                                            val current_light_in_scenario=light.split("#")
                                            val current_light_in_db=light_database_handler.getLightsByLname(current_light_in_scenario[0])
                                            current_light_to_change.add(current_light_in_db)










                                        }
                                        val sorted_lights = getLightsGroupedBySameMac(current_light_to_change)
                                        val final_light_to_do= arrayListOf<List<Light>>()
                                        for (same_mac in sorted_lights){
                                            val listt = same_mac.map { Light ->
                                                val status = light_scenario.find { it.startsWith("${Light.Lname}#") }?.substringAfter("#") ?: ""
                                                Light.status = status
                                                Light

                                            }
//                                println(current_light_in_scenario)
//                                println(listt[0].Lname)
//                                println(listt[0].status)


                                            final_light_to_do.add(listt)

                                        }
//                        println(final_light_to_do)

                                        if (next){
                                            next=false
                                            for (same in final_light_to_do){

                                                try {
                                                    println((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)

                                                    val send=udp_light_scenario(requireContext(),same)


                                                    if (send){
                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)
//
                                                        }

                                                        for (light in same){

                                                            light_scenario_done.add(light.Lname)
                                                            println(same.count())
                                                            println(all_scenario_count)




                                                        }


                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }




                                    }
                                    if (plug_scenario.size !=0 ){
//                        Handler(Looper.getMainLooper()).post {
////                            loadingIndicator =view.findViewById(R.id.loadingIndicator)
//                            loadPercentage.addPercentage(20) // for example
//                            updateLoadingIndicator()
//                        }

                                        var current_plug_in_scenario= ArrayList<String>()
                                        for (plug in plug_scenario){
                                            current_plug_in_scenario.add(plug.split("#")[0])


                                        }
                                        println(plug_scenario)
                                        val same_macs=plug_database_handler.getPlugsBySameMacForPnames(current_plug_in_scenario)
                                        val final_same_mac= ArrayList<Plug>()
                                        for (plug in same_macs){
                                            final_same_mac.add(plug[0])

                                        }
//                        println(same_macs)
//                        val refreshed_plugs=refresh_plug_for_scenario(requireContext(),final_same_mac)
//
//
//
//                        println("plugs refreshed...")

                                        val current_plug_to_change= arrayListOf<Plug>()
                                        for (plug in plug_scenario){



                                            val current_plug_in_scenario=plug.split("#")
                                            val current_plug_in_db=plug_database_handler.getPlugByCname(current_plug_in_scenario[0])
                                            if (current_plug_in_db != null) {
                                                current_plug_to_change.add(current_plug_in_db)
                                            }









                                        }

                                        val sorted_plugs = getPlugsGroupedBySameMac(current_plug_to_change)


                                        val final_plug_to_do= arrayListOf<List<Plug>>()
                                        for (same_mac in sorted_plugs){
                                            val listt = same_mac.map { Plug ->
                                                val status = plug_scenario.find { it.startsWith("${Plug.Pname}#") }?.substringAfter("#") ?: ""
                                                Plug.status = status
                                                Plug

                                            }
                                            println(current_plug_in_scenario)
                                            println(listt[0].Pname)
                                            println(listt[0].status)


                                            final_plug_to_do.add(listt)

                                        }

                                        println(sorted_plugs)
                                        next=true

                                        if (next){
                                            next=false
                                            for (same in final_plug_to_do){

                                                try {
                                                    println((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)

                                                    val send=udp_plug_scenario(requireContext(),same)


                                                    if (send){
                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)
//
                                                        }
                                                        for (plug in same){
                                                            plug_scenario_done.add(plug.Pname)
                                                            println(same.count())
                                                            println(all_scenario_count)

//                                            println(taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))

//                                            val a =12
//                                            println("dddddddddddddddddddddddddddddddd $a")





                                                        }


                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }




                                    }
                                    if (valve_scenario.size !=0 ){
//                        Handler(Looper.getMainLooper()).post {
////                            loadingIndicator =view.findViewById(R.id.loadingIndicator)
//                            loadPercentage.addPercentage(20) // for example
//                            updateLoadingIndicator()
//                        }

                                        var current_valve_in_scenario= ArrayList<String>()
                                        for (valve in valve_scenario){
                                            current_valve_in_scenario.add(valve.split("#")[0])


                                        }
                                        println(valve_scenario)
                                        val same_macs=valve_database_handler.getValvesBySameMacForPnames(current_valve_in_scenario)
                                        val final_same_mac= ArrayList<valve>()
                                        for (valve in same_macs){
                                            final_same_mac.add(valve[0])

                                        }
//                        println(same_macs)

                                        val current_valve_to_change= arrayListOf<valve>()
                                        for (valve in valve_scenario){


                                            println(valve)
                                            val current_valve_in_scenario=valve.split("#")
                                            val current_valve_in_db=valve_database_handler.getvalveByCname(current_valve_in_scenario[0])
                                            if (current_valve_in_db != null) {
                                                current_valve_to_change.add(current_valve_in_db)
                                            }










                                        }
                                        val sorted_valve = getvalvesGroupedBySameMac(current_valve_to_change)
                                        val final_valve_to_do= arrayListOf<List<valve>>()
                                        for (same_mac in sorted_valve){
                                            val listt = same_mac.map { valve ->
                                                val status = valve_scenario.find { it.startsWith("${valve.Vname}#") }?.substringAfter("#") ?: ""
                                                valve.status = status
                                                valve

                                            }
//                                println(current_light_in_scenario)
//                                println(listt[0].Lname)
//                                println(listt[0].status)


                                            final_valve_to_do.add(listt)

                                        }
                                        next=true

                                        if (next){
                                            next=false
                                            for (same in final_valve_to_do){

                                                try {
                                                    println((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)

                                                    val send=udp_valve_for_scenario(requireContext(),same)


                                                    if (send){

                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)
//
                                                        }
                                                        for (valve in same){
                                                            valve_scenario_done.add(valve.Vname)
                                                            println(same.count())
                                                            println(all_scenario_count)

//                                            println(taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))

//                                            val a =12
//                                            println("dddddddddddddddddddddddddddddddd $a")





                                                        }


                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }




                                    }

                                    if (curtain_scenario.size !=0 ){
//                        Handler(Looper.getMainLooper()).post {
////                            loadingIndicator =view.findViewById(R.id.loadingIndicator)
//                            loadPercentage.addPercentage(20) // for example
//                            updateLoadingIndicator()
//                        }

                                        var current_curtain_in_scenario= ArrayList<String>()
                                        for (curtain in curtain_scenario){
                                            current_curtain_in_scenario.add(curtain.split("#")[0])


                                        }
                                        println(curtain_scenario)
                                        val same_macs=curtain_database_handler.getcurtainsBySameMacForPnames(current_curtain_in_scenario)
                                        val final_same_mac= ArrayList<curtain>()
                                        for (curtain in same_macs){
                                            final_same_mac.add(curtain[0])

                                        }
//                        println(same_macs)
//                        val refreshed_curtain= refresh_curtain_for_scenario(this,final_same_mac)
//
//
//
//                        println("curtains refreshed...")

                                        val current_curtain_to_change= arrayListOf<curtain>()
                                        for (curtain in curtain_scenario){


                                            println(curtain)
                                            val current_curtain_in_scenario=curtain.split("#")
                                            val current_curtain_in_db=curtain_database_handler.getCurtainByCname(current_curtain_in_scenario[0])
                                            if (current_curtain_in_db != null) {
                                                current_curtain_to_change.add(current_curtain_in_db)
                                            }










                                        }
                                        val sorted_curtain = getcurtainGroupedBySameMac(current_curtain_to_change)

                                        val final_curtain_to_do= arrayListOf<List<curtain>>()
                                        for (same_mac in sorted_curtain){
                                            val listt = same_mac.map { curtain ->
                                                val status = curtain_scenario.find { it.startsWith("${curtain.Cname}#") }?.substringAfter("#") ?: ""
                                                curtain.status = status
                                                curtain

                                            }
//                                println(current_light_in_scenario)
//                                println(listt[0].Lname)
//                                println(listt[0].status)


                                            final_curtain_to_do.add(listt)

                                        }
                                        next=true

                                        if (next){
                                            next=false
                                            for (same in final_curtain_to_do){

                                                try {
//                                    println((taghsim(1.0 ,all_scenario_count.toDouble() ))* 100)

                                                    val send= udp_curtain_for_scenario(requireContext(),same[0],same[0].status)


                                                    if (send){
                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(1.0 ,all_scenario_count.toDouble() ))* 100)
//
                                                        }

                                                        curtain_scenario_done.add(same[0].Cname)
//                                        println(same.count())
//                                        println(all_scenario_count)

//                                            println(taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))

//                                            val a =12
//                                            println("dddddddddddddddddddddddddddddddd $a")








                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }




                                    }
                                    if (thermostat_scenario.size !=0 ){
//                        Handler(Looper.getMainLooper()).post {
////                            loadingIndicator =view.findViewById(R.id.loadingIndicator)
//                            loadPercentage.addPercentage(20) // for example
//                            updateLoadingIndicator()
//                        }

                                        var current_Thermostst_in_scenario= ArrayList<String>()
                                        for (Thermostst in thermostat_scenario){
                                            current_Thermostst_in_scenario.add(Thermostst.split("#")[0])


                                        }
                                        println(thermostat_scenario)
                                        val same_macs=thermostat_database_handler.getThermoststsBySameMacForPnames(current_Thermostst_in_scenario)
                                        val final_same_mac= ArrayList<Thermostst>()
                                        for (Thermostst in same_macs){
                                            final_same_mac.add(Thermostst[0])

                                        }
//                        println(same_macs)
//                        val refreshed_Thermostst= refresh_thermostat_for_scenario(this,final_same_mac)
//
//
//
//                        println("Thermoststs refreshed...")

                                        val current_Thermostst_to_change= arrayListOf<Thermostst>()
                                        for (Thermostst in thermostat_scenario){


                                            println(Thermostst)
                                            val current_Thermostst_in_scenario=Thermostst.split("#")
                                            val current_Thermostst_in_db=thermostat_database_handler.getThermostatByName(current_Thermostst_in_scenario[0])
                                            if (current_Thermostst_in_db != null) {
                                                current_Thermostst_to_change.add(current_Thermostst_in_db)
                                            }


                                        }
                                        val sorted_Thermostst = arrayListOf<List<Thermostst>>()

                                        for (light in current_Thermostst_to_change){
                                            val a= arrayListOf <Thermostst>()
                                            a.add(light)
                                            sorted_Thermostst.add(a)

                                        }
                                        val final_thermostat_to_do= arrayListOf<List<Thermostst>>()
                                        for (same_mac in sorted_Thermostst){

                                            val listt = same_mac.map { Thermostat ->

                                                val on_off =( thermostat_scenario.find { it.startsWith("${Thermostat.name}#") }?.substringAfter("#") ?: "").substringBefore("!")
                                                val themp = (thermostat_scenario.find { it.startsWith("${Thermostat.name}#") }?.substringAfter("!") ?: "").substringBefore("$")
                                                val mod = (thermostat_scenario.find { it.startsWith("${Thermostat.name}#") }?.substringAfter("$") ?: "").substringBefore("@")
                                                val fan = thermostat_scenario.find { it.startsWith("${Thermostat.name}#") }?.substringAfter("@") ?: ""
                                                Thermostat.mood = mod
                                                Thermostat.on_off = on_off
                                                Thermostat.fan_status = fan
                                                Thermostat.temperature = themp

                                                println(Thermostat.on_off.toString() +Thermostat.temperature.toString()+ Thermostat.fan_status.toString()+Thermostat.mood.toString())

                                                Thermostat


                                            }
//                                println(current_light_in_scenario)
//                                println(listt[0].Lname)
//                                println(listt[0].status)


                                            final_thermostat_to_do.add(listt)

                                        }
//                        println(sorted_Thermostst)
                                        next=true

                                        if (next){
                                            next=false
                                            for (same in final_thermostat_to_do){

                                                try {
//                                    println((taghsim(1.0 ,all_scenario_count.toDouble() ))* 100)

                                                    val send= udp_thermostat_for_scenario(requireContext(),same[0],same[0].mac,same[0].mood,same[0].temperature,same[0].fan_status,same[0].on_off,same[0].ip)


                                                    if (send){
                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(1.0 ,all_scenario_count.toDouble() ))* 100)
//
                                                        }


                                                        thermostat_scenario_done.add(same[0].name)
//                                        println(same.count())
//                                        println(all_scenario_count)

//                                            println(taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))

//                                            val a =12
//                                            println("dddddddddddddddddddddddddddddddd $a")








                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }




                                    }

                                    if (fan_scenario.size !=0 ){
//                        Handler(Looper.getMainLooper()).post {
////                            loadingIndicator =view.findViewById(R.id.loadingIndicator)
//                            loadPercentage.addPercentage(20) // for example
//                            updateLoadingIndicator()
//                        }

                                        var current_fan_in_scenario= ArrayList<String>()
                                        for (fan in fan_scenario){
                                            current_fan_in_scenario.add(fan.split("#")[0])


                                        }
                                        println(fan_scenario)
                                        val same_macs=fan_database_handler.getfansBySameMacForLnames(current_fan_in_scenario)
                                        val final_same_mac= ArrayList<fan>()
                                        for (fan in same_macs){
                                            final_same_mac.add(fan[0])

                                        }
//                        println(same_macs)
//                        val refreshed_fans= refresh_fan_for_scenario(this,final_same_mac)
//
//
//
//                        println("fans refreshed...")

                                        val current_fan_to_change= arrayListOf<fan>()
                                        for (fan in fan_scenario){



                                            val current_fan_in_scenario=fan.split("#")
                                            val current_fan_in_db=fan_database_handler.getfanByCname(current_fan_in_scenario[0])
                                            if (current_fan_in_db != null) {
                                                current_fan_to_change.add(current_fan_in_db)
                                            }



                                        }
                                        val sorted_fan = getfansGroupedBySameMac(current_fan_to_change)
                                        val final_fan_to_do= arrayListOf<List<fan>>()
                                        for (same_mac in sorted_fan){
                                            val listt = same_mac.map { fan ->
                                                val status = fan_scenario.find { it.startsWith("${fan.Fname}#") }?.substringAfter("#") ?: ""
                                                fan.status = status
                                                fan

                                            }
//                                println(current_light_in_scenario)
//                                println(listt[0].Lname)
//                                println(listt[0].status)


                                            final_fan_to_do.add(listt)

                                        }
                                        next=true
                                        if (next){
                                            next=false
                                            for (same in final_fan_to_do){

                                                try {
                                                    println((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)

                                                    val send= udp_fan_for_scenario(requireContext(),same)


                                                    if (send){
                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)
//
                                                        }

                                                        for (fan in same){
                                                            fan_scenario_done.add(fan.Fname)
                                                            println(same.count())
                                                            println(all_scenario_count)

//                                            println(taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))

//                                            val a =12
//                                            println("dddddddddddddddddddddddddddddddd $a")





                                                        }


                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }




                                    }
//                    println(light_scenario_done)
//                    println(light_scenario)




                                    if (light_scenario_done.count()< light_scenario.count()){
                                        println("seciond light scenario check")

                                        var light_to_do= arrayListOf<String>()
                                        for (light in light_scenario){
                                            if (light.split("#")[0] !in light_scenario_done){
                                                light_to_do.add(light)
                                            }
                                        }
                                        println(light_to_do)


                                        var current_light_in_scenario= ArrayList<String>()
                                        for (light in light_to_do){
                                            current_light_in_scenario.add(light.split("#")[0])


                                        }

                                        val same_macs=light_database_handler.getLightsBySameMacForLnames(current_light_in_scenario)
                                        val final_same_mac= ArrayList<Light>()
                                        for (light in same_macs){
                                            final_same_mac.add(light[0])

                                        }
//                        println(same_macs)


                                        val current_light_to_change1= arrayListOf<Light>()

                                        for (light in light_to_do){




                                            val current_light_in_scenario=light.split("#")
                                            val current_light_in_db=light_database_handler.getLightsByLname(current_light_in_scenario[0])
                                            current_light_to_change1.add(current_light_in_db)










                                        }
                                        val sorted_lights = getLightsGroupedBySameMac(current_light_to_change1)
                                        sorted_lights[0]
                                        val final_light_to_do= arrayListOf<List<Light>>()
                                        for (same_mac in sorted_lights){
                                            val listt = same_mac.map { Light ->
                                                val status = light_to_do.find { it.startsWith("${Light.Lname}#") }?.substringAfter("#") ?: ""
                                                Light.status = status
                                                Light

                                            }
//                                println(current_light_in_scenario)
//                                println(listt[0].Lname)
//                                println(listt[0].status)


                                            final_light_to_do.add(listt)

                                        }
                                        next=true
                                        if (next){
                                            next=false
                                            for (same in final_light_to_do){

                                                try {
                                                    println((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)

                                                    val send=udp_light_scenario(requireContext(),same)


                                                    if (send){
                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)
//
                                                        }

                                                        for (light in same){

                                                            light_scenario_done.add(light.Lname)
                                                            println(same.count())
                                                            println(all_scenario_count)

//





                                                        }


                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }




                                    }
                                    if (thermostat_scenario_done.count()< thermostat_scenario.count()){
                                        println("thermostat second")
                                        var thermostat_to_do= arrayListOf<String>()
                                        for (thermostat in thermostat_scenario){
                                            if (thermostat.split("#")[0] !in thermostat_scenario_done){
                                                thermostat_to_do.add(thermostat)
                                            }
                                        }

                                        var current_Thermostst_in_scenario= ArrayList<String>()
                                        for (Thermostst in thermostat_to_do){
                                            current_Thermostst_in_scenario.add(Thermostst.split("#")[0])


                                        }
                                        println(thermostat_to_do)
                                        val same_macs=thermostat_database_handler.getThermoststsBySameMacForPnames(current_Thermostst_in_scenario)
                                        val final_same_mac= ArrayList<Thermostst>()
                                        for (Thermostst in same_macs){
                                            final_same_mac.add(Thermostst[0])

                                        }
//                        println(same_macs)
//                        val refreshed_Thermostst= refresh_thermostat_for_scenario(this,final_same_mac)
//
//
//
//                        println("Thermoststs refreshed...")

                                        val current_Thermostst_to_change= arrayListOf<Thermostst>()
                                        for (Thermostst in thermostat_to_do){


                                            println(Thermostst)
                                            val current_Thermostst_in_scenario=
                                                Thermostst.split("#")
                                            val current_Thermostst_in_db=thermostat_database_handler.getThermostatByName(current_Thermostst_in_scenario[0])
                                            if (current_Thermostst_in_db != null) {
                                                current_Thermostst_to_change.add(current_Thermostst_in_db)
                                            }


                                        }
                                        val sorted_Thermostst = arrayListOf<List<Thermostst>>()

                                        for (light in current_Thermostst_to_change){
                                            val a= arrayListOf <Thermostst>()
                                            a.add(light)
                                            sorted_Thermostst.add(a)

                                        }
                                        val final_thermostat_to_do= arrayListOf<List<Thermostst>>()
                                        for (same_mac in sorted_Thermostst){

                                            val listt = same_mac.map { Thermostat ->

                                                val on_off =( thermostat_to_do.find { it.startsWith("${Thermostat.name}#") }?.substringAfter("#") ?: "").substringBefore("!")
                                                val themp = (thermostat_to_do.find { it.startsWith("${Thermostat.name}#") }?.substringAfter("!") ?: "").substringBefore("$")
                                                val mod = (thermostat_to_do.find { it.startsWith("${Thermostat.name}#") }?.substringAfter("$") ?: "").substringBefore("@")
                                                val fan = thermostat_to_do.find { it.startsWith("${Thermostat.name}#") }?.substringAfter("@") ?: ""
                                                Thermostat.mood = mod
                                                Thermostat.on_off = on_off
                                                Thermostat.fan_status = fan
                                                Thermostat.temperature = themp

                                                println(Thermostat.on_off.toString() +Thermostat.temperature.toString()+ Thermostat.fan_status.toString()+Thermostat.mood.toString())

                                                Thermostat


                                            }
//                                println(current_light_in_scenario)
//                                println(listt[0].Lname)
//                                println(listt[0].status)


                                            final_thermostat_to_do.add(listt)

                                        }
//                        println(sorted_Thermostst)
                                        next=true

                                        if (next){
                                            next=false
                                            for (same in final_thermostat_to_do){

                                                try {
//                                    println((taghsim(1.0 ,all_scenario_count.toDouble() ))* 100)


                                                    val send= udp_thermostat_for_scenario(requireContext(),same[0],same[0].mac,same[0].mood,same[0].temperature,same[0].fan_status,same[0].on_off,same[0].ip)


                                                    if (send){
                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(1.0 ,all_scenario_count.toDouble() ))* 100)
//
                                                        }


                                                        thermostat_scenario_done.add(same[0].name)
//                                        println(same.count())
//                                        println(all_scenario_count)

//                                            println(taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))

//                                            val a =12
//                                            println("dddddddddddddddddddddddddddddddd $a")








                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }







                                    }
                                    if (curtain_scenario_done.count()< curtain_scenario.count()){
                                        println("curtain second")
                                        var curtain_to_do= arrayListOf<String>()
                                        for (curtain in curtain_scenario){
                                            if (curtain.split("#")[0]  !in curtain_scenario_done){
                                                curtain_to_do.add(curtain)
                                            }
                                        }


                                        var current_curtain_in_scenario = ArrayList<String>()
                                        for (curtain in curtain_to_do){
                                            current_curtain_in_scenario.add(curtain.split("#")[0])


                                        }
                                        println(curtain_to_do)
                                        val same_macs = curtain_database_handler.getcurtainsBySameMacForPnames(current_curtain_in_scenario)
                                        val final_same_mac = ArrayList<curtain>()
                                        for (curtain in same_macs){
                                            final_same_mac.add(curtain[0])

                                        }
//                        println(same_macs)
//                        val refreshed_curtain= refresh_curtain_for_scenario(this,final_same_mac)
//
//
//
//                        println("curtains refreshed...")

                                        val current_curtain_to_change = arrayListOf<curtain>()
                                        for (curtain in curtain_to_do){


                                            println(curtain)
                                            val current_curtain_in_scenario = curtain.split("#")
                                            val current_curtain_in_db =
                                                curtain_database_handler.getCurtainByCname(current_curtain_in_scenario[0])
                                            if (current_curtain_in_db != null) {
                                                current_curtain_to_change.add(current_curtain_in_db)
                                            }


                                        }

                                        val sorted_curtain = getcurtainGroupedBySameMac(current_curtain_to_change)

                                        val final_curtain_to_do = arrayListOf<List<curtain>>()
                                        for (same_mac in sorted_curtain){
                                            val listt = same_mac.map { curtain ->
                                                val status =
                                                    curtain_to_do.find { it.startsWith("${curtain.Cname}#") }?.substringAfter("#") ?: ""
                                                curtain.status = status
                                                curtain

                                            }
//                                println(current_light_in_scenario)
//                                println(listt[0].Lname)
//                                println(listt[0].status)


                                            final_curtain_to_do.add(listt)

                                        }
                                        next=true

                                        if (next){
                                            next = false
                                            for (same in final_curtain_to_do) {

                                                try {
//                                    println((taghsim(1.0 ,all_scenario_count.toDouble() ))* 100)

                                                    val send = udp_curtain_for_scenario(requireContext(), same[0], same[0].status)


                                                    if (send) {
                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress(
                                                                (taghsim(
                                                                    1.0,
                                                                    all_scenario_count.toDouble()
                                                                )) * 100
                                                            )
//
                                                        }

                                                        curtain_scenario_done.add(same[0].Cname)
//                                        println(same.count())
//                                        println(all_scenario_count)

//                                            println(taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))

//                                            val a =12
//                                            println("dddddddddddddddddddddddddddddddd $a")


                                                        next = true
                                                    }
                                                } catch (e: Exception) {
                                                    next = true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }




                                    }

                                    if (plug_scenario_done.count()< plug_scenario.count()){
                                        println("plug second")
                                        var plug_to_do= arrayListOf<String>()
                                        for (plug in plug_scenario){
                                            if (plug.split("#")[0] !in plug_scenario_done){
                                                plug_to_do.add(plug)
                                            }
                                        }


                                        var current_plug_in_scenario= ArrayList<String>()
                                        for (plug in plug_to_do){
                                            current_plug_in_scenario.add(plug.split("#")[0])


                                        }
                                        println(plug_scenario)
                                        val same_macs=plug_database_handler.getPlugsBySameMacForPnames(current_plug_in_scenario)
                                        val final_same_mac= ArrayList<Plug>()
                                        for (plug in same_macs){
                                            final_same_mac.add(plug[0])

                                        }
//                        println(same_macs)
//                        val refreshed_plugs=refresh_plug_for_scenario(requireContext(),final_same_mac)
//
//
//
//                        println("plugs refreshed...")

                                        val current_plug_to_change= arrayListOf<Plug>()
                                        for (plug in plug_to_do){



                                            val current_plug_in_scenario=plug.split("#")
                                            val current_plug_in_db=plug_database_handler.getPlugByCname(current_plug_in_scenario[0])
                                            if (current_plug_in_db != null) {
                                                current_plug_to_change.add(current_plug_in_db)
                                            }






                                        }

                                        val sorted_plugs = getPlugsGroupedBySameMac(current_plug_to_change)


                                        val final_plug_to_do= arrayListOf<List<Plug>>()
                                        for (same_mac in sorted_plugs){
                                            val listt = same_mac.map { Plug ->
                                                val status = plug_to_do.find { it.startsWith("${Plug.Pname}#") }?.substringAfter("#") ?: ""
                                                Plug.status = status
                                                Plug

                                            }
                                            println(current_plug_in_scenario)
                                            println(listt[0].Pname)
                                            println(listt[0].status)


                                            final_plug_to_do.add(listt)

                                        }

                                        println(sorted_plugs)
                                        next=true

                                        if (next){
                                            next=false
                                            for (same in final_plug_to_do){

                                                try {
                                                    println((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)

                                                    val send=udp_plug_scenario(requireContext(),same)


                                                    if (send){
                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)
//
                                                        }
                                                        for (plug in same){
                                                            plug_scenario_done.add(plug.Pname)
                                                            println(same.count())
                                                            println(all_scenario_count)

//                                            println(taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))

//                                            val a =12
//                                            println("dddddddddddddddddddddddddddddddd $a")





                                                        }


                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }









                                    }
                                    if (valve_scenario_done.count()< valve_scenario.count()){
                                        println("valve second")
                                        var valve_to_do= arrayListOf<String>()
                                        for (valve in valve_scenario){
                                            if (valve.split("#")[0]  !in valve_scenario_done){
                                                valve_to_do.add(valve)
                                            }
                                        }
                                        var current_valve_in_scenario= ArrayList<String>()
                                        for (valve in valve_to_do){
                                            current_valve_in_scenario.add(valve.split("#")[0])


                                        }
                                        println(valve_to_do)
                                        val same_macs=valve_database_handler.getValvesBySameMacForPnames(current_valve_in_scenario)
                                        val final_same_mac= ArrayList<valve>()
                                        for (valve in same_macs){
                                            final_same_mac.add(valve[0])

                                        }
//                        println(same_macs)

                                        val current_valve_to_change= arrayListOf<valve>()
                                        for (valve in valve_to_do){


                                            println(valve)
                                            val current_valve_in_scenario= valve.split("#")
                                            val current_valve_in_db=valve_database_handler.getvalveByCname(current_valve_in_scenario[0])
                                            if (current_valve_in_db != null) {
                                                current_valve_to_change.add(current_valve_in_db)
                                            }










                                        }
                                        val sorted_valve = getvalvesGroupedBySameMac(current_valve_to_change)
                                        val final_valve_to_do= arrayListOf<List<valve>>()
                                        for (same_mac in sorted_valve){
                                            val listt = same_mac.map { valve ->
                                                val status = valve_to_do.find { it.startsWith("${valve.Vname}#") }?.substringAfter("#") ?: ""
                                                valve.status = status
                                                valve

                                            }
//                                println(current_light_in_scenario)
//                                println(listt[0].Lname)
//                                println(listt[0].status)


                                            final_valve_to_do.add(listt)

                                        }
                                        next=true

                                        if (next){
                                            next=false
                                            for (same in final_valve_to_do){

                                                try {
                                                    println((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)

                                                    val send= udp_valve_for_scenario(requireContext(),same)


                                                    if (send){

                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)
//
                                                        }
                                                        for (valve in same){
                                                            valve_scenario_done.add(valve.Vname)
                                                            println(same.count())
                                                            println(all_scenario_count)

//                                            println(taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))

//                                            val a =12
//                                            println("dddddddddddddddddddddddddddddddd $a")





                                                        }


                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }






                                    }
                                    if (fan_scenario_done.count()< fan_scenario.count()){
                                        println("fan second")
                                        var fan_to_do= arrayListOf<String>()
                                        for (fan in fan_scenario){
                                            if (fan.split("#")[0]  !in fan_scenario_done){
                                                fan_to_do.add(fan)
                                            }
                                        }
                                        var current_fan_in_scenario= ArrayList<String>()
                                        for (fan in fan_to_do){
                                            current_fan_in_scenario.add(fan.split("#")[0])


                                        }
                                        println(fan_to_do)
                                        val same_macs=fan_database_handler.getfansBySameMacForLnames(current_fan_in_scenario)
                                        val final_same_mac= ArrayList<fan>()
                                        for (fan in same_macs){
                                            final_same_mac.add(fan[0])

                                        }
//                        println(same_macs)
//                        val refreshed_fans= refresh_fan_for_scenario(this,final_same_mac)
//
//
//
//                        println("fans refreshed...")

                                        val current_fan_to_change= arrayListOf<fan>()
                                        for (fan in fan_to_do){



                                            val current_fan_in_scenario= fan.split("#")
                                            val current_fan_in_db=fan_database_handler.getfanByCname(current_fan_in_scenario[0])
                                            if (current_fan_in_db != null) {
                                                current_fan_to_change.add(current_fan_in_db)
                                            }



                                        }
                                        val sorted_fan = getfansGroupedBySameMac(current_fan_to_change)
                                        val final_fan_to_do= arrayListOf<List<fan>>()
                                        for (same_mac in sorted_fan){
                                            val listt = same_mac.map { fan ->
                                                val status = fan_to_do.find { it.startsWith("${fan.Fname}#") }?.substringAfter("#") ?: ""
                                                fan.status = status
                                                fan

                                            }
//                                println(current_light_in_scenario)
//                                println(listt[0].Lname)
//                                println(listt[0].status)


                                            final_fan_to_do.add(listt)

                                        }
                                        next=true
                                        if (next){
                                            next=false
                                            for (same in final_fan_to_do){

                                                try {
                                                    println((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)

                                                    val send= udp_fan_for_scenario(requireContext(),same)


                                                    if (send){
                                                        Handler(Looper.getMainLooper()).post {
                                                            loading_view.increaseProgress((taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))* 100)
//
                                                        }

                                                        for (fan in same){
                                                            fan_scenario_done.add(fan.Fname)
                                                            println(same.count())
                                                            println(all_scenario_count)

//                                            println(taghsim(same.count().toDouble() ,all_scenario_count.toDouble() ))

//                                            val a =12
//                                            println("dddddddddddddddddddddddddddddddd $a")





                                                        }


                                                        next=true
                                                    }
                                                }catch (e:Exception){
                                                    next=true
                                                    println(e)
                                                    //
                                                }

                                            }

                                        }






                                    }







                                    requireActivity().runOnUiThread {
                                        SharedViewModel.update_is_doing("not bussy")
                                    }



                                    var fan_to_do = arrayListOf<String>()
                                    var valve_to_do= arrayListOf<String>()
                                    var plug_to_do= arrayListOf<String>()
                                    var curtain_to_do = arrayListOf<String>()
                                    var thermostat_to_do= arrayListOf<String>()
                                    var light_to_do= arrayListOf<String>()


                                    if (fan_scenario_done.count()< fan_scenario.count()) {


                                        for (fan in fan_scenario) {
                                            if (fan.split("#")[0] !in fan_scenario_done) {
                                                fan_to_do.add(fan)
                                            }
                                        }

                                    }
                                    if (valve_scenario_done.count()< valve_scenario.count()) {


                                        for (valve in valve_scenario) {
                                            if (valve.split("#")[0] !in valve_scenario_done) {
                                                valve_to_do.add(valve)
                                            }
                                        }


                                    }
                                    if (plug_scenario_done.count()< plug_scenario.count()) {


                                        for (plug in plug_scenario) {
                                            if (plug.split("#")[0] !in plug_scenario_done) {
                                                plug_to_do.add(plug)
                                            }
                                        }
                                    }

                                    if (curtain_scenario_done.count()< curtain_scenario.count()) {


                                        for (curtain in curtain_scenario) {
                                            if (curtain.split("#")[0] !in curtain_scenario_done) {
                                                curtain_to_do.add(curtain)
                                            }
                                        }
                                    }

                                    if (thermostat_scenario_done.count()< thermostat_scenario.count()) {


                                        for (thermostat in thermostat_scenario) {
                                            if (thermostat.split("#")[0] !in thermostat_scenario_done) {
                                                thermostat_to_do.add(thermostat)
                                            }
                                        }
                                    }
                                    if (light_scenario_done.count()< light_scenario.count()) {


                                        for (light in light_scenario) {
                                            if (light.split("#")[0] !in light_scenario_done) {
                                                light_to_do.add(light)
                                            }
                                        }
                                    }
                                    requireActivity().runOnUiThread{
                                        popupWindow.dismiss()
                                    }

                                    println("done")
                                    println(all_scenario_count)
                                    println(fan_to_do.count()+valve_to_do.count()+plug_to_do.count()+curtain_to_do.count()+thermostat_to_do.count()+light_to_do.count())
//                                    val percent_done = light_scenario_done.count() + thermostat_scenario_done.count() +  curtain_scenario_done.count() + valve_scenario_done.count()+fan_scenario_done.count() + plug_scenario_done.count() + music_scenario_done.count()


                                    if (0==fan_to_do.count()+valve_to_do.count()+plug_to_do.count()+curtain_to_do.count()+thermostat_to_do.count()+light_to_do.count()){
//                                        requireActivity().runOnUiThread{
//                                            Toast.makeText(requireContext(), "Scenario Done  "+calculatePercentage(percent_done.toDouble(), all_scenario_count.toDouble())+"%", Toast.LENGTH_LONG).show()
//                                        }


                                    }else if (all_scenario_count>fan_to_do.count()+valve_to_do.count()+plug_to_do.count()+curtain_to_do.count()+thermostat_to_do.count()+light_to_do.count()){
                                        val final_list_to_show= arrayListOf<String>()
                                        println(fan_to_do)
                                        println(curtain_to_do)
                                        println(thermostat_to_do)
                                        println(light_to_do)

                                        if (music_scenario_done.count() < music_scenario.count()){
                                            final_list_to_show.add("Music")

                                        }


                                        if (fan_to_do.count()>0){
                                            for (item in fan_to_do){
                                                final_list_to_show.add("${item.split("#")[0]}  (Fan)" )
                                            }


                                        }
                                        if (valve_to_do.count()>0){
                                            for (item in valve_to_do){
                                                final_list_to_show.add("${item.split("#")[0]}  (Valve)" )
                                            }


                                        }else if (plug_to_do.count()>0){
                                            for (item in plug_to_do){
                                                final_list_to_show.add("${item.split("#")[0]}  (Plug)" )
                                            }


                                        }
                                        if (curtain_to_do.count()>0){
                                            for (item in curtain_to_do){
                                                final_list_to_show.add("${item.split("#")[0]}  (Curtain)" )
                                            }


                                        }
                                        if (thermostat_to_do.count()>0){
                                            for (item in thermostat_to_do){
                                                final_list_to_show.add("${item.split("#")[0]}  (Thermostat)" )
                                            }


                                        }
                                        if (light_to_do.count()>0){
                                            for (item in light_to_do){
                                                final_list_to_show.add("${item.split("#")[0]}  (Light)" )
                                            }


                                        }
                                        requireActivity().runOnUiThread {

                                            popupWindow.dismiss()
                                        }
                                        println(final_list_to_show)


                                        recyclerView2 = customPopupView4.findViewById(R.id.undone_recycler_view)
                                        adapter2 = CustomAdapter(final_list_to_show)
                                        recyclerView2.adapter = adapter2
                                        val spanCount = 3
                                        recyclerView2.layoutManager = GridLayoutManager(requireContext(), 3)
                                        if (mode=="admin"){

                                            requireActivity().runOnUiThread{


                                                popupWindow4.showAtLocation(view, Gravity.CENTER, 0, 0)
                                            }
                                        }


                                        val timerTextView=customPopupView4.findViewById<TextView>(R.id.counterr)
                                        requireActivity().runOnUiThread{
                                            // زمان مورد نظر را به میلی‌ثانیه مشخص کنید (اینجا 5 ثانیه)
                                            val totalTimeInMillis: Long = 30000

                                            // ایجاد یک CountDownTimer با زمان مشخص
                                            val countDownTimer = object : CountDownTimer(totalTimeInMillis, 1000) {
                                                override fun onTick(millisUntilFinished: Long) {
                                                    // هر ثانیه یکبار فراخوانی می‌شود
                                                    val secondsRemaining = millisUntilFinished / 1000
                                                    timerTextView.text = secondsRemaining.toString()
                                                }

                                                override fun onFinish() {
                                                    // هنگامی که شمارش معکوس به پایان می‌رسد
                                                    timerTextView.text = "Done!"





                                                    popupWindow4.dismiss()

                                                    // انجام عملیات مورد نظر

                                                }
                                            }

                                            // شروع شمارش معکوس
                                            countDownTimer.start()
                                        }



                                    }
                                    val percent_done = light_scenario_done.count() + thermostat_scenario_done.count() +  curtain_scenario_done.count() + valve_scenario_done.count()+fan_scenario_done.count() + plug_scenario_done.count() + music_scenario_done.count()
                                    requireActivity().runOnUiThread{
                                        Toast.makeText(requireContext(), "Scenario Done  "+calculatePercentage(percent_done.toDouble(), all_scenario_count.toDouble()).toInt()+"%", Toast.LENGTH_LONG).show()
                                    }











                                }.start()


                            }else{
                                requireActivity().runOnUiThread{

                                    Toast.makeText(requireContext(), "Connect to a Griffin Network", Toast.LENGTH_SHORT).show()
                                }
                            }





                        }else{

                            Toast.makeText(requireContext(), "please Wait...", Toast.LENGTH_SHORT).show()
                        }


//





                    }else if (scenario_side=="delete"){

                        customPopupView2.findViewById<TextView>(R.id.text_msg).setText("Are you sure you want to delete ${selectedItem.scenario_name} ?")
                        popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                        yes_delete.setOnClickListener {

                            scenario_db_handler_2.delete_from_db_Scenariog(selectedItem.id)
                            popupWindow2.dismiss()
                            Toast.makeText(requireContext(),"Scenario ${selectedItem.scenario_name} Deleted",Toast.LENGTH_LONG).show()
                            SharedViewModel.update_Scenario_list(scenario_db_handler_2.getAllScenario())

                        }
                        cancel_delete.setOnClickListener {

                            popupWindow2.dismiss()
                            Toast.makeText(requireContext(),"Canceled",Toast.LENGTH_LONG).show()

                        }






                    }else if(scenario_side=="rename"){
                        popupWindow3.showAtLocation(view, Gravity.CENTER, 0, 0)
                        customPopupView3.findViewById<EditText>(R.id.scenario_name).setText(selectedItem.scenario_name)
                        customPopupView3.findViewById<Button>(R.id.ok_rename).setOnClickListener {


                            scenario_db_handler_2.updateScenarioNameById(selectedItem.id,customPopupView3.findViewById<EditText>(R.id.scenario_name).text.toString())


                            SharedViewModel.update_Scenario_list(scenario_db_handler_2.getAllScenario())
                            Toast.makeText(requireContext(),"Scenario Name changed",Toast.LENGTH_LONG).show()
                            popupWindow3.dismiss()

                        }
                        customPopupView3.findViewById<Button>(R.id.cancel_rename).setOnClickListener {
                            popupWindow3.dismiss()
                            Toast.makeText(requireContext(),"Canceled",Toast.LENGTH_LONG).show()

                        }

                    }else if (scenario_side=="time"){
                        val activity =requireActivity() as dashboard
                        val choosed_layout= activity.findViewById<ConstraintLayout>(R.id.exit_senario_layout)
                        val time_layout= activity.findViewById<LinearLayout>(R.id.time_layout)
                        val scenario_name= activity.findViewById<TextView>(R.id.scenario_name)


                        scenario_name.setText(selectedItem.scenario_name)
                        time_layout.visibility =View.VISIBLE
                        val alarmHandelerDb= alarm_handeler_db.getInstance(requireContext())
                        val alarm_list=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.scenario_name)
                        println(alarm_list)
                        for (item in timetabel_list){
                            item.setText("")
                        }

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
                            var my_indexes= arrayListOf<Int>(0)
                            for (num in my_indexes){
                                val next=when(alarm[num].next_status){
                                    "1"->"on"
                                    "0"->"off"
                                    else -> {
                                        "LOST"
                                    }
                                }
                                text += "  ==>  " + alarm[num].alarm_tyme

                            }

                            timetabel_list[counterr1].setText(days.uppercase(Locale.getDefault())+text)
                            counterr1+=1


                        }

                    }else if (scenario_side=="edit"){

                        Toast.makeText(context,"edit scenario",Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), griffin_home::class.java)
//                        val scenario_db_handler = scenario_db.Scenario_db.getInstance(requireContext())




                        intent.putExtra("mode", "scenario"+(selectedItem.id))
                        SharedViewModel.update_current_scenario(selectedItem)
                        intent.putExtra("name",selectedItem.scenario_name)
                        startActivity(intent)
                        activity.finish()


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


                    val textview_turn_of = customPopupView.findViewById<TextView>(R.id.textview_turn_of)
                    val textview_turn_on = customPopupView.findViewById<TextView>(R.id.textview_turn_on)
                    val hourPicker_off = customPopupView.findViewById<NumberPicker>(R.id.hourPicker_off)
                    val minutePicker_off = customPopupView.findViewById<NumberPicker>(R.id.minutePicker_off)

                    hourPicker_off.visibility=View.GONE
                    minutePicker_off.visibility=View.GONE
                    textview_turn_of.visibility=View.GONE
                    textview_turn_on.text = "Set time:"


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
                    hourPicker_on.minValue = 0
                    hourPicker_on.maxValue = 23
                    hourPicker_on.setOnScrollListener { _, _ ->
                        val currentValue = hourPicker_on.value+1
                        if (hourPicker_on.scrollY > 0) {
                            hourPicker_on.value = currentValue + 1
                        } else {
                            hourPicker_on.value = currentValue - 1
                        }
                    }
                    val minutePicker_on = customPopupView.findViewById<NumberPicker>(R.id.minutePicker_on)
                    minutePicker_on.minValue = 0
                    minutePicker_on.maxValue = 59
                    minutePicker_on.setOnScrollListener { _, _ ->
                        val currentValue = minutePicker_on.value+1
                        if (minutePicker_on.scrollY > 0) {
                            minutePicker_on.value = currentValue + 1
                        } else {
                            minutePicker_on.value = currentValue - 1
                        }
                    }






                    hourPicker_off.minValue = 0
                    hourPicker_off.maxValue = 23
                    hourPicker_off.setOnScrollListener { _, _ ->
                        val currentValue = hourPicker_off.value+1
                        if (hourPicker_off.scrollY > 0) {
                            hourPicker_off.value = currentValue + 1
                        } else {
                            hourPicker_off.value = currentValue - 1
                        }
                    }

                    minutePicker_off.minValue = 0
                    minutePicker_off.maxValue = 59
                    minutePicker_off.setOnScrollListener { _, _ ->
                        val currentValue = minutePicker_off.value+1
                        if (minutePicker_off.scrollY > 0) {
                            minutePicker_off.value = currentValue + 1
                        } else {
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

                    set_timer_plug_item_menu.setOnClickListener {

                        println(Calendar.SATURDAY)
                        println(Calendar.SUNDAY)

                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)




                    }

                    val alarm_database= alarm_handeler_db.getInstance(requireContext())
                    setalarm.setOnClickListener {
                        println(days_list)
                        val alarmHandelerDb= alarm_handeler_db.getInstance(requireContext())
                        val alarm_list=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.scenario_name)

                        if (alarm_list.count() < 4) {
                            val groups=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.scenario_name)
                            if (days_list.count() > 0) {
                                for (day in days_list) {
                                    println(day)
                                    val alarmName = "scenario_" + selectedItem.scenario_name+"("+day+")" + "_1"
                                    val alarmIntent = Intent(context, myBroadcastReceiverr::class.java)
                                    alarmIntent.putExtra("alarmName", alarmName)
                                    alarmIntent.putExtra("alarmH", hourPicker_on.value)
                                    alarmIntent.putExtra("alarmM", minutePicker_on.value)
                                    alarmIntent.putExtra("alarmD", day)
                                    val pendingIntent = PendingIntent.getBroadcast(
                                        context,
                                        alarmName.hashCode(),
                                        alarmIntent,
                                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                                    )
                                    val alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                    val calendar = Calendar.getInstance()

                                    calendar.set(Calendar.DAY_OF_WEEK, day)
                                    calendar.set(Calendar.HOUR_OF_DAY, hourPicker_on.value)
                                    calendar.set(Calendar.MINUTE, minutePicker_on.value)
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
                                    alarm_on.device_name = selectedItem.scenario_name
                                    alarm_on.alarm_tyme = hourPicker_on.value.toString() + ":" + minutePicker_on.value.toString()
                                    alarm_on.next_status = "1"
                                    alarm_on.grooup = groups.count().toString()
                                    alarm_database.set_to_db_alarm(alarm_on)

                                    alarmMgr.setExactAndAllowWhileIdle(
                                        AlarmManager.RTC_WAKEUP,
                                        calendar.timeInMillis,
                                        pendingIntent
                                    )
                                    println(alarmName + " seted")
                                }

                                val alarmHandelerDb= alarm_handeler_db.getInstance(requireContext())
                                val alarm_list=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.scenario_name)

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
                                    var my_indexes2= arrayListOf<Int>(0)
                                    for (num in my_indexes2){
                                        val next=when(alarm[num].next_status){
                                            "1"->"on"
                                            "0"->"off"
                                            else -> {
                                                "LOST"
                                            }
                                        }
                                        text += "  ==>  " + alarm[num].alarm_tyme + " (" + next + ") "

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
                        val alarm_list=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.scenario_name)



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
                            val alarm_list=alarmHandelerDb.getAlarmsGroupedByDeviceName(selectedItem.scenario_name)

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
                                    val next=when(alarm[num].next_status){
                                        "1"->"on"
                                        "0"->"off"
                                        else -> {
                                            "LOST"
                                        }
                                    }
                                    text += "  ==>  " + alarm[num].alarm_tyme + " (" + next + ") "

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

                recyclerView.adapter = adapter
                adapter.setItems(newlist)


            })













        })
        scenario_item_menu.setOnClickListener{
            println(scenario_side)


            val popupMenu = PopupMenu(context, scenario_item_menu)
            popupMenu.gravity = Gravity.TOP or Gravity.END
            popupMenu.menuInflater.inflate(R.menu.scenario_menu, popupMenu.menu)
            if (scenario_side== "delete"){
                popupMenu.menu.findItem(R.id.delete_scenario).setTitle("cancel delete scenario")

            }else if(scenario_side=="user"){
                popupMenu.menu.findItem(R.id.delete_scenario).setTitle("delete scenario")
                popupMenu.menu.findItem(R.id.rename_scenario).setTitle("rename scenario")
                popupMenu.menu.findItem(R.id.set_time_scenario).setTitle("Set time for scenario")
                popupMenu.menu.findItem(R.id.edit_scenario).setTitle("edit scenario")

            }else if(scenario_side=="rename"){
                popupMenu.menu.findItem(R.id.rename_scenario).setTitle("Cancel rename scenario")

            }else if(scenario_side=="time"){
                popupMenu.menu.findItem(R.id.set_time_scenario).setTitle("Cancel set time scenario")

            }else if(scenario_side=="edit"){
                popupMenu.menu.findItem(R.id.edit_scenario).setTitle("Cancel scenario")

            }
            popupMenu.setOnMenuItemClickListener { menuItem ->


                when(menuItem.itemId){
                    R.id.add_scenario ->{

                        Toast.makeText(context,"add scenario",Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), griffin_home::class.java)
                        val scenario_db_handler = scenario_db.Scenario_db.getInstance(requireContext())



                        val scenario_m=scenario_mudel()
                        scenario_m.fan=""
                        scenario_m.curtain=""
                        scenario_m.light=""
                        scenario_m.thermostat=""
                        scenario_m.plug=""
                        scenario_m.valve=""
                        scenario_m.music=""
                        scenario_m.scenario_name="Scenario"+ ((scenario_db_handler.getScenarioCount().toInt())+1)
                        scenario_db_handler.set_to_db_Scenario(scenario_m)
                        intent.putExtra("mode", "scenario"+(scenario_db_handler.getMaxId().toInt()))
                        intent.putExtra("name",scenario_m.scenario_name)

                        startActivity(intent)
                        activity.finish()

                    }


                    R.id.delete_scenario ->{
                        Toast.makeText(context,"delete_scenario is Active",Toast.LENGTH_SHORT).show()
                        if (scenario_side== "delete"){
                            scenario_side="user"

                            popupMenu.menu.findItem(R.id.delete_scenario).setTitle("delete scenario")
                            view.findViewById<TextView>(R.id.choosed_title).setText("Choose \nyour Scenario")

                            view.findViewById<TextView>(R.id.choosed_title).setTextColor(
                                ColorStateList.valueOf(Color.WHITE)
                            )


                        }else{
                            println(scenario_side)
                            popupMenu.menu.findItem(R.id.delete_scenario).setTitle("Cancel delete scenario")
                            view.findViewById<TextView>(R.id.choosed_title).setText("Delete \nyour Scenario")

                            popupMenu.menu.findItem(R.id.rename_scenario).setTitle("rename scenario")
                            popupMenu.menu.findItem(R.id.set_time_scenario).setTitle("Set time for scenario")
                            popupMenu.menu.findItem(R.id.edit_scenario).setTitle("edit scenario")
                            scenario_side="delete"

                            view.findViewById<TextView>(R.id.choosed_title).setTextColor(
                                ColorStateList.valueOf(Color.RED)
                            )

                        }


                    }
                    R.id.set_time_scenario -> {
                        val activity =requireActivity() as dashboard
                        val constraintLayout = activity.findViewById<ConstraintLayout>(R.id.dashbord_constraint)
                        val choosed_layout= activity.findViewById<ConstraintLayout>(R.id.exit_senario_layout)
                        val choosed_layout2= activity.findViewById<LinearLayout>(R.id.choosed_layout)
                        val time_layout= activity.findViewById<LinearLayout>(R.id.time_layout)



//                        Toast.makeText(context, "rename_scenario", Toast.LENGTH_SHORT).show()


                        Toast.makeText(context,"Set time scenario is Active",Toast.LENGTH_SHORT).show()
                        if (scenario_side== "time"){
                            choosed_layout.visibility=View.VISIBLE
                            time_layout.visibility =View.GONE
                            val params = choosed_layout2.layoutParams as ConstraintLayout.LayoutParams
                            params.height = 0
                            choosed_layout2.layoutParams = params
                            scenario_side="user"

                            popupMenu.menu.findItem(R.id.set_time_scenario).setTitle("Set time scenario")
                            view.findViewById<TextView>(R.id.choosed_title).setText("Choose \nyour Scenario")

                            view.findViewById<TextView>(R.id.choosed_title).setTextColor(
                                ColorStateList.valueOf(Color.WHITE)
                            )


                        }else{
                            choosed_layout.visibility=View.GONE
                            val params = choosed_layout2.layoutParams as ConstraintLayout.LayoutParams
                            params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                            choosed_layout2.layoutParams = params

                            println(scenario_side)
                            popupMenu.menu.findItem(R.id.set_time_scenario).setTitle("Cancel set time scenario")
                            view.findViewById<TextView>(R.id.choosed_title).setText("Set time \nyour Scenario")
                            scenario_side="time"
                            popupMenu.menu.findItem(R.id.delete_scenario).setTitle("delete scenario")
                            popupMenu.menu.findItem(R.id.rename_scenario).setTitle("rename scenario")

                            popupMenu.menu.findItem(R.id.edit_scenario).setTitle("edit scenario")

                            view.findViewById<TextView>(R.id.choosed_title).setTextColor(
                                ColorStateList.valueOf(Color.GREEN)
                            )

                        }



                    }
                    R.id.rename_scenario -> {



                        Toast.makeText(context, "rename_scenario", Toast.LENGTH_SHORT).show()


                        Toast.makeText(context,"delete_scenario is Active",Toast.LENGTH_SHORT).show()
                        if (scenario_side== "rename"){
                            scenario_side="user"

                            popupMenu.menu.findItem(R.id.rename_scenario).setTitle("rename scenario")
                            view.findViewById<TextView>(R.id.choosed_title).setText("Choose \nyour Scenario")

                            view.findViewById<TextView>(R.id.choosed_title).setTextColor(
                                ColorStateList.valueOf(Color.WHITE)
                            )


                        }else {
                            println(scenario_side)
                            popupMenu.menu.findItem(R.id.rename_scenario).setTitle("Cancel rename scenario")
                            view.findViewById<TextView>(R.id.choosed_title).setText("Rename \nyour Scenario")
                            popupMenu.menu.findItem(R.id.delete_scenario).setTitle("delete scenario")

                            popupMenu.menu.findItem(R.id.set_time_scenario).setTitle("Set time for scenario")
                            popupMenu.menu.findItem(R.id.edit_scenario).setTitle("edit scenario")
                            scenario_side="rename"

                            view.findViewById<TextView>(R.id.choosed_title).setTextColor(
                                ColorStateList.valueOf(Color.BLUE)
                            )

                        }

                    }
                    R.id.edit_scenario -> {



                        Toast.makeText(context, "edit_scenario", Toast.LENGTH_SHORT).show()


                        Toast.makeText(context,"edit_scenario is Active",Toast.LENGTH_SHORT).show()
                        if (scenario_side== "edit"){
                            scenario_side="user"

                            popupMenu.menu.findItem(R.id.edit_scenario).setTitle("edit scenario")
                            view.findViewById<TextView>(R.id.choosed_title).setText("Choose \nyour Scenario")

                            view.findViewById<TextView>(R.id.choosed_title).setTextColor(
                                ColorStateList.valueOf(Color.WHITE)
                            )


                        }else{
                            println(scenario_side)
                            popupMenu.menu.findItem(R.id.edit_scenario).setTitle("Cancel edit scenario")
                            view.findViewById<TextView>(R.id.choosed_title).setText("edit \nyour Scenario")
                            popupMenu.menu.findItem(R.id.delete_scenario).setTitle("delete scenario")
                            popupMenu.menu.findItem(R.id.rename_scenario).setTitle("rename scenario")
                            popupMenu.menu.findItem(R.id.set_time_scenario).setTitle("Set time for scenario")

                            scenario_side="edit"

                            view.findViewById<TextView>(R.id.choosed_title).setTextColor(
                                ColorStateList.valueOf(Color.BLUE)
                            )

                        }

                    }


                }
                true
            }
            popupMenu.show()
        }



//        var chooser_fragment_list= listOf(senario_btn_l1r1,senario_btn_l1r2,senario_btn_l1r3,senario_btn_l2r1 ,senario_btn_l2r2 ,senario_btn_l2r3 ,senario_btn_l3r1 ,senario_btn_l3r2,senario_btn_l3r3)
//        for (button in chooser_fragment_list) {
//            button.setButtonScaleOnTouchListener()
//
//        }


    }
    override fun onDestroy() {
        super.onDestroy()
         
    }
    override fun onPause() {
        super.onPause()
         
    }


}
