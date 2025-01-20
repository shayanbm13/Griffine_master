package com.example.griffin.mudels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.media.AudioManager
import android.util.AttributeSet
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat.getSystemService
import com.example.griffin.R

class CustomSeekBar(context: Context, attrs: AttributeSet) : AppCompatSeekBar(context, attrs) {

    private val paint = Paint()

    private val dotRadius = 5f
    private val totalDots = 10
    private var dotBitmapOff: Bitmap
    private var dotBitmapOn: Bitmap

    val seekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            // عملیات مورد نظر را انجام دهید
            // اینجا مثالی از نمایش پیام به کاربر است
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val volume = ((seekBar.progress / 100.0) * 12).toInt()
            println(seekBar.progress)
//
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0)



            println(seekBar.progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {

            // متدی که وقتی کاربر شروع به حرکت دادن سیکبار می‌کند فراخوانی می‌شود
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
            // متدی که وقتی کاربر دست از سیکبار برمی‌دارد فراخوانی می‌شود
        }

    }


    init {
        dotBitmapOff = BitmapFactory.decodeResource(resources, R.drawable.circular_dot_off)
        dotBitmapOn = BitmapFactory.decodeResource(resources, R.drawable.circular_dot_on)
        setOnSeekBarChangeListener(seekBarChangeListener)
    }

    fun setCustomProgress(progress: Int) {
        setProgress(progress)
        invalidate()
    }


    fun getCustomProgress(): Int {
        return progress
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val availableWidth = width - paddingLeft
        val interval = availableWidth.toFloat() / totalDots
        val progressInterval = max.toFloat() / totalDots

        val dotWidth = dotBitmapOff.width
        val dotHeight = dotBitmapOff.height

        for (i in 0 until totalDots) {
            val dotX = paddingLeft + interval * i
            val dotBitmap = if (progress >= progressInterval * i) dotBitmapOn else dotBitmapOff
            canvas.drawBitmap(dotBitmap, dotX - dotWidth / 2, height / 2f - dotHeight / 2, paint)
        }
    }}

