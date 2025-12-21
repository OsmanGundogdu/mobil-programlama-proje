package com.example.mobil_programlama_proje.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobil_programlama_proje.database.entity.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Query("SELECT * FROM User")
    List<User> getAllUsers();

    @Delete
    void delete(User user);
}
