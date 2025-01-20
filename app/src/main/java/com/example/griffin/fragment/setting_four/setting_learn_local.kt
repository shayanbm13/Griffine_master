package com.example.griffin.fragment.setting_four

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.example.griffin.R
import com.example.griffin.adapters.FragmentPagerAdapter_select_work
import com.example.griffin.adapters.FragmentPagerAdapter_setting_one
import com.example.griffin.database.six_workert_db
import com.example.griffin.fragment.setting_four.choose_lolcal_work.select_work_scenario
import com.example.griffin.mudels.*

class setting_learn_local : Fragment() , PagerChangeListener {
     
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewPager:NonSwipeableViewPager
    val sharedViewModel:SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_setting_learn_local, container, false)


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val learn_1_pole = view.findViewById<Button>(R.id.learn_1_pole)
        val learn_2_pole: Button = view.findViewById(R.id.learn_2_pole)
        val learn_3_pole: Button = view.findViewById(R.id.learn_3_pole)
        val learn_4_pole: Button = view.findViewById(R.id.learn_4_pole)
        val learn_5_pole: Button = view.findViewById(R.id.learn_5_pole)
        val learn_6_pole: Button = view.findViewById(R.id.learn_6_pole)


//        viewPager = view.findViewById(R.id.setting_one)
//        pagerAdapter = MyPagerAdapter(supportFragmentManager)
//        viewPager.adapter = pagerAdapter
//
//

//
//
//
//





    }

    override fun onResume() {
        super.onResume()
        val select_work_viewpager = requireView().findViewById<NonSwipeableViewPager>(R.id.select_work_viewpager)
        val learn_1_pole = view?.findViewById<Button>(R.id.learn_1_pole)
        val learn_2_pole: Button = requireView().findViewById(R.id.learn_2_pole)
        val learn_3_pole: Button = requireView().findViewById(R.id.learn_3_pole)
        val learn_4_pole: Button = requireView().findViewById(R.id.learn_4_pole)
        val learn_5_pole: Button = requireView().findViewById(R.id.learn_5_pole)
        val learn_6_pole: Button = requireView().findViewById(R.id.learn_6_pole)

        val left_button = requireView().findViewById<ImageButton>(R.id.left_button)
        val right_button = requireView().findViewById<ImageButton>(R.id.right_button)

        val local_name = requireView().findViewById<TextView>(R.id.local_name)

        var selected_pole:Int
        var selected_type:String



//        sharedViewModel.six_worker_list.observe(viewLifecycleOwner, Observer { six_worker_list ->
//
//
//
//
//
//        })





        println("resume")
        var current_index = 0
        var six_workert_db = six_workert_db.getInstance(requireContext())
        var all_six_worker = six_workert_db.getAllsix_workerts()
        var all_six_worker_count = ((all_six_worker.count())-1)
        var current_six_worker=six_workert_db.get_from_db_six_workert(current_index+1)
        println(current_six_worker?.name)
        current_six_worker = try {
//            six_workert_db.get_from_db_six_workert(current_index)
            all_six_worker[current_index]

        }catch (e:Exception){
            println(e)
            null

        }

        sharedViewModel.current_six_wirker.observe(viewLifecycleOwner, Observer { current_six_worker_change ->
            println("wwwwwwwwwwwwwwwww"+current_six_worker_change)
            if (current_six_worker_change == "rename"){
                if (current_six_worker !=null){
                    six_workert_db = com.example.griffin.database.six_workert_db.getInstance(requireContext())
                    all_six_worker = six_workert_db.getAllsix_workerts()
                    all_six_worker_count = ((all_six_worker.count())-1)
                    current_six_worker=six_workert_db.get_from_db_six_workert(current_index+1)
                    println(current_six_worker?.name)
                    current_six_worker = try {
//            six_workert_db.get_from_db_six_workert(current_index)
                        all_six_worker[current_index]

                    }catch (e:Exception){
                        println(e)
                        null

                    }
                    sharedViewModel.update_current_six_wirker(current_six_worker!!.id.toString())
                    local_name.setText(current_six_worker?.name)

                }

//                sharedViewModel.update_current_six_wirker("khali")

            }else if(current_six_worker_change == "delete"){
                all_six_worker = six_workert_db.getAllsix_workerts()
                all_six_worker_count = ((all_six_worker.count())-1)

                if (current_index >= 1){
                    current_index -=1

                    val current_local = all_six_worker[current_index]
                    sharedViewModel.update_current_six_wirker(current_local!!.id.toString())
                    local_name.setText(current_local!!.name)
                    select_work_viewpager.visibility=View.VISIBLE
                    if (current_six_worker?.pole_num!!.startsWith("3") ){
                        learn_4_pole.visibility=View.INVISIBLE
                        learn_5_pole.visibility=View.INVISIBLE
                        learn_6_pole.visibility=View.INVISIBLE

                    }else if (current_six_worker?.pole_num!!.startsWith("6") ){
                        learn_1_pole?.visibility=View.VISIBLE
                        learn_2_pole.visibility=View.VISIBLE
                        learn_3_pole.visibility=View.VISIBLE
                        learn_4_pole.visibility=View.VISIBLE
                        learn_5_pole.visibility=View.VISIBLE
                        learn_6_pole.visibility=View.VISIBLE
                    }else if (current_six_worker?.pole_num!!.startsWith("4") ){
                        learn_1_pole?.visibility=View.VISIBLE
                        learn_2_pole.visibility=View.VISIBLE
                        learn_3_pole.visibility=View.VISIBLE
                        learn_4_pole.visibility=View.VISIBLE
                        learn_5_pole.visibility=View.INVISIBLE
                        learn_6_pole.visibility=View.INVISIBLE
                    }

                    viewPager.currentItem=0

                    learn_1_pole?.setBackgroundResource(R.drawable.one_pole_off)
                    learn_2_pole.setBackgroundResource(R.drawable.two_pole_off)
                    learn_3_pole.setBackgroundResource(R.drawable.three_pole_off)
                    learn_4_pole.setBackgroundResource(R.drawable.four_pole_off)
                    learn_5_pole.setBackgroundResource(R.drawable.five_pole_off)
                    learn_6_pole.setBackgroundResource(R.drawable.six_pole_off)





                }else{
                    println(all_six_worker_count)
                    if (all_six_worker_count+1>0){
                        all_six_worker = six_workert_db.getAllsix_workerts()
                        all_six_worker_count = ((all_six_worker.count())-1)
                        val current_local = all_six_worker[all_six_worker_count]
                        sharedViewModel.update_current_six_wirker(current_local!!.id.toString())
                        local_name.setText(current_local!!.name)
                        select_work_viewpager.visibility=View.VISIBLE
                        if (current_six_worker?.pole_num!!.startsWith("3") ){
                            learn_4_pole.visibility=View.INVISIBLE
                            learn_5_pole.visibility=View.INVISIBLE
                            learn_6_pole.visibility=View.INVISIBLE

                        }else if (current_six_worker?.pole_num!!.startsWith("6")){
                            learn_1_pole?.visibility=View.VISIBLE
                            learn_2_pole.visibility=View.VISIBLE
                            learn_3_pole.visibility=View.VISIBLE
                            learn_4_pole.visibility=View.VISIBLE
                            learn_5_pole.visibility=View.VISIBLE
                            learn_6_pole.visibility=View.VISIBLE
                        }else if (current_six_worker?.pole_num!!.startsWith("4") ){
                            learn_1_pole?.visibility=View.VISIBLE
                            learn_2_pole.visibility=View.VISIBLE
                            learn_3_pole.visibility=View.VISIBLE
                            learn_4_pole.visibility=View.VISIBLE
                            learn_5_pole.visibility=View.INVISIBLE
                            learn_6_pole.visibility=View.INVISIBLE
                        }

                        viewPager.currentItem=0

                        learn_1_pole?.setBackgroundResource(R.drawable.one_pole_off)
                        learn_2_pole.setBackgroundResource(R.drawable.two_pole_off)
                        learn_3_pole.setBackgroundResource(R.drawable.three_pole_off)
                        learn_4_pole.setBackgroundResource(R.drawable.four_pole_off)
                        learn_5_pole.setBackgroundResource(R.drawable.five_pole_off)
                        learn_6_pole.setBackgroundResource(R.drawable.six_pole_off)




                    }else{

                        Toast.makeText(requireContext(), "You dont have local device", Toast.LENGTH_SHORT).show()
                        local_name.setText("")
                        learn_1_pole!!.visibility=View.INVISIBLE
                        learn_2_pole.visibility=View.INVISIBLE
                        learn_3_pole.visibility=View.INVISIBLE
                        learn_4_pole.visibility=View.INVISIBLE
                        learn_5_pole.visibility=View.INVISIBLE
                        learn_6_pole.visibility=View.INVISIBLE
                        select_work_viewpager.visibility=View.INVISIBLE

                    }



                    sharedViewModel.update_current_six_wirker("khali")
                }
            }

        })

        if (current_six_worker != null) {
            local_name.setText(current_six_worker!!.name)
            select_work_viewpager.visibility=View.VISIBLE
            sharedViewModel.update_current_six_wirker(current_six_worker!!.id.toString())

            if (current_six_worker!!.pole_num!!.startsWith("3")  ){
                learn_4_pole.visibility=View.INVISIBLE
                learn_5_pole.visibility=View.INVISIBLE
                learn_6_pole.visibility=View.INVISIBLE

            }else if (current_six_worker!!.pole_num!!.startsWith("6")){
                learn_4_pole.visibility=View.VISIBLE
                learn_5_pole.visibility=View.VISIBLE
                learn_6_pole.visibility=View.VISIBLE
            }else if (current_six_worker?.pole_num!!.startsWith("4") ){
                learn_1_pole?.visibility=View.VISIBLE
                learn_2_pole.visibility=View.VISIBLE
                learn_3_pole.visibility=View.VISIBLE
                learn_4_pole.visibility=View.VISIBLE
                learn_5_pole.visibility=View.INVISIBLE
                learn_6_pole.visibility=View.INVISIBLE
            }
        }else{
            Toast.makeText(requireContext(), "You dont have local device", Toast.LENGTH_SHORT).show()
            local_name.setText("")
            learn_1_pole!!.visibility=View.INVISIBLE
            learn_2_pole.visibility=View.INVISIBLE
            learn_3_pole.visibility=View.INVISIBLE
            learn_4_pole.visibility=View.INVISIBLE
            learn_5_pole.visibility=View.INVISIBLE
            learn_6_pole.visibility=View.INVISIBLE
            select_work_viewpager.visibility=View.INVISIBLE



        }

         
        val chooser_btns_list = listOf(
            learn_1_pole,
            learn_2_pole,
            learn_3_pole,
            learn_4_pole,
            learn_5_pole,
            learn_6_pole
        )

        fun selectButton(selectedButton: Button) {
            SoundManager.playSound()
            for (button in chooser_btns_list) {
                if (button == selectedButton) {
                    button.isSelected = true
//                    SoundManager.playSound()
                    if (button == learn_1_pole) {
                        button.setBackgroundResource(R.drawable.one_pole_on)
                    }
                    if (button == learn_2_pole) {
                        button.setBackgroundResource(R.drawable.two_pole_on)
                    }
                    if (button == learn_3_pole) {
                        button.setBackgroundResource(R.drawable.three_pole_on)
                    }
                    if (button == learn_4_pole) {
                        button.setBackgroundResource(R.drawable.four_pole_on)
                    }
                    if (button == learn_5_pole) {
                        button.setBackgroundResource(R.drawable.five_pole_on)

                    }
                    if (button == learn_6_pole) {
                        button.setBackgroundResource(R.drawable.six_pole_on)
                    }


                } else {
//                    SoundManager.playSound()
                    button!!.isSelected = false
                    if (button == learn_1_pole) {
                        button.setBackgroundResource(R.drawable.one_pole_off)
                    }
                    if (button == learn_2_pole) {
                        button.setBackgroundResource(R.drawable.two_pole_off)
                    }
                    if (button == learn_3_pole) {
                        button.setBackgroundResource(R.drawable.three_pole_off)
                    }
                    if (button == learn_4_pole) {
                        button.setBackgroundResource(R.drawable.four_pole_off)
                    }
                    if (button == learn_5_pole) {
                        button.setBackgroundResource(R.drawable.five_pole_off)
                    }
                    if (button == learn_6_pole) {
                        button.setBackgroundResource(R.drawable.six_pole_off)
                    }

                }
            }
        }

        viewPager = requireView().findViewById(R.id.select_work_viewpager)
        val pagerAdapter: FragmentPagerAdapter_select_work = FragmentPagerAdapter_select_work(childFragmentManager)
        viewPager.adapter = pagerAdapter





        learn_1_pole!!.setOnClickListener {
            selected_pole=1
            selectButton(learn_1_pole)
            viewPager.currentItem=1

            sharedViewModel.update_current_pole_six_wirker("${all_six_worker[current_index]!!.id},1")
        }
        learn_2_pole!!.setOnClickListener {
            selected_pole=2
            selectButton(learn_2_pole)
            viewPager.currentItem=1
            sharedViewModel.update_current_pole_six_wirker("${all_six_worker[current_index]!!.id},2")
        }
        learn_3_pole!!.setOnClickListener {
            selected_pole=3
            selectButton(learn_3_pole)
            viewPager.currentItem=1
            sharedViewModel.update_current_pole_six_wirker("${all_six_worker[current_index]!!.id},3")
        }
        learn_4_pole!!.setOnClickListener {
            selected_pole=4
            selectButton(learn_4_pole)
            viewPager.currentItem=1
            sharedViewModel.update_current_pole_six_wirker("${all_six_worker[current_index]!!.id},4")
        }
        learn_5_pole!!.setOnClickListener {
            selected_pole=5
            selectButton(learn_5_pole)
            viewPager.currentItem=1
            sharedViewModel.update_current_pole_six_wirker("${all_six_worker[current_index]!!.id},5")
        }
        learn_6_pole!!.setOnClickListener {
            selectButton(learn_6_pole)
            viewPager.currentItem=1
            selected_pole=6
            sharedViewModel.update_current_pole_six_wirker("${all_six_worker[current_index]!!.id},6")
        }



        right_button.setOnClickListener {

            if (current_index<all_six_worker_count){
                current_index +=1
                val current_local = all_six_worker[current_index]
                sharedViewModel.update_current_six_wirker(current_local!!.id.toString())
                current_six_worker=six_workert_db.get_from_db_six_workert(current_index+1)

                local_name.setText(current_local!!.name)
                learn_1_pole.setBackgroundResource(R.drawable.one_pole_off)
                learn_2_pole.setBackgroundResource(R.drawable.two_pole_off)
                learn_3_pole.setBackgroundResource(R.drawable.three_pole_off)
                learn_4_pole.setBackgroundResource(R.drawable.four_pole_off)
                learn_5_pole.setBackgroundResource(R.drawable.five_pole_off)
                learn_6_pole.setBackgroundResource(R.drawable.six_pole_off)
                viewPager.currentItem=0
                println(current_six_worker?.pole_num)
                if (current_local?.pole_num!!.startsWith("3")  ){
                    learn_4_pole.visibility=View.INVISIBLE
                    learn_5_pole.visibility=View.INVISIBLE
                    learn_6_pole.visibility=View.INVISIBLE

                }else if (current_local?.pole_num!!.startsWith("6")){
                    learn_4_pole.visibility=View.VISIBLE
                    learn_5_pole.visibility=View.VISIBLE
                    learn_6_pole.visibility=View.VISIBLE
                }else if (current_six_worker?.pole_num!!.startsWith("4") ){
                    learn_1_pole?.visibility=View.VISIBLE
                    learn_2_pole.visibility=View.VISIBLE
                    learn_3_pole.visibility=View.VISIBLE
                    learn_4_pole.visibility=View.VISIBLE
                    learn_5_pole.visibility=View.INVISIBLE
                    learn_6_pole.visibility=View.INVISIBLE
                }

            }
        }

        left_button.setOnClickListener {
            try {
                if (current_index >= 1){
                    current_index -=1
                    val current_local = all_six_worker[current_index]
                    current_six_worker=six_workert_db.get_from_db_six_workert(current_index+1)

                    sharedViewModel.update_current_six_wirker(current_local!!.id.toString())
                    local_name.setText(current_local!!.name)

                    if (current_local?.pole_num!!.startsWith("3") ){
                        learn_4_pole.visibility=View.INVISIBLE
                        learn_5_pole.visibility=View.INVISIBLE
                        learn_6_pole.visibility=View.INVISIBLE

                    }else if (current_local?.pole_num!!.startsWith("6")  ){
                        learn_4_pole.visibility=View.VISIBLE
                        learn_5_pole.visibility=View.VISIBLE
                        learn_6_pole.visibility=View.VISIBLE
                    }else if (current_six_worker?.pole_num!!.startsWith("4") ){
                        learn_1_pole?.visibility=View.VISIBLE
                        learn_2_pole.visibility=View.VISIBLE
                        learn_3_pole.visibility=View.VISIBLE
                        learn_4_pole.visibility=View.VISIBLE
                        learn_5_pole.visibility=View.INVISIBLE
                        learn_6_pole.visibility=View.INVISIBLE
                    }
                    viewPager.currentItem=0

                    learn_1_pole.setBackgroundResource(R.drawable.one_pole_off)
                    learn_2_pole.setBackgroundResource(R.drawable.two_pole_off)
                    learn_3_pole.setBackgroundResource(R.drawable.three_pole_off)
                    learn_4_pole.setBackgroundResource(R.drawable.four_pole_off)
                    learn_5_pole.setBackgroundResource(R.drawable.five_pole_off)
                    learn_6_pole.setBackgroundResource(R.drawable.six_pole_off)



                }
            }catch (e:Exception){


            }

        }


    }
    override fun onDestroy() {
        super.onDestroy()
         
         
    }
    override fun onPause() {
        super.onPause()
         
    }



    override fun onPagerItemChanged(position: Int) {
        viewPager.currentItem = position    }

    }