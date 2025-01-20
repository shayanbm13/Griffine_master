package com.example.griffin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class splash_screen : AppCompatActivity() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
        private val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            // دسترسی‌های استوریج را حذف کرده‌ایم
        )
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val constraintLayout = findViewById<ConstraintLayout>(R.id.root_layout)

        // بارگذاری و اعمال انیمیشن
        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.start_splash)
        constraintLayout.startAnimation(fadeInAnimation)

        if (arePermissionsGranted()) {
            // Permissions are already granted
            proceedWithFunctionality()
        } else {
            // Request necessary permissions excluding storage
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE)
        }
    }

    private fun arePermissionsGranted(): Boolean {
        return REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun proceedWithFunctionality() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler(Looper.getMainLooper()).postDelayed({
            val sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

            // بررسی کنیم که آیا کاربر لاگین کرده است یا نه
            if (!isLoggedIn) {
                // اگر لاگین نکرده باشد، به صفحه لاگین هدایت می‌شود
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish() // MainActivity را ببندیم تا به صفحه لاگین برویم
            } else {
                val intent = Intent(this, dashboard::class.java)
                startActivity(intent)
                finish()
            }
        }, 1500) // 1500 is the delayed time in milliseconds.
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                proceedWithFunctionality()
            } else {

                // Handle the case where permissions are not granted
                // به ادامه برنامه بدون بررسی دسترسی استوریج ادامه می‌دهیم
                proceedWithFunctionality()
            }
        }
    }
}