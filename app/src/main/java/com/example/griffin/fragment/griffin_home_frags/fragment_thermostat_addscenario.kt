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
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.griffin.R
import com.example.griffin.adapters.temp_add_scenario_Adapter
import com.example.griffin.adapters.temp_add_scenario_Adapter2
import com.example.griffin.database.Temperature_db
import com.example.griffin.database.scenario_db
import com.example.griffin.database.setting_network_db
import com.example.griffin.griffin_home
import com.example.griffin.mudels.*
import kotlin.concurrent.thread


class fragment_thermostat_addscenario : Fragment() {

    private  val SharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thermostat_addscenario, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.Thermostst_recyclerview)
        val layoutManager = GridLayoutManager(requireContext(), 3) // تعداد ستون‌ها را 3 قرار دهید
        recyclerView.layoutManager = layoutManager

        val database= Temperature_db.getInstance(requireContext())

        SharedViewModel.current_room.observe(viewLifecycleOwner, Observer { room ->

            val learn_Thermostst_db= Temperature_db.getInstance(requireContext())

//            learn_Thermostst_db.deleteRowsWithNullOrEmptyMac()
            SharedViewModel.update_temp_to_learn_list( learn_Thermostst_db.getThermostatsByRoomName(room!!.room_name))

            SharedViewModel.temp_to_learn_list.observe(viewLifecycleOwner, Observer { newlist ->
                val selected_Thermostst_list = mutableListOf<Thermostst>()

                SharedViewModel.current_scenario.observe(viewLifecycleOwner, Observer { current_scenarioo ->
                    val current_scenario = scenario_db.Scenario_db.getInstance(requireContext()).getScenarioById(current_scenarioo?.id)







                    if ( current_scenario!!.thermostat != ""){
                        val scenario_Thermostst_list = current_scenario?.thermostat?.split(",")
                        println(scenario_Thermostst_list)
                        if (scenario_Thermostst_list != null ) {
                            for (item in scenario_Thermostst_list ){
                                val listed_item = item.split("#")
                                if (listed_item.count() >0){
                                    val formattedString = listed_item[1]
                                    val parts = formattedString.split("!", "$", "@")
                                    val onOff = parts[0]
                                    val temperature = parts[1]
                                    val mood = parts[2]
                                    val fanStatus = parts[3]

                                    println(listed_item)
                                    val Thermostst = database.getThermostatByName(listed_item[0])
                                    if (Thermostst != null) {
                                        selected_Thermostst_list.add(Thermostst)
                                    }

                                    database.updatehermostatById(Thermostst!!.id ,temperature,"22",mood,fanStatus, onOff)

                                }

                            }

                        }

                    }
                    val activity =requireActivity() as griffin_home

                    var names : MutableList<String> =mutableListOf()
                    for (item in selected_Thermostst_list ){
                        item.name?.let { names.add(it) }


                    }

                    val adapter = temp_add_scenario_Adapter2(newlist,names) { selectedItem ->
                        println(selectedItem.id)

//                    Toast.makeText(requireContext(), selectedItem.Lname, Toast.LENGTH_SHORT).show()




                        if (selected_Thermostst_list.any { it.name == selectedItem.name }){
                            val index = selected_Thermostst_list.indexOfFirst { it.name == selectedItem.name }

                            selected_Thermostst_list.removeAt(index)
                            println("removed ... ")

                        }else{
                            selected_Thermostst_list.add(selectedItem)
                            println("added ... ")
                        }




                        var temp_layout = view.findViewById<ConstraintLayout>(R.id.temp_layout)

                        val temp_db = Temperature_db.getInstance(requireContext())


                        var termostats_in_room=temp_db.getThermostatsByRoomName(room!!.room_name)

                        val coler_on_off = view.findViewById<CheckBox>(R.id.coler_on_off)
                        var current_temperature_textViwe=view.findViewById<TextView>(R.id.current_temperature)

                        val change_fan_status = view.findViewById<Button>(R.id.change_fan_status)
                        var fanstatus_num=0
                        val status1 = view.findViewById<ImageView>(R.id.status1)
                        val status2 = view.findViewById<ImageView>(R.id.status2)
                        val status3 = view.findViewById<ImageView>(R.id.status3)
                        val status4 = view.findViewById<TextView>(R.id.status4)
                        val circularSeekBar=view.findViewById<CircularSeekBar>(R.id.circularSeekBar)

                        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
                        val winterside = view.findViewById<RadioButton>(R.id.radioOption1)
                        val summerside = view.findViewById<RadioButton>(R.id.radioOption2)
                        val temp_disconnected = view.findViewById<TextView>(R.id.temp_disconnected)

                        var current_thermostat=temp_db.get_from_db_Temprature(selectedItem.id)
                        var current_seekbar_temp=current_thermostat!!.temperature
                        var on_off_status=current_thermostat.on_off
                        var current_temperature=current_thermostat.current_temperature
                        var fanstatus=current_thermostat.fan_status
                        var mood=current_thermostat.mood

                        var isRefreshing = false

// Inside your adapter click listener or wherever you call `refresh_thermostat`:


                        fun fanstatus1(){
                            fanstatus="1"
                            if (status1.isVisible){
                                null

                            }else{
                                status1.alpha = 0f
                                status1.visibility = View.VISIBLE

                                status1.animate()
                                    .alpha(1f)
                                    .setDuration(1000)
                                    .setListener(null)

                            }
                            if (status2.isVisible){
                                status2.alpha = 1f
                                status2.visibility = View.GONE

                                status2.animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .setListener(null)

                            }

                            if (status3.isVisible){

                                status3.alpha = 1f
                                status3.visibility = View.GONE

                                status4.animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .setListener(null)
                            }

                            if (status4.isVisible){
                                status4.alpha = 1f
                                status4.visibility = View.GONE

                                status4.animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .setListener(null)

                            }

                        }
                        fun fanstatus2() {
                            fanstatus="2"
                            if (status1.isVisible){
                                null

                            }else{
                                status1.alpha = 0f
                                status1.visibility = View.VISIBLE

                                status1.animate()
                                    .alpha(1f)
                                    .setDuration(1000)
                                    .setListener(null)

                            }
                            if (status2.isVisible){
                                null
                            }else{

                                status2.alpha = 0f
                                status2.visibility = View.VISIBLE

                                status2.animate()
                                    .alpha(1f)
                                    .setDuration(1000)
                                    .setListener(null)
                            }

                            if (status3.isVisible){
                                status3.alpha = 1f
                                status3.visibility = View.GONE

                                status3.animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .setListener(null)
                            }

                            if (status4.isVisible){
                                status4.alpha = 1f
                                status4.visibility = View.GONE

                                status4.animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .setListener(null)
                            }

                        }
                        fun fanstatus3(){
                            fanstatus="3"
                            if (status1.isVisible){
                                null

                            }else{
                                status1.alpha = 0f
                                status1.visibility = View.VISIBLE

                                status1.animate()
                                    .alpha(1f)
                                    .setDuration(1000)
                                    .setListener(null)

                            }
                            if (status2.isVisible){
                                null
                            }else{

                                status2.alpha = 0f
                                status2.visibility = View.VISIBLE

                                status2.animate()
                                    .alpha(1f)
                                    .setDuration(1000)
                                    .setListener(null)
                            }
                            if (status3.isVisible){
                                null
                            }else{

                                status3.alpha = 0f
                                status3.visibility = View.VISIBLE

                                status3.animate()
                                    .alpha(1f)
                                    .setDuration(1000)
                                    .setListener(null)

                            }

                            if (status4.isVisible){
                                status4.alpha = 1f
                                status4.visibility = View.GONE

                                status4.animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .setListener(null)
                            }
                        }
                        fun fanstatus4(){
                            fanstatus="0"
                            if (status1.isVisible){
                                status1.alpha = 1f
                                status1.visibility = View.GONE

                                status1.animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .setListener(null)
                            }
                            if (status2.isVisible){
                                status2.alpha = 1f
                                status2.visibility = View.GONE

                                status2.animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .setListener(null)


                            }

                            if (status3.isVisible){

                                status3.alpha = 1f
                                status3.visibility = View.GONE

                                status3.animate()
                                    .alpha(0f)
                                    .setDuration(1000)
                                    .setListener(null)

                            }


                            if (status4.isVisible){
                                null
                            }else{
                                status4.alpha = 0f
                                status4.visibility = View.VISIBLE

                                status4.animate()
                                    .alpha(1f)
                                    .setDuration(1000)
                                    .setListener(null)

                            }

                        }

                        fun update_statuses_ui(){
                            try {
                                var new_db=Temperature_db.getInstance(requireContext())
                                var new_s = new_db.get_from_db_Temprature(selectedItem.id)
                                on_off_status=new_s!!.on_off
                                fanstatus=new_s!!.fan_status
                                current_temperature=new_s!!.current_temperature!!.toInt().toString()
                                mood=new_s!!.mood
                                current_seekbar_temp=new_s!!.temperature

                                circularSeekBar.setProgress(new_s!!.temperature!!.toInt())

                                current_temperature_textViwe.setText(current_temperature)

                                if (mood=="0"){
                                    radioGroup.check(R.id.radioOption1)
                                    winterside.setBackgroundResource(R.drawable.winter_side_on);
                                    summerside.setBackgroundResource(R.drawable.summer_side_off);

                                }else{
                                    radioGroup.check(R.id.radioOption2)
                                    winterside.setBackgroundResource(R.drawable.winter_side_off);
                                    summerside.setBackgroundResource(R.drawable.summer_side_on);
                                }
                                if (fanstatus=="0"){
                                    fanstatus4()
                                    fanstatus_num=0
                                }else if (fanstatus=="1"){
                                    fanstatus1()
                                    fanstatus_num=1

                                }else if (fanstatus=="2"){
                                    fanstatus2()
                                    fanstatus_num=2

                                }else if (fanstatus=="3"){
                                    fanstatus3()
                                    fanstatus_num=3

                                }

                                if (on_off_status=="0"){
                                    coler_on_off.isChecked=false
                                    coler_on_off.setBackgroundResource(R.drawable.coler_off)
                                }else if (on_off_status=="1"){
                                    coler_on_off.isChecked=true
                                    coler_on_off.setBackgroundResource(R.drawable.coler_on)
                                }



                                temp_disconnected.visibility=View.GONE

                                temp_layout.alpha = 0f
                                temp_layout.visibility = View.VISIBLE

                                temp_layout.animate()
                                    .alpha(1f)
                                    .setDuration(1200)
                                    .setListener(null)
                            }catch (e:Exception){
                                println(e)

                            }




                        }












                        update_statuses_ui()

                        fun set_all_status(newMood:String?,newFanStatus:String?,newOnOffStatus:String?,newCurrentSeekbarTemp:String?){
                            newMood?.let { mood = it }
                            newFanStatus?.let { fanstatus = it }
                            newOnOffStatus?.let { on_off_status = it }
                            newCurrentSeekbarTemp?.let { current_seekbar_temp = it }

                        }

                        //                mood= termostats_in_room[0].mood.toString()
                        //                fanstatus= termostats_in_room[0].fan_status.toString()
                        //                on_off_status= termostats_in_room[0].on_off.toString()




                        current_seekbar_temp=circularSeekBar.currentSelectedIndex


                        fun SeekBarStatus():String{
                            return circularSeekBar.currentSelectedIndex
                        }


                        radioGroup.setOnCheckedChangeListener { group, checkedId ->
                            if (checkedId == R.id.radioOption1) {
                                println("mod  changed 0 ")
                                // اگر گزینه 1 انتخاب شد

                                // تغییر تصویر زمینه به تصویر مرتبط با گزینه 1
                                winterside.setBackgroundResource(R.drawable.winter_side_on);
                                summerside.setBackgroundResource(R.drawable.summer_side_off);
                                mood="0"
                            } else if (checkedId == R.id.radioOption2) {
                                println("mod  changed 1 ")
                                // اگر گزینه 2 انتخاب شد
                                // تغییر تصویر زمینه به تصویر مرتبط با گزینه 2
                                mood="1"
                                summerside.setBackgroundResource(R.drawable.summer_side_on);
                                winterside.setBackgroundResource(R.drawable.winter_side_off);
                            }
                            learn_Thermostst_db.updatehermostatById(selectedItem.id,current_seekbar_temp,"22",mood,fanstatus,on_off_status)

                        }
                        coler_on_off.setOnClickListener {
                            if (coler_on_off.isChecked){
                                coler_on_off.setBackgroundResource(R.drawable.coler_on)
                                on_off_status="1"

                            }else{
                                coler_on_off.setBackgroundResource(R.drawable.coler_off)
                                on_off_status="0"


                            }
                            learn_Thermostst_db.updatehermostatById(selectedItem.id,current_seekbar_temp,"22",mood,fanstatus,on_off_status)

                        }

                        change_fan_status.setOnClickListener {

                            when(fanstatus_num ){
                                0-> {
                                    fanstatus1()
                                    fanstatus_num+=1
                                }
                                1 ->{
                                    fanstatus2()
                                    fanstatus_num+=1
                                }
                                2->{
                                    fanstatus3()
                                    fanstatus_num+=1
                                }
                                3->{
                                    fanstatus4()

                                    fanstatus_num=0
                                }
                            }
                            learn_Thermostst_db.updatehermostatById(selectedItem.id,current_seekbar_temp,"22",mood,fanstatus,on_off_status)

                        }

                        circularSeekBar.setOnCircularSeekBarChangeListener(object :
                            CircularSeekBar.OnCircularSeekBarChangeListener {
                            override fun onProgressChanged(seekBar: CircularSeekBar?, progress: Int, fromUser: Boolean) {

                            }

                            override fun onStartTrackingTouch(seekBar: CircularSeekBar?) {
                                // وقتی کاربر شروع به جابجایی سیکبار می‌کند، این متد فراخوانی می‌شود.
                            }

                            override fun onStopTrackingTouch(seekBar: CircularSeekBar?) {
                                // وقتی کاربر پایان دادن به جابجایی سیکبار می‌دهد، این متد فراخوانی می‌شود.
                                current_seekbar_temp=circularSeekBar.currentSelectedIndex
                                learn_Thermostst_db.updatehermostatById(selectedItem.id,current_seekbar_temp,"22",mood,fanstatus,on_off_status)


                            }
                        })

                        val ok_button=view.findViewById<Button>(R.id.ok__scenario_btn)
                        ok_button.setOnClickListener {
                            println(selected_Thermostst_list)
                            val temp_db = Temperature_db.getInstance(requireContext())
                            activity.viewPager.currentItem=0
                            val scenario_database= scenario_db.Scenario_db.getInstance(requireContext())
                            var Thermostst_scenario = scenario_database.getScenarioById(scenario_database.getMaxId())!!.thermostat
                            Toast.makeText(requireContext(), "Item Added", Toast.LENGTH_SHORT).show()

                            for (Thermostst in selected_Thermostst_list ){
                                println(Thermostst.name)
                                if (Thermostst_scenario == "") {
                                    Thermostst_scenario= "${temp_db.get_from_db_Temprature(Thermostst.id)!!.name}#${temp_db.get_from_db_Temprature(Thermostst.id)!!.on_off}!${temp_db.get_from_db_Temprature(Thermostst.id)!!.temperature}$${temp_db.get_from_db_Temprature(Thermostst.id)!!.mood}@${temp_db.get_from_db_Temprature(Thermostst.id)!!.fan_status}"

                                }else{
                                    val targetName = Thermostst?.name ?: ""
//                                    light_scenario = light_scenario?.split(",")?.filterNot { it.startsWith(targetName) }?.joinToString { "," }

                                    val a = Thermostst_scenario?.split(",")?.toMutableList()
                                    val index = a!!.indexOfFirst { it.startsWith(targetName) }
                                    if (index >=0){


                                        a.removeAt(index)
                                        Thermostst_scenario = a.joinToString(",")
                                    }


                                    Thermostst_scenario=Thermostst_scenario + ",${temp_db.get_from_db_Temprature(Thermostst.id)!!.name}#${temp_db.get_from_db_Temprature(Thermostst.id)!!.on_off}!${temp_db.get_from_db_Temprature(Thermostst.id)!!.temperature}$${temp_db.get_from_db_Temprature(Thermostst.id)!!.mood}@${temp_db.get_from_db_Temprature(Thermostst.id)!!.fan_status}"
                                }



                            }

                            val existingLightNames = selected_Thermostst_list.map { it.name }.toSet()

// سپس لیست b را بررسی کرده و آیتم‌هایی که نام لایتشان در لیست a وجود ندارد را حذف می‌کنیم
                            val listed_scenario_light = Thermostst_scenario!!.split(",").toMutableList()
                            val iterator = listed_scenario_light.iterator()
                            while (iterator.hasNext()) {
                                val item = iterator.next()
                                val lightName = item.substringBefore("#") // فرض بر این است که نام لایت قبل از "#" قرار دارد
                                if (!existingLightNames.contains(lightName.trim())) {
                                    iterator.remove() // اگر نام لایت در لیست a وجود ندارد، آن را از لیست b حذف کنید
                                }
                            }

                            Thermostst_scenario=listed_scenario_light.joinToString(",")

                            scenario_database.updateThermostatById(scenario_database.getMaxId(),Thermostst_scenario)

                            SharedViewModel.update_current_device("done")

                        }
                        try {


                        }catch (e:Exception){
                            println(e)
                        }
//                    println(selectedItem.id)


















                    }

                    recyclerView.adapter = adapter
                    adapter.setItems(newlist)




                })



            })




        })


    }


}