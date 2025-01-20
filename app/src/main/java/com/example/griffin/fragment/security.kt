package com.example.griffin.fragment

import android.app.AlertDialog
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.griffin.R
import com.example.griffin.database.Elevator_db
import com.example.griffin.database.security_db
import com.example.griffin.mudels.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class security : Fragment() {
     
    val SharedViewModel :SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_security, container, false)

        return view


    }

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


        val btn_arme = view.findViewById<Button>(R.id.btn_arme)
        val btn_disarme = view.findViewById<Button>(R.id.btn_disarme)
        val btn_sleep = view.findViewById<Button>(R.id.btn_sleep)
        val elevator = view.findViewById<Button>(R.id.elevator)

        var chooser_fragment_list= listOf(btn_arme,btn_disarme,btn_sleep)
        for (button in chooser_fragment_list) {
            button.setButtonScaleOnTouchListener()



        }

        try {
            val elevator_db_handler= Elevator_db.getInstance(requireContext())
            val a = elevator_db_handler.getAllElevators()[0]


        }catch (e:Exception){
            elevator.visibility=View.GONE

        }

        btn_disarme.setOnClickListener {
            if (SharedViewModel.countdownLiveData.value.toString()!= "999999" ){
                val security_db_handler = security_db.getInstance(requireContext())
                if (!security_db_handler.isEmptysecurity_tabale()){
                    val current_db = security_db_handler.get_from_db_security(1)

                    val time = (current_db!!.arm_active_deley?.toInt())?.times(1000)
                    val pass = current_db.password_security




                    val inflater5 = LayoutInflater.from(requireContext())
                    val popupView5: View = inflater5.inflate(R.layout.popup_layout, null)
                    val wrong_password_security = popupView5.findViewById<TextView>(R.id.wrong_password_security)
                    wrong_password_security.visibility=View.GONE
                    val ok = popupView5.findViewById<Button>(R.id.ok_btn)
                    val enterd_password = popupView5.findViewById<EditText>(R.id.intered_pass)



                    val alertDialogBuilder5 = AlertDialog.Builder(requireContext())
                    alertDialogBuilder5.setView(popupView5)

                    val tvCountdown = popupView5.findViewById<TextView>(R.id.tvCountdown)

                    var is_ok= false

                    SharedViewModel.countdownLiveData.observe(viewLifecycleOwner , Observer { counter ->

                        tvCountdown.text = counter.toString()
                        if (counter.toString() == "999999"){
                            tvCountdown.visibility=View.GONE

                        }
                        if (counter.toString() == "0"){
                            tvCountdown.setTextColor(Color.RED)

                        }

                    })

                    val horn = CarHornSoundPlayer.getInstance()
                    val alertDialog5 = alertDialogBuilder5.create()
                    alertDialog5.show()
                    ok.setOnClickListener {
                        if (enterd_password.text.toString() ==  pass.toString()){
                            alertDialog5.dismiss()
                            horn.stopHornSound()
                            Toast.makeText(requireContext(), "Disarmed...", Toast.LENGTH_LONG).show()
                            is_ok=true
                        }else{
                            wrong_password_security.visibility=View.VISIBLE
                        }
                    }
// حذف بک‌گراند دیالوگ الرت
                    alertDialog5.window?.setBackgroundDrawableResource(android.R.color.transparent)



//        }
                }


            }else{

                val security_db_handler = security_db.getInstance(requireContext())
                if (!security_db_handler.isEmptysecurity_tabale()){
                    val current_db = security_db_handler.get_from_db_security(1)

                    val time = (current_db!!.arm_active_deley?.toInt())?.times(1000)
                    val pass = current_db.password_security




                    val inflater5 = LayoutInflater.from(requireContext())
                    val popupView5: View = inflater5.inflate(R.layout.popup_layout, null)
                    val wrong_password_security = popupView5.findViewById<TextView>(R.id.wrong_password_security)
                    wrong_password_security.visibility=View.GONE
                    val ok = popupView5.findViewById<Button>(R.id.ok_btn)
                    val enterd_password = popupView5.findViewById<EditText>(R.id.intered_pass)



                    val alertDialogBuilder5 = AlertDialog.Builder(requireContext())
                    alertDialogBuilder5.setView(popupView5)

                    val tvCountdown = popupView5.findViewById<TextView>(R.id.tvCountdown)
                    tvCountdown.visibility=View.GONE
                    var is_ok= false

                    SharedViewModel.countdownLiveData.observe(viewLifecycleOwner , Observer { counter ->
                        tvCountdown.visibility=View.GONE
                        tvCountdown.text = counter.toString()



                    })

                    val horn = CarHornSoundPlayer.getInstance()
                    val alertDialog5 = alertDialogBuilder5.create()
                    alertDialog5.show()
                    ok.setOnClickListener {
                        if (enterd_password.text.toString() ==  pass.toString()){
                            alertDialog5.dismiss()
                            horn.stopHornSound()
                            Toast.makeText(requireContext(), "Disarmed...", Toast.LENGTH_LONG).show()
                            is_ok=true
                        }else{
                            wrong_password_security.visibility=View.VISIBLE
                        }
                    }
// حذف بک‌گراند دیالوگ الرت
                    alertDialog5.window?.setBackgroundDrawableResource(android.R.color.transparent)



//        }
                }


            }

        }
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

        elevator.setOnClickListener{

            loading_view.increaseProgress(0.0)



            val elevator_db_handler= Elevator_db.getInstance(requireContext())
            udp_elevator(requireContext() ,elevator_db_handler.getAllElevators()[0])




            popupWindow.showAtLocation(it, Gravity.CENTER, 0, 0)
            val handler = Handler()
            val delay: Long = 1000 // زمان تاخیر بر حسب میلی‌ثانیه
            val progressIncrement = 30.0 // میزان افزایش درصد
            var currentProgress = 0.0 // میزان پیشرفت فعلی

            val runnable = object : Runnable {
                override fun run() {
                    // افزایش مقدار پیشرفت
                    currentProgress += progressIncrement
                    // اطمینان حاصل کنید که پیشرفت بیش از 100 نشود
                    if (currentProgress > 100) {
                        currentProgress = 100.0
                        popupWindow.dismiss()

                    }
                    // بروزرسانی لودبنگ ویو با مقدار جدید
                    loading_view.increaseProgress(currentProgress)
                    // اگر پیشرفت هنوز کمتر از 100 است، تکرار این عملیات
                    if (currentProgress < 100) {
                        handler.postDelayed(this, delay)
                    }
                }
            }
            // اجرای runnable برای اولین بار
            handler.postDelayed(runnable, delay)


        }



    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }
    override fun onPause() {
        super.onPause()
         
    }
}