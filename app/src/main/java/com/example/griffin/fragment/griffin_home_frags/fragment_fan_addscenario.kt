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
import com.example.griffin.adapters.fan_add_scenario_Adapter
import com.example.griffin.database.fan_db
import com.example.griffin.database.scenario_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.fan


class fragment_fan_addscenario : Fragment() {

    private  val SharedViewModel: SharedViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fan_addscenario, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.fans_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager

        val database= fan_db.getInstance(requireContext())

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val learn_fant_db= fan_db.getInstance(requireContext())

//            learn_fan_db.deleteRowsWithNullOrEmptyMac()
            SharedViewModel.update_fan_to_learn_list( learn_fant_db.getfansByRoomName(room!!.room_name))

            SharedViewModel.fan_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->
                val selected_fan_list = mutableListOf<fan>()


                SharedViewModel.current_scenario.observe(viewLifecycleOwner, Observer { current_scenarioo ->
                    val current_scenario = scenario_db.Scenario_db.getInstance(requireContext()).getScenarioById(current_scenarioo?.id)

                    if ( current_scenario!!.fan != ""){
                        val scenario_fan_list = current_scenario?.fan?.split(",")
                        println(scenario_fan_list)
                        if (scenario_fan_list != null ) {
                            for (item in scenario_fan_list ){
                                val listed_item = item.split("#")
                                if (listed_item.count() >0){

                                    println(listed_item)
                                    val fan = database.get_from_db_fan_By_name(listed_item[0])
                                    if (fan != null) {
                                        selected_fan_list.add(fan)
                                    }

                                    database.updateStatusbyId(fan!!.id , listed_item[1])

                                }

                            }


                        }

                    }

                    var names : MutableList<String> =mutableListOf()
                    for (item in selected_fan_list ){
                        item.Fname?.let { names.add(it) }


                    }


                    val adapter = fan_add_scenario_Adapter2(newlist,names) { selectedItem ->
//                    Toast.makeText(requireContext(), selectedItem.Lname, Toast.LENGTH_SHORT).show()
                        SharedViewModel.update_current_device(selectedItem)
                        if (selected_fan_list.any { it.Fname == selectedItem.Fname }){
                            val index = selected_fan_list.indexOfFirst { it.Fname == selectedItem.Fname }

                            selected_fan_list.removeAt(index)

                        }else{
                            selected_fan_list.add(selectedItem)
                        }
                        val activity =requireActivity() as griffin_home

                        val ok_button=view.findViewById<Button>(R.id.ok__scenario_btn)

                        ok_button.setOnClickListener {
                            val fan_db = fan_db.getInstance(requireContext())
                            activity.viewPager.currentItem=0
                            val scenario_database= scenario_db.Scenario_db.getInstance(requireContext())
                            var fan_scenario = scenario_database.getScenarioById(scenario_database.getMaxId())!!.fan
                            Toast.makeText(requireContext(), "Item Added", Toast.LENGTH_SHORT).show()

                            for (fan in selected_fan_list ){
                                if (fan_scenario == "") {
                                    fan_scenario= "${fan!!.Fname}#${fan_db.getStatusById(fan.id)}"

                                }else{
                                    val targetName = fan?.Fname ?: ""
//                                    light_scenario = light_scenario?.split(",")?.filterNot { it.startsWith(targetName) }?.joinToString { "," }

                                    val a = fan_scenario?.split(",")?.toMutableList()
                                    val index = a!!.indexOfFirst { it.startsWith(targetName) }
                                    if (index >=0){


                                        a.removeAt(index)
                                        fan_scenario = a.joinToString(",")
                                    }

                                    fan_scenario=fan_scenario + ",${fan!!.Fname}#${fan_db.getStatusById(fan.id)}"
                                }



                            }
                            val existingLightNames = selected_fan_list.map { it.Fname }.toSet()

// سپس لیست b را بررسی کرده و آیتم‌هایی که نام لایتشان در لیست a وجود ندارد را حذف می‌کنیم
                            val listed_scenario_fan = fan_scenario!!.split(",").toMutableList()
                            val iterator = listed_scenario_fan.iterator()
                            while (iterator.hasNext()) {
                                val item = iterator.next()
                                val lightName = item.substringBefore("#") // فرض بر این است که نام لایت قبل از "#" قرار دارد
                                if (!existingLightNames.contains(lightName.trim())) {
                                    iterator.remove() // اگر نام لایت در لیست a وجود ندارد، آن را از لیست b حذف کنید
                                }
                            }

                            fan_scenario=listed_scenario_fan.joinToString(",")

                            scenario_database.updateFanById(scenario_database.getMaxId(),fan_scenario)
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