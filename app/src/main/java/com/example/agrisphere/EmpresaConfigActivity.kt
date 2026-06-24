package com.example.agrisphere

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EmpresaConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_config)

        val etNombre = findViewById<EditText>(R.id.etNombreEmpresa)
        val etLatitud = findViewById<EditText>(R.id.etLatitud)
        val etLongitud = findViewById<EditText>(R.id.etLongitud)
        val etRadio = findViewById<EditText>(R.id.etRadio)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarConfig)

        // Recuperamos el token que le pasaremos desde la pantalla de Login
        val token = intent.getStringExtra("TOKEN_JWT") ?: ""

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString()
            val lat = etLatitud.text.toString()
            val lon = etLongitud.text.toString()
            val radio = etRadio.text.toString()

            if (nombre.isEmpty() || lat.isEmpty() || lon.isEmpty() || radio.isEmpty()) {
                Toast.makeText(this, "Llena todos los campos de la geocerca", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Guardando configuración...", Toast.LENGTH_SHORT).show()

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // Convertimos los textos a los tipos de dato que maneja el backend
                    val request = EmpresaConfigRequest(
                        nombre = nombre,
                        geocerca_latitud = lat.toDouble(),
                        geocerca_longitud = lon.toDouble(),
                        geocerca_radio_metros = radio.toInt()
                    )

                    // Enviamos el token con el prefijo "Bearer " para que coincida
                    val response = RetrofitClient.apiService.configurarEmpresa(
                        token = "Bearer $token",
                        request = request
                    )

                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@EmpresaConfigActivity, "¡Parámetros actualizados en la BD!", Toast.LENGTH_LONG).show()
                            finish() // Cierra esta pantalla y regresa
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Toast.makeText(this@EmpresaConfigActivity, "Error al guardar: $errorBody", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EmpresaConfigActivity, "Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}