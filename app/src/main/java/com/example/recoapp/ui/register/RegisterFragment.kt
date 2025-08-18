package com.example.recoapp.ui.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.recoapp.R
import com.example.recoapp.databinding.FragmentRegisterBinding
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment para registrar nuevos residuos sólidos
 * 
 * Permite al usuario de ECOLIM S.A.C. registrar residuos con información
 * detallada incluyendo tipo, cantidad, ubicación, fecha y comentarios.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var registerViewModel: RegisterViewModel
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var selectedDate = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        registerViewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupWasteTypeDropdown()
        setupDatePicker()
        setupButtons()
        observeViewModel()

        return root
    }

    private fun setupWasteTypeDropdown() {
        // Tipos de residuo definidos
        val wasteTypes = arrayOf(
            getString(R.string.organic_waste),
            getString(R.string.recyclable_waste),
            getString(R.string.hazardous_waste),
            getString(R.string.electronic_waste),
            getString(R.string.general_waste)
        )
        
        val adapter = ArrayAdapter(requireContext(), 
            android.R.layout.simple_dropdown_item_1line, wasteTypes)
        binding.etWasteType.setAdapter(adapter)
    }

    private fun setupDatePicker() {
        // Establecer fecha actual por defecto
        binding.etWasteDate.setText(dateFormat.format(selectedDate.time))
        
        binding.etWasteDate.setOnClickListener {
            showDatePickerDialog()
        }
        
        binding.tilWasteDate.setEndIconOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate.set(year, month, dayOfMonth)
                binding.etWasteDate.setText(dateFormat.format(selectedDate.time))
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        
        // No permitir fechas futuras
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun setupButtons() {
        binding.btnSaveWaste.setOnClickListener {
            saveWasteRecord()
        }
        
        binding.btnScanCode.setOnClickListener {
            // TODO: Implementar escáner de código QR/Barcode
            Toast.makeText(context, "Funcionalidad de escaneo próximamente", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveWasteRecord() {
        val tipo = binding.etWasteType.text.toString().trim()
        val cantidadStr = binding.etWasteQuantity.text.toString().trim()
        val ubicacion = binding.etWasteLocation.text.toString().trim()
        val comentarios = binding.etWasteComments.text.toString().trim()

        // Validaciones
        if (tipo.isEmpty()) {
            binding.tilWasteType.error = getString(R.string.error_empty_field)
            return
        } else {
            binding.tilWasteType.error = null
        }

        if (cantidadStr.isEmpty()) {
            binding.tilWasteQuantity.error = getString(R.string.error_empty_field)
            return
        } else {
            binding.tilWasteQuantity.error = null
        }

        val cantidad = cantidadStr.toDoubleOrNull()
        if (cantidad == null || cantidad <= 0) {
            binding.tilWasteQuantity.error = getString(R.string.error_invalid_quantity)
            return
        }

        if (ubicacion.isEmpty()) {
            binding.tilWasteLocation.error = getString(R.string.error_empty_field)
            return
        } else {
            binding.tilWasteLocation.error = null
        }

        // Guardar el residuo
        registerViewModel.saveWaste(tipo, cantidad, ubicacion, selectedDate.time, comentarios)
    }

    private fun observeViewModel() {
        registerViewModel.saveResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, getString(R.string.success_save), Toast.LENGTH_LONG).show()
                clearForm()
            } else {
                Toast.makeText(context, getString(R.string.error_save_failed), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun clearForm() {
        binding.etWasteType.text = null
        binding.etWasteQuantity.text?.clear()
        binding.etWasteLocation.text?.clear()
        binding.etWasteComments.text?.clear()
        selectedDate = Calendar.getInstance()
        binding.etWasteDate.setText(dateFormat.format(selectedDate.time))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}