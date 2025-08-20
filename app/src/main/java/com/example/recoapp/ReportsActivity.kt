package com.example.recoapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ReportsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reports)

        val editFecha = findViewById<EditText>(R.id.editFecha)
        val editRegistro = findViewById<EditText>(R.id.editRegistro)
        val editResponsable = findViewById<EditText>(R.id.editResponsable)
        val editUbicacion = findViewById<EditText>(R.id.editUbicacion)
        val editPesoBruto = findViewById<EditText>(R.id.editPesoBruto)
        val editPlastico = findViewById<EditText>(R.id.editPlastico)
        val editPapelCarton = findViewById<EditText>(R.id.editPapelCarton)
        val editOrganico = findViewById<EditText>(R.id.editOrganico)
        val editMetales = findViewById<EditText>(R.id.editMetales)
        val editObservaciones = findViewById<EditText>(R.id.editObservaciones)
        val btnEnviarReporte = findViewById<Button>(R.id.btnEnviarReporte)
        val btnBack = findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val today = Date()
        editFecha.setText(simpleDateFormat.format(today))

        editRegistro.setText("1001")
        editResponsable.setText("Juan Pérez")
        editUbicacion.setText("Planta Principal")
        editPesoBruto.setText("350.50")
        editPlastico.setText("120.00")
        editPapelCarton.setText("85.75")
        editOrganico.setText("105.20")
        editMetales.setText("25.00")
        editObservaciones.setText("Reporte diario de recolección de residuos.")

        btnEnviarReporte.setOnClickListener {
            Toast.makeText(this, "Datos de prueba enviados al 'backend'", Toast.LENGTH_SHORT).show()
        }
    }
}
