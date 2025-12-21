package com.example.mobil_programlama_proje.data.remote

import com.example.mobil_programlama_proje.data.model.Post
import retrofit2.http.GET

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}