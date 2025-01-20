package com.example.griffin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R

class stylesAdapter(private val images: List<Int>, private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<stylesAdapter.ViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(resourceName: String)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.style_imageview)


        fun bind(imageResId: Int) {
            imageView.setImageResource(imageResId)
            val resourceName = itemView.resources.getResourceEntryName(imageResId)
            itemView.setOnClickListener { onItemClickListener.onItemClick(resourceName) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.styles_image, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageRes = images[position]
        holder.bind(imageRes)
    }


    override fun getItemCount(): Int {
        return images.size
    }
}
