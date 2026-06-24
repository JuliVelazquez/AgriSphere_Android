package com.example.agrisphere

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.jvm.java

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        //placeholder para el flujo de "olvidé mi contraseña"
        //reemplazar el toast con un Intent hacia la pantalla de recuperación cuando el backend esté listo
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Función en desarrollo (INV-12)", Toast.LENGTH_SHORT).show()
        }

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

                                // Se almacena el token JWT localmente y se limpia el stack de navegación para evitar el retorno al Login.
                                if (token != null) {
                                    // 1. Guardamos el token en la memoria del teléfono
                                    val sharedPreferences = getSharedPreferences("SesionUsuario", MODE_PRIVATE)
                                    sharedPreferences.edit().putString("TOKEN_JWT", token).apply()

                                    Toast.makeText(this@MainActivity, "¡Sesión iniciada!", Toast.LENGTH_SHORT).show()

                                    // 2. Lanza la nueva interfaz donde se mostrará el QR
                                    val intent = Intent(this@MainActivity, EstadoLaboralActivity::class.java)
                                    startActivity(intent)

                                    // 3. Destruimos el Login para que no se pueda regresar a él
                                    finish()
                                }

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