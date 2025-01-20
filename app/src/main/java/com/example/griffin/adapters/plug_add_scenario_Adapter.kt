package com.example.griffin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.mudels.Plug


class plug_add_scenario_Adapter(
    private var items: List<Plug?>,
    private val onItemClick: (Plug) -> Unit
) : RecyclerView.Adapter<plug_add_scenario_Adapter.ViewHolder>() {

    private val clickStates = MutableList(items.size) { false }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.plug_learn_added)

        fun bind(item: Plug) {
            // Set the text
            button.text = item.Pname

            // Set the initial image based on the click state
            val position = adapterPosition // دریافت موقعیت در آداپتر
            if (position != RecyclerView.NO_POSITION) {
                if (clickStates[position]) {
                    button.setBackgroundResource(R.drawable.plug_selected)
                } else {
                    button.setBackgroundResource(R.drawable.plug_of)
                }
            }

            button.setOnClickListener {
//                    resetClickStates()
                // Toggle the click state for this item
                clickStates[position] = !clickStates[position]
                // Update the button's image based on the new click state
                if (clickStates[position]) {
                    button.setBackgroundResource(R.drawable.plug_selected)
                } else {
                    button.setBackgroundResource(R.drawable.plug_of)
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
            .inflate(R.layout.plug_learn_model, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    fun setItems(newItems: List<Plug?>) {
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