package com.example.recoapp.ui.history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recoapp.R
import com.example.recoapp.data.entity.Residuo
import com.example.recoapp.databinding.FragmentHistoryBinding
import com.example.recoapp.ui.history.adapter.WasteHistoryAdapter

/**
 * Fragment que muestra el historial de residuos recolectados
 * 
 * Utiliza RecyclerView para mostrar la lista de residuos registrados
 * en ECOLIM S.A.C. con funcionalidades de búsqueda y CRUD.
 */
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var wasteAdapter: WasteHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupRecyclerView()
        setupSearchFunctionality()
        observeViewModel()

        return root
    }

    private fun setupRecyclerView() {
        wasteAdapter = WasteHistoryAdapter(
            onEditClick = { residuo ->
                // TODO: Implementar edición de residuo
                Toast.makeText(context, "Editar: ${residuo.tipo}", Toast.LENGTH_SHORT).show()
            },
            onDeleteClick = { residuo ->
                showDeleteConfirmation(residuo)
            },
            onItemClick = { residuo ->
                // TODO: Mostrar detalles del residuo
                showWasteDetails(residuo)
            }
        )

        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = wasteAdapter
        }
    }

    private fun setupSearchFunctionality() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: ""
                historyViewModel.searchWaste(query)
            }
        })
    }

    private fun observeViewModel() {
        // Observar lista de residuos
        historyViewModel.residuos.observe(viewLifecycleOwner) { residuos ->
            wasteAdapter.submitList(residuos)
            updateEmptyState(residuos.isEmpty())
            updateStatistics(residuos)
        }

        // Observar resultado de eliminación
        historyViewModel.deleteResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, getString(R.string.success_delete), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al eliminar el registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyState.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewHistory.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }

    private fun updateStatistics(residuos: List<Residuo>) {
        val totalRecords = residuos.size
        val totalWeight = residuos.sumOf { it.cantidad }

        binding.tvTotalRecords.text = totalRecords.toString()
        binding.tvTotalWeight.text = String.format("%.1f kg", totalWeight)
    }

    private fun showDeleteConfirmation(residuo: Residuo) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Registro")
            .setMessage("¿Está seguro que desea eliminar este registro de ${residuo.tipo}?")
            .setPositiveButton("Eliminar") { _, _ ->
                historyViewModel.deleteWaste(residuo)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun showWasteDetails(residuo: Residuo) {
        val details = buildString {
            appendLine("Tipo: ${residuo.tipo}")
            appendLine("Cantidad: ${residuo.cantidad} kg")
            appendLine("Ubicación: ${residuo.ubicacion}")
            appendLine("Fecha: ${java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault()).format(residuo.fecha)}")
            if (residuo.comentarios.isNotEmpty()) {
                appendLine("Comentarios: ${residuo.comentarios}")
            }
        }

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Detalles del Residuo")
            .setMessage(details)
            .setPositiveButton("Cerrar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}