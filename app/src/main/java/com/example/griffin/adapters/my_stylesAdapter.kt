package com.example.griffin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R

class my_stylesAdapter(private val items: List<String?>, private val onButtonClicked: (String) -> Unit) :
    RecyclerView.Adapter<my_stylesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.my_styles_row)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.style_rows, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.button.text = item

        holder.button.setOnClickListener {
            if (item != null) {
                onButtonClicked(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}