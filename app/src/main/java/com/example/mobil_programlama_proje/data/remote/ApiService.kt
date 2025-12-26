package com.example.mobil_programlama_proje.data.remote

import com.example.mobil_programlama_proje.data.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    // Şimdilik boş kalsın veya eski projende ne varsa onu ekleriz.
    // Hata almamak için geçici bir test fonksiyonu:
    @GET("posts") // Backend'deki doğru endpoint neyse o olmalı (JSONPlaceholder için "posts")
    suspend fun getPosts(): List<Post> // Eğer Liste olarak dönüyorsa bu şekilde
}