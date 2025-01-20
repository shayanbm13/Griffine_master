import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.griffin.R

data class Item2(val name: String)

class devicesAdapter(
    private var drawable: Drawable?,
    private var buttonText: String,
    private val onItemClick: (Item2) -> Unit
) : RecyclerView.Adapter<devicesAdapter.ItemViewHolder>() {

    private val items: MutableList<Item2> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.devices_item, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(item: Item2) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun updateDrawableAndText(newDrawable: Drawable?, newText: String) {
        drawable = newDrawable
        buttonText = newText
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val addButton: Button = itemView.findViewById(R.id.my_device_row)

        fun bind(item: Item2) {
            addButton.text = buttonText
            addButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)

            addButton.setOnClickListener {
                onItemClick.invoke(item)
            }
        }
    }
}
