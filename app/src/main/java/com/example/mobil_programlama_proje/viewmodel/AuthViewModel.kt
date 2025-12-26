package com.example.mobil_programlama_proje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobil_programlama_proje.data.model.LoginRequest
import com.example.mobil_programlama_proje.data.remote.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, pass)
                val response = repository.loginUser(request)

                if (response.isSuccessful) {
                    val authData = response.body()
                    // TODO: Başarılı! Token'ı kaydet ve ana ekrana yönlendir
                } else {
                    // TODO: Hata mesajını kullanıcıya göster
                }
            } catch (e: Exception) {
                // İnternet hatası vs.
            }
        }
    }
}