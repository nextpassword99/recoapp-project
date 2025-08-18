package com.example.recoapp.ui.history.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recoapp.R
import com.example.recoapp.data.entity.Residuo
import com.example.recoapp.databinding.ItemWasteRecordBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Adapter para RecyclerView que muestra el historial de residuos
 * 
 * Implementa ListAdapter para un rendimiento eficiente con DiffUtil
 * para ECOLIM S.A.C. sistema de gestión de residuos.
 */
class WasteHistoryAdapter(
    private val onEditClick: (Residuo) -> Unit,
    private val onDeleteClick: (Residuo) -> Unit,
    private val onItemClick: (Residuo) -> Unit
) : ListAdapter<Residuo, WasteHistoryAdapter.WasteViewHolder>(WasteDiffCallback()) {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteViewHolder {
        val binding = ItemWasteRecordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WasteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WasteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WasteViewHolder(
        private val binding: ItemWasteRecordBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(residuo: Residuo) {
            binding.apply {
                // Información básica
                tvWasteType.text = residuo.tipo
                tvWasteQuantity.text = "${residuo.cantidad} kg"
                tvWasteLocation.text = residuo.ubicacion
                tvWasteDate.text = dateFormat.format(residuo.fecha)

                // Comentarios (solo mostrar si hay contenido)
                if (residuo.comentarios.isNotEmpty()) {
                    tvWasteComments.text = residuo.comentarios
                    tvWasteComments.visibility = View.VISIBLE
                } else {
                    tvWasteComments.visibility = View.GONE
                }

                // Color del indicador según tipo de residuo
                colorIndicator.setBackgroundColor(getColorForWasteType(residuo.tipo))

                // Click listeners
                root.setOnClickListener {
                    onItemClick(residuo)
                }

                btnEdit.setOnClickListener {
                    onEditClick(residuo)
                }

                btnDelete.setOnClickListener {
                    onDeleteClick(residuo)
                }

                // Mostrar botones de acción en click largo
                root.setOnLongClickListener {
                    val isVisible = actionButtons.visibility == View.VISIBLE
                    actionButtons.visibility = if (isVisible) View.GONE else View.VISIBLE
                    true
                }
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

    /**
     * DiffUtil.ItemCallback para calcular diferencias entre listas de manera eficiente
     */
    class WasteDiffCallback : DiffUtil.ItemCallback<Residuo>() {
        override fun areItemsTheSame(oldItem: Residuo, newItem: Residuo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Residuo, newItem: Residuo): Boolean {
            return oldItem == newItem
        }
    }
}