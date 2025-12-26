package com.example.mobil_programlama_proje.viewmodel

import androidx.lifecycle.LiveData // Bunu ekle
import androidx.lifecycle.MutableLiveData // Bunu ekle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobil_programlama_proje.data.model.LoginRequest
import com.example.mobil_programlama_proje.data.remote.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> = _loginResult

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, pass)
                val response = repository.loginUser(request)

                if (response.isSuccessful) {
                    // Başarılı durumda sonucu true yap
                    _loginResult.postValue(true)
                } else {
                    // Hata durumunda sonucu false yap
                    _loginResult.postValue(false)
                }
            } catch (e: Exception) {
                // İnternet hatası vb. durumlarda false yap
                _loginResult.postValue(false)
            }
        }
    }

    // Ekranlar arası geçişte sonucun sıfırlanması için gerekebilir
    fun resetLoginResult() {
        _loginResult.value = null
    }
}