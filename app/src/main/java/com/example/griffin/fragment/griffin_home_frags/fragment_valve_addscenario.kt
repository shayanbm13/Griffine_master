package com.example.griffin.fragment.griffin_home_frags

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.griffin.R
import com.example.griffin.adapters.valve_add_scenario_Adapter
import com.example.griffin.adapters.valve_add_scenario_Adapter2
import com.example.griffin.database.valve_db
import com.example.griffin.database.scenario_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.valve
import com.example.griffin.mudels.SharedViewModel


class fragment_valve_addscenario : Fragment() {

    private  val SharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_valve_addscenario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.valve_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager

        val database= valve_db.getInstance(requireContext())

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val learn_valvet_db= valve_db.getInstance(requireContext())

//            learn_valve_db.deleteRowsWithNullOrEmptyMac()
            SharedViewModel.update_valve_to_learn_list( learn_valvet_db.getvalvesByRoomName(room!!.room_name))

            SharedViewModel.valve_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->
                val selected_valve_list = mutableListOf<valve>()

                SharedViewModel.current_scenario.observe(viewLifecycleOwner, Observer { current_scenarioo ->
                    val current_scenario = scenario_db.Scenario_db.getInstance(requireContext()).getScenarioById(current_scenarioo?.id)

                    if ( current_scenario!!.valve != ""){
                        val scenario_valve_list = current_scenario?.valve?.split(",")
                        println(scenario_valve_list)
                        if (scenario_valve_list != null ) {
                            for (item in scenario_valve_list ){
                                val listed_item = item.split("#")
                                if (listed_item.count() >0){

                                    println(listed_item)
                                    val valve = database.getvalveByCname(listed_item[0])
                                    if (valve != null) {
                                        selected_valve_list.add(valve)
                                    }

                                    database.updateStatusbyId(valve!!.id , listed_item[1])

                                }

                            }
                        }

                    }

                    var names : MutableList<String> =mutableListOf()
                    for (item in selected_valve_list ){
                        item.Vname?.let { names.add(it) }


                    }

                    val activity =requireActivity() as griffin_home

                    val adapter = valve_add_scenario_Adapter2(newlist, names) { selectedItem ->
//                    Toast.makeText(requireContext(), selectedItem.Lname, Toast.LENGTH_SHORT).show()

                        SharedViewModel.update_current_device(selectedItem)
                        if (selected_valve_list.any { it.Vname == selectedItem.Vname }){
                            val index = selected_valve_list.indexOfFirst { it.Vname == selectedItem.Vname }

                            selected_valve_list.removeAt(index)

                        }else{
                            selected_valve_list.add(selectedItem)
                        }


                        val ok_button=view.findViewById<Button>(R.id.ok__scenario_btn)

                        ok_button.setOnClickListener {
                            val valve_db = valve_db.getInstance(requireContext())
                            activity.viewPager.currentItem=0
                            val scenario_database= scenario_db.Scenario_db.getInstance(requireContext())
                            var valve_scenario = scenario_database.getScenarioById(scenario_database.getMaxId())!!.valve
                            Toast.makeText(requireContext(), "Item Added", Toast.LENGTH_SHORT).show()

                            for (valve in selected_valve_list ){
                                if (valve_scenario == "") {
                                    valve_scenario= "${valve!!.Vname}#${valve_db.get_from_db_valve(valve.id)!!.status}"

                                }else{
                                    val targetName = valve?.Vname ?: ""
//                                    light_scenario = light_scenario?.split(",")?.filterNot { it.startsWith(targetName) }?.joinToString { "," }

                                    val a = valve_scenario?.split(",")?.toMutableList()
                                    val index = a!!.indexOfFirst { it.startsWith(targetName) }
                                    if (index >=0){


                                        a.removeAt(index)
                                        valve_scenario = a.joinToString(",")
                                    }
                                    valve_scenario=valve_scenario + ",${valve!!.Vname}#${valve_db.get_from_db_valve(valve.id)!!.status}"
                                }



                            }
                            val existingLightNames = selected_valve_list.map { it.Vname }.toSet()

// سپس لیست b را بررسی کرده و آیتم‌هایی که نام لایتشان در لیست a وجود ندارد را حذف می‌کنیم
                            val listed_scenario_valve = valve_scenario!!.split(",").toMutableList()
                            val iterator = listed_scenario_valve.iterator()
                            while (iterator.hasNext()) {
                                val item = iterator.next()
                                val lightName = item.substringBefore("#") // فرض بر این است که نام لایت قبل از "#" قرار دارد
                                if (!existingLightNames.contains(lightName.trim())) {
                                    iterator.remove() // اگر نام لایت در لیست a وجود ندارد، آن را از لیست b حذف کنید
                                }
                            }

                            valve_scenario=listed_scenario_valve.joinToString(",")

                            scenario_database.updateValveById(scenario_database.getMaxId(),valve_scenario)

                            SharedViewModel.update_current_device("done")
                        }





                    }

                    recyclerView.adapter = adapter
                    adapter.setItems(newlist)

                })


            })




        })




    }

}