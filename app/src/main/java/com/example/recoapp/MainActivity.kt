package com.example.recoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.recoapp.auth.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        session = SessionManager(this)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "RecoApp"

        val btnToRegister = findViewById<Button>(R.id.btn_to_register)
        val btnToHistory = findViewById<Button>(R.id.btn_to_history)
        val btnToReports = findViewById<Button>(R.id.btn_to_reports)
        val btnLogout = findViewById<Button>(R.id.btn_logout)

        btnToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnToHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        btnToReports.setOnClickListener {
            startActivity(Intent(this, ReportsActivity::class.java))
        }

        btnLogout.setOnClickListener {
            session.clear()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        if (this::session.isInitialized && session.fetchAuthToken() == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}

