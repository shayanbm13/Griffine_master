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
import com.example.griffin.adapters.plug_add_scenario_Adapter
import com.example.griffin.adapters.plug_add_scenario_Adapter2
import com.example.griffin.database.plug_db

import com.example.griffin.database.scenario_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.Plug

import com.example.griffin.mudels.SharedViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_plug_addscenario.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_plug_addscenario : Fragment() {
    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plug_addscenario, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.plugs_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager

        val database= plug_db.getInstance(requireContext())

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val learn_plugt_db= plug_db.getInstance(requireContext())

//            learn_plug_db.deleteRowsWithNullOrEmptyMac()
            SharedViewModel.update_plug_to_learn_list( learn_plugt_db.getPlugsByRoomName(room!!.room_name))

            SharedViewModel.plug_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->
                val selected_plug_list = mutableListOf<Plug>()

                SharedViewModel.current_scenario.observe(viewLifecycleOwner, Observer { current_scenarioo ->
                    val current_scenario = scenario_db.Scenario_db.getInstance(requireContext()).getScenarioById(current_scenarioo?.id)
                    if ( current_scenario!!.plug != ""){
                        val scenario_plug_list = current_scenario?.plug?.split(",")

                        if (scenario_plug_list != null ) {
                            for (item in scenario_plug_list ){
                                val listed_item = item.split("#")
                                if (listed_item.count() >0){


                                    val plug = database.getPlugByCname(listed_item[0])
                                    if (plug != null) {
                                        selected_plug_list.add(plug)
                                    }

                                    database.updateStatusbyId(plug!!.id , listed_item[1])

                                }

                            }


                        }

                    }
                    var names : MutableList<String> =mutableListOf()
                    for (item in selected_plug_list ){
                        item.Pname?.let { names.add(it) }


                    }


                    val activity =requireActivity() as griffin_home


                    val adapter = plug_add_scenario_Adapter2(newlist,names) { selectedItem ->
//                    Toast.makeText(requireContext(), selectedItem.Lname, Toast.LENGTH_SHORT).show()
                        SharedViewModel.update_current_device(selectedItem)

                        if (selected_plug_list.any { it.Pname == selectedItem.Pname }){
                            val index = selected_plug_list.indexOfFirst { it.Pname == selectedItem.Pname }

                            println("ghabl   " +selected_plug_list )
                            selected_plug_list.removeAt(index)
                            println("baad   " +selected_plug_list )

                        }else{
                            selected_plug_list.add(selectedItem)
                        }


                        val ok_button=view.findViewById<Button>(R.id.ok__scenario_btn)

                        ok_button.setOnClickListener {
                            activity.viewPager.currentItem=0
                            val plug_db_handler = plug_db.getInstance(requireContext())
                            val scenario_database= scenario_db.Scenario_db.getInstance(requireContext())
                            var plug_scenario = scenario_database.getScenarioById(scenario_database.getMaxId())!!.plug
                            Toast.makeText(requireContext(), "Item Added", Toast.LENGTH_SHORT).show()

                            for (plug in selected_plug_list ){
                                if (plug_scenario == "") {
                                    plug_scenario= "${plug!!.Pname}#${plug_db_handler.getPlugByCname(plug.Pname)?.status}"

                                }else{
                                    val targetName = plug?.Pname ?: ""
//                                    light_scenario = light_scenario?.split(",")?.filterNot { it.startsWith(targetName) }?.joinToString { "," }

                                    val a = plug_scenario?.split(",")?.toMutableList()
                                    val index = a!!.indexOfFirst { it.startsWith(targetName) }
                                    if (index >=0){


                                        a.removeAt(index)
                                        plug_scenario = a.joinToString(",")
                                    }


                                    plug_scenario=plug_scenario + ",${plug!!.Pname}#${plug_db_handler.getPlugByCname(plug.Pname)?.status}"
                                }



                            }

                            val existingLightNames = selected_plug_list.map { it.Pname }.toSet()

// سپس لیست b را بررسی کرده و آیتم‌هایی که نام لایتشان در لیست a وجود ندارد را حذف می‌کنیم

                            val listed_scenario_plug = plug_scenario!!.split(",").toMutableList()
                            val iterator = listed_scenario_plug.iterator()
                            while (iterator.hasNext()) {
                                val item = iterator.next()
                                val lightName = item.substringBefore("#") // فرض بر این است که نام لایت قبل از "#" قرار دارد
                                if (!existingLightNames.contains(lightName.trim())) {
                                    iterator.remove() // اگر نام لایت در لیست a وجود ندارد، آن را از لیست b حذف کنید
                                }
                            }
                            plug_scenario=listed_scenario_plug.joinToString(",")
                            scenario_database.updatePlugById(scenario_database.getMaxId(),plug_scenario)

                            println(plug_scenario)
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