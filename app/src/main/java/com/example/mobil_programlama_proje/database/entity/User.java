package com.example.mobil_programlama_proje.database.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int userId;

    public String name;
    public String email;
}
