package com.example.griffin.fragment.setting_four

import android.app.AlertDialog
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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.griffin.R
import com.example.griffin.adapters.FragmentPagerAdapter_select_work
import com.example.griffin.database.six_workert_db
import com.example.griffin.mudels.PagerChangeListener
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.receiveUdpMessage
import com.example.griffin.mudels.send_to_six_worker_scenario


class select_work_local : Fragment() {
     
    val sharedViewModel : SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_work, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



//        ok.setOnClickListener {
//
////            pagerChangeListener?.onPagerItemChanged(0)
//
//        }





    }

    override fun onResume() {
        super.onResume()

        val scenario_choosed_work = requireView().findViewById<Button>(R.id.scenario_choosed_work)
        val light_choosed_work: Button = requireView().findViewById(R.id.light_choosed_work)
        val plug_choosed_work: Button = requireView().findViewById(R.id.plug_choosed_work)
        val curtain_choosed_work: Button = requireView().findViewById(R.id.curtain_choosed_work)
        val cancel: Button = requireView().findViewById(R.id.cancel_choosed_work)



        val  six_workert_db = six_workert_db.getInstance(requireContext())

         
        val chooser_btns_list = listOf(
            scenario_choosed_work,
            light_choosed_work,
            plug_choosed_work,
            curtain_choosed_work,




            )

        fun selectButton(selectedButton: Button) {
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    clickSound.start()
                    if (button == scenario_choosed_work) {
                        button.setBackgroundResource(R.drawable.scenario_on)
                        light_choosed_work.setBackgroundResource(R.drawable.light_off)
                        plug_choosed_work.setBackgroundResource(R.drawable.plug_off)
                        curtain_choosed_work.setBackgroundResource(R.drawable.curtain_off)
                    }
                    else if (button == light_choosed_work) {
                        button.setBackgroundResource(R.drawable.light_on)
                        scenario_choosed_work.setBackgroundResource(R.drawable.scenario_off)
                        plug_choosed_work.setBackgroundResource(R.drawable.plug_off)
                        curtain_choosed_work.setBackgroundResource(R.drawable.curtain_off)
                    }
                    else if (button == plug_choosed_work) {
                        button.setBackgroundResource(R.drawable.plug_on)
                        scenario_choosed_work.setBackgroundResource(R.drawable.scenario_off)
                        light_choosed_work.setBackgroundResource(R.drawable.light_off)
                        curtain_choosed_work.setBackgroundResource(R.drawable.curtain_off)
                    }
                    else if (button == curtain_choosed_work) {
                        button.setBackgroundResource(R.drawable.curtain_on)
                        scenario_choosed_work.setBackgroundResource(R.drawable.scenario_off)
                        light_choosed_work.setBackgroundResource(R.drawable.light_off)
                        plug_choosed_work.setBackgroundResource(R.drawable.plug_off)
                    }


//                    else if (button == scenario_choosed_work) {
//                        button.setBackgroundResource(R.drawable.scenario_off)
//
//                    }
//                    else if (button == light_choosed_work) {
//                        button.setBackgroundResource(R.drawable.light_off)
//                    }
//                    else if (button == plug_choosed_work) {
//                        button.setBackgroundResource(R.drawable.plug_off)
//                    }
//                    else if (button == curtain_choosed_work) {
//                        button.setBackgroundResource(R.drawable.curtain_off)
//                    }







                }
            }
        }
        val pagerChangeListener = parentFragment as? PagerChangeListener


        val inflater2 = LayoutInflater.from(requireContext())

        val customPopupView2: View = inflater2.inflate(R.layout.popup_delete_timetable, null)
        val popupView2: View = inflater2.inflate(R.layout.popup_delete_timetable, null)




        val popupWidth2 = resources.getDimension(`in`.nouri.dynamicsizeslib.R.dimen._170mdp).toInt()
        val popupHeight2 = resources.getDimension(`in`.nouri.dynamicsizeslib.R.dimen._77mdp).toInt()

        val popupWindow2 = PopupWindow(customPopupView2, popupWidth2, popupHeight2, true)

        val alertDialogBuilder2 = AlertDialog.Builder(requireContext())
        alertDialogBuilder2.setView(popupView2)

        val alertDialog2 = alertDialogBuilder2.create()
        alertDialog2.setCanceledOnTouchOutside(false)

        val yes_select = customPopupView2.findViewById<Button>(R.id.yes_delete)
        val cancel_select = customPopupView2.findViewById<Button>(R.id.cancel_delete)
        val text_msg = customPopupView2.findViewById<TextView>(R.id.text_msg)
        text_msg.setText("Are you sure you want to select this one??")


        sharedViewModel.current_pole_six_wirker.observe(viewLifecycleOwner , Observer { current ->
            val current_id_pole=current!!.split(",")
            val current_id =current_id_pole[0]
            val current_pole =current_id_pole[1]
            val current_six_worker = six_workert_db.get_from_db_six_workert(current_id.toInt())
            var types= try {
                current_six_worker!!.type!!.split(",").toMutableList()

            }catch (e:Exception){
                println(e)
                null
            }
            val type = types?.get(((current_pole.toInt())-1))
            println("type is          "+type)



            if (type == "scenario"){
                selectButton(scenario_choosed_work)

            }else if (type == "light"){
                selectButton(light_choosed_work)

            }else if (type == "plug"){
                selectButton(plug_choosed_work)

            }else if (type == "curtain"){
                selectButton(curtain_choosed_work)

            }else{
                scenario_choosed_work.setBackgroundResource(R.drawable.scenario_off)
                light_choosed_work.setBackgroundResource(R.drawable.light_off)
                plug_choosed_work.setBackgroundResource(R.drawable.plug_off)
                curtain_choosed_work.setBackgroundResource(R.drawable.curtain_off)

            }





            scenario_choosed_work.setOnClickListener {
                selectButton(scenario_choosed_work)





                if (type!="scenario"){
                    popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                    yes_select.setOnClickListener {
                        pagerChangeListener?.onPagerItemChanged(2)
                        types?.set((current_pole.toInt())-1, "scenario")
                        current_six_worker!!.type=types!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker.id,current_six_worker)
                        popupWindow2.dismiss()
                    }

                    cancel_select.setOnClickListener {
                        popupWindow2.dismiss()

                    }








                }else{
                    types?.set((current_pole.toInt())-1, "scenario")
                    current_six_worker!!.type=types!!.joinToString(separator = ",")
                    six_workert_db.updatesix_workertById(current_six_worker!!.id,current_six_worker)
                    pagerChangeListener?.onPagerItemChanged(2)


                }


            }

            light_choosed_work.setOnClickListener {
                selectButton(light_choosed_work)
                var counter = 0

                if (type!="light" ){
                    popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                    yes_select.setOnClickListener {
                        pagerChangeListener?.onPagerItemChanged(3)
                        if (types != null){
                            types[current_pole.toInt()-1] ="light"

                        }
                        current_six_worker!!.type = types!!.joinToString(separator = ",")
                        println(current_six_worker!!.type)
                        println(current_six_worker!!.id)
                        six_workert_db.updatesix_workertById(current_six_worker.id,current_six_worker)
                        popupWindow2.dismiss()
                    }
                    cancel_select.setOnClickListener {
                        popupWindow2.dismiss()

                    }


                }else{

                    types?.set((current_pole.toInt())-1, "light")
                    current_six_worker!!.type=types!!.joinToString(separator = ",")
                    println(current_six_worker.type)
                    six_workert_db.updatesix_workertById(current_six_worker.id,current_six_worker)
                    pagerChangeListener?.onPagerItemChanged(3)


                }



            }

            plug_choosed_work.setOnClickListener {
                selectButton(plug_choosed_work)
                var counter = 0

                if (type!="plug" ){
                    popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                    yes_select.setOnClickListener {
                        pagerChangeListener?.onPagerItemChanged(5)
                        types?.set((current_pole.toInt())-1, "plug")
                        current_six_worker!!.type=types!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker.id,current_six_worker)
                        popupWindow2.dismiss()
                    }
                    cancel_select.setOnClickListener {
                        popupWindow2.dismiss()

                    }


                }else{
                    pagerChangeListener?.onPagerItemChanged(5)


                }


            }
            curtain_choosed_work.setOnClickListener {
                selectButton(curtain_choosed_work)
                var counter = 0

                if (type!="curtain" ){
                    popupWindow2.showAtLocation(view, Gravity.CENTER, 0, 0)
                    yes_select.setOnClickListener {
                        pagerChangeListener?.onPagerItemChanged(4)
                        types?.set((current_pole.toInt())-1, "curtain")
                        current_six_worker!!.type=types!!.joinToString(separator = ",")
                        six_workert_db.updatesix_workertById(current_six_worker.id,current_six_worker)
                        popupWindow2.dismiss()
                    }
                    cancel_select.setOnClickListener {
                        popupWindow2.dismiss()

                    }


                }else{
                    pagerChangeListener?.onPagerItemChanged(4)


                }

            }

            cancel.setOnClickListener {

                pagerChangeListener?.onPagerItemChanged(0)

            }


        })


    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }



}