package com.example.griffin.fragment.setting_four.choose_lolcal_work

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.griffin.R
import com.example.griffin.adapters.curtain_add_scenario_Adapter
import com.example.griffin.database.curtain_db
import com.example.griffin.database.light_db
import com.example.griffin.database.six_workert_db
import com.example.griffin.mudels.PagerChangeListener
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.udp_six_worker
import java.lang.reflect.Executable

// TODO: Rename parameter arguments, choose names that match

class select_work_curtain : Fragment() {
     
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val sharedViewModel : SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_select_work_curtain, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       
    }

    override fun onResume() {
        super.onResume()

        val sw_chose_curtain = requireView().findViewById<Button>(R.id.sw_chose_curtain)
        val curtain_func_on: Button = requireView().findViewById(R.id.curtain_func_open)
        val curtain_func_off: Button = requireView().findViewById(R.id.curtain_func_close)
        val curtain_func_toggle: Button = requireView().findViewById(R.id.curtain_func_halfo)
        val sw_chose_curtain_cancel: Button = requireView().findViewById(R.id.sw_chose_curtain_cancel)
        val sw_chose_curtain_ok: Button = requireView().findViewById(R.id.sw_chose_curtain_ok)

        val curtain_db = curtain_db.getInstance(requireContext())

         
        val chooser_btns_list = listOf(
            sw_chose_curtain,
            curtain_func_on,
            curtain_func_off,
            curtain_func_toggle,

            )

        fun selectButton(selectedButton: Button) {
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    clickSound.start()

                    if (button==curtain_func_on){
                        button.setBackgroundResource(R.drawable.functionality_open_anable_on)
                    }
                    if (button==curtain_func_off){
                        button.setBackgroundResource(R.drawable.functionality_close_anable_on)
                    }
                    if (button==curtain_func_toggle){
                        button.setBackgroundResource(R.drawable.functionality_halfopen_anable_on)
                    }


                } else {
//                    clickSound.start()
                    button.isSelected = false

                    if (button==curtain_func_on){
                        button.setBackgroundResource(R.drawable.functionality_open_anable_off)
                    }
                    if (button==curtain_func_off){
                        button.setBackgroundResource(R.drawable.functionality_close_anable_off)
                    }
                    if (button==curtain_func_toggle){
                        button.setBackgroundResource(R.drawable.functionality_halfopen_anable_off)
                    }


                }
            }
        }

        val pagerChangeListener = parentFragment as? PagerChangeListener



        val  six_workert_db = six_workert_db.getInstance(requireContext())






        sharedViewModel.current_pole_six_wirker.observe(viewLifecycleOwner, Observer { current ->
            val current_id_pole=current!!.split(",")
            val current_id =current_id_pole[0]
            val current_pole =current_id_pole[1]
            val current_six_worker = six_workert_db.get_from_db_six_workert(current_id.toInt())

            println(current_six_worker?.work_name)
            var statuses= try {
                current_six_worker!!.status!!.split(",").toMutableList()

            }catch (e:Exception){
                null

            }
            var types=try {

                current_six_worker!!.type!!.split(",").toMutableList()
            }catch (e:Exception){
                null
            }

            val status = statuses?.get(((current_pole.toInt())-1))

            var curtains_id= try {
                current_six_worker!!.sub_type!!.split(",").toMutableList()

            }catch (e:Exception){
                null
            }
            val curtain_id = curtains_id?.get(((current_pole.toInt())-1))

            var curtains_names=try {

                current_six_worker!!.work_name!!.split(",").toMutableList()
            }catch (e:Exception){
                null
            }
            println(curtains_names?.get(current_pole.toInt() -1))
            try {
                if (curtains_names?.get(current_pole.toInt() -1) != null && curtains_names?.get(current_pole.toInt() -1) != ""){

                    val current_curtain = curtain_db.get_from_db_curtain(curtain_id!!.toInt())
                    sw_chose_curtain.setText(current_curtain!!.Cname)

                }else{
                    sw_chose_curtain.setText("select")
                }
            }catch (e:Exception){
                sw_chose_curtain.setText("select")
            }


//            val curtain_name = curtains_names?.get(((current_pole.toInt())-1))

            when(status){
                "on"-> selectButton(curtain_func_on)
                "off"-> selectButton(curtain_func_off)
                "T"-> selectButton(curtain_func_toggle)
            }


            fun state_coder(pole_num:String? , pole_state:String) :String {
                val state = arrayListOf<String>("-","-","-","-","-","-","-","-","-","-","-","-","-","-","-","-")
                state[(pole_num?.toInt())?.minus(1)!!]=if (pole_state == "on"){
                    "1"
                }else if (pole_state =="off"){
                    "0"
                }else{
                    "t"
                }
                return state.joinToString(separator = "")

            }

            curtain_func_on.setOnClickListener {
                if (current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1]!="" ){



                    selectButton(curtain_func_on)
//            pagerChangeListener?.onPagerItemChanged(3)
                    if (status == "on"){
                        val selected_curtain = curtain_db.get_from_db_curtain(curtains_id?.get((current_pole.toInt() -1))?.toInt())
                        types?.set((current_pole.toInt())-1, "curtain")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "on")
                        curtains_names?.set((current_pole.toInt())-1, selected_curtain!!.mac.toString())
                        println(selected_curtain?.Cname)
                        println(current_pole)
                        println(selected_curtain!!.mac)
                        current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()


                        requireActivity().runOnUiThread{

                            Thread{
                                val response = udp_six_worker(this,selected_curtain?.mac,"0"+current_pole,"Crtn",selected_curtain?.sub_type,"00",selected_curtain?.ip,current_six_worker)

                                if (response){
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }

                                }else if(current_six_worker.pole_num!!.contains("-")){
                                    statuses?.set((current_pole.toInt())-1, "on")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread(){

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Failed click again...", Toast.LENGTH_SHORT).show()
                                    }
                                    curtains_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        curtain_func_on.setBackgroundResource(R.drawable.functionality_open_anable_off)
                                        curtain_func_off.setBackgroundResource(R.drawable.functionality_close_anable_off)
                                        curtain_func_toggle.setBackgroundResource(R.drawable.functionality_halfopen_anable_off)


                                    }
                                }
                            }.start()
                        }


                    }else{
                        val selected_curtain = curtain_db.get_from_db_curtain(curtains_id?.get((current_pole.toInt() -1))?.toInt())
                        curtains_names?.set((current_pole.toInt())-1, selected_curtain!!.mac.toString())
                        current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "curtain")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "on")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()
                        requireActivity().runOnUiThread{
                            Thread{
                                val response = udp_six_worker(this,selected_curtain?.mac,"0"+current_pole,"Crtn",selected_curtain?.sub_type,"00",selected_curtain?.ip,current_six_worker)

                                if (response){
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }

                                }else if(current_six_worker.pole_num!!.contains("-")){
                                    statuses?.set((current_pole.toInt())-1, "on")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread(){

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Failed click again...", Toast.LENGTH_SHORT).show()
                                    }
                                    curtains_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        curtain_func_on.setBackgroundResource(R.drawable.functionality_open_anable_off)
                                        curtain_func_off.setBackgroundResource(R.drawable.functionality_close_anable_off)
                                        curtain_func_toggle.setBackgroundResource(R.drawable.functionality_halfopen_anable_off)


                                    }
                                }
                            }.start()

                        }

                    }
                }else{

                    Toast.makeText(requireContext(), "please select a y first", Toast.LENGTH_SHORT).show()
                }

            }

            curtain_func_off.setOnClickListener {
                if (current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1]!="" ){


                    selectButton(curtain_func_off)
                    if (status == "off"){
                        val selected_curtain = curtain_db.get_from_db_curtain(curtains_id?.get((current_pole.toInt() -1))?.toInt())
                        curtains_names?.set((current_pole.toInt())-1, selected_curtain!!.mac.toString())
                        current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "curtain")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "off")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()

                        requireActivity().runOnUiThread{

                            Thread{
                                val response = udp_six_worker(this,selected_curtain?.mac,"0"+current_pole,"Crtn",selected_curtain?.sub_type,"99",selected_curtain?.ip,current_six_worker)

                                if (response){
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }

                                }else if(current_six_worker.pole_num!!.contains("-")){
                                    statuses?.set((current_pole.toInt())-1, "off")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread(){

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Failed click again...", Toast.LENGTH_SHORT).show()
                                    }
                                    curtains_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        curtain_func_on.setBackgroundResource(R.drawable.functionality_open_anable_off)
                                        curtain_func_off.setBackgroundResource(R.drawable.functionality_close_anable_off)
                                        curtain_func_toggle.setBackgroundResource(R.drawable.functionality_halfopen_anable_off)


                                    }
                                }
                            }.start()
                        }


                    }else{
                        val selected_curtain = curtain_db.get_from_db_curtain(curtains_id?.get((current_pole.toInt() -1))?.toInt())
                        curtains_names?.set((current_pole.toInt())-1, selected_curtain!!.mac.toString())
                        current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "curtain")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "off")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()

                        requireActivity().runOnUiThread{

                            Thread{
                                val response = udp_six_worker(this,selected_curtain?.mac,"0"+current_pole,"Crtn",selected_curtain?.sub_type,"99",selected_curtain?.ip,current_six_worker)

                                if (response){
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }

                                }else if(current_six_worker.pole_num!!.contains("-")){
                                    statuses?.set((current_pole.toInt())-1, "off")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread(){

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Failed click again...", Toast.LENGTH_SHORT).show()
                                    }
                                    curtains_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        curtain_func_on.setBackgroundResource(R.drawable.functionality_open_anable_off)
                                        curtain_func_off.setBackgroundResource(R.drawable.functionality_close_anable_off)
                                        curtain_func_toggle.setBackgroundResource(R.drawable.functionality_halfopen_anable_off)


                                    }
                                }
                            }.start()
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "please select a curtain first", Toast.LENGTH_SHORT).show()
                }

            }
            curtain_func_toggle.setOnClickListener {

                if (current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1]!="" ){


                    selectButton(curtain_func_toggle)
                    if (status == "T"){
                        val selected_curtain = curtain_db.get_from_db_curtain(curtains_id?.get((current_pole.toInt() -1))?.toInt())
                        curtains_names?.set((current_pole.toInt())-1, selected_curtain!!.mac.toString())
                        current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "curtain")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "T")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()


                        requireActivity().runOnUiThread{
                            Thread{

                                val response = udp_six_worker(this,selected_curtain?.mac,"0"+current_pole,"Crtn",selected_curtain?.sub_type,"50",selected_curtain?.ip,current_six_worker)

                                if (response){
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }


                                }else if(current_six_worker.pole_num!!.contains("-")){
                                    statuses?.set((current_pole.toInt())-1, "T")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread(){

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Failed click again...", Toast.LENGTH_SHORT).show()
                                    }
                                    curtains_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        curtain_func_on.setBackgroundResource(R.drawable.functionality_open_anable_off)
                                        curtain_func_off.setBackgroundResource(R.drawable.functionality_close_anable_off)
                                        curtain_func_toggle.setBackgroundResource(R.drawable.functionality_halfopen_anable_off)


                                    }
                                }
                            }.start()


                        }


                    }else{
                        val selected_curtain = curtain_db.get_from_db_curtain(curtains_id?.get((current_pole.toInt() -1))?.toInt())
                        curtains_names?.set((current_pole.toInt())-1, selected_curtain!!.mac.toString())
                        current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "curtain")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "T")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()

                        requireActivity().runOnUiThread{
                            Thread{
                                val response = udp_six_worker(this,selected_curtain?.mac,"0"+current_pole,"Crtn",selected_curtain?.sub_type,"50",selected_curtain?.ip,current_six_worker)

                                if (response){
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }

                                }else if(current_six_worker.pole_num!!.contains("-")){
                                    statuses?.set((current_pole.toInt())-1, "T")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread(){

                                        Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                                    }
                                }else{
                                    requireActivity().runOnUiThread{

                                        Toast.makeText(requireContext(), "Failed click again...", Toast.LENGTH_SHORT).show()
                                    }
                                    curtains_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        curtain_func_on.setBackgroundResource(R.drawable.functionality_open_anable_off)
                                        curtain_func_off.setBackgroundResource(R.drawable.functionality_close_anable_off)
                                        curtain_func_toggle.setBackgroundResource(R.drawable.functionality_halfopen_anable_off)


                                    }
                                }
                            }.start()

                        }

                    }
                }else{
                    Toast.makeText(requireContext(), "please select a curtain first", Toast.LENGTH_SHORT).show()
                }
//            pagerChangeListener?.onPagerItemChanged(4)

            }

            sw_chose_curtain_cancel.setOnClickListener {

                pagerChangeListener?.onPagerItemChanged(1)

            }
            sw_chose_curtain_ok.setOnClickListener {

//            pagerChangeListener?.onPagerItemChanged(0)

            }
            sw_chose_curtain.setOnClickListener {
                selectButton(sw_chose_curtain)
                sw_chose_curtain.setBackgroundResource(R.drawable.select_back_on)
                Handler().postDelayed({
                    sw_chose_curtain.setBackgroundResource(R.drawable.select_back)
                }, 1000)

                var newlist = curtain_db.getAllcurtains()
//            val inter= db_handler.getItemsWithInExex()

                val inflater4 = LayoutInflater.from(requireContext())
                val customPopupView4: View = inflater4.inflate(R.layout.add_music_to_scenario_popup, null)



                val popupWidth4 = 650
                val popupHeight4 = 650
                val popupWindow4 = PopupWindow(customPopupView4, popupWidth4, popupHeight4, true)
                popupWindow4.isFocusable = true

                val recyclerView: RecyclerView = customPopupView4.findViewById(R.id.scenario_add_music_recycler_view)
                val cancel_btn: Button = customPopupView4.findViewById(R.id.cancel)
                val layoutManager = GridLayoutManager(requireContext(), 3) // ????? ??????? ?? 3 ???? ????
                recyclerView.layoutManager = layoutManager
                cancel_btn.setOnClickListener {
                    popupWindow4.dismiss()
                }
                popupWindow4.showAtLocation(it, Gravity.CENTER, 0, 0)
                val adapter = curtain_add_scenario_Adapter(newlist) { selectedItem ->
                    println(selectedItem.Cname)
                    curtains_id?.set((current_pole.toInt())-1, selectedItem.id.toString())
                    println(curtains_id)
                    println("name is "+ curtains_names)
                    current_six_worker!!.sub_type=curtains_id!!.joinToString(separator = ",")
                    curtains_names?.set((current_pole.toInt())-1, selectedItem.mac.toString())
                    current_six_worker.work_name=curtains_names?.joinToString(separator = ",")
                    six_workert_db.updatesix_workertById(current_six_worker.id,current_six_worker)



                    popupWindow4.dismiss()
                    Toast.makeText(requireContext(), "${selectedItem.Cname} Setted for this item", Toast.LENGTH_SHORT).show()
                    sw_chose_curtain.setText(selectedItem.Cname)


                }
                recyclerView.adapter = adapter
                adapter.setItems(newlist)




            }




        })
    
    
    }
    override fun onDestroy() {
        super.onDestroy()
         
    }
    override fun onPause() {
        super.onPause()
         
    }


}