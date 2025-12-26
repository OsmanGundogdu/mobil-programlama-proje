package com.example.mobil_programlama_proje.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    // Şimdilik boş kalsın veya eski projende ne varsa onu ekleriz.
    // Hata almamak için geçici bir test fonksiyonu:
    @GET("test")
    suspend fun testConnection(): Response<Unit>
}