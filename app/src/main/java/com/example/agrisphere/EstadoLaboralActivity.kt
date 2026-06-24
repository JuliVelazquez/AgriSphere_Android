package com.example.agrisphere

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EstadoLaboralActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estado_laboral)

        val ivQrCode = findViewById<ImageView>(R.id.ivQrCode)
        val tvFecha = findViewById<TextView>(R.id.tvFecha)

        // 1. Mostrar la fecha actual del sistema
        val formatoFecha = SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy", Locale("es", "MX"))
        val fechaActual = formatoFecha.format(Date())
        tvFecha.text = fechaActual

        // 2. Recuperar el token desde SharedPreferences
        val sharedPreferences = getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("TOKEN_JWT", null)

        // 3. Generar el QR
        if (token != null) {
            try {
                val barcodeEncoder = BarcodeEncoder()
                // Convertimos el string del token en una imagen de 500x500 px
                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(token, BarcodeFormat.QR_CODE, 500, 500)
                // Se la asignamos a nuestra vista de imagen en el XML
                ivQrCode.setImageBitmap(bitmap)
            } catch (e: Exception) {
                // Muestra un toast en caso de error
                Toast.makeText(this, "Error al generar el QR", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        } else {
            Toast.makeText(this, "No se encontró sesión activa", Toast.LENGTH_LONG).show()
        }
    }
}