package com.example.griffin

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.adapters.CameraAdadpter
import com.example.griffin.database.camera_db
import com.example.griffin.mudels.camera
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

class RTSPVideoPlayerActivity : AppCompatActivity() {

    private var libVlc: LibVLC? = null
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var videoLayout: VLCVideoLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rtspvideo_player)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        enableFullScreenMode()

        videoLayout = findViewById(R.id.videoLayout)

        val cameraDatabase = camera_db.getInstance(this)
        val cameras = cameraDatabase.getAllcameras()

        if (cameras.isNotEmpty()) {
            cameras[0]?.let { playCameraStream(it) }  // پخش اولین دوربین
        }


        setupRecyclerView(cameras.filterNotNull())

        val back = findViewById<Button>(R.id.camera_back)
        back.setOnClickListener { finish() }
    }

    private fun enableFullScreenMode() {
        val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val decorView: View = window.decorView
            decorView.systemUiVisibility = flags
            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    decorView.systemUiVisibility = flags
                }
            }
        }
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun setupRecyclerView(cameras: List<camera>) {
        val recyclerView: RecyclerView = findViewById(R.id.camera_learn_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CameraAdadpter(cameras) { selectedCamera ->
            playCameraStream(selectedCamera)
        }
        recyclerView.adapter = adapter
        adapter.setItems(cameras)
    }

    private fun playCameraStream(selectedCamera: camera) {
        // انتشار منابع قبلی
        mediaPlayer?.release()
        libVlc?.release()

        // آماده‌سازی و پخش جریان
        val rtspUri = "rtsp://${selectedCamera.user}:${selectedCamera.pass}@${selectedCamera.ip}:${selectedCamera.port}/cam/realmonitor?channel=${selectedCamera.chanel}&subtype=${selectedCamera.subtype}"
        libVlc = LibVLC(this)
        val media = Media(libVlc, Uri.parse(rtspUri)).apply {
            addOption(":network-caching=50")  // افزایش کش شبکه به ۱۰۰۰ میلی‌ثانیه
            addOption(":rtsp-tcp")  // استفاده از پروتکل TCP
            addOption(":codec=mediacodec,iomx")  // استفاده از شتاب سخت‌افزاری
            addOption(":avcodec-hw=any")  // فعال‌سازی شتاب سخت‌افزاری برای همه کدک‌ها
            addOption(":rtsp-caching=3000")  // افزایش کش RTSP
            addOption(":live-caching=3000")  // افزایش کش جریان زنده
        }

        mediaPlayer = MediaPlayer(libVlc).apply {
            attachViews(videoLayout, null, false, false)
            this.media = media
            play()
        }

        // نمایش با انیمیشن
        videoLayout.alpha = 0f
        videoLayout.visibility = View.VISIBLE
        videoLayout.animate()
            .alpha(1f)
            .setDuration(1500)
            .setListener(null)
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun onDestroy() {
        super.onDestroy()
        // آزاد کردن منابع
        mediaPlayer?.release()
        libVlc?.release()
    }
}











//package com.example.griffin
//
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.view.View
//import android.view.WindowManager
//import android.widget.Button
//import android.widget.ImageButton
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContentProviderCompat.requireContext
//import androidx.recyclerview.widget.GridLayoutManager
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.griffin.R
//import com.example.griffin.adapters.CameraAdadpter
//import com.example.griffin.adapters.LearnThermostatAdadpter
//import com.example.griffin.database.camera_db
//import com.example.griffin.mudels.camera
//import org.videolan.libvlc.LibVLC
//import org.videolan.libvlc.Media
//import org.videolan.libvlc.MediaPlayer
//import org.videolan.libvlc.util.VLCVideoLayout
//
//
//class RTSPVideoPlayerActivity : AppCompatActivity() {
//
//    private lateinit var libVlc: LibVLC
//    private lateinit var mediaPlayer: MediaPlayer
//    private lateinit var videoLayout: VLCVideoLayout
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_rtspvideo_player)
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//        var currentApiVersion: Int = 0
//        currentApiVersion = Build.VERSION.SDK_INT
//        val flags: Int = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
//                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
////        window.decorView.systemUiVisibility = flags
//            val decorView: View = window.decorView
//            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
//                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN === 0) {
//                    decorView.systemUiVisibility = flags
//                }
//            }
//        }
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//
//
//
//        val camera_database2=camera_db.getInstance(this)
//
//        var cameras2= camera_database2.getAllcameras()
//
//        if (cameras2.count() > 0){
//
//            var first_cam=cameras2[0]
//            val rtspUri2="rtsp://${first_cam!!.user}:${first_cam.pass}@${first_cam.ip}:${first_cam.port}/cam/realmonitor?channel=${first_cam.chanel}&subtype=${first_cam.subtype}"
//
//            videoLayout=findViewById(R.id.videoLayout)
//            videoLayout.alpha = 0f
//            videoLayout.visibility = View.VISIBLE
//
//            videoLayout.animate()
//                .alpha(1f)
//                .setDuration(1500)
//                .setListener(null)
//            // Initialize LibVLC
//            libVlc = LibVLC(this)
//
//            // Find the VLCVideoLayout view in your layout
//            videoLayout = findViewById(R.id.videoLayout)
//
//            // Set the RTSP URL for the live stream
////            val rtspUri = "rtsp://admin:hamid5005668@192.168.0.109:554/cam/realmonitor?channel=1&subtype=0"
//
//// Create a media source
//            val media = Media(libVlc, Uri.parse(rtspUri2))
//
//// Set the network-caching option to reduce delay (in milliseconds)
//            media.addOption(":network-caching=200")
//
//// Create a MediaPlayer
//            mediaPlayer = MediaPlayer(libVlc)
//
//// Attach the VLCVideoLayout to the MediaPlayer
//            mediaPlayer.attachViews(videoLayout, null, false, false)
//
//// Set the media source for the MediaPlayer
//            mediaPlayer.media = media
//
//// Start playback
//            mediaPlayer.play()
//
//
//
//        }
//
//
//
//
//        val recyclerView: RecyclerView = findViewById(R.id.camera_learn_recyclerview)
////        val layoutManager = GridLayoutManager(this, 1) // تعداد ستون‌ها را 3 قرار دهید
//        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//
//        val camera_database=camera_db.getInstance(this)
//
//        var cameras= camera_database.getAllcameras()
//
//        val adapter = CameraAdadpter(cameras) { selected_camera ->
//            videoLayout=findViewById(R.id.videoLayout)
//            videoLayout.alpha = 0f
//            videoLayout.visibility = View.VISIBLE
//
//            videoLayout.animate()
//                .alpha(1f)
//                .setDuration(1500)
//                .setListener(null)
//
//            if (::mediaPlayer.isInitialized) {
//                mediaPlayer.release()
//            }
//
//            if (::libVlc.isInitialized) {
//                libVlc.release()
//            }
//            val rtspUri="rtsp://${selected_camera.user}:${selected_camera.pass}@${selected_camera.ip}:${selected_camera.port}/cam/realmonitor?channel=${selected_camera.chanel}&subtype=${selected_camera.subtype}"
//
//            // Initialize LibVLC
//            libVlc = LibVLC(this)
//
//            // Find the VLCVideoLayout view in your layout
//            videoLayout = findViewById(R.id.videoLayout)
//
//            // Set the RTSP URL for the live stream
////            val rtspUri = "rtsp://admin:hamid5005668@192.168.0.109:554/cam/realmonitor?channel=1&subtype=0"
//
//// Create a media source
//            val media = Media(libVlc, Uri.parse(rtspUri))
//
//// Set the network-caching option to reduce delay (in milliseconds)
//            media.addOption(":network-caching=200")
//
//// Create a MediaPlayer
//            mediaPlayer = MediaPlayer(libVlc)
//
//// Attach the VLCVideoLayout to the MediaPlayer
//            mediaPlayer.attachViews(videoLayout, null, false, false)
//
//// Set the media source for the MediaPlayer
//            mediaPlayer.media = media
//
//// Start playback
//            mediaPlayer.play()
//
//        }
//
//        recyclerView.adapter = adapter
//        adapter.setItems(cameras)
//
//
//        val back=findViewById<Button>(R.id.camera_back)
//        back.setOnClickListener {
//            finish()
//        }
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//
//        // Release resources when the activity is destroyed
//        if (::mediaPlayer.isInitialized) {
//            mediaPlayer.release()
//        }
//
//        if (::libVlc.isInitialized) {
//            libVlc.release()
//        }
//    }
//}
