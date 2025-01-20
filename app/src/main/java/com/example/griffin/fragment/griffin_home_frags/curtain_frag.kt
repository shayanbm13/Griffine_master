package com.example.griffin.fragment.griffin_home_frags

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.griffin.R
import com.example.griffin.adapters.LearnCurtainAdapter
import com.example.griffin.database.curtain_db
import com.example.griffin.database.setting_network_db
import com.example.griffin.mudels.*
import java.util.*


class curtain_frag : Fragment() {

    private  val SharedViewModel: SharedViewModel by activityViewModels()
    private val requestQueue: Queue<Pair<curtain, Button>> = LinkedList()
    private var isProcessing = false
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 400



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curtain_frag, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var curtain_layout =view.findViewById<ConstraintLayout>(R.id.curtain_layout)
        val recyclerView: RecyclerView = view.findViewById(R.id.curtain_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.layoutManager = layoutManager

        val database= curtain_db.getInstance(requireContext())

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val learn_curtain_db= curtain_db.getInstance(requireContext())
//            learn_light_db.deleteRowsWithNullOrEmptyMac()
            SharedViewModel.update_curtain_to_learn_list( learn_curtain_db.getAllcurtainsByRoomName(room!!.room_name))

            SharedViewModel.curtain_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->
                val curtain_open=view.findViewById<Button>(R.id.curtain_open)
                val curtain_mid=view.findViewById<Button>(R.id.curtain_mid)
                val curtain_close=view.findViewById<Button>(R.id.curtain_close)
                val temp_disconnected=view.findViewById<TextView>(R.id.temp_disconnected)


                val adapter = LearnCurtainAdapter(newlist) { selectedItem ->
//                    Toast.makeText(requireContext(), selectedItem.Lname, Toast.LENGTH_SHORT).show()
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
                                var is_ok =refresh_curtain(this,selectedItem)
                                val handler = Handler(Looper.getMainLooper())

                                handler.post {
                                    if (is_ok){
                                        temp_disconnected.visibility=View.GONE
                                        curtain_layout.alpha=0f
                                        curtain_layout.visibility=View.VISIBLE
                                        curtain_layout.animate().alpha(1f).setDuration(400).setListener(null)

                                    }else{

                                        temp_disconnected.alpha=0f
                                        temp_disconnected.visibility=View.VISIBLE
                                        temp_disconnected.animate().alpha(1f).setDuration(400).setListener(null)
                                    }


                                    val database1= curtain_db.getInstance(requireContext())
                                    var status=database1.get_from_db_curtain(selectedItem.id)!!.status

//                    try {
////                        udp_light(this,selectedItem)
//                    }catch (e:Exception){
//                        println(e)
////
//                    }


//                                    fun open(){
//                                        curtain_open.setBackgroundResource(R.drawable.curtain_open_on)
//                                        curtain_mid.setBackgroundResource(R.drawable.curtain_mid_off)
//                                        curtain_close.setBackgroundResource(R.drawable.curtain_close_off)
//                                        status="00"
//
//                                    }
//                                    fun mid(){
//                                        curtain_open.setBackgroundResource(R.drawable.curtain_open_off)
//                                        curtain_mid.setBackgroundResource(R.drawable.curtain_mid_on)
//                                        curtain_close.setBackgroundResource(R.drawable.curtain_close_off)
//                                        status="50"
//                                    }
//                                    fun close(){
//                                        curtain_open.setBackgroundResource(R.drawable.curtain_open_off)
//                                        curtain_mid.setBackgroundResource(R.drawable.curtain_mid_off)
//                                        curtain_close.setBackgroundResource(R.drawable.curtain_close_on)
//                                        status="99"
//                                    }


                                    fun open() {
                                        curtain_open.setBackgroundResource(R.drawable.curtain_open_on)
                                        curtain_mid.setBackgroundResource(R.drawable.curtain_mid_off)
                                        curtain_close.setBackgroundResource(R.drawable.curtain_close_off)
                                    }

                                    fun mid() {
                                        curtain_open.setBackgroundResource(R.drawable.curtain_open_off)
                                        curtain_mid.setBackgroundResource(R.drawable.curtain_mid_on)
                                        curtain_close.setBackgroundResource(R.drawable.curtain_close_off)
                                    }

                                    fun close() {
                                        curtain_open.setBackgroundResource(R.drawable.curtain_open_off)
                                        curtain_mid.setBackgroundResource(R.drawable.curtain_mid_off)
                                        curtain_close.setBackgroundResource(R.drawable.curtain_close_on)
                                    }
                                    fun processQueue() {
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
                                        val (curtain, button) = requestQueue.poll()
                                        val previousStatus = curtain.status
                                        var status =when(button.id){
                                            R.id.curtain_mid-> "50"
                                            R.id.curtain_close-> "99"
                                            R.id.curtain_open-> "00"


                                            else -> {""}
                                        }

                                        val curtainDb=curtain_db.getInstance(requireContext())
                                        Thread {
                                            try {
                                                val isSuccessful = udp_curtain(requireContext(), curtain,status)
                                                requireActivity().runOnUiThread {
                                                    val my_status = curtainDb.getStatusById(curtain.id)
                                                    if (isSuccessful) {
                                                        // Update button state based on new status
                                                        if (my_status=="50"){
                                                            mid()

                                                        }else if (my_status=="99"){

                                                            close()

                                                        }else if (my_status=="00"){

                                                            open()
                                                        }
                                                        println("suseesssssssssss")
//                                                        button.setBackgroundResource(if (curtain.status == "1") R.drawable.coler_on else R.drawable.coler_off)

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




                                    curtain_open.setOnClickListener {
                                        requestQueue.add(Pair(selectedItem, curtain_open))
//                                            udp_curtain(requireContext(), selectedItem, "00")


                                        if (!isProcessing) {
                                            processQueue()
                                        }
                                    }

                                    curtain_mid.setOnClickListener {
                                        requestQueue.add(Pair(selectedItem, curtain_mid))
//                                            udp_curtain(requireContext(), selectedItem, "00")


                                        if (!isProcessing) {
                                            processQueue()
                                        }
                                    }

                                    curtain_close.setOnClickListener {
                                        requestQueue.add(Pair(selectedItem, curtain_close))
//                                            udp_curtain(requireContext(), selectedItem, "00")


                                        if (!isProcessing) {
                                            processQueue()
                                        }
                                    }



                                    when(status){
                                        "00"-> open()
                                        "50"-> mid()
                                        "99"-> close()
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
