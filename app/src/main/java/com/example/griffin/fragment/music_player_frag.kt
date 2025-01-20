package com.example.griffin.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.fragment.griffin_home_frags.MusicAdapter
import com.example.griffin.mudels.MusicModel
import com.example.griffin.mudels.SharedViewModel
import com.example.griffin.mudels.musicList


class music_player_frag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private  val SharedViewModel: SharedViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_player_frag, container, false)
        println("create")
    }

    @SuppressLint("Range")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("createddd")
//
//        val musicList = mutableListOf<MusicModel>()

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val cursor = requireContext().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )

        val new_list=
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))

                val new=MusicModel(title, artist, path, path,"false")
                val current_urls = mutableListOf<String>()
                for (item in musicList){
                    current_urls.add(item.audioUrl)

                }
                // افزودن اطلاعات به لیست

                if (new.audioUrl !in current_urls){
                    musicList.add(new)

                }

            }
            cursor.close()
        } else {

        }

        val recyclerView: RecyclerView = requireView().findViewById(R.id.music_recyclerview)
        val show_musics = view?.findViewById<Button>(R.id.show_musics)
        val adapter = MusicAdapter.getInstance(requireContext())

//        adapter.onItemClickCallback = { position ->
//            // اجرای کد مورد نظر با استفاده از position
//            println("clickd")
//            // مثال: نمایش جزئیات، پخش موسیقی، و غیره.
//        }

        val layoutManager = LinearLayoutManager(requireContext())

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


        val targetPosition = 5
        val offset = 200 // موقعیت دقیق که می‌خواهید اسکرول شود (به صورت پیکسل)

        var play_target : MusicModel?=null

        for ( item in musicList){
            if (item.isplaying=="true"){

                play_target=item
            }

        }

        if (play_target!=null){
            println(play_target)
            println("ok")
            var index= musicList.indexOf(play_target)
            println(index)
            if (musicList.count()== index+4){
                index -=3

            }else if(musicList.count() < index+3){
                index -=2

            }
            layoutManager.scrollToPositionWithOffset(index, offset)

        }






    }

    override fun onResume() {
        super.onResume()
        println("resume")

    }

}
