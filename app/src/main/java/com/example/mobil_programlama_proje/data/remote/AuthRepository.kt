package com.example.mobil_programlama_proje.data.remote

import com.example.mobil_programlama_proje.database.dao.UserDao
import com.example.mobil_programlama_proje.database.entity.User

class AuthRepository(private val userDao: UserDao) {

    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }
}