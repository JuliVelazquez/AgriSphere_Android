package com.example.agrisphere

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EstadoLaboralActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estado_laboral)

        val ivQrCode = findViewById<ImageView>(R.id.ivQrCode)
        val tvFecha = findViewById<TextView>(R.id.tvFecha)
        val tvSaludo = findViewById<TextView>(R.id.tvSaludo)
        val tvDatosEmpleado = findViewById<TextView>(R.id.tvDatosEmpleado)
        val btnBypassEscaneo = findViewById<Button>(R.id.btnBypassEscaneo)

        // Botón de Bypass temporal
        btnBypassEscaneo.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        // Mostrar fecha actual
        val formatoFecha = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale("es", "MX"))
        tvFecha.text = formatoFecha.format(Date())

        // Recuperar token
        val sharedPreferences = getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN_JWT", null)

        if (token != null) {
            // Hacer la petición a la API en un hilo secundario
            lifecycleScope.launch {
                try {
                    // FastAPI requiere la palabra "Bearer " antes del token
                    val response = RetrofitClient.apiService.obtenerPerfilEmpleado("Bearer $token")

                    if (response.isSuccessful && response.body() != null) {
                        val empleado = response.body()!!.data

                        // 1. Llenar los textos con los datos reales
                        tvSaludo.text = "¡Hola, ${empleado.nombreCompleto}!"

                        val supervisorTexto = empleado.nombreSupervisor ?: "No asignado"
                        val deptoTexto = empleado.departamento ?: "General"

                        tvDatosEmpleado.text = """
                            Rol: ${empleado.rol}
                            Depto: $deptoTexto
                            ID: EMP-${empleado.idEmpleado}
                            Supervisor: $supervisorTexto
                        """.trimIndent()

                        // 2. Generar el QR con el string del servidor
                        val barcodeEncoder = BarcodeEncoder()
                        val bitmap: Bitmap = barcodeEncoder.encodeBitmap(empleado.qrString, BarcodeFormat.QR_CODE, 500, 500)
                        ivQrCode.setImageBitmap(bitmap)

                    } else {
                        Toast.makeText(this@EstadoLaboralActivity, "Error de sesión. Por favor ingresa de nuevo.", Toast.LENGTH_LONG).show()
                        cerrarSesion()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@EstadoLaboralActivity, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        } else {
            cerrarSesion()
        }
    }

    private fun cerrarSesion() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}