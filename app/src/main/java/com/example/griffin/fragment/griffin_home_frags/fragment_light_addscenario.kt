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
import com.example.griffin.R
import com.example.griffin.adapters.light_add_scenario_Adapter2
import com.example.griffin.database.light_db
import com.example.griffin.database.scenario_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.Light
import com.example.griffin.mudels.SharedViewModel

class fragment_light_addscenario : Fragment() {
    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_light_addscenario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.lights_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager

        val database= light_db.getInstance(requireContext())

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val learn_light_db= light_db.getInstance(requireContext())

//            learn_light_db.deleteRowsWithNullOrEmptyMac()
            SharedViewModel.update_light_to_learn_list( learn_light_db.getAllLightsByRoomName(room!!.room_name))

            SharedViewModel.light_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->
                val selected_light_list = mutableListOf<Light>()



                SharedViewModel.current_scenario.observe(viewLifecycleOwner, Observer { current_scenarioo ->
                    val current_scenario = scenario_db.Scenario_db.getInstance(requireContext()).getScenarioById(current_scenarioo?.id)
                    println("     "+current_scenario)
                    println("     "+current_scenario?.light)
                    if ( current_scenario!!.light != ""){
                        val scenario_light_list = current_scenario?.light?.split(",")
                        println(scenario_light_list)
                        if (scenario_light_list != null ) {
                            for (item in scenario_light_list ){
                                val listed_item = item.split("#")
                                if (listed_item.count() >0){

                                    println(listed_item)
                                    val light = database.getLightsByLname(listed_item[0])
                                    selected_light_list.add(light)

                                    database.updateStatusById(light.id , listed_item[1])

                                }

                            }
//                            val new_newlist = newlist
//                            for (item in selected_light_list){
//
//                                if (item in newlist){
//                                    val index = newlist.indexOf(item)
//                                    new_newlist[index]
//                                }
//                            }

                        }

                    }

                    var names : MutableList<String> =mutableListOf()
                    for (item in selected_light_list ){
                        item.Lname?.let { names.add(it) }


                    }


                    val adapter = light_add_scenario_Adapter2(newlist,names) { selectedItem ->
//                    Toast.makeText(requireContext(), selectedItem.Lname, Toast.LENGTH_SHORT).show()
                        SharedViewModel.update_current_device(selectedItem)


                        if (selected_light_list.any { it.Lname == selectedItem.Lname }){
                            val index = selected_light_list.indexOfFirst { it.Lname == selectedItem.Lname }

                            selected_light_list.removeAt(index)

                        }else{
                            selected_light_list.add(selectedItem)
                        }

                        val activity =requireActivity() as griffin_home
                        val ok_button=view.findViewById<Button>(R.id.ok__scenario_btn)

                        ok_button.setOnClickListener {
                            val light_db_handler = light_db.getInstance(requireContext())
                            activity.viewPager.currentItem=0
                            val scenario_database=scenario_db.Scenario_db.getInstance(requireContext())
                            var light_scenario = scenario_database.getScenarioById(scenario_database.getMaxId())!!.light
//                            val listed_light_scenario =light_scenario?.split(",")
                            Toast.makeText(requireContext(), "Item Added", Toast.LENGTH_SHORT).show()

                            for (light in selected_light_list ){

                                if (light_scenario == "") {
                                    light_scenario= "${light!!.Lname}#${light_db_handler.getLightsByLname(light.Lname).status}"

                                }else{


//                                    // فیلتر کردن لیست برای حذف آیتم‌هایی که با targetName مطابقت دارند
                                    val targetName = light?.Lname ?: ""
//                                    light_scenario = light_scenario?.split(",")?.filterNot { it.startsWith(targetName) }?.joinToString { "," }

                                    val a = light_scenario?.split(",")?.toMutableList()
                                    val index = a!!.indexOfFirst { it.startsWith(targetName) }
                                    if (index >=0){


                                        a.removeAt(index)
                                        light_scenario = a.joinToString(",")
                                    }



                                    light_scenario=light_scenario + ",${light!!.Lname}#${light_db_handler.getLightsByLname(light.Lname).status}"



                                }



                            }

                            val existingLightNames = selected_light_list.map { it.Lname }.toSet()

// سپس لیست b را بررسی کرده و آیتم‌هایی که نام لایتشان در لیست a وجود ندارد را حذف می‌کنیم
                            val listed_scenario_light = light_scenario!!.split(",").toMutableList()
                            val iterator = listed_scenario_light.iterator()
                            while (iterator.hasNext()) {
                                val item = iterator.next()
                                val lightName = item.substringBefore("#") // فرض بر این است که نام لایت قبل از "#" قرار دارد
                                if (!existingLightNames.contains(lightName.trim())) {
                                    iterator.remove() // اگر نام لایت در لیست a وجود ندارد، آن را از لیست b حذف کنید
                                }
                            }

                            light_scenario=listed_scenario_light.joinToString(",")

                            scenario_database.updateLightById(current_scenario?.id,light_scenario)

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