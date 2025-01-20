package com.example.griffin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R

class changeroom_adapter(private val itemList: ArrayList<String?>, private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<changeroom_adapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textItem: TextView = itemView.findViewById(R.id.textItem)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = itemList[position]
                    if (clickedItem != null) {
                        onItemClick.invoke(clickedItem)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.textItem.text = item

        // تنظیم رنگ بک‌گراند با توجه به موقعیت ایتم در لیست
        if (position % 2 == 0) {
            // رنگ زمینه زوج
            holder.itemView.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.evenBackgroundColor))
        } else {
            // رنگ زمینه فرد
            holder.itemView.setBackgroundColor(holder.itemView.context.resources.getColor(R.color.oddBackgroundColor))
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}