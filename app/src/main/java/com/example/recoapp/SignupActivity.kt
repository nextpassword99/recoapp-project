package com.example.recoapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.recoapp.auth.SessionManager
import com.example.recoapp.network.AuthRepository
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private val repo = AuthRepository()
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        session = SessionManager(this)

        val nameInput = findViewById<EditText>(R.id.inputName)
        val emailInput = findViewById<EditText>(R.id.inputEmail)
        val passwordInput = findViewById<EditText>(R.id.inputPassword)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val progress = findViewById<ProgressBar>(R.id.progress)

        btnSignup.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progress.visibility = View.VISIBLE
            btnSignup.isEnabled = false
            lifecycleScope.launch {
                try {
                    val res = repo.register(name, email, password)
                    session.saveAuthToken(res.token)
                    Toast.makeText(this@SignupActivity, "Cuenta creada", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@SignupActivity, "Error al registrarse", Toast.LENGTH_SHORT).show()
                } finally {
                    progress.visibility = View.GONE
                    btnSignup.isEnabled = true
                }
            }
        }
    }
}
