package com.example.griffin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.LiveData
import androidx.viewpager.widget.ViewPager
import com.example.griffin.adapters.FragmentPagerAdapter_selected_devices
import com.example.griffin.database.*
import com.example.griffin.fragment.griffin_home_frags.MusicAdapter
import com.example.griffin.mudels.*
import devicesAdapter
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.dnsoverhttps.DnsOverHttps
import org.json.JSONObject
import java.io.IOException
import java.net.InetAddress
import java.text.SimpleDateFormat
import java.util.*





class griffin_home : AppCompatActivity() {
    private lateinit var wakeLock: PowerManager.WakeLock
    lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: FragmentPagerAdapter_selected_devices
    private val SharedViewModel: SharedViewModel by viewModels()
    private lateinit var gestureDetector: GestureDetector
    private lateinit var lightsLiveData: LiveData<List<Light?>>
    private lateinit var valveLiveData: LiveData<List<valve?>>
    private lateinit var ThermosatLiveData: LiveData<List<Thermostst?>>
    private lateinit var fanLiveData: LiveData<List<fan?>>
    private lateinit var CurtainLiveData: LiveData<List<curtain?>>
    private lateinit var PlugLiveData: LiveData<List<Plug?>>
    var current_index=0

    lateinit var loadingIndicator: View


    private lateinit var room_image_home: androidx.constraintlayout.widget.ConstraintLayout

    lateinit var adapter: devicesAdapter


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


    @SuppressLint("MissingInflatedId", "SuspiciousIndentation", "Range")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_griffin_home)

        MediaPlayer.create(this , R.raw.zapsplat_multimedia_button_click_bright_003_92100)
        val receivedBundle = intent.extras
        val receivedMessage = if (receivedBundle != null) {
            receivedBundle.getString("mode")
        } else {
            "defaultMode"
        }
        if (receivedBundle != null){
            try {
                val s_name = receivedBundle.getString("name")
                val scenario_db = scenario_db.Scenario_db.getInstance(this)
                SharedViewModel.update_current_scenario(scenario_db.getScenarioByScenarioName(s_name))

            }catch (e:Exception){
                println("102 griffin_home")
                println(e)

            }

        }
        fun getScreenSizeInInches(resources: Resources): Double {
            val displayMetrics = resources.displayMetrics
            val screenWidthPixels = displayMetrics.widthPixels
            val screenHeightPixels = displayMetrics.heightPixels
            val screenDensity = displayMetrics.densityDpi.toDouble()

            val screenWidthInches = screenWidthPixels / screenDensity
            val screenHeightInches = screenHeightPixels / screenDensity

            return Math.sqrt(Math.pow(screenWidthInches, 2.0) + Math.pow(screenHeightInches, 2.0))
        }
        val screenSizeInInches = getScreenSizeInInches(resources)
        // استفاده از مقدار اندازه‌ی صفحه به واحد اینچ
//        if (screenSizeInInches < 9){
//            val fontSizeInPt = 12f // اندازه فونت را به 12 پوینت تغییر دهید
//            val t = findViewById<TextView>(R.id.date)
//            t.textSize = 20f
//        }
        println("Screen size in inches: $screenSizeInInches")
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakeLock")

        // روشن کردن صفحه در صورت خاموش شدن
        wakeLock.acquire()




//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN)
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

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY


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


        val show_musics = findViewById<Button>(R.id.show_musics)
        val previus_musics = findViewById<Button>(R.id.previus_musics)
        val play_puse_musics = findViewById<Button>(R.id.play_puse_musics)
        val next_musics = findViewById<Button>(R.id.next_musics)







        val device_light=findViewById<Button>(R.id.device_light)
        val device_temp=findViewById<Button>(R.id.device_temp)
        val device_curtain=findViewById<Button>(R.id.device_curtain)
        val device_plug=findViewById<Button>(R.id.device_plug)
        val device_valve=findViewById<Button>(R.id.device_valve)
        val device_fan=findViewById<Button>(R.id.device_fan)
        val lineSeekBar=findViewById<CustomSeekBar>(R.id.lineSeekBar)



        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)



        lineSeekBar.setCustomProgress((currentVolume * 100) / 12)

        val musicPlayer = Music_player.getInstance(this)

        show_musics.setOnClickListener {
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

                        }

                    }
                    cursor.close()
                } else {

                }



            val adapter = MusicAdapter.getInstance(this)
            adapter.updateAdapterValue(musicList)

            viewPager.currentItem = 25


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


//        hideSystemUI()
        val receivedBundel = intent.extras

        viewPager = findViewById(R.id.frag_home_2)
        pagerAdapter = FragmentPagerAdapter_selected_devices(supportFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.currentItem=0

        SharedViewModel.current_room.observe(this, androidx.lifecycle.Observer { curent_room ->


            println("ine")

            val on_status = findViewById<Button>(R.id.on_status)
            val off_status = findViewById<Button>(R.id.off_status)
            val third_status = findViewById<Button>(R.id.third_status)
            SharedViewModel.current_device.observe(this, androidx.lifecycle.Observer { current ->



                third_status.visibility =View.GONE
                on_status.visibility =View.VISIBLE
                off_status.visibility =View.VISIBLE


                current?.let {
                    when (it) {
                        is curtain -> {
                            third_status.visibility =View.VISIBLE
                            on_status.visibility =View.VISIBLE
                            on_status.setText("Open")
                            off_status.visibility =View.VISIBLE
                            off_status.setText("Close")



                            val curtain_db_handler = curtain_db.getInstance(this)
                            val my_curtain = curtain_db_handler.get_from_db_curtain(it.id)
                            if (my_curtain != null) {
                                println(my_curtain.status)
                            }
                            if (my_curtain != null) {
                                if (my_curtain.status == "00"){
                                    off_status.setBackgroundResource(R.drawable.select_back_on)
                                    on_status.setBackgroundResource(R.drawable.select_back)
                                    third_status.setBackgroundResource(R.drawable.select_back)
                                }else if (my_curtain.status == "50"){
                                    off_status.setBackgroundResource(R.drawable.select_back)
                                    on_status.setBackgroundResource(R.drawable.select_back)
                                    third_status.setBackgroundResource(R.drawable.select_back_on)
                                }else if (my_curtain.status == "99"){
                                    off_status.setBackgroundResource(R.drawable.select_back)
                                    third_status.setBackgroundResource(R.drawable.select_back)
                                    on_status.setBackgroundResource(R.drawable.select_back_on)
                                }
                            }

                            on_status.setOnClickListener {

                                if (third_status != null) {
                                    curtain_db_handler.updateStatusById(third_status.id,"99")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back)
                                on_status.setBackgroundResource(R.drawable.select_back_on)
                                third_status.setBackgroundResource(R.drawable.select_back)
                            }
                            off_status.setOnClickListener {

                                if (my_curtain != null) {
                                    curtain_db_handler.updateStatusById(my_curtain.id,"00")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back_on)
                                on_status.setBackgroundResource(R.drawable.select_back)
                                third_status.setBackgroundResource(R.drawable.select_back)
                            }
                            third_status.setOnClickListener {

                                if (my_curtain != null) {
                                    curtain_db_handler.updateStatusById(my_curtain.id,"50")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back)
                                on_status.setBackgroundResource(R.drawable.select_back)
                                third_status.setBackgroundResource(R.drawable.select_back_on)
                            }

                        }
                        is Plug -> {
                            println("plugggggggggggggggggg")
                            third_status.visibility =View.GONE
                            on_status.visibility =View.VISIBLE
                            off_status.visibility =View.VISIBLE

                            val plug_db_handler = plug_db.getInstance(this)
                            val my_plug = plug_db_handler.get_from_db_Plug(it.id)
                            if (my_plug != null) {
                                println(my_plug.status)
                            }
                            if (my_plug != null) {
                                if (my_plug.status == "0"){
                                    off_status.setBackgroundResource(R.drawable.select_back_on)
                                    on_status.setBackgroundResource(R.drawable.select_back)
                                }else if (my_plug.status == "1"){
                                    off_status.setBackgroundResource(R.drawable.select_back)
                                    on_status.setBackgroundResource(R.drawable.select_back_on)
                                }
                            }

                            on_status.setOnClickListener {

                                if (my_plug != null) {
                                    plug_db_handler.updateStatusbyId(my_plug.id,"1")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back)
                                on_status.setBackgroundResource(R.drawable.select_back_on)
                            }
                            off_status.setOnClickListener {

                                if (my_plug != null) {
                                    plug_db_handler.updateStatusbyId(my_plug.id,"0")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back_on)
                                on_status.setBackgroundResource(R.drawable.select_back)
                            }


                        }
                        is valve -> {
                            third_status.visibility =View.GONE
                            on_status.visibility =View.VISIBLE
                            off_status.visibility =View.VISIBLE


                            val valve_db_handler = valve_db.getInstance(this)
                            val my_valve = valve_db_handler.get_from_db_valve(it.id)
                            if (my_valve != null) {
                                println(my_valve.status)
                            }
                            if (my_valve != null) {
                                if (my_valve.status == "0"){
                                    off_status.setBackgroundResource(R.drawable.select_back_on)
                                    on_status.setBackgroundResource(R.drawable.select_back)
                                }else if (my_valve.status == "1"){
                                    off_status.setBackgroundResource(R.drawable.select_back)
                                    on_status.setBackgroundResource(R.drawable.select_back_on)
                                }
                            }

                            on_status.setOnClickListener {

                                if (my_valve != null) {
                                    valve_db_handler.updateStatusbyId(my_valve.id,"1")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back)
                                on_status.setBackgroundResource(R.drawable.select_back_on)
                            }
                            off_status.setOnClickListener {

                                if (my_valve != null) {
                                    valve_db_handler.updateStatusbyId(my_valve.id,"0")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back_on)
                                on_status.setBackgroundResource(R.drawable.select_back)
                            }


                        }
                        is fan -> {
                            third_status.visibility =View.GONE
                            on_status.visibility =View.VISIBLE
                            off_status.visibility =View.VISIBLE

                            val fan_db_handler = fan_db.getInstance(this)
                            val my_fan = fan_db_handler.get_from_db_fan(it.id)
                            if (my_fan != null) {
                                println(my_fan.status)
                            }
                            if (my_fan != null) {
                                if (my_fan.status == "0"){
                                    off_status.setBackgroundResource(R.drawable.select_back_on)
                                    on_status.setBackgroundResource(R.drawable.select_back)
                                }else if (my_fan.status == "1"){
                                    off_status.setBackgroundResource(R.drawable.select_back)
                                    on_status.setBackgroundResource(R.drawable.select_back_on)
                                }
                            }

                            on_status.setOnClickListener {

                                if (my_fan != null) {
                                    fan_db_handler.updateStatusbyId(my_fan.id,"1")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back)
                                on_status.setBackgroundResource(R.drawable.select_back_on)
                            }
                            off_status.setOnClickListener {

                                if (my_fan != null) {
                                    fan_db_handler.updateStatusbyId(my_fan.id,"0")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back_on)
                                on_status.setBackgroundResource(R.drawable.select_back)
                            }



                        }
                        is Thermostst -> {
                            third_status.visibility =View.GONE
                            on_status.visibility =View.VISIBLE
                            off_status.visibility =View.VISIBLE


                        }
                        is Light -> {
                            println("lighteeeee")
                            third_status.visibility =View.GONE
                            on_status.visibility =View.VISIBLE
                            off_status.visibility =View.VISIBLE
                            val lightDatabase =  light_db.getInstance(this)
                            val my_light = lightDatabase.get_from_db_light(it.id)
                            if (my_light != null) {
                                if (my_light.status == "off"){
                                    off_status.setBackgroundResource(R.drawable.select_back_on)
                                    on_status.setBackgroundResource(R.drawable.select_back)
                                }else if (my_light.status == "on"){
                                    off_status.setBackgroundResource(R.drawable.select_back)
                                    on_status.setBackgroundResource(R.drawable.select_back_on)
                                }
                            }

                            on_status.setOnClickListener {

                                if (my_light != null) {
                                    lightDatabase.updateStatusById(my_light.id,"on")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back)
                                on_status.setBackgroundResource(R.drawable.select_back_on)
                            }
                            off_status.setOnClickListener {

                                if (my_light != null) {
                                    lightDatabase.updateStatusById(my_light.id,"off")
                                }
                                off_status.setBackgroundResource(R.drawable.select_back_on)
                                on_status.setBackgroundResource(R.drawable.select_back)
                            }


                        }
                        is String -> {
                            if (it == "done"){
                                third_status.visibility =View.GONE
                                on_status.visibility =View.GONE
                                off_status.visibility =View.GONE
                            }
                        }
                        else -> {
                            // اگر نوع ناشناخته باشد
                            println("Unknown type: ${it::class.simpleName}")
                        }
                    }
                } ?: run {
                    // اگر current مقدار null باشد
                    println("current_light is null")
                }

            })


            val lightDatabase=light_db.getInstance(this)
            lightsLiveData = lightDatabase.getLightsWithNonEmptyMacByRoomName(curent_room!!.room_name)
            lightsLiveData.observe(this, androidx.lifecycle.Observer { lights ->
            val musicplayer_layout= findViewById<ConstraintLayout>(R.id.musicplayer_layout)
                if (lights.count() > 0){

                    device_light.isEnabled=true
                    device_light.visibility= View.VISIBLE
                    device_light.setOnClickListener {

                        if (receivedBundel!=null) {
                            musicplayer_layout.visibility=View.VISIBLE
                            val viewTop = findViewById<View>(R.id.choosed_layout)

                            // تغییر layout_height به 0dp
                            val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                            params.height = 0
                            viewTop.layoutParams = params
                            val receivedBundle = intent.extras
                            val receivedMessage = if (receivedBundle != null) {
                                receivedBundle.getString("mode")
                            } else {
                                "defaultMode"
                            }

                            if (receivedMessage == "admin") {
                                musicplayer_layout.visibility=View.VISIBLE
                                val viewTop = findViewById<View>(R.id.choosed_layout)

                                // تغییر layout_height به 0dp
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = 0
                                viewTop.layoutParams = params
                                viewPager.currentItem=2

                            } else if(receivedMessage!!.startsWith("scenario") ){
                                musicplayer_layout.visibility=View.GONE
                                val viewTop = findViewById<View>(R.id.choosed_layout)
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                                viewTop.layoutParams = params

                                val inflater = LayoutInflater.from(this)

                                val customPopupView: View = inflater.inflate(R.layout.popup_addlight, null)
                                val popupView: View = inflater.inflate(R.layout.popup_addlight, null)

                                val edit_plug_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
                                val ok_name_plug_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
                                val select_automate = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
                                val select_manual = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
                                val on_off_test_plug_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)
                                val send_to = customPopupView.findViewById<Button>(R.id.send_to)
                                send_to.visibility=View.INVISIBLE
                                on_off_test_plug_pupop.visibility=View.INVISIBLE
                                ok_name_plug_pupop.visibility=View.INVISIBLE
                                select_automate.setText("select automate")
                                select_manual.setText("Select manual")
                                val popupWidth = 390
                                val popupHeight = 350
                                // ایجاد لایه‌ی کاستوم
                                // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                                val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)
                                val alertDialogBuilder = AlertDialog.Builder(this)
                                alertDialogBuilder.setView(popupView)
                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.setCanceledOnTouchOutside(false)
                                edit_plug_name_pupop.visibility=View.INVISIBLE
//            edit_light_name_pupop.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD





                                select_automate.setOnClickListener {
                                    val light_db= light_db.getInstance(this)
                                    val my_light=light_db.getAllLightsByRoomName(curent_room!!.room_name)
                                    val scenario_database=scenario_db.Scenario_db.getInstance(this)

                                    val regex = Regex("\\d+")
                                    val matchResult = regex.find(receivedMessage)
                                    val scenario_number = matchResult?.value?.toInt()
                                    var current_scenario=scenario_database.getScenarioById(scenario_number)
                                    println(scenario_number)
                                    var selected_for_scenario= current_scenario!!.light
                                    for (light in my_light){
                                        if ((selected_for_scenario == "") && !(selected_for_scenario.contains(light!!.Lname!!) )) {
                                            selected_for_scenario= "${light!!.Lname}#${light.status}"

                                        }else{
                                            selected_for_scenario=selected_for_scenario + ",${light!!.Lname}#${light.status}"
                                        }

                                    }
                                    scenario_database.updateLightById(scenario_number,selected_for_scenario)
                                    println(selected_for_scenario)

                                    Toast.makeText(this, "added..", Toast.LENGTH_SHORT).show()
                                    popupWindow.dismiss()
                                }


                                select_manual.setOnClickListener {
                                    viewPager.currentItem=19
                                    val learn_light_db= light_db.getInstance(this)
                                    SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(curent_room!!.room_name))

                                    popupWindow.dismiss()




                                }



                                popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)

                            }else{
                                musicplayer_layout.visibility=View.VISIBLE
                                val viewTop = findViewById<View>(R.id.choosed_layout)

                                // تغییر layout_height به 0dp
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = 0
                                viewTop.layoutParams = params

                                val handler = Handler(Looper.getMainLooper())
                                viewPager.currentItem=1
                                Thread{
                                    try {
                                        handler.post {
                                            SharedViewModel.update_light_ref_status("loading")
                                        }
//                                        UdpListener8089.pause()
                                        fun isConnectedToWifi(context: Context): Boolean {
                                            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                                            return networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
                                        }
                                        val wifiManager = this.applicationContext.getSystemService(
                                            Context.WIFI_SERVICE) as WifiManager
                                        val wifiInfo = wifiManager.connectionInfo
                                        val ssid = wifiInfo.ssid
                                        val db_ssid = setting_network_db.getInstance(this).get_from_db_network_manual(1)?.modem_ssid


                                        if (isConnectedToWifi(this) && ssid.replace("\"", "")  ==db_ssid ){

                                            val ref =refresh_light(this,curent_room)

                                            val learn_light_db= light_db.getInstance(this)
//                                        UdpListener8089.resume()

                                            handler.post {
                                                SharedViewModel.update_light_ref_status("loading" )
                                                if(ref){
                                                    SharedViewModel.update_light_ref_status("ok" )

                                                }else{
                                                    SharedViewModel.update_light_ref_status("failed" )
                                                }
                                                // اجرای تغییرات در ترد اصلی (UI Thread)

                                                SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(curent_room!!.room_name))

                                            }
                                        }else{
                                            this.runOnUiThread{
                                                Toast.makeText(this, "Connect to a Griffin Network", Toast.LENGTH_SHORT).show()

                                            }
                                        }
                                    }catch (e:Exception){

                                        println(e)
                                    }

                                }.start()

                            }

                        }else{
                            musicplayer_layout.visibility=View.VISIBLE
                            val viewTop = findViewById<View>(R.id.choosed_layout)

                            // تغییر layout_height به 0dp
                            val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                            params.height = 0
                            viewTop.layoutParams = params
                            val handler = Handler(Looper.getMainLooper())
                            viewPager.currentItem=1
                            Thread{
                                try {
                                    handler.post {
                                        SharedViewModel.update_light_ref_status("loading")
                                    }
//                                    UdpListener8089.pause()
                                    fun isConnectedToWifi(context: Context): Boolean {
                                        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                                        return networkInfo?.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
                                    }
                                    val wifiManager = this.applicationContext.getSystemService(
                                        Context.WIFI_SERVICE) as WifiManager
                                    val wifiInfo = wifiManager.connectionInfo
                                    val ssid = wifiInfo.ssid
                                    val db_ssid = setting_network_db.getInstance(this).get_from_db_network_manual(1)?.modem_ssid

                                    if (isConnectedToWifi(this) && ssid.replace("\"", "") ==db_ssid ){


                                        val ref =refresh_light(this,curent_room)
//                                    UdpListener8089.resume()
                                        val learn_light_db= light_db.getInstance(this)


                                        println(ref)
                                        handler.post {

                                            SharedViewModel.update_light_ref_status("loading" )
                                            if(ref){
                                                SharedViewModel.update_light_ref_status("ok" )

                                            }else{
                                                SharedViewModel.update_light_ref_status("failed" )
                                            }
                                            // اجرای تغییرات در ترد اصلی (UI Thread)

                                            SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(curent_room!!.room_name))

                                        }
                                    }else{
                                        this.runOnUiThread{
                                            Toast.makeText(this, "Connect to a Griffin Network", Toast.LENGTH_SHORT).show()

                                        }
                                    }
                                }catch (e:Exception){
                                    println(e)
                                    handler.post {
                                        SharedViewModel.update_light_ref_status("failed" )
                                    }
                                }



                            }.start()


                        }
                    }



                }else{
                    device_light.isEnabled=false
                    device_light.visibility= View.GONE
                    viewPager.currentItem=0
                }







            })
            val CurtainDatabase=curtain_db.getInstance(this)
            CurtainLiveData = CurtainDatabase.getcurtainsWithNonEmptyMacByRoomName(curent_room!!.room_name)
            CurtainLiveData.observe(this, androidx.lifecycle.Observer { lights ->
            val musicplayer_layout= findViewById<ConstraintLayout>(R.id.musicplayer_layout)
                if (lights.count() > 0){

                    device_curtain.isEnabled=true
                    device_curtain.visibility= View.VISIBLE
                    device_curtain.setOnClickListener {

                        if (receivedBundel!=null) {
                            val receivedMessage = receivedBundel!!.getString("mode")

                            if (receivedMessage == "admin") {
                                viewPager.currentItem=8
                                musicplayer_layout.visibility=View.VISIBLE
                                val viewTop = findViewById<View>(R.id.choosed_layout)

                                // تغییر layout_height به 0dp
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = 0
                                viewTop.layoutParams = params


                            } else if(receivedMessage!!.startsWith("scenario") ){

                                val inflater = LayoutInflater.from(this)

                                val customPopupView: View = inflater.inflate(R.layout.popup_addlight, null)
                                val popupView: View = inflater.inflate(R.layout.popup_addlight, null)

                                val edit_plug_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
                                val ok_name_plug_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
                                val select_automate = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
                                val select_manual = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
                                val on_off_test_plug_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)
                                val send_to = customPopupView.findViewById<Button>(R.id.send_to)
                                send_to.visibility=View.INVISIBLE
                                on_off_test_plug_pupop.visibility=View.INVISIBLE
                                ok_name_plug_pupop.visibility=View.INVISIBLE
                                select_automate.setText("select automate")
                                select_manual.setText("Select manual")
                                val popupWidth = 390
                                val popupHeight = 350
                                // ایجاد لایه‌ی کاستوم
                                // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                                val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)
                                val alertDialogBuilder = AlertDialog.Builder(this)
                                alertDialogBuilder.setView(popupView)
                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.setCanceledOnTouchOutside(false)
                                edit_plug_name_pupop.visibility=View.INVISIBLE
//            edit_light_name_pupop.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD



                                select_automate.setOnClickListener {
                                    val curtain_db= curtain_db.getInstance(this)
                                    val my_curtain=curtain_db.getAllcurtainsByRoomName(curent_room!!.room_name)
                                    val scenario_database= scenario_db.Scenario_db.getInstance(this)

                                    val regex = Regex("\\d+")
                                    val matchResult = regex.find(receivedMessage)
                                    val scenario_number = matchResult?.value?.toInt()
                                    var current_scenario=scenario_database.getScenarioById(scenario_number)
                                    println(scenario_number)
                                    var selected_for_scenario= current_scenario!!.curtain
                                    for (curtain in my_curtain){

                                        if ((selected_for_scenario == "") && !(selected_for_scenario!!.contains(curtain!!.Cname!!) )) {
                                            selected_for_scenario= "${curtain!!.Cname}#${curtain.status}"

                                        }else{
                                            selected_for_scenario=selected_for_scenario + ",${curtain!!.Cname}#${curtain.status}"
                                        }

                                    }
                                    scenario_database.updateCurtainById(scenario_number,selected_for_scenario)
                                    println(selected_for_scenario)
                                    Toast.makeText(this, "added..", Toast.LENGTH_SHORT).show()
                                    popupWindow.dismiss()
                                }


                                select_manual.setOnClickListener {

                                    val learn_curtain_db= curtain_db.getInstance(this)
                                    SharedViewModel.update_curtain_to_learn_list( learn_curtain_db.getAllcurtainsByRoomName(curent_room!!.room_name))
                                    viewPager.currentItem=22
                                    popupWindow.dismiss()
                                }



                                popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)

                            }else{

                                viewPager.currentItem=9
                                musicplayer_layout.visibility=View.GONE
                                val viewTop = findViewById<View>(R.id.choosed_layout)
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                                viewTop.layoutParams = params


                            }

                        }else{
                            viewPager.currentItem=9
                            musicplayer_layout.visibility=View.GONE
                            val viewTop = findViewById<View>(R.id.choosed_layout)
                            val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                            params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                            viewTop.layoutParams = params

                        }
                    }



                }else{
                    device_curtain.isEnabled=false
                    device_curtain.visibility= View.GONE
                    viewPager.currentItem=0
                }







            })
            val plugDatabase=plug_db.getInstance(this)
            PlugLiveData = plugDatabase.getplugsWithNonEmptyMacByRoomName(curent_room!!.room_name)
            PlugLiveData.observe(this, androidx.lifecycle.Observer { lights ->
            val musicplayer_layout= findViewById<ConstraintLayout>(R.id.musicplayer_layout)
                if (lights.count() > 0){

                    device_plug.isEnabled=true
                    device_plug.visibility= View.VISIBLE
                    device_plug.setOnClickListener {

                        if (receivedBundel!=null) {
                            val receivedMessage = receivedBundel!!.getString("mode")

                            if (receivedMessage == "admin") {
                                viewPager.currentItem=11
                                musicplayer_layout.visibility=View.VISIBLE
                                val viewTop = findViewById<View>(R.id.choosed_layout)

                                // تغییر layout_height به 0dp
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = 0
                                viewTop.layoutParams = params


                            } else if(receivedMessage!!.startsWith("scenario") ){

                                val inflater = LayoutInflater.from(this)

                                val customPopupView: View = inflater.inflate(R.layout.popup_addlight, null)
                                val popupView: View = inflater.inflate(R.layout.popup_addlight, null)

                                val edit_plug_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
                                val ok_name_plug_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
                                val select_automate = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
                                val select_manual = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
                                val on_off_test_plug_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)
                                val send_to = customPopupView.findViewById<Button>(R.id.send_to)
                                send_to.visibility=View.INVISIBLE
                                on_off_test_plug_pupop.visibility=View.INVISIBLE
                                ok_name_plug_pupop.visibility=View.INVISIBLE
                                select_automate.setText("select automate")
                                select_manual.setText("Select manual")
                                val popupWidth = 390
                                val popupHeight = 350
                                // ایجاد لایه‌ی کاستوم
                                // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                                val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)
                                val alertDialogBuilder = AlertDialog.Builder(this)
                                alertDialogBuilder.setView(popupView)
                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.setCanceledOnTouchOutside(false)
                                edit_plug_name_pupop.visibility=View.INVISIBLE
//            edit_light_name_pupop.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD



                                select_automate.setOnClickListener {
                                    val plug_db= plug_db.getInstance(this)
                                    val my_plug=plug_db.getPlugsByRoomName(curent_room!!.room_name)
                                    val scenario_database=scenario_db.Scenario_db.getInstance(this)

                                    val regex = Regex("\\d+")
                                    val matchResult = regex.find(receivedMessage)
                                    val scenario_number = matchResult?.value?.toInt()
                                    var current_scenario=scenario_database.getScenarioById(scenario_number)
                                    println(scenario_number)
                                    var selected_for_scenario= current_scenario!!.plug
                                    for (plug in my_plug){

                                        if ((selected_for_scenario == "") && !(selected_for_scenario!!.contains(plug!!.Pname!!) )) {
                                            selected_for_scenario= "${plug!!.Pname}#${plug.status}"

                                        }else{
                                            selected_for_scenario=selected_for_scenario + ",${plug!!.Pname}#${plug.status}"
                                        }

                                    }
                                    scenario_database.updatePlugById(scenario_number,selected_for_scenario)
                                    println(selected_for_scenario)
                                    Toast.makeText(this, "added..", Toast.LENGTH_SHORT).show()
                                    popupWindow.dismiss()
                                }


                                select_manual.setOnClickListener {
                                    viewPager.currentItem=20
                                    val learn_plug_db= plug_db.getInstance(this)
                                    SharedViewModel.update_plug_to_learn_list( learn_plug_db.getPlugsByRoomName(curent_room!!.room_name))

                                    popupWindow.dismiss()
                                }



                                popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)

                            }else{

                                viewPager.currentItem=12
                                musicplayer_layout.visibility=View.GONE
                                val viewTop = findViewById<View>(R.id.choosed_layout)
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                                viewTop.layoutParams = params


                            }

                        }else{
                            viewPager.currentItem=12
                            musicplayer_layout.visibility=View.GONE
                            val viewTop = findViewById<View>(R.id.choosed_layout)
                            val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                            params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                            viewTop.layoutParams = params

                        }
                    }



                }else{
                    device_plug.isEnabled=false
                    device_plug.visibility= View.GONE
                    viewPager.currentItem=0
                }







            })

            val fanDatabase= fan_db.getInstance(this)
            fanLiveData = fanDatabase.getfansWithNonEmptyMacByRoomName(curent_room!!.room_name)
            fanLiveData.observe(this, androidx.lifecycle.Observer { lights ->
                val musicplayer_layout= findViewById<ConstraintLayout>(R.id.musicplayer_layout)
                if (lights.count() > 0){

                    device_fan.isEnabled=true
                    device_fan.visibility= View.VISIBLE
                    device_fan.setOnClickListener {

                        if (receivedBundel!=null) {
                            val receivedMessage = receivedBundel!!.getString("mode")

                            if (receivedMessage == "admin") {
                                viewPager.currentItem=17
                                musicplayer_layout.visibility= View.VISIBLE
                                val viewTop = findViewById<View>(R.id.choosed_layout)

                                // تغییر layout_height به 0dp
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = 0
                                viewTop.layoutParams = params


                            } else if(receivedMessage!!.startsWith("scenario") ){

                                val inflater = LayoutInflater.from(this)

                                val customPopupView: View = inflater.inflate(R.layout.popup_addlight, null)
                                val popupView: View = inflater.inflate(R.layout.popup_addlight, null)

                                val edit_plug_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
                                val ok_name_plug_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
                                val select_automate = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
                                val select_manual = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
                                val on_off_test_plug_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)
                                val send_to = customPopupView.findViewById<Button>(R.id.send_to)
                                send_to.visibility=View.INVISIBLE
                                on_off_test_plug_pupop.visibility=View.INVISIBLE
                                ok_name_plug_pupop.visibility=View.INVISIBLE
                                select_automate.setText("select automate")
                                select_manual.setText("Select manual")
                                val popupWidth = 390
                                val popupHeight = 350
                                // ایجاد لایه‌ی کاستوم
                                // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                                val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)
                                val alertDialogBuilder = AlertDialog.Builder(this)
                                alertDialogBuilder.setView(popupView)
                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.setCanceledOnTouchOutside(false)
                                edit_plug_name_pupop.visibility=View.INVISIBLE
//            edit_light_name_pupop.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD



                                select_automate.setOnClickListener {
                                    val fan_db= fan_db.getInstance(this)
                                    val my_fan=fan_db.getfansByRoomName(curent_room!!.room_name)
                                    val scenario_database= scenario_db.Scenario_db.getInstance(this)

                                    val regex = Regex("\\d+")
                                    val matchResult = regex.find(receivedMessage)
                                    val scenario_number = matchResult?.value?.toInt()
                                    var current_scenario=scenario_database.getScenarioById(scenario_number)
                                    println(scenario_number)
                                    var selected_for_scenario= current_scenario!!.fan
                                    for (fan in my_fan){

                                        if ((selected_for_scenario == "") && !(selected_for_scenario!!.contains(fan!!.Fname!!) )) {
                                            selected_for_scenario= "${fan!!.Fname}#${fan.status}"

                                        }else{
                                            selected_for_scenario=selected_for_scenario + ",${fan!!.Fname}#${fan.status}"
                                        }

                                    }
                                    scenario_database.updateFanById(scenario_number,selected_for_scenario)
                                    println(selected_for_scenario)
                                    Toast.makeText(this, "added..", Toast.LENGTH_SHORT).show()
                                    popupWindow.dismiss()
                                }


                                select_manual.setOnClickListener {
                                    viewPager.currentItem=24
                                    val learn_fan_db= fan_db.getInstance(this)
                                    SharedViewModel.update_fan_to_learn_list( learn_fan_db.getfansByRoomName(curent_room!!.room_name))

                                    popupWindow.dismiss()
                                }



                                popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)

                            }else{

                                viewPager.currentItem=18
                                musicplayer_layout.visibility= View.GONE
                                val viewTop = findViewById<View>(R.id.choosed_layout)
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                                viewTop.layoutParams = params


                            }

                        }else{
                            viewPager.currentItem=18
                            musicplayer_layout.visibility= View.GONE
                            val viewTop = findViewById<View>(R.id.choosed_layout)
                            val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                            params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                            viewTop.layoutParams = params

                        }
                    }



                }else{
                    device_fan.isEnabled=false
                    device_fan.visibility= View.GONE
                    viewPager.currentItem=0
                }







            })

            val valveDatabase= valve_db.getInstance(this)
            valveLiveData = valveDatabase.getvalvesWithNonEmptyMacByRoomName(curent_room!!.room_name)
            valveLiveData.observe(this, androidx.lifecycle.Observer { lights ->
                val musicplayer_layout= findViewById<ConstraintLayout>(R.id.musicplayer_layout)
                if (lights.count() > 0){

                    device_valve.isEnabled=true
                    device_valve.visibility= View.VISIBLE
                    device_valve.setOnClickListener {

                        if (receivedBundel!=null) {
                            val receivedMessage = receivedBundel!!.getString("mode")

                            if (receivedMessage == "admin") {
                                viewPager.currentItem=14
                                musicplayer_layout.visibility= View.VISIBLE
                                val viewTop = findViewById<View>(R.id.choosed_layout)

                                // تغییر layout_height به 0dp
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = 0
                                viewTop.layoutParams = params


                            } else if(receivedMessage!!.startsWith("scenario") ){

                                val inflater = LayoutInflater.from(this)

                                val customPopupView: View = inflater.inflate(R.layout.popup_addlight, null)
                                val popupView: View = inflater.inflate(R.layout.popup_addlight, null)

                                val edit_plug_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
                                val ok_name_plug_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
                                val select_automate = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
                                val select_manual = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
                                val on_off_test_plug_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)
                                val send_to = customPopupView.findViewById<Button>(R.id.send_to)
                                send_to.visibility=View.INVISIBLE
                                on_off_test_plug_pupop.visibility=View.INVISIBLE
                                ok_name_plug_pupop.visibility=View.INVISIBLE
                                select_automate.setText("select automate")
                                select_manual.setText("Select manual")
                                val popupWidth = 390
                                val popupHeight = 350
                                // ایجاد لایه‌ی کاستوم
                                // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                                val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)
                                val alertDialogBuilder = AlertDialog.Builder(this)
                                alertDialogBuilder.setView(popupView)
                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.setCanceledOnTouchOutside(false)
                                edit_plug_name_pupop.visibility=View.INVISIBLE
//            edit_light_name_pupop.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD



                                select_automate.setOnClickListener {
                                    val valve_db= valve_db.getInstance(this)
                                    val my_valve=valve_db.getvalvesByRoomName(curent_room!!.room_name)
                                    val scenario_database=scenario_db.Scenario_db.getInstance(this)

                                    val regex = Regex("\\d+")
                                    val matchResult = regex.find(receivedMessage)
                                    val scenario_number = matchResult?.value?.toInt()
                                    var current_scenario=scenario_database.getScenarioById(scenario_number)
                                    println(scenario_number)
                                    var selected_for_scenario= current_scenario!!.valve
                                    for (valve in my_valve){

                                        if ((selected_for_scenario == "") && !(selected_for_scenario!!.contains(valve!!.Vname!!) )) {
                                            selected_for_scenario= "${valve!!.Vname}#${valve.status}"

                                        }else{
                                            selected_for_scenario=selected_for_scenario + ",${valve!!.Vname}#${valve.status}"
                                        }

                                    }
                                    scenario_database.updateValveById(scenario_number,selected_for_scenario)
                                    println(selected_for_scenario)
                                    Toast.makeText(this, "added..", Toast.LENGTH_SHORT).show()
                                    popupWindow.dismiss()
                                }


                                select_manual.setOnClickListener {

                                    val learn_valve_db= valve_db.getInstance(this)
                                    SharedViewModel.update_valve_to_learn_list( learn_valve_db.getvalvesByRoomName(curent_room!!.room_name))
                                    viewPager.currentItem=21
                                    popupWindow.dismiss()
                                }



                                popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)

                            }else{

                                viewPager.currentItem=15
                                musicplayer_layout.visibility= View.GONE
                                val viewTop = findViewById<View>(R.id.choosed_layout)
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                                viewTop.layoutParams = params


                            }

                        }else{
                            viewPager.currentItem=15
                            musicplayer_layout.visibility= View.GONE
                            val viewTop = findViewById<View>(R.id.choosed_layout)
                            val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                            params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                            viewTop.layoutParams = params

                        }
                    }
                }else{
                    device_valve.isEnabled=false
                    device_valve.visibility= View.GONE
                    viewPager.currentItem=0
                }
            })

            //////////////// sms ////////////////////

//            class SMSReceiver : BroadcastReceiver() {
//                override fun onReceive(context: Context, intent: Intent) {
//                    Log.d("BroadcastReceiver", "onReceive")
//                    if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
//                        Log.d("BroadcastReceiver", "SMS received")
//                        // Will do stuff with message here
//                    }
//                }


            val thermostat_db= Temperature_db.getInstance(this)
            ThermosatLiveData= thermostat_db.getThermostatsWithNonEmptyMacByRoomName(curent_room!!.room_name)
            ThermosatLiveData.observe(this, androidx.lifecycle.Observer { Thermostat ->
                val musicplayer_layout= findViewById<ConstraintLayout>(R.id.musicplayer_layout)
                    if (Thermostat.count() > 0){

                        device_temp.isEnabled=true
                        device_temp.visibility= View.VISIBLE
                        device_temp.setOnClickListener {

                            if (receivedBundel!=null) {

                                val receivedMessage = receivedBundel!!.getString("mode")

                                if (receivedMessage == "admin") {
                                    viewPager.currentItem=5
                                    musicplayer_layout.visibility=View.VISIBLE
                                    val viewTop = findViewById<View>(R.id.choosed_layout)

                                    // تغییر layout_height به 0dp
                                    val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                    params.height = 0
                                    viewTop.layoutParams = params

                                } else if(receivedMessage!!.startsWith("scenario") ){

                                    val inflater = LayoutInflater.from(this)

                                    val customPopupView: View = inflater.inflate(R.layout.popup_addlight, null)
                                    val popupView: View = inflater.inflate(R.layout.popup_addlight, null)

                                    val edit_plug_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
                                    val ok_name_plug_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
                                    val select_automate = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
                                    val select_manual = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
                                    val on_off_test_plug_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)
                                    val send_to = customPopupView.findViewById<Button>(R.id.send_to)
                                    send_to.visibility=View.INVISIBLE
                                    on_off_test_plug_pupop.visibility=View.INVISIBLE
                                    ok_name_plug_pupop.visibility=View.INVISIBLE
                                    select_automate.setText("select automate")
                                    select_manual.setText("Select manual")
                                    val popupWidth = 390
                                    val popupHeight = 350
                                    // ایجاد لایه‌ی کاستوم
                                    // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                                    val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)
                                    val alertDialogBuilder = AlertDialog.Builder(this)
                                    alertDialogBuilder.setView(popupView)
                                    val alertDialog = alertDialogBuilder.create()
                                    alertDialog.setCanceledOnTouchOutside(false)
                                    edit_plug_name_pupop.visibility=View.INVISIBLE
//            edit_light_name_pupop.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD



                                    select_automate.setOnClickListener {
                                        val Thermostst_db= Temperature_db.getInstance(this)
                                        val my_Thermostst=Thermostst_db.getThermostatsByRoomName(curent_room!!.room_name)
                                        val scenario_database= scenario_db.Scenario_db.getInstance(this)

                                        val regex = Regex("\\d+")
                                        val matchResult = regex.find(receivedMessage)
                                        val scenario_number = matchResult?.value?.toInt()
                                        var current_scenario=scenario_database.getScenarioById(scenario_number)
                                        println(scenario_number)
                                        var selected_for_scenario= current_scenario!!.thermostat
                                        for (Thermostst in my_Thermostst){

                                            if ((selected_for_scenario == "") && !(selected_for_scenario!!.contains(Thermostst!!.name!!) )) {
                                                selected_for_scenario= "${Thermostst!!.name}#${Thermostst.on_off}!${Thermostst.temperature}$${Thermostst.mood}@${Thermostst.fan_status}"

                                            }else{
                                                selected_for_scenario=selected_for_scenario + ",${Thermostst!!.name}#${Thermostst.on_off}!${Thermostst.temperature}$${Thermostst.mood}@${Thermostst.fan_status}"
                                            }

                                        }
                                        scenario_database.updateThermostatById(scenario_number,selected_for_scenario)
                                        println(selected_for_scenario)
                                        Toast.makeText(this, "added..", Toast.LENGTH_SHORT).show()

                                        popupWindow.dismiss()
                                    }


                                    select_manual.setOnClickListener {

                                        musicplayer_layout.visibility=View.GONE
                                        val viewTop = findViewById<View>(R.id.choosed_layout)
                                        val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                        params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                                        viewTop.layoutParams = params

                                        val learn_Thermostst_db= Temperature_db.getInstance(this)
                                        SharedViewModel.update_temp_to_learn_list( learn_Thermostst_db.getThermostatsByRoomName(curent_room!!.room_name))
                                        viewPager.currentItem=23
                                        popupWindow.dismiss()
                                    }



                                    popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)

                                }else{

                                    viewPager.currentItem=6
                                    musicplayer_layout.visibility=View.GONE
                                    val viewTop = findViewById<View>(R.id.choosed_layout)
                                    val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                    params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                                    viewTop.layoutParams = params


                                }

                            }else{
                                musicplayer_layout.visibility=View.GONE
                                val viewTop = findViewById<View>(R.id.choosed_layout)
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = ConstraintLayout.LayoutParams.MATCH_PARENT
                                viewTop.layoutParams = params
                                viewPager.currentItem=6
                            }
                        }



                    }else{
                        device_temp.isEnabled=false
                        device_temp.visibility= View.GONE
                        musicplayer_layout.visibility=View.VISIBLE
                        viewPager.currentItem=0
                    }







            })
        })



        val btn_intercom_h= findViewById<Button>(R.id.intercom_home)
        val btn_setting_h= findViewById<Button>(R.id.btn_setting_h)
        val camera_home= findViewById<Button>(R.id.camera_home)


        val back_to_dashboard_home=findViewById<Button>(R.id.back_to_dashboard_home)
        var dashboard_btn_list= listOf(back_to_dashboard_home,btn_setting_h,btn_intercom_h,camera_home,next_musics,play_puse_musics,previus_musics,show_musics)
        for (button in dashboard_btn_list) {
            button.setButtonScaleOnTouchListener()



        }

        back_to_dashboard_home.setOnClickListener {
            val intent = Intent(this, dashboard::class.java)


            if (receivedMessage=="admin") {
                intent.putExtra("mode", "admin")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)
            finish()

        }
        val choose_device_item_menu= findViewById<ImageButton>(R.id.choose_device_item_menu)
        val musicplayer_layout= findViewById<ConstraintLayout>(R.id.musicplayer_layout)

        if (receivedBundel!=null){

            val receivedMessage=receivedBundel!!.getString("mode")

            if ( receivedMessage == "admin"){
                choose_device_item_menu.isEnabled=true
                choose_device_item_menu.visibility= View.VISIBLE
//                addlightitem_menu.isEnabled=true
//                addlightitem_menu.visibility= View.VISIBLE
//                hideSystemUI()






                choose_device_item_menu.setOnClickListener {
//                    hideSystemUI()
                    val popupMenu = PopupMenu(this, choose_device_item_menu)
                    popupMenu.gravity = Gravity.TOP or Gravity.END
                    popupMenu.menuInflater.inflate(R.menu.add_device_menu, popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when(menuItem.itemId){
                            R.id.add_light -> {

                                viewPager.currentItem=2

                                musicplayer_layout.visibility=View.VISIBLE
                                val viewTop = findViewById<View>(R.id.choosed_layout)

                                // تغییر layout_height به 0dp
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = 0
                                viewTop.layoutParams = params
                            }
                            R.id.add_temp -> {
                                musicplayer_layout.visibility=View.VISIBLE


                                // تغییر layout_height به 0dp

                                val viewTop = findViewById<View>(R.id.choosed_layout)

                                // تغییر layout_height به 0dp
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = 0
                                viewTop.layoutParams = params

                                viewPager.currentItem=5

                            }
                            R.id.add_curtain -> {
                                viewPager.currentItem=8
                                musicplayer_layout.visibility=View.VISIBLE
                                val viewTop = findViewById<View>(R.id.choosed_layout)

                                // تغییر layout_height به 0dp
                                val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                                params.height = 0
                                viewTop.layoutParams = params
                            }R.id.add_plug -> {
                                viewPager.currentItem=11
                                musicplayer_layout.visibility=View.VISIBLE
                            val viewTop = findViewById<View>(R.id.choosed_layout)

                            // تغییر layout_height به 0dp
                            val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                            params.height = 0
                            viewTop.layoutParams = params
                            }R.id.add_valve -> {
                                viewPager.currentItem=14
                                musicplayer_layout.visibility=View.VISIBLE
                            val viewTop = findViewById<View>(R.id.choosed_layout)

                            // تغییر layout_height به 0dp
                            val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                            params.height = 0
                            viewTop.layoutParams = params
                            }R.id.add_fan -> {
                                viewPager.currentItem=17
                                musicplayer_layout.visibility=View.VISIBLE
                            val viewTop = findViewById<View>(R.id.choosed_layout)

                            // تغییر layout_height به 0dp
                            val params = viewTop.layoutParams as ConstraintLayout.LayoutParams
                            params.height = 0
                            viewTop.layoutParams = params
                            }
                        }
                        true
                    }
                    popupMenu.show()

                }







            }

        }












    }




    @SuppressLint("WrongViewCast")
    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakeLock")

        // روشن کردن صفحه در صورت خاموش شدن
        wakeLock.acquire()
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

        val receivedBundle = intent.extras
        val receivedMessage = if (receivedBundle != null) {
            receivedBundle.getString("mode")
        } else {
            "defaultMode"
        }

        println(receivedMessage)
        var current_room:rooms? = null


        fun sortRoomsByType(roomsList: List<rooms>): List<rooms> {
            // ترتیب تایپ‌ها بر اساس اولویت شما
            val typeOrder = listOf("livi", "tvro", "dini", "kitc", "mast", "kids", "gues", "gust", "room", "bath", "yard")

            // ایجاد Comparator برای مرتب‌سازی لیست بر اساس اولویت تایپ‌ها که با حروف خاصی شروع می‌شود
            val comparator = Comparator<rooms> { room1, room2 ->
                val index1 = typeOrder.indexOfFirst { room1.room_type!!.startsWith(it, ignoreCase = true) }
                val index2 = typeOrder.indexOfFirst { room2.room_type!!.startsWith(it, ignoreCase = true) }
                // در صورتی که هیچکدام از تایپ‌ها مطابق با اولویت نباشند، به ترتیب اصلی باقی بمانند
                when {
                    index1 == -1 && index2 == -1 -> 0 // هر دو خارج از لیست
                    index1 == -1 -> 1 // فقط اولی خارج از لیست
                    index2 == -1 -> -1 // فقط دومی خارج از لیست
                    else -> index1.compareTo(index2) // مقایسه بر اساس ترتیب یافت شده
                }
            }

            // مرتب‌سازی لیست با استفاده از Comparator سفارشی
            return roomsList.sortedWith(comparator)
        }
        room_image_home=findViewById(R.id.room_image_home)
        var next_room_home=findViewById<Button>(R.id.next_room_home)
        var previous_room_home=findViewById<Button>(R.id.previous_room_home)
        var room_name_home=findViewById<TextView>(R.id.room_name_home)

        val databasehelper=rooms_db.getInstance(this)
        var rooms=sortRoomsByType(databasehelper.getAllRooms() as List<rooms>)
        var rooms_count= rooms.count()

        current_room=rooms[current_index]
        SharedViewModel.update_current_room(current_room)


        if(rooms_count!=0){
            try {
                val imageName=rooms[current_index]!!.room_image+"3"
                imageName?.let {
                    val imageResource = resources.getIdentifier(it, "drawable", this.packageName)
                    room_image_home.setBackgroundResource(imageResource)
                    current_room=rooms[current_index]
                    SharedViewModel.update_current_room(current_room)
                    room_name_home.text=current_room!!.room_name

//                Toast.makeText(this, current_room!!.room_name, Toast.LENGTH_SHORT).show()
                }
            }catch (e:Exception){
                rooms[current_index]!!.room_image="s1_room_"
                val imageName=rooms[current_index]!!.room_image+"3"
                imageName?.let {
                    val imageResource = resources.getIdentifier(it, "drawable", this.packageName)
                    room_image_home.setBackgroundResource(imageResource)
                    current_room=rooms[current_index]
                    SharedViewModel.update_current_room(current_room)
                    room_name_home.text=current_room!!.room_name

//                Toast.makeText(this, current_room!!.room_name, Toast.LENGTH_SHORT).show()
                }
            }




            fun next_room_func(){


                if (current_index < rooms_count-1){

                    val imageName=rooms[current_index+1]!!.room_image+"3"
                    imageName?.let {
                        val imageResource = resources.getIdentifier(it, "drawable", this.packageName)
                        room_image_home.setBackgroundResource(imageResource)
                        if (current_index < rooms_count){
                            current_index += 1
                        }else{
                            current_index = 0
                        }

                        current_room=rooms[current_index]
                        SharedViewModel.update_current_room(current_room)
                        room_name_home.text=current_room!!.room_name

                    }

                }else{


                    val imageName=rooms[0]!!.room_image+"3"
                    imageName?.let {
                        val imageResource = resources.getIdentifier(it, "drawable", this.packageName)
                        room_image_home.setBackgroundResource(imageResource)

                        current_index = 0

                        current_room=rooms[current_index]
                        SharedViewModel.update_current_room(current_room)
                        room_name_home.text=current_room!!.room_name
                    }
                }





            }
            fun previous_room_func(){

                if (current_index > 0){

                    val imageName=rooms[current_index-1]!!.room_image+"3"
                    imageName?.let {
                        val imageResource = resources.getIdentifier(it, "drawable", this.packageName)
                        room_image_home.setBackgroundResource(imageResource)
                        if (0 < current_index){
                            current_index -= 1
                        }else{
                            current_index = rooms_count
                        }
                        current_room=rooms[current_index]
                        SharedViewModel.update_current_room(current_room)
                        room_name_home.text=current_room!!.room_name
                    }


                }else{


                    val imageName=rooms[rooms_count-1]!!.room_image+"3"
                    imageName?.let {
                        val imageResource = resources.getIdentifier(it, "drawable", this.packageName)
                        room_image_home.setBackgroundResource(imageResource)

                        current_index = rooms_count-1

                        current_room=rooms[current_index]
                        SharedViewModel.update_current_room(current_room)
                        room_name_home.text=current_room!!.room_name
                    }
                }



            }

//            next_room_home.setOnClickListener{
//
//                next_room_func()
//
//
//            }
//            previous_room_home.setOnClickListener{
//
//                previous_room_func()
//
//            }




            class GestureListener : GestureDetector.SimpleOnGestureListener() {
                override fun onDown(p0: MotionEvent): Boolean {
                    return true
                }

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    if (e1 != null) {
                        if (Math.abs(velocityX) > Math.abs(velocityY)) {
                            // تشخیص حرکت به چپ یا راست بر اساس مقدار velocityX
                            if (velocityX > 0) {

                                next_room_func()
                            } else {

                                previous_room_func()
                            }
                        }
                    }
                    return super.onFling(e1, e2, velocityX, velocityY)
                }
//                override fun onFling(
//                    e1: MotionEvent,
//                    e2: MotionEvent,
//                    velocityX: Float,
//                    velocityY: Float
//                ): Boolean {
//
//                    return true
//                }
            }

            gestureDetector = GestureDetector(this, GestureListener())



        }
















        val weather_image=findViewById<ImageView>(R.id.weather_image_h)
        val temp_textview=findViewById<TextView>(R.id.temp_textview_h)
        val weather_text=findViewById<TextView>(R.id.weather_text_h)
        val internetconection=findViewById<TextView>(R.id.internetconection_h)
        val btn_setting_h= findViewById<Button>(R.id.btn_setting_h)
        val btn_camera_h= findViewById<Button>(R.id.camera_home)
        val btn_intercom_h= findViewById<Button>(R.id.intercom_home)

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
                    dashboard.InternetCheck { isConnected ->
                        if (isConnected) {
                            if (isConnectedToWifi(context)) {
                                internetconection.text = "Connected"
                            }

                        } else {
                            if (isConnectedToWifi(context)) {
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




        btn_setting_h.setOnClickListener {
            val intent = Intent(this, setting::class.java)


            if (receivedMessage=="admin") {
                intent.putExtra("mode", "admin")
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)

        }
        btn_camera_h.setOnClickListener {
            val intent = Intent(this, RTSPVideoPlayerActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)

        }
        btn_intercom_h.setOnClickListener {
            val intent = Intent(this, intercom::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            startActivity(intent)

        }

    }






    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            // بررسی آیا لمس داخل ConstraintLayout انجام شده است
            val isTouchInsideConstraintLayout = isTouchInsideView(event.x, event.y, room_image_home)

            if (isTouchInsideConstraintLayout) {
                // اگر لمس داخل ConstraintLayout باشد، gestureDetector اجرا می‌شود
                try {
                    gestureDetector.onTouchEvent(event)
                }catch (e: Exception){
                        println(e)
                }

            }
        }
        return super.onTouchEvent(event)
    }

    private fun isTouchInsideView(x: Float, y: Float, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]

        // مختصات ConstraintLayout
        val left = viewX
        val right = left + view.width
        val top = viewY
        val bottom = top + view.height

        // بررسی آیا موقعیت لمس داخل ConstraintLayout است یا خیر
        return x >= left && x <= right && y >= top && y <= bottom
    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }
    override fun onPause() {
        super.onPause()
         
    }


}