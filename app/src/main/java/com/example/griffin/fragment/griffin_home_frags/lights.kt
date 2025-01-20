package com.example.griffin.fragment.griffin_home_frags

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.griffin.R
import com.example.griffin.adapters.LightAdapter
import com.example.griffin.dashboard
import com.example.griffin.database.light_db
import com.example.griffin.mudels.Light
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.refresh_light
import com.example.griffin.mudels.udp_light
import java.util.*

class lights : Fragment() {

    private val SharedViewModel: SharedViewModel by activityViewModels()

    private val requestQueue: Queue<Pair<Light, String>> = LinkedList()
    private var isProcessing = false
    private val handler = Handler(Looper.getMainLooper())
    private val delayMillis: Long = 400

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lights, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.lights_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager

        val database = light_db.getInstance(requireContext())
        var learnLightDb = light_db.getInstance(requireContext())
        val progressLoading = view.findViewById<ProgressBar>(R.id.prigress_loading)
        val failedLoad = view.findViewById<TextView>(R.id.faild_load)

        progressLoading.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        failedLoad.visibility = View.GONE

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->
            SharedViewModel.light_ref_status.observe(viewLifecycleOwner, Observer { status ->
                when (status) {
                    "ok" -> {
                        progressLoading.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                        failedLoad.visibility = View.GONE
                        learnLightDb = light_db.getInstance(requireContext())
                        SharedViewModel.update_light_to_learn_list(learnLightDb.getAllLightsByRoomName(room!!.room_name))
                    }
                    "failed" -> {
                        failedLoad.visibility = View.VISIBLE
                        progressLoading.visibility = View.GONE
                        Handler(Looper.getMainLooper()).postDelayed({
                            failedLoad.visibility = View.GONE

                            progressLoading.visibility = View.GONE
                            recyclerView.visibility = View.VISIBLE
                            failedLoad.visibility = View.GONE
                            learnLightDb = light_db.getInstance(requireContext())
                            SharedViewModel.update_light_to_learn_list(learnLightDb.getAllLightsByRoomName(room!!.room_name))

                        }, 1500) // 3000 is the delayed time in milliseconds.
                    }
                    "loading" -> {
                        progressLoading.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                        failedLoad.visibility = View.GONE
                    }
                }

                SharedViewModel.light_to_learn_list.observe(viewLifecycleOwner, Observer { newList ->
                    val adapter = LightAdapter(newList) { selectedItem, lightSwitch->

                        println(lightSwitch)
                        println(selectedItem.Lname)
                        requestQueue.add(Pair(selectedItem, selectedItem.status!!))
                        if (!isProcessing) {
                            processQueue()
                        }
                    }
                    recyclerView.adapter = adapter
                    val distinctList = newList.distinct()
                    adapter.setItems(distinctList)
                })
            })
        })
    }

    private fun processQueue() {
        try {
            if (requestQueue.isEmpty()) {
                isProcessing = false
                return
            }

            isProcessing = true
            val (light, switch) = requestQueue.poll()
            val previousStatus = light.status

//        println(switch.isChecked)
            if (switch=="off") {
                light_db.getInstance(requireContext()).updateStatusById(light.id, "off")
            } else {
                light_db.getInstance(requireContext()).updateStatusById(light.id, "on")
            }

            Thread {
                try {
                    println("sended")
                    val res = udp_light(requireContext(), light)
                    println("2")
                    println(res)


                } catch (e: Exception) {
                    println(e)
                    print("lights page")
                } finally {
                    handler.postDelayed({
                        processQueue()
                    }, delayMillis)
                }
            }.start()
        }catch (e:Exception){
            println(e)

        }


    }
}