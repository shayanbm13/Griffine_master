package com.example.griffin.fragment.griffin_home_frags
import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.griffin.dashboard

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val keyguardManager = context?.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

            // دریافت KeyguardLock
            val keyguardLock = keyguardManager.newKeyguardLock(Context.KEYGUARD_SERVICE)
//            println("ssssssssssssssssssssssss")

            // باز کردن قفل صفحه نمایش
            keyguardLock.disableKeyguard()
            Toast.makeText(context.applicationContext, "dsdadasdasdad", Toast.LENGTH_SHORT).show()
            // شروع فعالیت Dashboard
            val intent = Intent(context.applicationContext, dashboard::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.applicationContext.startActivity(intent)
        }
    }
}