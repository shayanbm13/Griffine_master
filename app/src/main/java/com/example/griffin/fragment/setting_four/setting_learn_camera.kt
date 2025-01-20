package com.example.griffin.fragment.setting_four

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.griffin.R
import com.example.griffin.database.camera_db
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.camera

class setting_learn_camera : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var currentCam = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting_learn_camera, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cameraUser = view.findViewById<EditText>(R.id.learn_camera_user)
        val cameraPassword = view.findViewById<EditText>(R.id.learn_camera_password)
        val cameraIpAddress = view.findViewById<EditText>(R.id.learn_camera_ipaddress)
        val cameraPort = view.findViewById<EditText>(R.id.learn_camera_port)
        val cameraChannel = view.findViewById<EditText>(R.id.learn_camera_channel)
        val cameraSubtype = view.findViewById<EditText>(R.id.learn_camera_subtype)

        val camName = view.findViewById<TextView>(R.id.cam_name)
        val nextCam = view.findViewById<ImageButton>(R.id.next_cam)
        val exCam = view.findViewById<ImageButton>(R.id.ex_cam)

        var camDb = camera_db.getInstance(requireContext())
        var camLists = camDb.getAllcameras()
        var camCount = camLists.count()

        if (camCount > 0) {
            val camera = camLists[0]
            camName.text = camera?.CAMname
            cameraIpAddress.setText( camera?.ip)
            cameraChannel.setText(camera?.chanel)
            cameraUser.setText(camera?.user)
            cameraPassword.setText(camera?.pass)
            cameraPort.setText(camera?.port)
            cameraSubtype.setText(camera?.subtype)

            println("test 1 ")
            println(camLists[0]!!.id)
            sharedViewModel.update_current_camera(camLists[0])
        }

        nextCam.setOnClickListener {
            if (camCount  > currentCam+(1) ) {
                val camera = camLists[currentCam+(1)]
                camName.text = camera?.CAMname
                cameraIpAddress.setText( camera?.ip)
                cameraChannel.setText(camera?.chanel)
                cameraUser.setText(camera?.user)
                cameraPassword.setText(camera?.pass)
                cameraPort.setText(camera?.port)
                cameraSubtype.setText(camera?.subtype)
//                println(camera!!.id)
                currentCam++
                println("test 2 ")
                println(camera!!.id)
                sharedViewModel.update_current_camera(camera)
            } else if (camCount == currentCam+(1) ) {
                camName.setText("camera ${currentCam + (2)}")
                cameraIpAddress.setText("")
                cameraChannel.setText("")
                cameraUser.setText("")
                cameraPassword.setText("")
                cameraPort.setText("")
                cameraSubtype.setText("")
                val camera = camera()
                println("test 3 ")
                println(camera!!.id)
                sharedViewModel.update_current_camera(camera)
                currentCam++
            }
        }

        exCam.setOnClickListener {
            if (currentCam >0) {
                if (currentCam > camCount){
                    val camera = camLists[camCount - (1)]
                    camName.text = camera?.CAMname
                    cameraIpAddress.setText(camera?.ip)
                    cameraChannel.setText(camera?.chanel)
                    cameraUser.setText(camera?.user)
                    cameraPassword.setText(camera?.pass)
                    cameraPort.setText(camera?.port)
                    cameraSubtype.setText(camera?.subtype)
                    currentCam--
                    println("test 4 ")
                    println(camera!!.id)
                    sharedViewModel.update_current_camera(camera)
                }else{
                    if (currentCam!=0){

                        val camera = camLists[currentCam - 1]
                        camName.text = camera?.CAMname
                        cameraIpAddress.setText(camera?.ip)
                        cameraChannel.setText(camera?.chanel)
                        cameraUser.setText(camera?.user)
                        cameraPassword.setText(camera?.pass)
                        cameraPort.setText(camera?.port)
                        cameraSubtype.setText(camera?.subtype)
                        currentCam--
                        println("test 5 ")
                        println(camera!!.id)
                        sharedViewModel.update_current_camera(camera)
                    }

                }


            }
        }


        sharedViewModel.cam_update.observe(viewLifecycleOwner, Observer { up ->
            camDb = camera_db.getInstance(requireContext())
            camLists = camDb.getAllcameras()
            camCount = camLists.count()
            if (up == "1"){
                sharedViewModel.update_cam_update("0")
                if (camCount > 0) {
                    val camera = camLists[camCount - (1)]
                    camName.text = camera?.CAMname
                    cameraIpAddress.setText( camera?.ip)
                    cameraChannel.setText(camera?.chanel)
                    cameraUser.setText(camera?.user)
                    cameraPassword.setText(camera?.pass)
                    cameraPort.setText(camera?.port)
                    cameraSubtype.setText(camera?.subtype)
                    currentCam=0
                } else if (camCount == 0) {
                    cameraIpAddress.setText("")
                    cameraChannel.setText("")
                    cameraUser.setText("")
                    cameraPassword.setText("")
                    cameraPort.setText("")
                    cameraSubtype.setText("")
                    val camera = camera()
                    currentCam=0
                    println("test 6 ")
                    println(camera!!.id)
                    sharedViewModel.update_current_camera(camera)

                }

            }else if (up=="2"){

                sharedViewModel.update_cam_update("0")



            }



        })


        val editTextList = listOf(
            cameraIpAddress,
            cameraChannel,
            cameraUser,
            cameraPassword,
            cameraPort,
            cameraSubtype
        )

        for (editText in editTextList) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // اجرای عملیات قبل از تغییر متن
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // اجرای عملیات در زمان تغییر متن
                }

                override fun afterTextChanged(s: Editable?) {


                    if ((camName.text.toString() != "" && camName.text.toString() != null) && (cameraIpAddress.text.toString() != "" && cameraIpAddress.text.toString() != null) && (cameraChannel.text.toString() != "" && cameraChannel.text.toString() != null) && (cameraUser.text.toString() != "" && cameraUser.text.toString() != null) && (cameraPassword.text.toString() != "" && cameraPassword.text.toString() != null) && (cameraPort.text.toString() != "" && cameraPort.text.toString() != null) && (cameraSubtype.text.toString() != "" && cameraSubtype.text.toString() != null)) {


                        val myCamera = camera()
                        myCamera.CAMname = camName.text.toString()
                        myCamera.ip = cameraIpAddress.text.toString()
                        myCamera.chanel = cameraChannel.text.toString()
                        myCamera.user = cameraUser.text.toString()
                        myCamera.pass = cameraPassword.text.toString()
                        myCamera.port = cameraPort.text.toString()
                        myCamera.subtype = cameraSubtype.text.toString()
                        val current_cam = camDb.getcameraByCAMname(camName.text.toString())
                        myCamera.id = current_cam?.id

//
//                        currentCam++
                        // بررسی و آپدیت SharedViewModel و دیگر متغیرها
                        println("test 7 ")
                        println(myCamera!!.id)
                        sharedViewModel.update_current_camera(myCamera)
                        camDb = camera_db.getInstance(requireContext())
                        camLists = camDb.getAllcameras()
                        camCount = camLists.count()

                    }
                }
            })
        }
    }
}
