package com.example.recoapp.ui.reports

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recoapp.R
import com.example.recoapp.databinding.FragmentReportsBinding
import com.example.recoapp.ui.reports.adapter.WasteBreakdownAdapter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment para generar reportes y estadísticas de residuos
 * 
 * Permite filtrar y visualizar estadísticas detalladas de los
 * residuos recolectados para ECOLIM S.A.C.
 */
class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var reportsViewModel: ReportsViewModel
    private lateinit var breakdownAdapter: WasteBreakdownAdapter
    
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var dateFromSelected: Calendar? = null
    private var dateToSelected: Calendar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reportsViewModel = ViewModelProvider(this)[ReportsViewModel::class.java]
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupWasteTypeFilter()
        setupDatePickers()
        setupBreakdownRecyclerView()
        setupButtons()
        observeViewModel()

        // Cargar datos iniciales
        reportsViewModel.loadAllData()

        return root
    }

    private fun setupWasteTypeFilter() {
        val wasteTypes = arrayOf(
            "Todos",
            getString(R.string.organic_waste),
            getString(R.string.recyclable_waste),
            getString(R.string.hazardous_waste),
            getString(R.string.electronic_waste),
            getString(R.string.general_waste)
        )
        
        val adapter = ArrayAdapter(requireContext(), 
            android.R.layout.simple_dropdown_item_1line, wasteTypes)
        binding.etFilterType.setAdapter(adapter)
        binding.etFilterType.setText("Todos", false)
    }

    private fun setupDatePickers() {
        binding.etDateFrom.setOnClickListener { showDatePickerFrom() }
        binding.tilDateFrom.setEndIconOnClickListener { showDatePickerFrom() }
        
        binding.etDateTo.setOnClickListener { showDatePickerTo() }
        binding.tilDateTo.setEndIconOnClickListener { showDatePickerTo() }
    }

    private fun showDatePickerFrom() {
        val calendar = dateFromSelected ?: Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                dateFromSelected = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                binding.etDateFrom.setText(dateFormat.format(dateFromSelected!!.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun showDatePickerTo() {
        val calendar = dateToSelected ?: Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                dateToSelected = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }
                binding.etDateTo.setText(dateFormat.format(dateToSelected!!.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun setupBreakdownRecyclerView() {
        breakdownAdapter = WasteBreakdownAdapter()
        
        binding.recyclerViewBreakdown.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = breakdownAdapter
        }
    }

    private fun setupButtons() {
        binding.btnApplyFilters.setOnClickListener {
            applyFilters()
        }
        
        binding.btnClearFilters.setOnClickListener {
            clearFilters()
        }
        
        binding.btnGenerateReport.setOnClickListener {
            generateReport()
        }
    }

    private fun applyFilters() {
        val selectedType = binding.etFilterType.text.toString()
        val dateFrom = dateFromSelected?.time
        val dateTo = dateToSelected?.time
        
        reportsViewModel.applyFilters(
            type = if (selectedType == "Todos") null else selectedType,
            dateFrom = dateFrom,
            dateTo = dateTo
        )
    }

    private fun clearFilters() {
        binding.etFilterType.setText("Todos", false)
        binding.etDateFrom.text?.clear()
        binding.etDateTo.text?.clear()
        dateFromSelected = null
        dateToSelected = null
        
        reportsViewModel.loadAllData()
    }

    private fun generateReport() {
        // TODO: Implementar generación de reporte exportable
        // Por ejemplo, generar CSV o PDF
        Toast.makeText(
            context, 
            "Generando reporte...\nFuncionalidad próximamente", 
            Toast.LENGTH_LONG
        ).show()
    }

    private fun observeViewModel() {
        reportsViewModel.totalRecords.observe(viewLifecycleOwner) { total ->
            binding.tvTotalRecordsReport.text = total.toString()
        }
        
        reportsViewModel.totalWeight.observe(viewLifecycleOwner) { weight ->
            binding.tvTotalWeightReport.text = String.format("%.1f kg", weight)
        }
        
        reportsViewModel.breakdown.observe(viewLifecycleOwner) { breakdown ->
            breakdownAdapter.submitList(breakdown)
        }
        
        reportsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // TODO: Mostrar/ocultar loading indicator si es necesario
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}