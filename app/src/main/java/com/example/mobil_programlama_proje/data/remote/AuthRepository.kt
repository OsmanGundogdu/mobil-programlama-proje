package com.example.mobil_programlama_proje.data.remote

import com.example.mobil_programlama_proje.data.model.LoginRequest
import com.example.mobil_programlama_proje.data.model.AuthResponse
import retrofit2.Response

interface AuthRepository {
    suspend fun loginUser(request: LoginRequest): Response<AuthResponse>
}