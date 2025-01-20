package com.example.griffin

import android.Manifest.permission.*
import android.content.Context
import android.net.sip.SipManager
import android.os.Bundle
import android.os.PowerManager
import android.view.TextureView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

//import org.linphone.core.*
//import org.linphone.core.tools.Log






class intercom : AppCompatActivity() {

//    private lateinit var core: Core
    private lateinit var wakeLock: PowerManager.WakeLock
//    // Create a Core listener to listen for the callback we need
//    private val coreListener = object : CoreListenerStub() {
//        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState, message: String) {
//            Log.i("[Account] Registration state changed: $state, $message")
//        }
//    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intercom)


        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakeLock")

        // روشن کردن صفحه در صورت خاموش شدن
        wakeLock.acquire()
//
//        val factory = Factory.instance()
//        factory.setDebugMode(true, "Hello Linphone")
//        core = factory.createCore(null, null, this)
//
//        // Configure your SIP account details here
//        val username = "100"
//        val password = "hamid5005"
//        val domain = "192.168.0.115"
//        val transportType = TransportType.Udp // Change to your desired transport type
//
//        val authInfo = Factory.instance().createAuthInfo(username, null, password, null, null, domain, null)
//
//        val accountParams = core.createAccountParams()
//        val identity = Factory.instance().createAddress("sip:$username@$domain")
//        accountParams.identityAddress = identity
//
//        val address = Factory.instance().createAddress("sip:$domain")
//        address?.transport = transportType
//        accountParams.serverAddress = address
//        accountParams.isRegisterEnabled = true
//
//
//        core.isVideoCaptureEnabled = false
//        core.isVideoDisplayEnabled = true
//
//
//
//
//
//        val account = core.createAccount(accountParams)
//        core.addAuthInfo(authInfo)
//        core.addAccount(account)
//        core.defaultAccount = account
//
//        core.addListener(coreListener)
//
//        core.start()
//
//
//
//        val remoteVideoSurface = findViewById<TextureView>(R.id.remote_video_view)
//
//
//        val call_btn=findViewById<Button>(R.id.call_btn)
//        call_btn.setOnClickListener {
//
//
//
//
//            val callee = Factory.instance().createAddress("sip:101@$domain")
//            val params = core.createCallParams(null)
//            params!!.mediaEncryption = MediaEncryption.SRTP
//            params.mediaEncryption = MediaEncryption.SRTP
//
//            params.isVideoEnabled=true
//
//
//            val call = core.inviteAddressWithParams(callee!!, params)
//            call?.addListener(object: CallListenerStub() {
//                override fun onStateChanged(call: Call, state: Call.State, message: String) {
//                    when (state) {
//                        Call.State.Connected -> Log.i("Call is connected")
//                        else -> Log.i("Call is $state")
//                    }
//                }
//            })
//
//
//            core.nativeVideoWindowId = remoteVideoSurface
//
//
//
//
//        }




    }

    override fun onResume() {
        super.onResume()
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakeLock")

        // روشن کردن صفحه در صورت خاموش شدن
        wakeLock.acquire()
    }


    override fun onDestroy() {
        super.onDestroy()
//        core.removeListener(coreListener)
//        core.terminateAllCalls()
//        core.stop()
        // Stop Linphone Core when the activity is destroyed

    }



}