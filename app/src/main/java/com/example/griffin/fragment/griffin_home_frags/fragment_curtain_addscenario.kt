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
import com.example.griffin.adapters.curtain_add_scenario_Adapter
import com.example.griffin.adapters.curtain_add_scenario_Adapter2
import com.example.griffin.database.curtain_db
import com.example.griffin.database.scenario_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.curtain
import com.example.griffin.mudels.SharedViewModel


class fragment_curtain_addscenario : Fragment() {

    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_curtain_addscenario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.curtain_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager

        val database= curtain_db.getInstance(requireContext())

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val learn_curtain_db= curtain_db.getInstance(requireContext())

//            learn_curtain_db.deleteRowsWithNullOrEmptyMac()
            SharedViewModel.update_curtain_to_learn_list( learn_curtain_db.getAllcurtainsByRoomName(room!!.room_name))

            SharedViewModel.curtain_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->
                val selected_curtain_list = mutableListOf<curtain>()

                SharedViewModel.current_scenario.observe(viewLifecycleOwner, Observer { current_scenarioo ->

                    val current_scenario = scenario_db.Scenario_db.getInstance(requireContext()).getScenarioById(current_scenarioo?.id)

                    if ( current_scenario!!.curtain != ""){
                        val scenario_curtain_list = current_scenario?.curtain?.split(",")
                        println(scenario_curtain_list)
                        if (scenario_curtain_list != null ) {
                            for (item in scenario_curtain_list ){
                                val listed_item = item.split("#")
                                if (listed_item.count() >0){

                                    println(listed_item)
                                    val curtain = database.getCurtainByCname(listed_item[0])
                                    if (curtain != null) {
                                        selected_curtain_list.add(curtain)
                                    }

                                    database.updateStatusById(curtain!!.id , listed_item[1])

                                }

                            }

                        }

                    }

                    var names : MutableList<String> =mutableListOf()
                    for (item in selected_curtain_list ){
                        item.Cname?.let { names.add(it) }


                    }

                    val adapter = curtain_add_scenario_Adapter2(newlist , names) { selectedItem ->
//                    Toast.makeText(requireContext(), selectedItem.Lname, Toast.LENGTH_SHORT).show()

                        SharedViewModel.update_current_device(selectedItem)
                        if (selected_curtain_list.any { it.Cname == selectedItem.Cname }){
                            val index = selected_curtain_list.indexOfFirst { it.Cname == selectedItem.Cname }

                            selected_curtain_list.removeAt(index)

                        }else{
                            selected_curtain_list.add(selectedItem)
                        }


                        val ok_button=view.findViewById<Button>(R.id.ok__scenario_btn)
                        val activity =requireActivity() as griffin_home
                        ok_button.setOnClickListener {
                            val curtain_db = curtain_db.getInstance(requireContext())
                            activity.viewPager.currentItem=0
                            Toast.makeText(requireContext(), "Item Added", Toast.LENGTH_SHORT).show()
                            val scenario_database= scenario_db.Scenario_db.getInstance(requireContext())
                            var curtain_scenario = scenario_database.getScenarioById(scenario_database.getMaxId())!!.curtain


                            for (curtain in selected_curtain_list ){
                                if (curtain_scenario == "") {
                                    curtain_scenario= "${curtain!!.Cname}#${curtain_db.getStatusById(curtain.id)}"

                                }else{


//                                    // فیلتر کردن لیست برای حذف آیتم‌هایی که با targetName مطابقت دارند
                                    val targetName = curtain?.Cname ?: ""
//                                    light_scenario = light_scenario?.split(",")?.filterNot { it.startsWith(targetName) }?.joinToString { "," }

                                    val a = curtain_scenario?.split(",")?.toMutableList()
                                    val index = a!!.indexOfFirst { it.startsWith(targetName) }
                                    if (index >=0){


                                        a.removeAt(index)
                                        curtain_scenario = a.joinToString(",")
                                    }

                                    curtain_scenario=curtain_scenario + ",${curtain!!.Cname}#${curtain_db.getStatusById(curtain.id)}"
                                }



                            }
                            val existingLightNames = selected_curtain_list.map { it.Cname }.toSet()

// سپس لیست b را بررسی کرده و آیتم‌هایی که نام لایتشان در لیست a وجود ندارد را حذف می‌کنیم
                            val listed_scenario_light = curtain_scenario!!.split(",").toMutableList()
                            val iterator = listed_scenario_light.iterator()
                            while (iterator.hasNext()) {
                                val item = iterator.next()
                                val lightName = item.substringBefore("#") // فرض بر این است که نام لایت قبل از "#" قرار دارد
                                if (!existingLightNames.contains(lightName.trim())) {
                                    iterator.remove() // اگر نام لایت در لیست a وجود ندارد، آن را از لیست b حذف کنید
                                }
                            }

                            curtain_scenario=listed_scenario_light.joinToString(",")

                            scenario_database.updateCurtainById(scenario_database.getMaxId(),curtain_scenario)
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