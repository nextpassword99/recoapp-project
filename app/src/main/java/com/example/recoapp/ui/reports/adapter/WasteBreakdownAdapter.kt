package com.example.recoapp.ui.reports.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recoapp.data.dao.EstadisticaTipo
import com.example.recoapp.databinding.ItemWasteBreakdownBinding

/**
 * Adapter para el desglose de residuos por tipo en reportes
 * 
 * Muestra estadísticas agrupadas por tipo de residuo para ECOLIM S.A.C.
 */
class WasteBreakdownAdapter : ListAdapter<WasteBreakdownItem, WasteBreakdownAdapter.BreakdownViewHolder>(BreakdownDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakdownViewHolder {
        val binding = ItemWasteBreakdownBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BreakdownViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BreakdownViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BreakdownViewHolder(
        private val binding: ItemWasteBreakdownBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WasteBreakdownItem) {
            binding.apply {
                tvBreakdownType.text = item.tipo
                tvBreakdownCount.text = "${item.count} registros"
                tvBreakdownWeight.text = String.format("%.1f kg", item.totalWeight)
                
                // Color del indicador según tipo de residuo
                colorIndicatorBreakdown.setBackgroundColor(getColorForWasteType(item.tipo))
            }
        }

        private fun getColorForWasteType(tipo: String): Int {
            return when (tipo.lowercase()) {
                "residuos orgánicos" -> Color.parseColor("#4CAF50") // Verde
                "residuos reciclables" -> Color.parseColor("#2196F3") // Azul
                "residuos peligrosos" -> Color.parseColor("#F44336") // Rojo
                "residuos electrónicos" -> Color.parseColor("#FF9800") // Naranja
                "residuos generales" -> Color.parseColor("#9E9E9E") // Gris
                else -> Color.parseColor("#607D8B") // Azul gris por defecto
            }
        }
    }

    class BreakdownDiffCallback : DiffUtil.ItemCallback<WasteBreakdownItem>() {
        override fun areItemsTheSame(oldItem: WasteBreakdownItem, newItem: WasteBreakdownItem): Boolean {
            return oldItem.tipo == newItem.tipo
        }

        override fun areContentsTheSame(oldItem: WasteBreakdownItem, newItem: WasteBreakdownItem): Boolean {
            return oldItem == newItem
        }
    }
}

/**
 * Data class para los elementos del desglose de residuos
 */
data class WasteBreakdownItem(
    val tipo: String,
    val count: Int,
    val totalWeight: Double
)