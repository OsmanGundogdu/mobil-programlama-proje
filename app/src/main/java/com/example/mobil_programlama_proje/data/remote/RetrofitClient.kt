package com.example.mobil_programlama_proje.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Kanka buraya kendi backend URL'ini yazmalısın
    private const val BASE_URL = "https://senin-backend-adresin.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Genel işlemler için eski servis
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // Yeni eklediğimiz Auth işlemleri için servis
    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
}