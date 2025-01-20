package com.example.griffin

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.griffin.adapters.MyPagerAdapter
import com.example.griffin.adapters.ScenarioAdapter
import com.example.griffin.database.*
import com.example.griffin.fragment.griffin_home_frags.MusicAdapter
import com.example.griffin.mudels.*
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.dnsoverhttps.DnsOverHttps

import org.json.JSONObject
import java.io.IOException
import java.net.*
import java.text.SimpleDateFormat
import java.util.*


class dashboard : AppCompatActivity(), ContextListener  {
//
//    private val screenReceiver = ScreenReceiver(this)
    private val REQUEST_CODE_STORAGE = 100
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: MyPagerAdapter

    private  val PERMISSION_REQUEST_FOREGROUND_SERVICE = 101

//    private lateinit var udpReceiverThread: UdpReceiverThread

    private  val SharedViewModel: SharedViewModel by viewModels()
    private lateinit var viewModel: UdpViewModel


    fun taghsim(dividend: Double, divisor: Double): Double {
        val result = dividend / divisor // انجام تقسیم بدون ضرب در 100
        return String.format("%.4f", result).toDouble() // تبدیل نتیجه به چهار رقم اعشاری
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


    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
        private const val MIN_TIME_BW_UPDATES: Long = 1000 * 60 * 2 // 2 minutes
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10f // 10 meters
    }


    private fun checkForegroundServicePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val hasForegroundServicePermission = checkSelfPermission(android.Manifest.permission.FOREGROUND_SERVICE)
            if (hasForegroundServicePermission != PackageManager.PERMISSION_GRANTED) {
                // درخواست دسترسی از کاربر
                requestPermissions(arrayOf(android.Manifest.permission.FOREGROUND_SERVICE), PERMISSION_REQUEST_FOREGROUND_SERVICE)
            } else {
                // دسترسی به سرویس فورگراند داریم
                // ادامه اجرای برنامه
            }
        } else {
            // در SDK های کمتر از 23، دسترسی اتوماتیک است و نیازی به درخواست آن نیست
            // ادامه اجرای برنامه
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) &&
                (grantResults.isNotEmpty() && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                // Permissions granted, proceed with the action
            } else {
                // Permissions denied, handle appropriately
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }





    private lateinit var wakeLock: PowerManager.WakeLock
    @SuppressLint("MissingInflatedId", "Range", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
//         
        super.onCreate(savedInstanceState)


        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_dashboard)
        val receivedBundle = intent.extras
        val receivedMessage = if (receivedBundle != null) {
            receivedBundle.getString("mode")
        } else {
            "defaultMode"
        }
        val message_response = setting_simcard_messageresponse_db.getInstance(this)
        if (!message_response.isDatabaseValid(this)){
            val simcard_messageresponse= simcard_messageresponse()
            simcard_messageresponse.scenario_r="Scenario done"
            simcard_messageresponse.module_r="Module command done"
            simcard_messageresponse.sensor_r="Sensor command done"
            simcard_messageresponse.security_r="Security command done"
            message_response.set_to_db_simcard_message_response(simcard_messageresponse)
//            Toast.makeText(this, "seted...", Toast.LENGTH_SHORT).show()
            println("Seted")

        }

        var compName: ComponentName
            compName = ComponentName(this, DeviceAdmin::class.java)
            val enableResult = 1
            val deviceManger = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            val active = deviceManger.isAdminActive(compName)
            if (active) {
               println("admin active")
            }
            else {
                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!")
                startActivityForResult(intent, enableResult)
            }
//            deviceManger.lockNow()

//        if (!Settings.canDrawOverlays(this)) {
//            val intent =
//                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
//            startActivityForResult(intent, 0)
//        }




        val network_db=setting_network_db.getInstance(this)
        if (network_db.isDatabaseEmpty()){
            val network_manual1=network_manual()
            network_manual1.modem_ssid=""
            val network=network_db.set_to_db_network_manual(network_manual1)

        }



        val masterMaster_slave_db_handler = Master_slave_db.getInstance(this)
        val masterSlaveDb=masterMaster_slave_db_handler.getAllMaster_slaves()

        if (masterSlaveDb.isEmpty()){
            val my_master = Master_slave()
            my_master.id=1
            my_master.status="master"
            masterMaster_slave_db_handler.set_to_db_Master_slave(my_master)

        }
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)


        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakeLock")

        // روشن کردن صفحه در صورت خاموش شدن
        wakeLock.acquire()

        checkForegroundServicePermission()
//        val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
//        startActivity(intent)
        UdpListener8090.setContextListener(this@dashboard)
        if (UdpListener8090.activity == this@dashboard) {
            println("boood")

        }else{


            println("added")
            UdpListener8090.init(this@dashboard)
        }
        UdpListener8090.start()

//        startListen90g()


        val serviceIntent = Intent(this@dashboard, MyForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        UdpListener8089.setContextListener(this@dashboard)
        UdpListener8089.init(this@dashboard)
//        startListening()
        UdpListener8089.start()
//        UdpListener8089.aut_recconect()
//        ScreenReceiver.initialize(this)
//        try {
//            unregisterReceiver(ScreenReceiver)
//        }catch (e:Exception){
//            println("reciver not exist")
//
//        }
//        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
////        unregisterReceiver(ScreenReceiver)
//        registerReceiver(ScreenReceiver, filter)


//        registerReceiver(screenReceiver, filter)


        var currentApiVersion: Int = 0
        currentApiVersion = Build.VERSION.SDK_INT
        val flags: Int = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
//        window.decorView.systemUiVisibility = flags
            val decorView: View = window.decorView
            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN === 0) {
                    decorView.systemUiVisibility = flags
                }
            }
        }
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        setContentView(R.layout.activity_dashboard)




//        val wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE, "MyApp::WakeLock")
//        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/)





        val btn_iphone = findViewById<Button>(R.id.btn_iphone)
        val btn_camera = findViewById<Button>(R.id.btn_camera)
        val btn_display_off = findViewById<Button>(R.id.btn_display_off)
        val btn_setting = findViewById<Button>(R.id.btn_setting)
        val btn_griffin_home = findViewById<Button>(R.id.btn_griffin_home)
        val btn_exit_senario = findViewById<Button>(R.id.btn_exit_senario)
        val btn_enter_senario = findViewById<Button>(R.id.btn_enter_senario)
        val weather_image=findViewById<ImageView>(R.id.weather_image)
        val temp_textview=findViewById<TextView>(R.id.temp_textview)
        val weather_text=findViewById<TextView>(R.id.weather_text)
        val internetconection=findViewById<TextView>(R.id.internetconection)
        val show_musics = findViewById<Button>(R.id.show_musics)
        val previus_musics = findViewById<Button>(R.id.previus_musics)
        val play_puse_musics = findViewById<Button>(R.id.play_puse_musics)
        val next_musics = findViewById<Button>(R.id.next_musics)



        btn_display_off.setOnClickListener {

            val intent = Intent(this, black_screen::class.java)
            startActivity(intent)
//            overridePendingTransition(R.anim.snowflakes_in, R.anim.snowflakes_in)



//            var compName: ComponentName
//            compName = ComponentName(this, DeviceAdmin::class.java)
//            val enableResult = 1
//            val deviceManger = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//            val active = deviceManger.isAdminActive(compName)
//            if (active) {
//               println("admin active")
//            }
//            else {
//                val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
//                intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName)
//                intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "You should enable the app!")
//                startActivityForResult(intent, enableResult)
//            }
//            deviceManger.lockNow()
        }


        val inflater = LayoutInflater.from(this)

//        val customPopupView: View = inflater.inflate(R.layout.scenario_loading, null)
        val popupView: View = inflater.inflate(R.layout.scenario_loading, null)
        val popupWidth = 530
        val popupHeight = 530
        // ایجاد لایه‌ی کاستوم
        // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
//        val loadingView=popupView.findViewById<CustomLoadingCircle>(R.id.custom_loading_view)
        val popupWindow = PopupWindow(popupView, popupWidth, popupHeight, true)
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(popupView)
        val alertDialog = alertDialogBuilder.create()
        val loading_view=popupView.findViewById<CustomLoadingLine>(R.id.loadingAnim)


        btn_enter_senario.setOnClickListener {
            try {
                val db_handler = scenario_db.Scenario_db.getInstance(this)

                val inter= db_handler.getItemsWithInExIn()


                val status = SharedViewModel.is_doing.value

                println(status)
                var scenario_side="user"

                val selectedItem = inter[0]

                if (scenario_side=="user"){

                    println("userrrr")

                    if ((status != "bussy") || (status==null)){
                        Toast.makeText(this, selectedItem.scenario_name, Toast.LENGTH_SHORT).show()
                        popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)
                        Thread{
                            this.runOnUiThread {
                                SharedViewModel.update_is_doing("bussy")
                            }

                            loading_view.increaseProgress(0.0)

                            val light_database_handler=light_db.getInstance(this)
                            val thermostat_database_handler= Temperature_db.getInstance(this)
                            val curtain_database_handler= curtain_db.getInstance(this)
                            val valve_database_handler= valve_db.getInstance(this)
                            val fan_database_handler= fan_db.getInstance(this)
                            val plug_database_handler= plug_db.getInstance(this)

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

                            val scenario_database_handler= scenario_db.Scenario_db.getInstance(this)
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



                                val musicPlayer = Music_player.getInstance(this)
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
//                        val refreshed_lights=refresh_light_for_scenario(this,final_same_mac)



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

                                            val send=udp_light_scenario(this!!,same)


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
//                        val refreshed_plugs=refresh_plug_for_scenario(this,final_same_mac)
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

                                            val send=udp_plug_scenario(this,same)


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

                                            val send=udp_valve_for_scenario(this,same)


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

                                            val send= udp_curtain_for_scenario(this!!,same[0],same[0].status)


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

                                            val send= udp_thermostat_for_scenario(this!!,same[0],same[0].mac,same[0].mood,same[0].temperature,same[0].fan_status,same[0].on_off,same[0].ip)


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

                                            val send= udp_fan_for_scenario(this,same)


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

                                            val send=udp_light_scenario(this!!,same)


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


                                            val send= udp_thermostat_for_scenario(this!!,same[0],same[0].mac,same[0].mood,same[0].temperature,same[0].fan_status,same[0].on_off,same[0].ip)


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

                                            val send = udp_curtain_for_scenario(this!!, same[0], same[0].status)


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
//                        val refreshed_plugs=refresh_plug_for_scenario(this,final_same_mac)
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

                                            val send=udp_plug_scenario(this,same)


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

                                            val send= udp_valve_for_scenario(this,same)


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

                                            val send= udp_fan_for_scenario(this,same)


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







                            this.runOnUiThread {
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
                            this.runOnUiThread{
                                popupWindow.dismiss()
                            }

                            println("done")
                            println(all_scenario_count)
                            println(fan_to_do.count()+valve_to_do.count()+plug_to_do.count()+curtain_to_do.count()+thermostat_to_do.count()+light_to_do.count())
                            if (0==fan_to_do.count()+valve_to_do.count()+plug_to_do.count()+curtain_to_do.count()+thermostat_to_do.count()+light_to_do.count()){
                                this.runOnUiThread{
                                    Toast.makeText(this, "Scenario Done", Toast.LENGTH_SHORT).show()
                                }


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
                                this.runOnUiThread {

                                    popupWindow.dismiss()
                                }
                                println(final_list_to_show)





                            }




                        }.start()






                    }else{

                        Toast.makeText(this, "please Wait...", Toast.LENGTH_SHORT).show()
                    }


                }











            }catch(e:Exception){

                println(e)

            }




        }


        btn_enter_senario.setOnLongClickListener {

            val db_handler = scenario_db.Scenario_db.getInstance(this)
            var newlist = db_handler.getAllScenario()
            val inter= db_handler.getItemsWithInExIn()

            val inflater4 = LayoutInflater.from(this)
            val customPopupView4: View = inflater4.inflate(R.layout.add_music_to_scenario_popup, null)



            val popupWidth4 = 650
            val popupHeight4 = 650
            val popupWindow4 = PopupWindow(customPopupView4, popupWidth4, popupHeight4, true)
            popupWindow4.isFocusable = true

            val recyclerView: RecyclerView = customPopupView4.findViewById(R.id.scenario_add_music_recycler_view)
            val cancel_btn: Button = customPopupView4.findViewById(R.id.cancel)
            val layoutManager = GridLayoutManager(this, 3) // تعداد ستون‌ها را 3 قرار دهید
            recyclerView.layoutManager = layoutManager
            cancel_btn.setOnClickListener {
                popupWindow4.dismiss()
            }
            popupWindow4.showAtLocation(it, Gravity.CENTER, 0, 0)
            val adapter = ScenarioAdapter(newlist) { selectedItem ->

                if (inter.count()==0){
                    selectedItem.in_ex="in"
                    db_handler.updateScenarioById(selectedItem.id,selectedItem)

                }else{
                    inter[0].in_ex=""
                    db_handler.updateScenarioById(inter[0].id,inter[0])
                    selectedItem.in_ex="in"
                    db_handler.updateScenarioById(selectedItem.id,selectedItem)
                }
                popupWindow4.dismiss()
                Toast.makeText(this, "${selectedItem.scenario_name} Setted for Inter Scenario", Toast.LENGTH_SHORT).show()


            }
            recyclerView.adapter = adapter
            adapter.setItems(newlist)




            return@setOnLongClickListener true
        }


        btn_exit_senario.setOnLongClickListener {

            val db_handler = scenario_db.Scenario_db.getInstance(this)
            var newlist = db_handler.getAllScenario()
            val inter= db_handler.getItemsWithInExex()

            val inflater4 = LayoutInflater.from(this)
            val customPopupView4: View = inflater4.inflate(R.layout.add_music_to_scenario_popup, null)



            val popupWidth4 = 650
            val popupHeight4 = 650
            val popupWindow4 = PopupWindow(customPopupView4, popupWidth4, popupHeight4, true)
            popupWindow4.isFocusable = true

            val recyclerView: RecyclerView = customPopupView4.findViewById(R.id.scenario_add_music_recycler_view)
            val cancel_btn: Button = customPopupView4.findViewById(R.id.cancel)
            val layoutManager = GridLayoutManager(this, 3) // تعداد ستون‌ها را 3 قرار دهید
            recyclerView.layoutManager = layoutManager
            cancel_btn.setOnClickListener {
                popupWindow4.dismiss()
            }
            popupWindow4.showAtLocation(it, Gravity.CENTER, 0, 0)
            val adapter = ScenarioAdapter(newlist) { selectedItem ->

                if (inter.count()==0){
                    selectedItem.in_ex="ex"
                    db_handler.updateScenarioById(selectedItem.id,selectedItem)

                }else{
                    inter[0].in_ex=""
                    db_handler.updateScenarioById(inter[0].id,inter[0])
                    selectedItem.in_ex="ex"
                    db_handler.updateScenarioById(selectedItem.id,selectedItem)
                }
                popupWindow4.dismiss()
                Toast.makeText(this, "${selectedItem.scenario_name} Setted for Inter Scenario", Toast.LENGTH_SHORT).show()


            }
            recyclerView.adapter = adapter
            adapter.setItems(newlist)




            return@setOnLongClickListener true
        }



        viewPager = findViewById(R.id.choosed_viewpager)
        pagerAdapter = MyPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        btn_setting.setOnClickListener {
            val intent = Intent(this, setting::class.java)


            if (receivedMessage=="admin") {
                intent.putExtra("mode", "admin")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            finish()

        }
        btn_griffin_home.setOnClickListener {

            val isempty = rooms_db.getInstance(this).isEmptynetwork_tabale()

            if (!isempty){
                val intent = Intent(this, griffin_home::class.java)


                if (receivedMessage=="admin") {
                    intent.putExtra("mode", "admin")
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)

            }else{
                Toast.makeText(this, "add room first", Toast.LENGTH_SHORT).show()

            }


        }











        btn_exit_senario.setOnClickListener {

            if (true){




                try {

                    val elevator_db_handler= Elevator_db.getInstance(this)
                    udp_elevator(this ,elevator_db_handler.getAllElevators()[0])

                }catch (e:Exception){

                    println(e)
                }



                try {


                    val db_handler = scenario_db.Scenario_db.getInstance(this)

                    val inter= db_handler.getItemsWithInExex()

                    val status = SharedViewModel.is_doing.value

                    println(status)
                    var scenario_side="user"

                    val selectedItem = inter[0]

                    if (scenario_side=="user"){

                        println("userrrr")




                        if ((status != "bussy") || (status==null)){
                            Toast.makeText(this, selectedItem.scenario_name, Toast.LENGTH_SHORT).show()
                            popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)


                            Thread{
                                this.runOnUiThread {
                                    SharedViewModel.update_is_doing("bussy")
                                }

                                loading_view.increaseProgress(0.0)

                                val light_database_handler=light_db.getInstance(this)
                                val thermostat_database_handler= Temperature_db.getInstance(this)
                                val curtain_database_handler= curtain_db.getInstance(this)
                                val valve_database_handler= valve_db.getInstance(this)
                                val fan_database_handler= fan_db.getInstance(this)
                                val plug_database_handler= plug_db.getInstance(this)

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



                                val scenario_database_handler= scenario_db.Scenario_db.getInstance(this)
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



                                    val musicPlayer = Music_player.getInstance(this)
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
//                        val refreshed_lights=refresh_light_for_scenario(this,final_same_mac)



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

                                                val send=udp_light_scenario(this!!,same)


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
//                        val refreshed_plugs=refresh_plug_for_scenario(this,final_same_mac)
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

                                                val send=udp_plug_scenario(this,same)


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

                                                val send=udp_valve_for_scenario(this,same)


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

                                                val send= udp_curtain_for_scenario(this!!,same[0],same[0].status)


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

                                                val send= udp_thermostat_for_scenario(this!!,same[0],same[0].mac,same[0].mood,same[0].temperature,same[0].fan_status,same[0].on_off,same[0].ip)


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

                                                val send= udp_fan_for_scenario(this,same)


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

                                                val send=udp_light_scenario(this!!,same)


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


                                                val send= udp_thermostat_for_scenario(this!!,same[0],same[0].mac,same[0].mood,same[0].temperature,same[0].fan_status,same[0].on_off,same[0].ip)


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

                                                val send = udp_curtain_for_scenario(this!!, same[0], same[0].status)


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
//                        val refreshed_plugs=refresh_plug_for_scenario(this,final_same_mac)
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

                                                val send=udp_plug_scenario(this,same)


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

                                                val send= udp_valve_for_scenario(this,same)


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

                                                val send= udp_fan_for_scenario(this,same)


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







                                this.runOnUiThread {
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
                                this.runOnUiThread{
                                    popupWindow.dismiss()
                                }

                                println("done")
                                println(all_scenario_count)
                                println(fan_to_do.count()+valve_to_do.count()+plug_to_do.count()+curtain_to_do.count()+thermostat_to_do.count()+light_to_do.count())
                                if (0==fan_to_do.count()+valve_to_do.count()+plug_to_do.count()+curtain_to_do.count()+thermostat_to_do.count()+light_to_do.count()){
                                    this.runOnUiThread{
                                        Toast.makeText(this, "Scenario Done", Toast.LENGTH_SHORT).show()
                                    }


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
                                    this.runOnUiThread {

                                        popupWindow.dismiss()
                                    }
                                    println(final_list_to_show)





                                }



                            }.start()





                        }else{

                            Toast.makeText(this, "please Wait...", Toast.LENGTH_SHORT).show()
                        }


                    }



                }catch(e:Exception){

                    println(e)

                }

            }

        }



        val senario_selector_btn: Button = findViewById(R.id.senario_selector_btn)
        val favorite_selector_btn: Button = findViewById(R.id.favorite_selector_btn)
        val security_selector_btn: Button = findViewById(R.id.security_selector_btn)
        val energy_selector_btn: Button = findViewById(R.id.energy_selector_btn)

        val musicPlayer = Music_player.getInstance(this)




        show_musics.setOnClickListener {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_STORAGE)
            }
            println("ssssssssss"+musicList)
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA
            )

            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

            val cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
            )

            val new_list=
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                        val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                        val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                        val new=MusicModel(title, artist, path, path,"false")
                        val current_urls = mutableListOf<String>()
                        for (item in musicList){
                            current_urls.add(item.audioUrl)

                        }
                        // افزودن اطلاعات به لیست

                        if (new.audioUrl !in current_urls){
                            musicList.add(new)
                            println("added")

                        }

                    }
                    cursor.close()
                } else {

                }



            val adapter = MusicAdapter.getInstance(this)
            adapter.updateAdapterValue(musicList)
            viewPager.currentItem = 4
            favorite_selector_btn.setBackgroundResource(R.drawable.favorite_unselect)

            security_selector_btn.setBackgroundResource(R.drawable.seciurity_btn_unselect)

            energy_selector_btn.setBackgroundResource(R.drawable.energy_metering_unselect)
            senario_selector_btn.setBackgroundResource(R.drawable.senario_btn_unselect)

        }

        previus_musics.setOnClickListener {
            var play_target = "khali"
            var index= -1

            for ( item in musicList){
                if ((item.isplaying=="true") || (item.isplaying=="pause")  ){
                    index= musicList.indexOf(item)
//                    play_target=item.audioUrl
                }

            }
            if (index == -1){


            }else{
                if ((index - 1) <= 0){


                    musicPlayer.playMusic(musicList[0].audioUrl)
                    var new_list= musicList

                    var current=new_list[index]
                    current.isplaying="false"
                    new_list[index]=current

                    var new_status_music=new_list[0]
                    new_status_music.isplaying="true"
                    new_list[0]= new_status_music
                    musicList=new_list
                }else if ((index - 1) <= musicList.count()){
                    musicPlayer.playMusic(musicList[(index - 1)].audioUrl)
                    var new_list= musicList

                    var current=new_list[index]
                    current.isplaying="false"
                    new_list[index]=current

                    var new_status_music=new_list[(index - 1 )]
                    new_status_music.isplaying="true"
                    new_list[(index - 1 )]= new_status_music
                    musicList=new_list

                }

            }

            val adapter = MusicAdapter.getInstance(this)
            adapter.updateAdapterValue(musicList)

            println(musicList)



        }
        play_puse_musics.setOnClickListener {
            println(musicList)
            if (musicList.count()==0 ){
                musicList= musicList2
            }
            var index= -1
            var previus_playing=""

            for ( item in musicList){
                if ((item.isplaying=="true") || (item.isplaying=="pause")  ){
                    index= musicList.indexOf(item)
//                    play_target=item.audioUrl
                }

            }

            var play_target = "khali"
            if (index!=-1){
                play_target= "por"

            }


            if (play_target=="khali"){
                if (musicList.count() > 0){

                    musicPlayer.playMusic(musicList[0].audioUrl)




                    var new_list= musicList

                    var new_status_music=new_list[0]
                    new_status_music.isplaying="true"
                    new_list[0]= new_status_music
                    musicList=new_list
                    println("play first")
                    println(musicList)
                }


            }else{
                if (musicPlayer.isPlaying()){

                    var new_list= musicList
                    var current=new_list[index]
                    current.isplaying="pause"
                    new_list[index]=current
                    musicList=new_list

                    musicPlayer.pauseMusic()
                    previus_playing=play_target
                    println("stop")

                }else{
                    var new_list= musicList
                    var current=new_list[index]
                    current.isplaying="true"
                    new_list[index]=current
                    musicList=new_list

                    musicPlayer.pauseMusic()
                    previus_playing=play_target
                    musicPlayer.resumeMusic()
                    println("resume")

                }



            }
            val adapter = MusicAdapter.getInstance(this)
            adapter.updateAdapterValue(musicList)


        }
        next_musics.setOnLongClickListener {
            var current = musicPlayer.getCurrentPosition()
            musicPlayer.seekTo(current + 10000)

            true
        }
        previus_musics.setOnLongClickListener {
            var current = musicPlayer.getCurrentPosition()
            musicPlayer.seekTo(current - 10000)

            true
        }
        next_musics.setOnClickListener {

            var index= -1

            for ( item in musicList){
                if ((item.isplaying=="true") || (item.isplaying=="pause")  ){
                    index= musicList.indexOf(item)
//                    play_target=item.audioUrl
                }

            }
            if (index == -1){


            }else{

                if ((index+1) >= musicList.count()){


                    musicPlayer.playMusic(musicList[0].audioUrl)
                    var new_list= musicList

                    var current=new_list[index]
                    current.isplaying="false"
                    new_list[index]=current


                    var new_status_music=new_list[0]
                    new_status_music.isplaying="true"
                    new_list[0]= new_status_music
                    musicList=new_list
                }else if ((index + 1) < musicList.count()){
                    musicPlayer.playMusic(musicList[(index + 1)].audioUrl)
                    var new_list= musicList
                    var current=new_list[index]
                    current.isplaying="false"
                    new_list[index]=current

                    var new_status_music=new_list[(index + 1 )]
                    new_status_music.isplaying="true"
                    new_list[(index + 1 )]= new_status_music
                    musicList=new_list
                }
            }
            val adapter = MusicAdapter.getInstance(this)
            adapter.updateAdapterValue(musicList)
            println(musicList)


        }





//         

        //      ########################################################################
//      buttons settings animation and sound

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






//,btn_arme,btn_disarme,btn_sleep

        var dashboard_btn_list= listOf(btn_iphone,btn_camera,btn_display_off,btn_setting,btn_griffin_home,btn_exit_senario,btn_enter_senario,next_musics,play_puse_musics,previus_musics,show_musics)
        for (button in dashboard_btn_list) {
            button.setButtonScaleOnTouchListener()



        }
        btn_camera.setOnClickListener {
            val intent = Intent(this, RTSPVideoPlayerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)

        }



//      ########################################################################
//      chooser buttons


        val chooser_btns_list = listOf(senario_selector_btn, favorite_selector_btn, security_selector_btn, energy_selector_btn)
        fun selectButton(selectedButton: Button) {
            SoundManager.playSound()
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    SoundManager.playSound()
                    if (button==senario_selector_btn){
                        button.setBackgroundResource(R.drawable.senario_btn_select)
                    }
                    if (button==favorite_selector_btn){
                        button.setBackgroundResource(R.drawable.favorite_select)
                    }
                    if (button==security_selector_btn){
                        button.setBackgroundResource(R.drawable.seciurity_btn_select)
                    }
                    if (button==energy_selector_btn){
                        button.setBackgroundResource(R.drawable.energy_metering_select)
                    }

                } else {
//                    SoundManager.playSound()
                    button.isSelected = false
                    if (button==senario_selector_btn){
                        button.setBackgroundResource(R.drawable.senario_btn_unselect)
                    }
                    if (button==favorite_selector_btn){
                        button.setBackgroundResource(R.drawable.favorite_unselect)
                    }
                    if (button==security_selector_btn){
                        button.setBackgroundResource(R.drawable.seciurity_btn_unselect)
                    }
                    if (button==energy_selector_btn){
                        button.setBackgroundResource(R.drawable.energy_metering_unselect)
                    }

                }
            }
        }


        senario_selector_btn.setOnClickListener {
            selectButton(senario_selector_btn)
            viewPager.currentItem = 0

        }

        favorite_selector_btn.setOnClickListener {
            selectButton(favorite_selector_btn)
            viewPager.currentItem = 1
        }

        security_selector_btn.setOnClickListener {
            selectButton(security_selector_btn)
            viewPager.currentItem = 2
        }

        energy_selector_btn.setOnClickListener {
            selectButton(energy_selector_btn)
            viewPager.currentItem = 3
        }


        fun fist_run_chose(viewPager: ViewPager){
                if (viewPager.currentItem == 0 ){
                    senario_selector_btn.setBackgroundResource(R.drawable.senario_btn_select)

                }
//                if (viewPager.currentItem == 1 ){
//                    favorite_selector_btn.setBackgroundResource(R.drawable.favorite_select)
//
//                }
//                if (viewPager.currentItem == 2 ){
//                    security_selector_btn.setBackgroundResource(R.drawable.seciurity_btn_select)
//
//                }
//                if (viewPager.currentItem == 3 ){
//                    energy_selector_btn.setBackgroundResource(R.drawable.energy_metering_select)
//
//                }





        }
        fist_run_chose(viewPager)





        val clock = findViewById<TextView>(R.id.clockid)
        val namedate=findViewById<TextView>(R.id.namedate)
        val date=findViewById<TextView>(R.id.date)

        // ایجاد یک Timer و Handler جدید
        var timer = Timer()
        var handler = Handler()

        // شروع به‌روزرسانی مقدار ساعت هر ثانیه
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    // به‌روزرسانی مقدار ساعت
                    val currentDate = Calendar.getInstance().time
                    val dateFormat = SimpleDateFormat("hh:mm a")
                    val currentTime = dateFormat.format(currentDate.time)
                    clock.text = currentTime


                    val calendar = Calendar.getInstance()
                    calendar.time = currentDate
                    val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                    val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
                    val formattedDayOfWeek = dayOfWeekFormat.format(currentDate)
                    val formattedMonth = monthFormat.format(currentDate)
                    val year = calendar.get(Calendar.YEAR)

                    val formattedDate = "$formattedDayOfWeek\n$formattedMonth $year"
                    namedate.text=formattedDate

                    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
                    date.text=dayOfMonth.toString()

//8 12 16 22

                }
            }
        }, 0, 60000)










    }





    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakeLock")

        // روشن کردن صفحه در صورت خاموش شدن
        wakeLock.acquire()
//        val filter = IntentFilter(Intent.ACTION_SCREEN_OFF)
//        registerReceiver(screenReceiver, filter)
        hideSystemUI()

//        fun security_popup(){
//
//
//            val security_db_handler = security_db.getInstance(this)
//            if (!security_db_handler.isEmptysecurity_tabale()) {
//                val current_db = security_db_handler.get_from_db_security(1)
//
//                val time = (current_db!!.arm_active_deley?.toInt())?.times(1000)
//                val pass = current_db.password_security
//
//
//                val inflater5 = LayoutInflater.from(this)
//                val popupView5: View = inflater5.inflate(R.layout.popup_layout, null)
//                val wrong_password_security =
//                    popupView5.findViewById<TextView>(R.id.wrong_password_security)
//                wrong_password_security.visibility = View.GONE
//                val ok = popupView5.findViewById<Button>(R.id.ok_btn)
//                val enterd_password = popupView5.findViewById<EditText>(R.id.intered_pass)
//
//
//                val alertDialogBuilder5 = AlertDialog.Builder(this)
//                alertDialogBuilder5.setView(popupView5)
//
//                val tvCountdown = popupView5.findViewById<TextView>(R.id.tvCountdown)
//
//                var is_ok = false
//
//                val horn = CarHornSoundPlayer.getInstance()
//                val alertDialog5 = alertDialogBuilder5.create()
//                alertDialog5.show()
//                alertDialog5.setCanceledOnTouchOutside(false)
//                alertDialog5.setCancelable(false)
//                ok.setOnClickListener {
//                    if (enterd_password.text.toString() == pass.toString()) {
//                        alertDialog5.dismiss()
//                        horn.stopHornSound()
//                        Toast.makeText(this, "Disarmed...", Toast.LENGTH_LONG).show()
//                        is_ok = true
//                        SharedViewModel.countdownLiveData.postValue(999999)
//                    } else {
//                        wrong_password_security.visibility = View.VISIBLE
//                    }
//                }
//                // حذف بک‌گراند دیالوگ الرت
//                alertDialog5.window?.setBackgroundDrawableResource(android.R.color.transparent)
//
//                val countDownTimer = object :
//                    CountDownTimer(time!!.toLong(), 1000) { // 10000 میلی‌ثانیه معادل 10 ثانیه است
//                    override fun onTick(millisUntilFinished: Long) {
//                        val seconds = millisUntilFinished / 1000
//                        tvCountdown.text = seconds.toString()
//                        SharedViewModel.countdownLiveData.postValue(seconds)
//                    }
//
//                    override fun onFinish() {
//                        SharedViewModel.countdownLiveData.postValue(999999)
//                        tvCountdown.text = "0"
//                        if (!is_ok) {
//                            println("bang")
//                            tvCountdown.setTextColor(Color.RED)
//                            horn.startHornSound()
//
//                        } else {
//                            alertDialog5.dismiss()
//                            horn.stopHornSound()
//                            println("disarmed")
//
//                        }
//                        // اینجا می‌توانید کدی برای اجرا پس از پایان شمارنده قرار دهید
//                        //                    println("finish")
//                    }
//                }
//                countDownTimer.start()
//
//
//                //        }
//            }
//        }




//        fun isPortInUse(port: Int): Boolean {
//            var isUsed = false
//            var socket: DatagramSocket? = null
//            try {
//                // تلاش برای اتصال به پورت
//                socket = DatagramSocket(port)
//                // اگر به پورت موفقیت‌آمیز بایند شود، به این معناست که پورت قبلاً درگیر بوده است
//                isUsed = false
//            } catch (e: IOException) {
//                // اگر خطایی رخ داده، به این معناست که پورت در حال حاضر درگیر است
//                isUsed = true
//            } finally {
//                socket?.close()
//            }
//            return isUsed
//        }


//        @SuppressLint("ResourceType")
//        fun listenner(){
//            var socket: DatagramSocket? = null
//            var isSocketOpen = false // وضعیت باز بودن سوکت
//            GlobalScope.launch(Dispatchers.IO) {
//                val bufferSize = 1024
//                val receiveData = ByteArray(bufferSize)
//
//                while (true) {
//                    if (!isSocketOpen) {
//                        socket = DatagramSocket(8090)
//                        isSocketOpen = true
//                        println("Listenning to 8090..")
//                    }
//
//                    val receivePacket = DatagramPacket(receiveData, receiveData.size)
//
//
//                }
//            }
//
//        }









//        val handlerr = Handler()
//        val runnable = object : Runnable {
//            override fun run() {
//                val isPortUsed = isPortInUse(8090)
//                if (isPortUsed){
//                    println("Port $8090 is in use: $isPortUsed")
//
//                }else{
//                    listenner()
//
//                }
//
//                // اینجا کدی که می‌خواهید هر 5 ثانیه اجرا شود را قرار دهید
//
//
//                // به عنوان مثال:
//                // checkSomething()
//                println("Checking something...")
//                handlerr.postDelayed(this, 5000) // اجرای مجدد Runnable هر 5 ثانیه
//            }
//        }
//
//// اولین اجرای Runnable
//        handlerr.post(runnable)





        val weather_image=findViewById<ImageView>(R.id.weather_image)
        val temp_textview=findViewById<TextView>(R.id.temp_textview)
        val weather_text=findViewById<TextView>(R.id.weather_text)
        val internetconection=findViewById<TextView>(R.id.internetconection)





        fun weather(loation:String,apiKey:String) {

            val dohClient = OkHttpClient()

            val dns = DnsOverHttps.Builder()
                .client(dohClient)
                .url("https://free.shecan.ir/dns-query".toHttpUrl()) // تغییر URL به free.shecan.ir
                .bootstrapDnsHosts(
                    InetAddress.getByName("178.22.122.100"),
                    InetAddress.getByName("185.51.200.2")
                )
                .build()

// ساختن کلاینت OkHttp با DNS over HTTPS
            val client = OkHttpClient.Builder()
                .dns(dns)
                .build()

// تعریف پارامترهای درخواست
            val baseUrl = "https://api.openweathermap.org/data/2.5/"
            val endpoint = "weather"
            val apiKey =  apiKey// کلید API خود را وارد کنید
            val location = loation // نام مکان مورد نظر

// ساخت URL درخواست
            val urlBuilder = (baseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
            urlBuilder?.addQueryParameter("q", location)
            urlBuilder?.addQueryParameter("appid", apiKey)

            val url = urlBuilder?.build()?.toString()

// ساخت درخواست
            val request = url?.let { it1 ->
                Request.Builder()
                    .url(it1)
                    .build()
            }

// چاپ درخواست برای بررسی
            println(request)

// ارسال درخواست
            request?.let {
                client.newCall(it).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        response.use {
                            if (!it.isSuccessful) throw IOException("Unexpected code $it")

                            // پردازش پاسخ
                            println(it.body?.string())
                        }
                    }
                })
            }





//            val dohClient = OkHttpClient()
//
//            val dns = DnsOverHttps.Builder()
//                .client(dohClient)
//                .url("https://dns.google/dns-query".toHttpUrl())
//                .bootstrapDnsHosts(
//                    InetAddress.getByName("178.22.122.100"),
//                    InetAddress.getByName("185.51.200.2")
//                )
//                .build()
//
//            val client = OkHttpClient.Builder()
//                .dns(dns)
//                .build()
//
//            val baseUrl = "https://api.openweathermap.org/data/2.5/"
//            val endpoint = "weather"
//            val apiKey = apiKey
////                "5dab3c5870888bb3ee81e939fc4d4dcc"
//            val location = loation
//
//            val urlBuilder = (baseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
//            urlBuilder?.addQueryParameter("q", location)
//            urlBuilder?.addQueryParameter("appid", apiKey)
//
//            val url = urlBuilder?.build()?.toString()
//
//            val request = url?.let { it1 ->
//                Request.Builder()
//                    .url(it1)
//                    .build()
//            }
//            println(request)


//            val client = OkHttpClient()
//            val baseUrl = "https://api.openweathermap.org/data/2.5/"
//            val endpoint = "weather"
//            val apiKey = apiKey
////                "5dab3c5870888bb3ee81e939fc4d4dcc"
//            val location = loation
////               location "Tehran"
//
//            val urlBuilder = (baseUrl + endpoint).toHttpUrlOrNull()?.newBuilder()
//            urlBuilder?.addQueryParameter("q", location)
//            urlBuilder?.addQueryParameter("appid", apiKey)
//
//            val url = urlBuilder?.build()?.toString()
//
//            val request = url?.let { it1 ->
//                Request.Builder()
//                    .url(it1)
//                    .build()
//            }

            if (request != null) {
                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        println("Failed to make request: ${e.message}")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        if (response.isSuccessful) {
                            val responseBody = response.body?.string()

                            val jsonResponse = JSONObject(responseBody!!)
                            val main = jsonResponse.getJSONObject("main")
                            val temperature = main.getDouble("temp").toInt()-273.15.toInt()
                            val weatherArray = jsonResponse.getJSONArray("weather")
                            val weatherObject = weatherArray.getJSONObject(0).getString("main")
                            val weatherdescription = weatherArray.getJSONObject(0).getString("description")



                            runOnUiThread {

                                weather_text.text = weatherObject
                                if (weather_text.length()>8){
                                    weather_text.setTextSize(TypedValue.COMPLEX_UNIT_PT, 10F)
                                }else{
                                    weather_text.setTextSize(TypedValue.COMPLEX_UNIT_PT, 14F)

                                }

                                weather_text.text=weatherObject
                                var righttemp = "$temperature°c"
                                temp_textview.text=righttemp
                                if (weatherObject=="Clouds"&& weatherdescription=="overcast clouds"){
                                    weather_image.setImageResource(R.drawable.icon_cloud)
                                }
                                if (weatherObject=="Clouds"&& weatherdescription=="broken clouds"||weatherdescription=="scattered clouds"||weatherdescription=="few clouds"){
                                    weather_image.setImageResource(R.drawable.icon_partly_cloudy)
                                }

                                if (weatherObject=="Rain"){
                                    weather_image.setImageResource(R.drawable.icon_rain)
                                }
                                if (weatherObject=="Clear"){
                                    weather_image.setImageResource(R.drawable.icon_sunny)
                                }
                                if (weatherObject== "Thunderstorm"|| weatherObject=="Squall"||weatherObject=="Tornado"){
                                    weather_image.setImageResource(R.drawable.icon_thunder)
                                }
                                if (weatherObject== "Snow"){
                                    weather_image.setImageResource(R.drawable.icon_snow)
                                }

//


                                println("temperature: $temperature")

                                println("weatherObject: $weatherObject")
                            }





                        } else {
                            println("Request failed with code: ${response.code}")
                        }
                    }
                })
            }



        }


        val databaseHelper = setting_network_db.getInstance(this)

        var timer = Timer()
        if (!databaseHelper.isEmptynetwork_tabale()){
            var current_db: network_manual? = databaseHelper.get_from_db_network_manual(1)


            var apiKey=current_db!!.api_key.toString()
            var location=current_db!!.city_name.toString()


            val task = object : TimerTask() {
                override fun run() {
                    weather(location,apiKey)
                }
            }
            // زمان شروع (به طور پیش‌فرض فعلی)
            val startTime = Date()

            // دوره تکرار (هر 3 ساعت)
            val period = 3 * 60 * 60 * 1000L

            // اجرای فانکشن هر 3 ساعت با تاخیر صفر
            timer.scheduleAtFixedRate(task, startTime, period)





        }






        fun isConnectedToWifi(context: Context): Boolean {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as? WifiManager
            return wifiManager?.isWifiEnabled == true && wifiManager.connectionInfo.ipAddress != 0
        }


        fun check_connection(context: Context) {
            val timer = Timer()

            val task = object : TimerTask() {
                override fun run() {
                    // کدی که هر 5 دقیقه اجرا می‌شود
                    InternetCheck { isConnected ->
                        if (isConnected) {
                            if (isConnectedToWifi(context)) {
                                internetconection.text = "Connected"
                            }

                        } else {
                            if ( isConnectedToWifi(context)) {
                                internetconection.text = "No internet"

                            } else {
                                internetconection.text = "No access"

                            }
                        }
                    }.execute()

                }
            }

            // تنظیم زمان شروع و دوره تکرار (هر 5 دقیقه)
            val delay = 0L
            val period = 5 * 60 * 1000L
            timer.scheduleAtFixedRate(task, delay, period)
        }

        check_connection(this)















    }

    override fun onDestroy() {
        super.onDestroy()
        println("destroyed_dash")
//         
//         
    }
    override fun getContext(): Context {
        return applicationContext
    }

    class InternetCheck(private val callback: (Boolean) -> Unit) : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg params: Void?): Boolean {
            return try {
                val sock = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)
                sock.connect(socketAddress, 3000)
                sock.close()
                true
            } catch (e: IOException) {
                false
            }
        }

        override fun onPostExecute(result: Boolean) {
            callback(result)
        }
    }



    override fun onPause() {
        super.onPause()
        println("pppppp")
//         
    }






    fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window,
            window.decorView.findViewById(android.R.id.content)).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())

            // When the screen is swiped up at the bottom
            // of the application, the navigationBar shall
            // appear for some time
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }









}