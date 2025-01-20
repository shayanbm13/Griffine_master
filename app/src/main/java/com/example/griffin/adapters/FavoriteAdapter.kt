package com.example.griffin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R
import com.example.griffin.fragment.favorite
import com.example.griffin.mudels.Favorite
import com.example.griffin.mudels.scenario

class FavoriteAdapter(
    private var items: List<Favorite?>,
    private val onItemClick: (Favorite) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    private val clickStates = MutableList(items.size) { false }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.favorite_item)

        fun bind(item: Favorite) {
            // Set the text
            button.text = item.name

            // Set the initial image based on the click state
            val position = adapterPosition // دریافت موقعیت در آداپتر


            button.setOnClickListener {
//                    resetClickStates()
                // Toggle the click state for this item




                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_model, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        if (item != null) {
            holder.bind(item)
        }
    }
    fun setItems(newItems: List<Favorite?>) {
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