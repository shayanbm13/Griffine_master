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
import com.example.griffin.adapters.plug_add_scenario_Adapter
import com.example.griffin.database.plug_db
import com.example.griffin.database.six_workert_db
import com.example.griffin.mudels.PagerChangeListener
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.udp_six_worker
import java.lang.reflect.Executable

class select_work_plug : Fragment() {
     

    val sharedViewModel : SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_work_plug, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    
    }

    override fun onResume() {
        super.onResume()


        val sw_chose_plug = requireView().findViewById<Button>(R.id.sw_chose_plug)
        val plug_func_on: Button = requireView().findViewById(R.id.plug_func_on)
        val plug_func_off: Button = requireView().findViewById(R.id.plug_func_off)
        val plug_func_toggle: Button = requireView().findViewById(R.id.plug_func_toggle)
        val sw_chose_plug_cancel: Button = requireView().findViewById(R.id.sw_chose_plug_cancel)
        val sw_chose_plug_ok: Button = requireView().findViewById(R.id.sw_chose_plug_ok)

        val plug_db = plug_db.getInstance(requireContext())

         
        val chooser_btns_list = listOf(
            sw_chose_plug,
            plug_func_on,
            plug_func_off,
            plug_func_toggle,

            )

        fun selectButton(selectedButton: Button) {
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    clickSound.start()

                    if (button==plug_func_on){
                        button.setBackgroundResource(R.drawable.functionality_on_anable_on)
                    }
                    if (button==plug_func_off){
                        button.setBackgroundResource(R.drawable.functionality_off_anable_on)
                    }
                    if (button==plug_func_toggle){
                        button.setBackgroundResource(R.drawable.functionality_toggle_anable_on)
                    }


                } else {
//                    clickSound.start()
                    button.isSelected = false

                    if (button==plug_func_on){
                        button.setBackgroundResource(R.drawable.functionality_on_anable_off)
                    }
                    if (button==plug_func_off){
                        button.setBackgroundResource(R.drawable.functionality_off_anable_off)
                    }
                    if (button==plug_func_toggle){
                        button.setBackgroundResource(R.drawable.functionality_toggle_anable_off)
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

            var plugs_id= try {
                current_six_worker!!.sub_type!!.split(",").toMutableList()

            }catch (e:Exception){
                null
            }
            val plug_id = plugs_id?.get(((current_pole.toInt())-1))

            var plugs_names=try {

                current_six_worker!!.work_name!!.split(",").toMutableList()
            }catch (e:Exception){
                null
            }
            println(plugs_names?.get(current_pole.toInt() -1))
            try {
                if (plugs_names?.get(current_pole.toInt() -1) != null && plugs_names?.get(current_pole.toInt() -1) != ""){


                    val current_plug = plug_db.get_from_db_Plug(plug_id!!.toInt())
                    sw_chose_plug.setText(current_plug!!.Pname)

                }else{
                    sw_chose_plug.setText("select")
                }
            }catch (e:Exception){
                sw_chose_plug.setText("select")
            }


//            val plug_name = plugs_names?.get(((current_pole.toInt())-1))

            when(status){
                "on"-> selectButton(plug_func_on)
                "off"-> selectButton(plug_func_off)
                "T"-> selectButton(plug_func_toggle)
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

            plug_func_on.setOnClickListener {
                if (current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1]!="" ){



                    selectButton(plug_func_on)
//            pagerChangeListener?.onPagerItemChanged(3)
                    if (status == "on"){
                        val selected_plug = plug_db.get_from_db_Plug(plugs_id?.get((current_pole.toInt() -1))?.toInt())
                        plugs_names?.set((current_pole.toInt())-1, selected_plug!!.mac.toString())
                        current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "plug")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "on")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()


                        requireActivity().runOnUiThread{

                            Thread{
                                val final_subtype = plug_db.getPlugsByMacAddress(selected_plug!!.mac).sortedBy { it!!.subtype }[0]!!.subtype
                                if (final_subtype=="0000"){
                                    selected_plug!!.subtype= (selected_plug!!.subtype!!.toInt()+1).toString()

                                }
                                val response = udp_six_worker(this,selected_plug?.mac,"0"+current_pole,"Plug","0000",state_coder(selected_plug?.subtype,"on"),selected_plug?.ip,current_six_worker)

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
                                    plugs_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        plug_func_on.setBackgroundResource(R.drawable.functionality_on_anable_off)
                                        plug_func_off.setBackgroundResource(R.drawable.functionality_off_anable_off)
                                        plug_func_toggle.setBackgroundResource(R.drawable.functionality_toggle_anable_off)


                                    }
                                }
                            }.start()
                        }


                    }else{
                        val selected_plug = plug_db.get_from_db_Plug(plugs_id?.get((current_pole.toInt() -1))?.toInt())
                        plugs_names?.set((current_pole.toInt())-1, selected_plug!!.mac.toString())
                        current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "plug")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "on")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()
                        requireActivity().runOnUiThread{
                            Thread{
                                val final_subtype = plug_db.getPlugsByMacAddress(selected_plug!!.mac).sortedBy { it!!.subtype }[0]!!.subtype
                                if (final_subtype=="0000"){
                                    selected_plug!!.subtype= (selected_plug!!.subtype!!.toInt()+1).toString()

                                }
                                val response = udp_six_worker(this,selected_plug?.mac,"0"+current_pole,"Plug","0000",state_coder(selected_plug?.subtype,"on"),selected_plug?.ip,current_six_worker)

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
                                    plugs_names?.set((current_pole.toInt())-1,"")
                                    current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        plug_func_on.setBackgroundResource(R.drawable.functionality_on_anable_off)
                                        plug_func_off.setBackgroundResource(R.drawable.functionality_off_anable_off)
                                        plug_func_toggle.setBackgroundResource(R.drawable.functionality_toggle_anable_off)


                                    }
                                }
                            }.start()

                        }

                    }
                }else{

                    Toast.makeText(requireContext(), "please select a y first", Toast.LENGTH_SHORT).show()
                }

            }

            plug_func_off.setOnClickListener {
                if (current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1]!="" ){


                    selectButton(plug_func_off)
                    if (status == "off"){
                        val selected_plug = plug_db.get_from_db_Plug(plugs_id?.get((current_pole.toInt() -1))?.toInt())
                        plugs_names?.set((current_pole.toInt())-1, selected_plug!!.mac.toString())
                        current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "plug")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "off")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()

                        requireActivity().runOnUiThread{

                            Thread{
                                val final_subtype = plug_db.getPlugsByMacAddress(selected_plug!!.mac).sortedBy { it!!.subtype }[0]!!.subtype
                                if (final_subtype=="0000"){
                                    selected_plug!!.subtype= (selected_plug!!.subtype!!.toInt()+1).toString()

                                }
                                val response = udp_six_worker(this,selected_plug?.mac,"0"+current_pole,"Plug","0000",state_coder(selected_plug?.subtype,"off"),selected_plug?.ip,current_six_worker)

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
                                    plugs_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        plug_func_on.setBackgroundResource(R.drawable.functionality_on_anable_off)
                                        plug_func_off.setBackgroundResource(R.drawable.functionality_off_anable_off)
                                        plug_func_toggle.setBackgroundResource(R.drawable.functionality_toggle_anable_off)


                                    }
                                }
                            }.start()
                        }


                    }else{
                        val selected_plug = plug_db.get_from_db_Plug(plugs_id?.get((current_pole.toInt() -1))?.toInt())
                        plugs_names?.set((current_pole.toInt())-1, selected_plug!!.mac.toString())
                        current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "plug")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "off")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()

                        requireActivity().runOnUiThread{

                            Thread{
                                val final_subtype = plug_db.getPlugsByMacAddress(selected_plug!!.mac).sortedBy { it!!.subtype }[0]!!.subtype
                                if (final_subtype=="0000"){
                                    selected_plug!!.subtype= (selected_plug!!.subtype!!.toInt()+1).toString()

                                }
                                val response = udp_six_worker(this,selected_plug?.mac,"0"+current_pole,"Plug","0000",state_coder(selected_plug?.subtype,"off"),selected_plug?.ip,current_six_worker)

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
                                    plugs_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        plug_func_on.setBackgroundResource(R.drawable.functionality_on_anable_off)
                                        plug_func_off.setBackgroundResource(R.drawable.functionality_off_anable_off)
                                        plug_func_toggle.setBackgroundResource(R.drawable.functionality_toggle_anable_off)


                                    }
                                }
                            }.start()
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "please select a plug first", Toast.LENGTH_SHORT).show()
                }

            }
            plug_func_toggle.setOnClickListener {

                if (current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1]!="" ){


                    selectButton(plug_func_toggle)
                    if (status == "T"){
                        val selected_plug = plug_db.get_from_db_Plug(plugs_id?.get((current_pole.toInt() -1))?.toInt())
                        plugs_names?.set((current_pole.toInt())-1, selected_plug!!.mac.toString())
                        current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "plug")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "T")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()


                        requireActivity().runOnUiThread{
                            Thread{
                                val final_subtype = plug_db.getPlugsByMacAddress(selected_plug!!.mac).sortedBy { it!!.subtype }[0]!!.subtype
                                if (final_subtype=="0000"){
                                    selected_plug!!.subtype= (selected_plug!!.subtype!!.toInt()+1).toString()

                                }

                                val response = udp_six_worker(this,selected_plug?.mac,"0"+current_pole,"Plug","0000",state_coder(selected_plug?.subtype,"t"),selected_plug?.ip,current_six_worker)

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
                                    plugs_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "plug")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        plug_func_on.setBackgroundResource(R.drawable.functionality_on_anable_off)
                                        plug_func_off.setBackgroundResource(R.drawable.functionality_off_anable_off)
                                        plug_func_toggle.setBackgroundResource(R.drawable.functionality_toggle_anable_off)


                                    }
                                }
                            }.start()


                        }


                    }else{
                        val selected_plug = plug_db.get_from_db_Plug(plugs_id?.get((current_pole.toInt() -1))?.toInt())
                        plugs_names?.set((current_pole.toInt())-1, selected_plug!!.mac.toString())
                        current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                        types?.set((current_pole.toInt())-1, "plug")
                        current_six_worker.type =types!!.joinToString( separator = ",")
                        statuses?.set((current_pole.toInt())-1, "T")
                        current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                        println(current_six_worker.status)
                        Toast.makeText(requireContext(), "UPDATED...", Toast.LENGTH_SHORT).show()
                        Toast.makeText(requireContext(), "wait until device response", Toast.LENGTH_SHORT).show()

                        requireActivity().runOnUiThread{
                            Thread{
                                val final_subtype = plug_db.getPlugsByMacAddress(selected_plug!!.mac).sortedBy { it!!.subtype }[0]!!.subtype
                                if (final_subtype=="0000"){
                                    selected_plug!!.subtype= (selected_plug!!.subtype!!.toInt()+1).toString()

                                }
                                val response = udp_six_worker(this,selected_plug?.mac,"0"+current_pole,"Plug","0000",state_coder(selected_plug?.subtype,"t"),selected_plug?.ip,current_six_worker)

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
                                    plugs_names?.set((current_pole.toInt())-1, "")
                                    current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                                    types?.set((current_pole.toInt())-1, "")
                                    current_six_worker.type =types!!.joinToString( separator = ",")
                                    statuses?.set((current_pole.toInt())-1, "")
                                    current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                                    requireActivity().runOnUiThread{
                                        plug_func_on.setBackgroundResource(R.drawable.functionality_on_anable_off)
                                        plug_func_off.setBackgroundResource(R.drawable.functionality_off_anable_off)
                                        plug_func_toggle.setBackgroundResource(R.drawable.functionality_toggle_anable_off)


                                    }
                                }
                            }.start()

                        }

                    }
                }else{
                    Toast.makeText(requireContext(), "please select a plug first", Toast.LENGTH_SHORT).show()
                }
//            pagerChangeListener?.onPagerItemChanged(4)

            }

            sw_chose_plug_cancel.setOnClickListener {

                pagerChangeListener?.onPagerItemChanged(1)

            }
            sw_chose_plug_ok.setOnClickListener {

//            pagerChangeListener?.onPagerItemChanged(0)

            }
            sw_chose_plug.setOnClickListener {
                selectButton(sw_chose_plug)
                sw_chose_plug.setBackgroundResource(R.drawable.select_back_on)
                Handler().postDelayed({
                    sw_chose_plug.setBackgroundResource(R.drawable.select_back)
                }, 1000)

                var newlist = plug_db.getAllPlugs()
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
                val adapter = plug_add_scenario_Adapter(newlist) { selectedItem ->
                    println(selectedItem.Pname)
                    plugs_id?.set((current_pole.toInt())-1, selectedItem.id.toString())
                    plugs_names?.set((current_pole.toInt())-1, selectedItem.mac.toString())
                    println(plugs_id)
                    println("name is "+ plugs_names)
                    current_six_worker!!.sub_type=plugs_id!!.joinToString(separator = ",")
                    current_six_worker.work_name=plugs_names?.joinToString(separator = ",")
                    six_workert_db.updatesix_workertById(current_six_worker.id,current_six_worker)



                    popupWindow4.dismiss()
                    Toast.makeText(requireContext(), "${selectedItem.Pname} Setted for this item", Toast.LENGTH_SHORT).show()
                    sw_chose_plug.setText(selectedItem.Pname)


                }
                recyclerView.adapter = adapter
                adapter.setItems(newlist)




            }




        })
        
        
        
        
        
        
    }
    override fun onPause() {
        super.onPause()
         
    }
    override fun onDestroy() {
        super.onDestroy()
         
    }


}