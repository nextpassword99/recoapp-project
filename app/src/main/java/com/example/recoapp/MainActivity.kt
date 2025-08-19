package com.example.recoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnToRegister = findViewById<Button>(R.id.btn_to_register)
        val btnToHistory = findViewById<Button>(R.id.btn_to_history)
        val btnToReports = findViewById<Button>(R.id.btn_to_reports)

        btnToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnToHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        btnToReports.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }
    }
}
