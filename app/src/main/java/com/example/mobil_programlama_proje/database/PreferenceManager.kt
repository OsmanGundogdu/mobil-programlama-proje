package com.example.mobil_programlama_proje.database

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("SmartNotePrefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LAST_EMAIL = "last_email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in" // <-- YENİ EKLENDİ
    }

    // Email işlemleri
    fun saveLastEmail(email: String) {
        sharedPreferences.edit().putString(KEY_LAST_EMAIL, email).apply()
    }

    fun getLastEmail(): String {
        return sharedPreferences.getString(KEY_LAST_EMAIL, "") ?: ""
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
}