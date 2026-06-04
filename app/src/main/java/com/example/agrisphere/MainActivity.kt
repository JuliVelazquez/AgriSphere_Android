package com.example.agrisphere

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Enlazamos el código con tu diseño XML usando los IDs que les pusimos
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // 2. Le decimos al botón qué hacer cuando alguien le dé clic
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()

            // Si los campos están vacíos, regañamos al usuario
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Por ahora, solo mostraremos un mensaje flotante simulando que entra
                Toast.makeText(this, "Intentando entrar como: $email", Toast.LENGTH_LONG).show()
            }
        }
    }
}