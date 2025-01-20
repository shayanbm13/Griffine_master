package com.example.griffin.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.mudels.Light





class LightAdapter(
    private var items: List<Light?>,

    private val onItemClick: (Light , SwitchCompat) -> Unit
) : RecyclerView.Adapter<LightAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lightLayer: ConstraintLayout = itemView.findViewById(R.id.light_layer)
        val lightSwitch: SwitchCompat = itemView.findViewById(R.id.light_on_of)


        @SuppressLint("ClickableViewAccessibility")
        fun bind(item: Light) {
            lightLayer.findViewById<TextView>(R.id.light_name).text = item.Lname

            lightSwitch.setOnTouchListener { _, event ->
                // جلوگیری از تغییر وضعیت با درگ کردن
                if (event.actionMasked == MotionEvent.ACTION_MOVE) {
                    return@setOnTouchListener true
                }
                false
            }

            // انجام عملیات مربوط به اطلاعات و موارد دیگر
            lightSwitch.isChecked = item.status=="on"



            lightSwitch.setOnClickListener {
                onItemClick(item, lightSwitch)
                if(item.status=="on"){
                    item.status="off"

                }else{
                    item.status="on"

                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.light_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (item != null) {
            holder.bind(item)
        }
    }


    fun updateLight(id: Int, updatedLight: Light) {
        val updatedItems = items.toMutableList()
        val position = updatedItems.indexOfFirst { it?.id == id }
        if (position != -1) {
            updatedItems[position] = updatedLight
            items = updatedItems
            notifyItemChanged(position)
        }
    }


    fun updateSwitchStatus(item: Light, isChecked: Boolean) {
        item.status = if (isChecked) "on" else "off"
        notifyDataSetChanged()
    }


    fun setItems(newItems: List<Light?>) {
        items = newItems
        notifyDataSetChanged() // اینجا مهم است تا RecyclerView را رفرش کنید
    }

    override fun getItemCount(): Int {
        return items.size
    }
    fun clearItems() {
        items = emptyList()
        notifyDataSetChanged()
    }

}