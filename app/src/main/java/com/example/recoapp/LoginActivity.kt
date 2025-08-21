package com.example.recoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.recoapp.auth.SessionManager
import com.example.recoapp.network.AuthRepository
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val repo = AuthRepository()
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)
        if (session.fetchAuthToken() != null) {
            goToMain()
            return
        }

        val emailInput = findViewById<EditText>(R.id.inputEmail)
        val passwordInput = findViewById<EditText>(R.id.inputPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvToSignup = findViewById<TextView>(R.id.tvToSignup)
        val progress = findViewById<ProgressBar>(R.id.progress)

        btnLogin.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progress.visibility = View.VISIBLE
            btnLogin.isEnabled = false
            lifecycleScope.launch {
                try {
                    val res = repo.login(email, password)
                    session.saveAuthToken(res.token)
                    Toast.makeText(this@LoginActivity, "Bienvenido ${res.user.name}", Toast.LENGTH_SHORT).show()
                    goToMain()
                } catch (e: Exception) {
                    Toast.makeText(this@LoginActivity, "Error de inicio de sesión", Toast.LENGTH_SHORT).show()
                } finally {
                    progress.visibility = View.GONE
                    btnLogin.isEnabled = true
                }
            }
        }

        tvToSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::session.isInitialized && session.fetchAuthToken() != null) {
            goToMain()
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
