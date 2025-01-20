package com.example.griffin.fragment.setting_four

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.leanback.widget.SearchOrbView.Colors
import androidx.leanback.widget.Visibility
import androidx.lifecycle.Observer
import com.example.griffin.MyApplication
import com.example.griffin.R
import com.example.griffin.database.Ir_db
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.UdpListener8089

import com.example.griffin.mudels.aplicationSharedvVewModel
import com.example.griffin.mudels.send_ir_cmd


class setting_learn_ir : Fragment() {

    val SharedViewModel : SharedViewModel by activityViewModels()
    private lateinit var sharedViewModel2: aplicationSharedvVewModel

    fun findIndexesByValue(inputList: List<String>): List<List<Int>> {
        val index1 = mutableListOf<Int>()
        val index2 = mutableListOf<Int>()
        val index3 = mutableListOf<Int>()

        inputList.forEachIndexed { index, value ->
            when (value) {
                "1" -> index1.add(index)
                "2" -> index2.add(index)
                "3" -> index3.add(index)
            }
        }

        return listOf(index1, index2, index3)
    }

    fun get_from_indexes(index_list:List<Int>,value_list:List<String>):List<String>{
        var my_value = mutableListOf<String>()

        for (index in index_list){
            my_value.add(value_list.get(index))


        }

        return my_value


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_learn_ir, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)






        var is_lerning=false
        var type_learning = ""

        val nex_ir=view.findViewById<ImageButton>(R.id.next_ir)
        val ex_ir=view.findViewById<ImageButton>(R.id.ex_ir)
        val ir_name= view.findViewById<TextView>(R.id.ir_name)
        val first_device=view.findViewById<ImageButton>(R.id.first_devise)
        val second_device=view.findViewById<ImageButton>(R.id.second_devise)
        val therd_device=view.findViewById<ImageButton>(R.id.therd_devise)

        val power=view.findViewById<ImageButton>(R.id.power)
        val chanel_up=view.findViewById<ImageButton>(R.id.chanel_up)
        val chanel_down=view.findViewById<ImageButton>(R.id.chanel_down)
        val volume_up=view.findViewById<ImageButton>(R.id.volume_up)
        val volume_down=view.findViewById<ImageButton>(R.id.volume_down)
        val input=view.findViewById<ImageButton>(R.id.input)
        val left=view.findViewById<ImageButton>(R.id.left)
        val right=view.findViewById<ImageButton>(R.id.right)
        val up=view.findViewById<ImageButton>(R.id.up)
        val down=view.findViewById<ImageButton>(R.id.down)
        val ok=view.findViewById<ImageButton>(R.id.ok)
        val back=view.findViewById<ImageButton>(R.id.back)
        val background_layout=view.findViewById<ConstraintLayout>(R.id.background_layout)


        val inflater2 = LayoutInflater.from(requireContext())

        val customPopupView2: View = inflater2.inflate(R.layout.ir_learn_popup, null)
        val popupView2: View = inflater2.inflate(R.layout.ir_learn_popup, null)

        val popupWidth = 590
        val popupHeight = 470

        val done_ir_learn = customPopupView2.findViewById<Button>(R.id.done_ir_learn)
        val try_again_ir_learn = customPopupView2.findViewById<Button>(R.id.try_again_ir_learn)
        val loding_step_one=customPopupView2.findViewById<ProgressBar>(R.id.loding_step_one)
        val loding_step_two=customPopupView2.findViewById<ProgressBar>(R.id.loding_step_two)
        val step_one=customPopupView2.findViewById<TextView>(R.id.step_one)
        val step_two=customPopupView2.findViewById<TextView>(R.id.step_two)
        val check_learn_ir=customPopupView2.findViewById<TextView>(R.id.check_learn_ir)


        // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
        val popupWindow = PopupWindow(customPopupView2, popupWidth, popupHeight, true)

        val alertDialogBuilder2 = AlertDialog.Builder(requireContext())
        alertDialogBuilder2.setView(popupView2)

        val alertDialog2= alertDialogBuilder2.create()
        alertDialog2.setCanceledOnTouchOutside(false)


        popupWindow.setOnDismissListener {
            is_lerning=false
            type_learning=""

        }




        fun update_ui(types:List<String>){

            types.forEachIndexed { index,value ->
                when(value){
                    "power"->power.setBackgroundResource(R.drawable.power_icon_on)
                    "chanel_up"->chanel_up.setBackgroundResource(R.drawable.chanel_up_icon_on)
                    "chanel_down"->chanel_down.setBackgroundResource(R.drawable.chanel_down_icon_on)
                    "volume_up"->volume_up.setBackgroundResource(R.drawable.vloume_up_icon_on)
                    "volume_down"->volume_down.setBackgroundResource(R.drawable.volume_down_icon_on)
                    "input"->input.setBackgroundResource(R.drawable.input_icon_on)
                    "left"->left.setBackgroundResource(R.drawable.left_icon_on)
                    "right"->right.setBackgroundResource(R.drawable.right_icon_on)
                    "up"->up.setBackgroundResource(R.drawable.up_icon_on)
                    "down"->down.setBackgroundResource(R.drawable.down_icon_on)
                    "ok"->ok.setBackgroundResource(R.drawable.ok_icon_on)
                    "back"->back.setBackgroundResource(R.drawable.back_icon_on)


                }

            }

        }
        fun all_off(){
            power.setBackgroundResource(R.drawable.power_icon_off)
            chanel_up.setBackgroundResource(R.drawable.chanel_up_icon_off)
            chanel_down.setBackgroundResource(R.drawable.chanel_down_icon_off)
            volume_up.setBackgroundResource(R.drawable.volum_up_icon_off)
            volume_down.setBackgroundResource(R.drawable.volum_down_icon_off)
            input.setBackgroundResource(R.drawable.input_icon_off)
            left.setBackgroundResource(R.drawable.left_icon_off)
            right.setBackgroundResource(R.drawable.right_icon_off)
            up.setBackgroundResource(R.drawable.up_icon_off)
            down.setBackgroundResource(R.drawable.down_icon_off)
            ok.setBackgroundResource(R.drawable.ok_icon_off)
            back.setBackgroundResource(R.drawable.back_icon_off)


        }


        fun step_one_learn_ir(){
            step_one.setTextColor(Color.WHITE)
            loding_step_one.visibility=View.VISIBLE

            step_two.setTextColor(Color.GRAY)
            loding_step_two.visibility=View.INVISIBLE
            check_learn_ir.visibility=View.INVISIBLE
            done_ir_learn.isClickable=false

        }
        fun step_two_learn_ir(){
            step_one.setTextColor(Color.GREEN)
            loding_step_one.visibility=View.INVISIBLE

            step_two.setTextColor(Color.WHITE)
            loding_step_two.visibility=View.VISIBLE

            check_learn_ir.visibility=View.VISIBLE
            check_learn_ir.setTextColor(Color.WHITE)
            check_learn_ir.alpha=0f
            check_learn_ir.animate().alpha(1f).setDuration(800).setListener(null)
            done_ir_learn.isClickable=true

        }
        fun final_learn_ir(){

            step_one.setTextColor(Color.GREEN)
            loding_step_one.visibility=View.INVISIBLE

            step_two.setTextColor(Color.GREEN)
            step_two.visibility=View.VISIBLE

            check_learn_ir.visibility=View.VISIBLE
            check_learn_ir.alpha=0f
            check_learn_ir.animate().alpha(1f).setDuration(800).setListener(null)
            done_ir_learn.isClickable=true
        }

        val ir_db = Ir_db.getInstance(requireContext())
        var all_irs= ir_db.getAllIrs()
        var ir_count = all_irs.count()
        var current_index = 0
        if(all_irs.isNotEmpty()){
            first_device.visibility=View.VISIBLE
            second_device.visibility=View.VISIBLE
            therd_device.visibility=View.VISIBLE


            var current_ir =all_irs[0]
            var current_device="0"
            SharedViewModel.update_current_IR(current_ir)

            //defults

            ir_name.setText(current_ir!!.name)


            val drvices_index = findIndexesByValue(current_ir!!.sub_type!!.split(","))

            val first_drvice_Indexes = drvices_index[0]
            val second_drvice_Indexes = drvices_index[1]
            val third_drvice_Indexes = drvices_index[2]
            val current_types = get_from_indexes(first_drvice_Indexes,current_ir!!.sub_type!!.split(","))
            update_ui(current_types)
            //end defults


            first_device.setOnClickListener{
                current_device="1"
                val drvices_index = findIndexesByValue(current_ir!!.sub_type!!.split(","))

                val first_drvice_Indexes = drvices_index[0]
                val second_drvice_Indexes = drvices_index[1]
                val third_drvice_Indexes = drvices_index[2]

                println(first_device)
                all_off()
                background_layout.alpha=0f
                background_layout.visibility=View.VISIBLE
                background_layout.animate().alpha(1f).setDuration(1000).setListener(null)
                if (first_drvice_Indexes.isNotEmpty()){
                    val current_types = get_from_indexes(first_drvice_Indexes,current_ir!!.type!!.split(","))
                    println(current_types)

                    update_ui(current_types)

                }
            }
            second_device.setOnClickListener{
                current_device="2"
                val drvices_index = findIndexesByValue(current_ir!!.sub_type!!.split(","))
                val first_drvice_Indexes = drvices_index[0]
                val second_drvice_Indexes = drvices_index[1]
                val third_drvice_Indexes = drvices_index[2]
                all_off()
                background_layout.alpha=0f
                background_layout.visibility=View.VISIBLE
                background_layout.animate().alpha(1f).setDuration(1000).setListener(null)
                if (second_drvice_Indexes.isNotEmpty()){
                    val current_types = get_from_indexes(second_drvice_Indexes,current_ir!!.type!!.split(","))

                    update_ui(current_types)

                }
            }
            therd_device.setOnClickListener{
                current_device="3"
                val drvices_index = findIndexesByValue(current_ir!!.sub_type!!.split(","))

                val first_drvice_Indexes = drvices_index[0]
                val second_drvice_Indexes = drvices_index[1]
                val third_drvice_Indexes = drvices_index[2]
                all_off()
                background_layout.alpha=0f
                background_layout.visibility=View.VISIBLE
                background_layout.animate().alpha(1f).setDuration(1000).setListener(null)
                if (third_drvice_Indexes.isNotEmpty()){
                    val current_types = get_from_indexes(third_drvice_Indexes,current_ir!!.type!!.split(","))

                    update_ui(current_types)

                }


            }

            sharedViewModel2 = (requireContext().applicationContext as MyApplication).aplicationSharedvVewModel

            SharedViewModel.IR_update.observe(viewLifecycleOwner, Observer { current_status ->

                if (current_status=="R"){
                    all_irs=ir_db.getAllIrs()
                    ir_name.setText(all_irs[current_index]!!.name)

                    SharedViewModel.update_IR_update("")
                }else if (current_status == "D"){

                    if (current_index >= 1 ){

                        background_layout.visibility=View.INVISIBLE
                        current_ir=all_irs[current_index-1]
                        all_irs=ir_db.getAllIrs()


                        background_layout.visibility=View.INVISIBLE
                        current_index-=1
                        current_ir=all_irs[current_index]
                        ir_name.setText(current_ir!!.name)
                        ir_count=all_irs.count()
                        SharedViewModel.update_current_IR(current_ir)

                    }else{
                        all_irs=ir_db.getAllIrs()
                        ir_count=all_irs.count()
                        if(ir_count > 0){

                            current_ir=all_irs[0]
                            current_index=0
                            background_layout.visibility=View.INVISIBLE

                            background_layout.visibility=View.INVISIBLE

                            current_ir=all_irs[current_index]
                            ir_name.setText(current_ir!!.name)

                            SharedViewModel.update_current_IR(current_ir)

                        }else{
                            background_layout.visibility=View.INVISIBLE
                            first_device.visibility=View.INVISIBLE
                            second_device.visibility=View.INVISIBLE
                            therd_device.visibility=View.INVISIBLE
                            ir_name.setText("Empty")
                            all_irs=ir_db.getAllIrs()
                            ir_count=all_irs.count()
                        }

                    }


                    SharedViewModel.update_IR_update("")
                }

            })





            nex_ir.setOnClickListener {
                if (current_index+1 < ir_count) {
                    current_device="0"
                    //  mire baaadi
                    background_layout.visibility=View.INVISIBLE

                    current_index+=1
                    current_ir=all_irs[current_index]
                    ir_name.setText(current_ir!!.name)
                    SharedViewModel.update_current_IR(current_ir)
                }

            }
            ex_ir.setOnClickListener{

                if (current_index>0){
                    current_device="0"
                    //mire ghabli
                    background_layout.visibility=View.INVISIBLE
                    current_index-=1
                    current_ir=all_irs[current_index]
                    ir_name.setText(current_ir!!.name)
                    SharedViewModel.update_current_IR(current_ir)
                }
            }


            power.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                type_learning="power"


                step_one.setText("Step 1 : click power on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()
                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&& type_learning=="power"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()

                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])

                                    println("sended")
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("power".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                power.setBackgroundResource(R.drawable.power_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })
                true
            }
            chanel_up.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click chanel+ on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()
                type_learning="chanel_up"
                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&& type_learning=="chanel_up"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()

                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    println("sended")
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("chanel_up".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                chanel_up.setBackgroundResource(R.drawable.chanel_up_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })


                true
            }
            chanel_down.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click chanel- on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()
                type_learning="chanel_down"

                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&&type_learning=="chanel_down"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()
                                    println("sended")
                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("chanel_down".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                chanel_down.setBackgroundResource(R.drawable.chanel_down_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })


                true
            }

            volume_up.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click volume+ on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()
                type_learning="volume_up"
                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&&type_learning=="volume_up"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()
                                    println("sended")
                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("volume_up".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                volume_up.setBackgroundResource(R.drawable.vloume_up_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })


                true
            }
            volume_down.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click volume- on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()
                type_learning="volume_down"

                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&&type_learning=="volume_down"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()
                                    println("sended")
                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("volume_down".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                volume_down.setBackgroundResource(R.drawable.volume_down_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })

                true
            }
            input.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click input on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()
                type_learning="input"

                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&& type_learning=="input"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()
                                    println("sended")
                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("input".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                input.setBackgroundResource(R.drawable.input_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })


                true
            }
            left.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click left on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()
                type_learning="left"

                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&& type_learning=="left"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()
                                    println("sended")
                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("left".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                left.setBackgroundResource(R.drawable.left_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })

                true
            }
            right.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click right on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()
                type_learning="right"
                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&& type_learning=="right"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()
                                    println("sended")
                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("right".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                right.setBackgroundResource(R.drawable.power_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })

                true
            }
            up.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click up on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()

                type_learning="up"
                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&& type_learning=="up"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()
                                    println("sended")
                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("up".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                up.setBackgroundResource(R.drawable.up_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })

                true
            }
            down.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click down on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()
                type_learning="down"
                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&&type_learning=="down"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()
                                    println("sended")
                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("down".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                down.setBackgroundResource(R.drawable.down_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })

                true
            }
            ok.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click ok on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()

                type_learning="ok"

                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&&type_learning=="ok"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()
                                    println("sended")
                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("ok".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                ok.setBackgroundResource(R.drawable.ok_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })

                true
            }
            back.setOnLongClickListener {
                sharedViewModel2.update_current_IR_learning("")
                step_one.setText("Step 1 : click back on your control")
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                is_lerning=true
                step_one_learn_ir()

                type_learning="back"
                sharedViewModel2.current_IR_learning.observe(viewLifecycleOwner, Observer { recived_message ->
                    if (recived_message!=""&&type_learning=="back"){
                        println("1111111111111111111$recived_message")
                        val splited_message = recived_message!!.split("~>")



                        if (is_lerning && splited_message[1] == current_ir!!.mac) {
                            step_two_learn_ir()
                            println("pass")


                            // send and test

                            try {

                                Thread{
                                    UdpListener8089.pause()
                                    println("sended")
                                    send_ir_cmd(splited_message[2],splited_message[3],splited_message[1],splited_message[4],splited_message[5],splited_message[6])
                                    UdpListener8089.resume()
                                }.start()
                            }catch (e:Exception){
                                println(e)
                                println("injaa")
                                UdpListener8089.resume()
                            }

                            done_ir_learn.setOnClickListener {
                                current_ir!!.ip = splited_message[6]

// به‌روزرسانی subtype
                                val subtype = current_ir!!.sub_type!!.split(",").toMutableList()
                                subtype.add(current_device.trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.sub_type = subtype.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.sub_type)

// به‌روزرسانی cmd
                                val cmd = current_ir!!.cmd!!.split(",").toMutableList()
                                cmd.add("${splited_message[2]}:${splited_message[3]}:${splited_message[4]}:${splited_message[5]}".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.cmd = cmd.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.cmd)

// به‌روزرسانی type
                                val type = current_ir!!.type!!.split(",").toMutableList()
                                type.add("back".trim())
// حذف مقادیر خالی و به‌روزرسانی
                                current_ir!!.type = type.filterNot { it.isBlank() }.joinToString(",").trim()
                                println(current_ir!!.type)

// به‌روزرسانی پایگاه داده
                                ir_db.updateIrById(current_ir!!.id, current_ir!!)
                                popupWindow.dismiss()
                                all_irs = ir_db.getAllIrs()
                                back.setBackgroundResource(R.drawable.back_icon_on)
                            }

                            try_again_ir_learn.setOnClickListener{
                                sharedViewModel2.update_current_IR_learning("")
                                step_one_learn_ir()
                            }




                        }
                    }



                })

                true
            }



        }else{
            first_device.visibility=View.INVISIBLE
            second_device.visibility=View.INVISIBLE
            therd_device.visibility=View.INVISIBLE

        }



    }

}