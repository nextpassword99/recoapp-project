package com.example.recoapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "RecoApp"

        val btnToRegister = findViewById<TextView>(R.id.btn_to_register)
        val btnToHistory = findViewById<TextView>(R.id.btn_to_history)
        val btnToReports = findViewById<TextView>(R.id.btn_to_reports)

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

