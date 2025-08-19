package com.example.recoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recoapp.data.Waste
import java.text.SimpleDateFormat
import java.util.*

class WasteAdapter : RecyclerView.Adapter<WasteAdapter.ViewHolder>() {

    private var wasteList: List<Waste> = emptyList()

    fun submitList(list: List<Waste>) {
        wasteList = list
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val typeText: TextView = view.findViewById(R.id.textTipo)
        val quantityText: TextView = view.findViewById(R.id.textCantidad)
        val locationText: TextView = view.findViewById(R.id.textUbicacion)
        val dateText: TextView = view.findViewById(R.id.textFecha)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_waste, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = wasteList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val waste = wasteList[position]
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        holder.typeText.text = waste.type
        holder.quantityText.text = waste.quantity.toString()
        holder.locationText.text = waste.location
        holder.dateText.text = dateFormat.format(waste.date)
    }
}
