package com.example.griffin.fragment.setting_four.choose_lolcal_work


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
import com.example.griffin.adapters.ScenarioAdapter
import com.example.griffin.database.scenario_db
import com.example.griffin.database.six_workert_db
import com.example.griffin.mudels.PagerChangeListener
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.udp_six_worker


class select_work_scenario : Fragment() {

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

        return inflater.inflate(R.layout.fragment_select_work_scenario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }

    override fun onResume() {
        super.onResume()

        val sw_chose_scenario = requireView().findViewById<Button>(R.id.sw_chose_scenario)

        val sw_chose_scenario_cancel: Button = requireView().findViewById(R.id.sw_chose_scenario_cancel)
        val sw_chose_scenario_ok: Button = requireView().findViewById(R.id.sw_chose_scenario_ok)

        val scenario_db = scenario_db.Scenario_db.getInstance(requireContext())



        val pagerChangeListener = parentFragment as? PagerChangeListener



        val  six_workert_db = six_workert_db.getInstance(requireContext())



        sharedViewModel.current_pole_six_wirker.observe(viewLifecycleOwner, Observer { current ->
            val current_id_pole=current!!.split(",")
            val current_id =current_id_pole[0]
            val current_pole =current_id_pole[1]
            val current_six_worker = six_workert_db.get_from_db_six_workert(current_id.toInt())
            try {
                if (current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1].toString() !=""){

                    val current_curtain = scenario_db.getScenarioById(current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1].toInt())
                    sw_chose_scenario.setText(current_curtain!!.scenario_name)

                }else{
                    sw_chose_scenario.setText("select")

                }
            }catch (e:Exception){
                println(e)
                sw_chose_scenario.setText("select")
            }

            println(current_six_worker?.work_name)
            var statuses= try {
                current_six_worker!!.status!!.split(",").toMutableList()

            }catch (e:Exception){
                null

            }


            val status = statuses?.get(((current_pole.toInt())-1))

            var scenarios_id= try {
                current_six_worker!!.sub_type!!.split(",").toMutableList()

            }catch (e:Exception){
                null
            }
            var types=try {

                current_six_worker!!.type!!.split(",").toMutableList()
            }catch (e:Exception){
                null
            }
//            val scenario_id = scenarios_id?.get(((current_pole.toInt())-1))

            var scenarios_names=try {

                current_six_worker!!.work_name!!.split(",").toMutableList()
            }catch (e:Exception){
                null
            }
            println(scenarios_names?.get(current_pole.toInt() -1))
            if (scenarios_names?.get(current_pole.toInt() - 1) != null && scenarios_names[current_pole.toInt() - 1] != "") {

                try {
                    if (current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1].toString() !=""){

                        val current_curtain = scenario_db.getScenarioById(current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1].toInt())
                        sw_chose_scenario.setText(current_curtain!!.scenario_name)

                    }else{
                        sw_chose_scenario.setText("select")

                    }
                }catch (e:Exception){
                    println(e)
                    sw_chose_scenario.setText("select")
                }
            } else {
                sw_chose_scenario.setText("Select")
            }


//            val scenario_name = scenarios_names?.get(((current_pole.toInt())-1))

            try {
                if (current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1].toString() !=""){

                    val current_curtain = scenario_db.getScenarioById(current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1].toInt())
                    sw_chose_scenario.setText(current_curtain!!.scenario_name)

                }else{
                    sw_chose_scenario.setText("select")

                }
            }catch (e:Exception){
                println(e)
                sw_chose_scenario.setText("select")
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



            sw_chose_scenario_cancel.setOnClickListener {

                pagerChangeListener?.onPagerItemChanged(1)

            }
            sw_chose_scenario_ok.setOnClickListener {

//            pagerChangeListener?.onPagerItemChanged(0)

            }
            sw_chose_scenario.setOnClickListener {

                sw_chose_scenario.setBackgroundResource(R.drawable.select_back_on)
                Handler().postDelayed({
                    sw_chose_scenario.setBackgroundResource(R.drawable.select_back)
                }, 1000)

                var newlist = scenario_db.getAllScenario()
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
                val adapter = ScenarioAdapter(newlist) { selectedItem ->
                    println(selectedItem.scenario_name)
                    types?.set((current_pole.toInt())-1, "scenario")
                    current_six_worker!!.type =types!!.joinToString( separator = ",")
                    scenarios_id?.set((current_pole.toInt())-1, selectedItem.id.toString())
                    println(scenarios_id)
                    println("name is "+ scenarios_names)
                    current_six_worker!!.sub_type=scenarios_id!!.joinToString(separator = ",")
                    scenarios_names?.set((current_pole.toInt())-1, selectedItem.id.toString())
                    current_six_worker.work_name=scenarios_names?.joinToString(separator = ",")
                    six_workert_db.updatesix_workertById(current_six_worker.id,current_six_worker)



                    popupWindow4.dismiss()
                    Toast.makeText(requireContext(), "${selectedItem.scenario_name} Setted for this item", Toast.LENGTH_SHORT).show()
                    Thread{
                        val response =udp_six_worker(this,"--",current_pole,"scenario","--","--","--",current_six_worker)

                        if (response){
                            requireActivity().runOnUiThread{

                                Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                            }

                        }else if(current_six_worker.pole_num!!.contains("-")){
                            scenarios_id?.set((current_pole.toInt())-1, selectedItem.id.toString())
                            scenarios_names?.set((current_pole.toInt())-1, selectedItem.id.toString())
                            println(scenarios_id)
                            println("name is "+ scenarios_names)
                            current_six_worker!!.sub_type=scenarios_id!!.joinToString(separator = ",")
                            current_six_worker.work_name=scenarios_names?.joinToString(separator = ",")
                            six_workert_db.updatesix_workertById(current_six_worker.id,current_six_worker)
                            requireActivity().runOnUiThread(){

                                Toast.makeText(requireContext(), "Done...", Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            requireActivity().runOnUiThread{

                                Toast.makeText(requireContext(), "Failed click again...", Toast.LENGTH_SHORT).show()
                            }
                            scenarios_names?.set((current_pole.toInt())-1, "")
                            current_six_worker.work_name=scenarios_names?.joinToString(separator = ",")
                            types?.set((current_pole.toInt())-1, "")
                            current_six_worker!!.type =types!!.joinToString( separator = ",")
                            statuses?.set((current_pole.toInt())-1, "")
                            current_six_worker!!.status=statuses!!.joinToString(separator = ",")
                            six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                            requireActivity().runOnUiThread{

                                sw_chose_scenario.setText("")

                            }
                        }
                        requireActivity().runOnUiThread {

                            try {
                                if (current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1].toString() !=""){

                                    val current_curtain = scenario_db.getScenarioById(current_six_worker?.work_name?.split(",")!!.toMutableList()[(current_pole.toInt())-1].toInt())
                                    sw_chose_scenario.setText(current_curtain!!.scenario_name)

                                }else{
                                    sw_chose_scenario.setText("select")

                                }
                            }catch (e:Exception){
                                println(e)
                                sw_chose_scenario.setText("select")
                            }
                        }

                    }.start()



                }
                recyclerView.adapter = adapter
                adapter.setItems(newlist)




            }




        })



    }


}