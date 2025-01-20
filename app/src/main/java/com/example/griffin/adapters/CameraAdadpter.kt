package com.example.griffin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.mudels.camera

class CameraAdadpter(
    private var items: List<camera?>,
    private val onItemClick: (camera) -> Unit
) : RecyclerView.Adapter<CameraAdadpter.ViewHolder>() {



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.camera_learn_added)

        init {
            button.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    if (item != null) {
                        onItemClick(item)
                    }
                }
            }
        }

        fun bind(item: camera) {
            // Set the text
            button.text = item.CAMname
        }
    }
    fun resetClickStates() {

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.camera_learn_model, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    fun setItems(newItems: List<camera?>) {
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