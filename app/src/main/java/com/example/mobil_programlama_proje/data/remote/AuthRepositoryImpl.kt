package com.example.mobil_programlama_proje.data.remote

import com.example.mobil_programlama_proje.data.model.AuthResponse
import com.example.mobil_programlama_proje.data.model.LoginRequest
import retrofit2.Response

class AuthRepositoryImpl(
    private val apiService: AuthApiService
) : AuthRepository {
    override suspend fun loginUser(request: LoginRequest): Response<AuthResponse> {
        return apiService.login(request)
    }
}