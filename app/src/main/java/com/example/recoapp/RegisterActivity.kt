package com.example.recoapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.recoapp.data.AppDatabase
import com.example.recoapp.data.Waste
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var wasteTypes: Array<String>
    private lateinit var db: AppDatabase
    private var selectedDate: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = AppDatabase.getDatabase(this)

        val typeSpinner = findViewById<AutoCompleteTextView>(R.id.spinnerTipo)
        val quantityEditText = findViewById<EditText>(R.id.editCantidad)
        val locationEditText = findViewById<EditText>(R.id.editUbicacion)
        val dateEditText = findViewById<EditText>(R.id.editFecha)
        val commentsEditText = findViewById<EditText>(R.id.editComentarios)
        val saveButton = findViewById<Button>(R.id.btnGuardar)

        wasteTypes = arrayOf(
            getString(R.string.plastic),
            getString(R.string.paper),
            getString(R.string.glass),
            getString(R.string.organic),
            getString(R.string.other)
        )
        typeSpinner.setAdapter(
            ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                wasteTypes
            )
        )

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        dateEditText.setOnClickListener {
            DatePickerDialog(
                this, { _, year, month, day ->
                    calendar.set(year, month, day)
                    selectedDate = calendar.time
                    dateEditText.setText(dateFormat.format(selectedDate!!))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        saveButton.setOnClickListener {
            val type = typeSpinner.text.toString()
            val quantityStr = quantityEditText.text.toString()
            val location = locationEditText.text.toString()
            val comment = commentsEditText.text.toString()
            val date = selectedDate

            if (type.isNotEmpty() && quantityStr.isNotEmpty() && location.isNotEmpty() && date != null) {
                val quantity = quantityStr.toDoubleOrNull()
                if (quantity != null) {
                    val waste = Waste(
                        type = type,
                        quantity = quantity,
                        location = location,
                        date = date,
                        comment = comment
                    )

                    lifecycleScope.launch {
                        db.wasteDao().insert(waste)
                        Toast.makeText(
                            this@RegisterActivity,
                            R.string.correctly_saved,
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                } else {
                    Toast.makeText(this, R.string.invalid_quantity, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, R.string.incomplete_form, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
