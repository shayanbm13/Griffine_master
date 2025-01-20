package com.example.griffin.fragment.griffin_home_frags

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.mudels.fan

class fan_add_scenario_Adapter2 (
    private var items: List<fan?>,
    private val selectedItems: List<String>?, // اضافه کردن لیست آیتم‌های انتخاب‌شده
    private val onItemClick: (fan) -> Unit
) : RecyclerView.Adapter<fan_add_scenario_Adapter2.ViewHolder>() {

    private val clickStates = MutableList(items.size) { false }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.fan_learn_added)

        fun bind(item: fan) {
            // Set the text
            button.text = item.Fname

            // Set the initial image based on the click state
            val position = adapterPosition // دریافت موقعیت در آداپتر
            if (position != RecyclerView.NO_POSITION) {
                if (selectedItems?.contains(item.Fname) == true || clickStates[position]) {
                    button.setBackgroundResource(R.drawable.fan_on)
                    clickStates[position]=true
                } else {
                    button.setBackgroundResource(R.drawable.fan_of)
                }
            }

            button.setOnClickListener {
//                    resetClickStates()
                // Toggle the click state for this item
                clickStates[position] = !clickStates[position]
                // Update the button's image based on the new click state
                if (clickStates[position]) {
                    button.setBackgroundResource(R.drawable.fan_on)
                } else {
                    button.setBackgroundResource(R.drawable.fan_of)
                }
                onItemClick(item)
            }
        }
    }
    fun resetClickStates() {
        clickStates.fill(false)
        notifyDataSetChanged()

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fan_learn_added, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    fun setItems(newItems: List<fan?>) {
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