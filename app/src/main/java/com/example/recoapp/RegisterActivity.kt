package com.example.recoapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import android.location.Geocoder
import com.example.recoapp.data.AppDatabase
import com.example.recoapp.data.Waste
import com.example.recoapp.sync.SyncManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegisterActivity : AppCompatActivity() {

    private lateinit var wasteTypes: Array<String>
    private lateinit var db: AppDatabase
    private var selectedDate: Date? = null
    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val detected = result.data?.getStringExtra(CameraActivity.EXTRA_DETECTED_TYPE)
            if (!detected.isNullOrBlank()) {
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
    private val locationPermsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = (perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true)
        if (granted) {
            fetchLocationAndFill()
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

        tryAutofillLocation()

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

    private fun tryAutofillLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermsLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            fetchLocationAndFill()
        }
    }

    private fun fetchLocationAndFill() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val token = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, token.token)
            .addOnSuccessListener { loc ->
                if (loc == null) return@addOnSuccessListener
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val geocoder = Geocoder(this@RegisterActivity, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                        if (!addresses.isNullOrEmpty()) {
                            val line = addresses[0].getAddressLine(0)
                            withContext(Dispatchers.Main) {
                                findViewById<EditText>(R.id.editUbicacion).setText(line)
                            }
                        }
                    } catch (_: Exception) {
                    }
                }
            }
            .addOnFailureListener {
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}