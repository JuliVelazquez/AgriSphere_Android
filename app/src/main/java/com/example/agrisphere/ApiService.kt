package com.example.agrisphere

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

// ==========================================
// MODELOS DEL LOGIN
// ==========================================
data class Ubicacion(
    val latitud: Double,
    val longitud: Double
)

data class LoginRequest(
    val usuario: String,
    val password: String,
    val ui_device: String,
    val ubicacion: Ubicacion? = null
)

data class TokenDataResponse(
    val access_token: String,
    val usuario_id: Int,
    val rol: String
)

data class LoginResponse(
    val message: String,
    val data: TokenDataResponse
)

// ==========================================
// MODELOS DE EMPRESA
// ==========================================
data class EmpresaConfigRequest(
    val nombre: String,
    val geocerca_latitud: Double,
    val geocerca_longitud: Double,
    val geocerca_radio_metros: Int
)

data class EmpresaConfigResponse(
    val status: String,
    val message: String
)

// ==========================================
// LAS RUTAS (EL MENÚ)
// ==========================================
interface ApiService {

    // 1. ENDPOINT DE LOGIN
    @POST("api/auth/login")
    suspend fun iniciarSesion(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // 2. ENDPOINT DE EMPRESA
    @POST("api/auth/empresa/parametros")
    suspend fun configurarEmpresa(
        @Header("Authorization") token: String,
        @Body request: EmpresaConfigRequest
    ): Response<EmpresaConfigResponse>

    // 3. ENDPOINT DEL PERFIL DEL EMPLEADO (INV-18)
    @GET("api/auth/empleados/me")
    suspend fun obtenerPerfilEmpleado(
        @Header("Authorization") token: String
    ): Response<PerfilEmpleadoResponse>

}