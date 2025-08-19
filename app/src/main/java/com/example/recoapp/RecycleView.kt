package com.example.recoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recoapp.data.Waste
import java.text.SimpleDateFormat
import java.util.*


class WasteAdapter : ListAdapter<Waste, WasteAdapter.WasteViewHolder>(WasteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_waste, parent, false)
        return WasteViewHolder(view)
    }

    override fun onBindViewHolder(holder: WasteViewHolder, position: Int) {
        val currentWaste = getItem(position)
        holder.bind(currentWaste)
    }

    class WasteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val typeText: TextView = itemView.findViewById(R.id.textTipo)
        private val quantityText: TextView = itemView.findViewById(R.id.textCantidad)
        private val locationText: TextView = itemView.findViewById(R.id.textUbicacion)
        private val dateText: TextView = itemView.findViewById(R.id.textFecha)

        fun bind(waste: Waste) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val context = itemView.context

            typeText.text = context.getString(R.string.label_waste_type, waste.type)
            quantityText.text = context.getString(R.string.label_quantity, waste.quantity)
            locationText.text = context.getString(R.string.label_location, waste.location)
            dateText.text = context.getString(R.string.label_date, dateFormat.format(waste.date))
        }
    }

    class WasteDiffCallback : DiffUtil.ItemCallback<Waste>() {
        override fun areItemsTheSame(oldItem: Waste, newItem: Waste): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Waste, newItem: Waste): Boolean {
            return oldItem == newItem
        }
    }
}