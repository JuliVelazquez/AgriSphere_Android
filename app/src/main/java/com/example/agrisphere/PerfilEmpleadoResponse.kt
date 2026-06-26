package com.example.agrisphere

import com.google.gson.annotations.SerializedName

// Capa Externa
data class PerfilEmpleadoResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: PerfilEmpleadoData
)

// Capa Interna
data class PerfilEmpleadoData(
    @SerializedName("id_empleado") val idEmpleado: Int,
    @SerializedName("nombre_completo") val nombreCompleto: String,
    @SerializedName("rol") val rol: String,
    @SerializedName("departamento") val departamento: String?,
    @SerializedName("nombre_supervisor") val nombreSupervisor: String?,
    @SerializedName("fecha_hora_servidor") val fechaHoraServidor: String, // En JSON las fechas viajan como String
    @SerializedName("qr_string") val qrString: String
)