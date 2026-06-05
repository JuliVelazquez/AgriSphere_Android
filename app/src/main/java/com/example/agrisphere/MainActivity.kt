package com.example.agrisphere

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val usuarioIngresado = etEmail.text.toString() // se ingresa el usuario, no el correo
            val password = etPassword.text.toString()

            if (usuarioIngresado.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Conectando al servidor...", Toast.LENGTH_SHORT).show()

                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        // 1. ARMAMOS EL PAQUETE JSON PARA QUE SEA COMPATIBLE CON FASTAPI
                        val peticionLogin = LoginRequest(
                            usuario = usuarioIngresado,
                            password = password,
                            ui_device = "app_movil",
                            ubicacion = Ubicacion(
                                latitud = 21.5041,
                                longitud = -104.8945
                            )
                        )

                        // 2. HACEMOS EL DISPARO
                        val response = RetrofitClient.apiService.iniciarSesion(peticionLogin)

                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                val token = response.body()?.data?.access_token

                                // 3. ALERT DIALOG QUE REEMPLAZA AL TOAST
                                android.app.AlertDialog.Builder(this@MainActivity)
                                    .setTitle("¡Login Exitoso!")
                                    .setMessage("Tu Token JWT es:\n\n$token")
                                    .setPositiveButton("Ir a Configuración") { _, _ ->
                                        // Envía a la nueva pantalla llevando el nuevo token
                                        val intent = Intent(this@MainActivity, EmpresaConfigActivity::class.java)
                                        intent.putExtra("TOKEN_JWT", token)
                                        startActivity(intent)
                                    }
                                    .setCancelable(false) // Evita que se cierre por accidente al picar afuera
                                    .show()

                            } else {
                                val errorBody = response.errorBody()?.string()
                                Toast.makeText(this@MainActivity, "Error: $errorBody", Toast.LENGTH_LONG).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@MainActivity, "Error de red: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}