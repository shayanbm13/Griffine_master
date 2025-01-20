package com.example.griffin.fragment.setting_three

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.griffin.R
import com.example.griffin.database.six_workert_db
import com.example.griffin.mudels.*
import com.example.griffin.setting


class setting_local_moreoption : Fragment() {

    val sharedViewModel:SharedViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting_local_moreoption, container, false)
    }



    
    override fun onResume() {
        super.onResume()
        val send_to_key =requireView().findViewById<Button>(R.id.send_to_key)
        val rename =requireView().findViewById<Button>(R.id.rename)
        val more_option_dalate =requireView().findViewById<Button>(R.id.more_option_dalate)
        val more_option_rename =requireView().findViewById<Button>(R.id.rename)
        val more_option_find =requireView().findViewById<Button>(R.id.more_option_find)
        val sixWorkertDb=six_workert_db.getInstance(requireContext())


        more_option_find.setOnClickListener {
            Thread{
                requireActivity().runOnUiThread(){

                    Toast.makeText(requireContext(), "Looking for multifunctional", Toast.LENGTH_LONG).show()
                }
                var myip= checkIP(requireContext())
                get_mac_curtain(requireContext(),myip)
                UdpListener8089.pause()
                receiveUdpMessage({ receivedMessage ->
                    if (receivedMessage=="failed") {
                        println("broadcast failed..")
                    }else{
                        println(receivedMessage)
                        requireActivity().runOnUiThread {
                            var receivedmessage_decoded= extract_response(receivedMessage)
                            var macip= receivedmessage_decoded[0]
                            var type=receivedmessage_decoded[4]
                            var pole_num=receivedmessage_decoded[1]
                            var ip=receivedmessage_decoded[3]

//                            Toast.makeText(requireContext(), macip, Toast.LENGTH_SHORT).show()
//                            Toast.makeText(requireContext(), type, Toast.LENGTH_SHORT).show()
//                            Toast.makeText(requireContext(), pole_num, Toast.LENGTH_SHORT).show()
                            println(macip)
                            println(type)
                            println(pole_num)
                            println(ip)

                            if (type=="SixC"){
                                Toast.makeText(requireContext(), "Founded", Toast.LENGTH_SHORT).show()


                                val six_workert_db=six_workert_db.getInstance(requireContext())
                                six_workert_db.deleteRowsWithNullOrNewLocalName()
                                val added_six_worker =six_workert()
                                val six_worker_count = six_workert_db.getAllsix_workerts().count()
                                added_six_worker.name="new Local " + ((six_worker_count)+1)
                                added_six_worker.pole_num=pole_num.toInt().toString() + "-"
                                added_six_worker.sub_type=",,,,,"
                                added_six_worker.type=",,,,,"
                                added_six_worker.status=",,,,,"
                                added_six_worker.ip="192.168.1.1"
                                added_six_worker.mac=macip

                                added_six_worker.work_name=",,,,,"

                                six_workert_db.set_to_db_six_workert(added_six_worker)
                                sharedViewModel.update_six_worker_list(six_workert_db.getAllsix_workerts())
                                val settingactivity= requireActivity() as setting


                                settingactivity.changeViewPagerPage_four(0)
//                                settingactivity.changeViewPagerPage_four(2)



                            }


                        }

                    }


                    println("Received: $receivedMessage")
                }, 8089, 1300)
                UdpListener8089.resume()

            }.start()




        }

        sharedViewModel.current_six_wirker.observe(viewLifecycleOwner, Observer { current_six_worker_id ->
            println(current_six_worker_id)

            more_option_dalate.setOnClickListener {
                if(current_six_worker_id !="khali"){

                    sixWorkertDb.delete_from_db_six_workert(current_six_worker_id!!.toInt())
                    sharedViewModel.update_current_six_wirker("delete")
                }

            }
            more_option_rename.setOnClickListener {
                if(current_six_worker_id !="khali"){

                    val inflater = LayoutInflater.from(requireContext())

                    val customPopupView: View = inflater.inflate(R.layout.popup_addlight, null)
                    val popupView: View = inflater.inflate(R.layout.popup_addlight, null)

                    val edit_light_name_pupop = customPopupView.findViewById<EditText>(R.id.edit_light_name_pupop)
                    val ok_name_light_pupop = customPopupView.findViewById<Button>(R.id.ok_name_light_pupop)
                    val learn_light_pupop = customPopupView.findViewById<Button>(R.id.learn_light_pupop)
                    val delete_light_pupop = customPopupView.findViewById<Button>(R.id.delete_light_pupop)
                    val on_off_test_light_pupop = customPopupView.findViewById<Button>(R.id.on_off_test_light_pupop)
                    val send_to_pupop = customPopupView.findViewById<Button>(R.id.send_to)

                    learn_light_pupop.visibility=View.INVISIBLE
                    delete_light_pupop.visibility=View.INVISIBLE
                    on_off_test_light_pupop.visibility=View.INVISIBLE
                    send_to_pupop.visibility=View.INVISIBLE
                    val popupWidth = 480
                    val popupHeight = 570

                    // ایجاد لایه‌ی کاستوم


                    // ایجاد PopupWindow با استفاده از لایه‌ی کاستوم
                    val popupWindow = PopupWindow(customPopupView, popupWidth, popupHeight, true)

                    val alertDialogBuilder = AlertDialog.Builder(requireContext())
                    alertDialogBuilder.setView(popupView)

                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.setCanceledOnTouchOutside(false)




                    var updated_sixworker = sixWorkertDb.get_from_db_six_workert(current_six_worker_id?.toInt())
                    if (updated_sixworker != null) {
                        edit_light_name_pupop.setText(updated_sixworker.name)
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                        ok_name_light_pupop.setOnClickListener {

                            updated_sixworker?.name=edit_light_name_pupop.text.toString()
                            sixWorkertDb.updatesix_workertById(current_six_worker_id?.toInt(),updated_sixworker)
                            popupWindow.dismiss()
                            sharedViewModel.update_current_six_wirker("rename")
                        }
                    }


                }

            }



        })

    }



}