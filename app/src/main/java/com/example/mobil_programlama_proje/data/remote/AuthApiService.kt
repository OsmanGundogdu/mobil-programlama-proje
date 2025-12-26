package com.example.mobil_programlama_proje.data.remote

import com.example.mobil_programlama_proje.data.model.AuthResponse
import com.example.mobil_programlama_proje.data.model.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("auth/login") // Backend'deki login endpoint'in neyse onu yaz
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Varsa register buraya eklenebilir
}