package com.example.mobil_programlama_proje.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobil_programlama_proje.data.remote.AuthRepository
import com.example.mobil_programlama_proje.database.entity.User
import com.example.mobil_programlama_proje.database.PreferenceManager // <-- ÖNEMLİ IMPORT
import kotlinx.coroutines.launch

// Constructor'a preferenceManager eklendi
class AuthViewModel(
    private val repository: AuthRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    // Giriş sonucu (True: Başarılı, False: Kullanıcı bulunamadı)
    private val _loginResult = MutableLiveData<Boolean?>()
    val loginResult: LiveData<Boolean?> = _loginResult

    // Kayıt sonucu
    private val _registerResult = MutableLiveData<Boolean?>()
    val registerResult: LiveData<Boolean?> = _registerResult

    // --- YENİ EKLENEN: Kayıtlı Emaili Getir ---
    fun getSavedEmail(): String {
        return preferenceManager.getLastEmail()
    }

    fun login(email: String) {
        viewModelScope.launch {
            try {
                val user = repository.getUserByEmail(email)

                if (user != null) {
                    // --- YENİ EKLENEN: Giriş başarılıysa emaili hafızaya at ---
                    preferenceManager.saveLastEmail(email)
                    preferenceManager.setLoggedIn(true)
                    // ----------------------------------------------------------

                    _loginResult.postValue(true)
                } else {
                    _loginResult.postValue(false)
                }
            } catch (e: Exception) {
                _loginResult.postValue(false)
            }
        }
    }

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

    fun resetStates() {
        _loginResult.value = null
        _registerResult.value = null
    }
}