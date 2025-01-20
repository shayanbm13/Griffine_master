package com.example.griffin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.mudels.Light

class light_add_scenario_Adapter2(
    private var items: List<Light?>,
    private val selectedItems: List<String>?, // اضافه کردن لیست آیتم‌های انتخاب‌شده
    private val onItemClick: (Light) -> Unit
) : RecyclerView.Adapter<light_add_scenario_Adapter2.ViewHolder>() {

    private val clickStates = MutableList(items.size) { false }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.light_add_scenario)
        val name_list =""


        fun bind(item: Light) {
            button.text = item.Lname

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                // اگر آیتم در لیست آیتم‌های انتخاب‌شده باشد، بک‌گراند را تغییر دهید
                println(selectedItems)
                println(item.Lname)
                if (selectedItems?.contains(item.Lname) == true || clickStates[position]) {
                    println("bbbbooooooooooood")
                    button.setBackgroundResource(R.drawable.selected_light)
                    clickStates[position]=true
                } else {
                    button.setBackgroundResource(R.drawable.deselected_light)
                }
            }

            button.setOnClickListener {
                clickStates[position] = !clickStates[position]
                // اگر کلیک شده بود بک‌گراند را تغییر دهید
                if (clickStates[position]) {
                    button.setBackgroundResource(R.drawable.selected_light)
                } else {
                    button.setBackgroundResource(R.drawable.deselected_light)
                }
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.light_addscenario_model, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    fun setItems(newItems: List<Light?>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun clearItems() {
        items = emptyList()
        notifyDataSetChanged()
    }
}