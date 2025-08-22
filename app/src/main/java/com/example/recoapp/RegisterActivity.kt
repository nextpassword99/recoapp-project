package com.example.recoapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.activity.result.contract.ActivityResultContracts
import com.example.recoapp.data.AppDatabase
import com.example.recoapp.data.Waste
import com.example.recoapp.sync.SyncManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var wasteTypes: Array<String>
    private lateinit var db: AppDatabase
    private var selectedDate: Date? = null
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val detected = result.data?.getStringExtra(CameraActivity.EXTRA_DETECTED_TYPE)
            if (!detected.isNullOrBlank()) {
                // Buscar el índice del tipo detectado en el arreglo del Spinner
                val index = wasteTypes.indexOfFirst { it.equals(detected, ignoreCase = true) }
                if (index >= 0) {
                    findViewById<Spinner>(R.id.spinnerTipo).setSelection(index)
                    Toast.makeText(this, "Tipo detectado: $detected", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Tipo detectado no coincide con opciones", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Registrar"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = AppDatabase.getDatabase(this)

        val typeSpinner = findViewById<Spinner>(R.id.spinnerTipo)
        val quantityEditText = findViewById<EditText>(R.id.editCantidad)
        val locationEditText = findViewById<EditText>(R.id.editUbicacion)
        val dateEditText = findViewById<EditText>(R.id.editFecha)
        val commentsEditText = findViewById<EditText>(R.id.editComentarios)
        val saveButton = findViewById<Button>(R.id.btnGuardar)
        val btnDetectCamera = findViewById<Button>(R.id.btnDetectCamera)

        wasteTypes = arrayOf(
            getString(R.string.plastic),
            getString(R.string.paper),
            getString(R.string.glass),
            getString(R.string.organic),
            getString(R.string.other)
        )
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            wasteTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        typeSpinner.adapter = adapter

        btnDetectCamera.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            cameraLauncher.launch(intent)
        }

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        selectedDate = calendar.time
        dateEditText.setText(dateFormat.format(selectedDate!!))

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
            val type = typeSpinner.selectedItem?.toString() ?: ""
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
                        try { SyncManager(this@RegisterActivity).sync() } catch (_: Exception) { }
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}