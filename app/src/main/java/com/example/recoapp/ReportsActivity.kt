package com.example.recoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.recoapp.data.AppDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ReportsActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private var startDate: Date? = null
    private var endDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        db = AppDatabase.getDatabase(this)

        val typeSpinner = findViewById<AutoCompleteTextView>(R.id.spinnerTypeFilter)
        val startDateEditText = findViewById<EditText>(R.id.editStartDate)
        val endDateEditText = findViewById<EditText>(R.id.editEndDate)
        val generateButton = findViewById<Button>(R.id.btnGenerateReport)
        val resultTextView = findViewById<TextView>(R.id.textReportResult)

        val wasteTypes = arrayOf(
            getString(R.string.all_types),
            getString(R.string.plastic),
            getString(R.string.paper),
            getString(R.string.glass),
            getString(R.string.organic),
            getString(R.string.other)
        )
        typeSpinner.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, wasteTypes)
        )

        startDateEditText.setOnClickListener { showDatePickerDialog(it as EditText, true) }
        endDateEditText.setOnClickListener { showDatePickerDialog(it as EditText, false) }

        generateButton.setOnClickListener {
            val type = typeSpinner.text.toString()
            if (startDate != null && endDate != null) {
                lifecycleScope.launch {
                    val selectedType = if (type == getString(R.string.all_types)) null else type
                    val reportData = db.wasteDao()
                        .getWasteByFilters(startDate!!.time, endDate!!.time, selectedType)

                    val totalQuantity = reportData.sumOf { it.quantity }
                    val resultText = "Reporte Generado:\n\n" +
                            "Periodo: ${formatDate(startDate!!)} - ${formatDate(endDate!!)}\n" +
                            "Tipo de Residuo: ${type}\n" +
                            "Registros Encontrados: ${reportData.size}\n" +
                            "Cantidad Total: ${"%.2f".format(totalQuantity)} kg"
                    resultTextView.text = resultText
                }
            } else {
                Toast.makeText(this, R.string.select_date_range, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePickerDialog(editText: EditText, isStartDate: Boolean) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this, { _, year, month, day ->
                calendar.set(year, month, day)
                val selectedDate = calendar.time
                if (isStartDate) {
                    startDate = selectedDate
                } else {
                    endDate = selectedDate
                }
                editText.setText(formatDate(selectedDate))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun formatDate(date: Date): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
    }
}