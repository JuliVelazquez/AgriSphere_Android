package com.example.agrisphere

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // 1. Configuramos el RecyclerView
        val rvInvernaderos = findViewById<RecyclerView>(R.id.rvInvernaderos)
        rvInvernaderos.layoutManager = LinearLayoutManager(this)

        // 2. Creamos placeholders (mientras termina INV-17)
        val listaFalsa = listOf(
            Invernadero("INV-01", "Tomates", "● OK", "Hoy,\n07:15 AM", "Carlos R."),
            Invernadero("INV-04", "Pimientos", "ALERTA\nPLAGA", "Hoy,\n08:00 AM", "Ana V."),
            Invernadero("INV-07", "Pepinos", "● OK", "Ayer,\n16:45 PM", "Luis P.")
        )

        // 3. Conectamos los datos a la vista
        rvInvernaderos.adapter = InvernaderoAdapter(listaFalsa)
    }
}