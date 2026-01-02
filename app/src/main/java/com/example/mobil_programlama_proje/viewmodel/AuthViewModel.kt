package com.example.mobil_programlama_proje.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobil_programlama_proje.database.entity.User // Senin User Entity'n
import com.example.mobil_programlama_proje.data.remote.AuthRepository // Repository sınıfın
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    // Giriş sonucu (True: Başarılı, False: Kullanıcı bulunamadı)
    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> = _loginResult

    // Kayıt sonucu
    private val _registerResult = MutableLiveData<Boolean?>()
    val registerResult: LiveData<Boolean?> = _registerResult

    // ARTIK SADECE EMAIL ALIYORUZ
    fun login(email: String) {
        viewModelScope.launch {
            try {
                // Repository'de "getUserByEmail" gibi bir fonksiyon olduğunu varsayıyoruz
                // Eğer veritabanında bu mail varsa user dönecek, yoksa null dönecek.
                val user = repository.getUserByEmail(email)

                if (user != null) {
                    // Kullanıcı bulundu, giriş başarılı
                    _loginResult.postValue(true)
                } else {
                    // Kullanıcı yok, kayıt sayfasına yönlendirmek gerekebilir
                    _loginResult.postValue(false)
                }
            } catch (e: Exception) {
                _loginResult.postValue(false)
            }
        }
    }

    // YENİ EKLENEN KAYIT FONKSİYONU
    fun register(name: String, email: String) {
        viewModelScope.launch {
            try {
                val newUser = User(
                    name = name,
                    email = email
                )

                repository.insertUser(newUser)

                _registerResult.postValue(true)
            } catch (e: Exception) {
                e.printStackTrace()
                _registerResult.postValue(false)
            }
        }
    }

    // Ekranlar arası geçişte state temizlemek için
    fun resetStates() {
        _loginResult.value = null
        _registerResult.value = null
    }
}