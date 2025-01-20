package com.example.griffin

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PowerManager
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewpager.widget.ViewPager
import com.example.griffin.adapters.*
import com.example.griffin.database.*
import com.example.griffin.mudels.Config
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.SoundManager
import java.text.SimpleDateFormat
import java.util.*

class setting : AppCompatActivity() {
     

    private lateinit var viewPager_one: ViewPager
    private lateinit var pagerAdapter_one: FragmentPagerAdapter_setting_one
    private lateinit var viewPager_two: ViewPager
    private lateinit var pagerAdapter_two: FragmentPagerAdapter_setting_two
    private lateinit var viewPager_three: ViewPager
    private lateinit var pagerAdapter_three: FragmentPagerAdapter_setting_three
    lateinit var viewPager_four: ViewPager
    private lateinit var pagerAdapter_four: FragmentPagerAdapter_setting_four
    val SharedViewModel : SharedViewModel by viewModels()

    private lateinit var wakeLock: PowerManager.WakeLock
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        setContentView(R.layout.activity_setting)




        viewPager_one = findViewById(R.id.setting_one)
        pagerAdapter_one = FragmentPagerAdapter_setting_one(supportFragmentManager)
        viewPager_one.adapter = pagerAdapter_one


        viewPager_two = findViewById(R.id.setting_tow)
        pagerAdapter_two = FragmentPagerAdapter_setting_two(supportFragmentManager)
        viewPager_two.adapter = pagerAdapter_two

        viewPager_three = findViewById(R.id.setting_three)
        pagerAdapter_three = FragmentPagerAdapter_setting_three(supportFragmentManager)
        viewPager_three.adapter = pagerAdapter_three

        viewPager_four = findViewById(R.id.setting_four)
        pagerAdapter_four = FragmentPagerAdapter_setting_four(supportFragmentManager)
        viewPager_four.adapter = pagerAdapter_four



    }
    fun changeViewPagerPage_one(pageIndex: Int) {
        viewPager_one.currentItem = pageIndex
    }
    fun changeViewPagerPage_two(pageIndex: Int) {
        viewPager_two.currentItem = pageIndex
    }
    fun changeViewPagerPage_three(pageIndex: Int) {
        viewPager_three.currentItem = pageIndex
    }
    fun changeViewPagerPage_four(pageIndex: Int) {
        viewPager_four.currentItem = pageIndex
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakeLock")

        // روشن کردن صفحه در صورت خاموش شدن
        wakeLock.acquire()




        val setting_btn_back_to_dashboard = findViewById<Button>(R.id.setting_btn_back_to_dashboard)
        val setting_btn_griffin_home: Button =  findViewById(R.id.setting_btn_griffin_home)
        val setting_btn_exit_setting: Button =  findViewById(R.id.setting_btn_exit_setting)

//        viewPager = view.findViewById(R.id.setting_one)
//        pagerAdapter = MyPagerAdapter(supportFragmentManager)
//        viewPager.adapter = pagerAdapter
//
//
         
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
        var dashboard_btn_list= listOf(setting_btn_back_to_dashboard,setting_btn_griffin_home,setting_btn_exit_setting)
        for (button in dashboard_btn_list) {
            button.setButtonScaleOnTouchListener()



        }

        setting_btn_back_to_dashboard.setOnClickListener {
            if (viewPager_one.currentItem == 1){


                Config.mode="admin"
                val intent =Intent(this,dashboard::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("mode","admin")
                startActivity(intent)
            }else{

                Toast.makeText(this, "Please Enter Password First", Toast.LENGTH_SHORT).show()
            }

        }

        setting_btn_griffin_home.setOnClickListener {
            val isempty = rooms_db.getInstance(this).isEmptynetwork_tabale()

            if (!isempty){
                val intent = Intent(this, griffin_home::class.java)

                if (viewPager_one.currentItem == 1){


                    val intent2 = Intent(this, griffin_home::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent2.putExtra("mode", "admin")
                    startActivity(intent2)
                }else{

                    Toast.makeText(this, "Please Enter Password First", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this, "add room first", Toast.LENGTH_SHORT).show()

            }


        }

        setting_btn_exit_setting.setOnClickListener {
            Config.mode="user"
            val intent2 = Intent(this, dashboard::class.java)
            intent2.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            try {

                val lightDatabase = light_db.getInstance(this)
                lightDatabase.removeDuplicates()
                val curtainDatabase = curtain_db.getInstance(this)
                curtainDatabase.removeDuplicates()
                val fanDatabase = fan_db.getInstance(this)
                fanDatabase.removeDuplicates()
                val plugDatabase = plug_db.getInstance(this)
                plugDatabase.removeDuplicates()
                val roomsDatabase = rooms_db.getInstance(this)
                roomsDatabase.removeDuplicates()
                val sixWorkertDatabase = six_workert_db.getInstance(this)
                sixWorkertDatabase.removeDuplicates()
                val temperatureDatabase = Temperature_db.getInstance(this)
                temperatureDatabase.removeDuplicates()
                val valveDatabase = valve_db.getInstance(this)
                valveDatabase.removeDuplicates()
            }catch (e:Exception){
                println(e)
            }
            startActivity(intent2)
            val intent = Intent(this, dashboard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }







        val clock = findViewById<TextView>(R.id.clockid)



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

                }
            }
        }, 0, 60000)




        val internetconection=findViewById<TextView>(R.id.internetconection)

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



        val receivedBundel = intent.extras
        var receivedMessage ="null"
        if (receivedBundel !=null){
            receivedMessage = receivedBundel!!.getString("mode").toString()
        }
        println(receivedMessage)

        if (receivedMessage=="admin") {

            changeViewPagerPage_one(1)


        }
    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }
    override fun onPause() {
        super.onPause()
         
    }



}