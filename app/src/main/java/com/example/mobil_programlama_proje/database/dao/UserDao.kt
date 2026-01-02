package com.example.mobil_programlama_proje.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mobil_programlama_proje.database.entity.User

@Dao
interface UserDao {
    // Aynı mail ile kayıt olmaya çalışırsa eskisiyle değiştirir veya hata vermez
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user")
    suspend fun getAllUsers(): List<User>
}