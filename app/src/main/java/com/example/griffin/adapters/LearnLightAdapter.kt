package com.example.griffin.adapters

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import android.widget.Button
import com.example.griffin.mudels.Light

class Item(val title: String, val description: String)



class LearnLightAdapter(
    private var items: List<Light?>,
    private val onItemClick: (Light) -> Unit
) : RecyclerView.Adapter<LearnLightAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.light_learn_model)

        fun bind(item: Light) {
            button.text = item.Lname
            button.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.light_learn_added, parent, false)
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
