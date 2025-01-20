package com.example.griffin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.mudels.Light
import com.example.griffin.mudels.Plug




class light_add_scenario_Adapter(
        private var items: List<Light?>,
        private val onItemClick: (Light) -> Unit
    ) : RecyclerView.Adapter<light_add_scenario_Adapter.ViewHolder>() {

        private val clickStates = MutableList(items.size) { false }
        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val button: Button = itemView.findViewById(R.id.light_add_scenario)

            fun bind(item: Light) {
                // Set the text
                button.text = item.Lname

                // Set the initial image based on the click state
                val position = adapterPosition // دریافت موقعیت در آداپتر
                if (position != RecyclerView.NO_POSITION) {
                    if (clickStates[position]) {
                        button.setBackgroundResource(R.drawable.selected_light)
                    } else {
                        button.setBackgroundResource(R.drawable.deselected_light)
                    }
                }

                button.setOnClickListener {
//                    resetClickStates()
                    // Toggle the click state for this item
                    clickStates[position] = !clickStates[position]
                    // Update the button's image based on the new click state
                    if (clickStates[position]) {
                        button.setBackgroundResource(R.drawable.selected_light)
                    } else {
                        button.setBackgroundResource(R.drawable.deselected_light)
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