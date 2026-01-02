package com.example.mobil_programlama_proje.database.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    public int categoryId;

    public String categoryName;
}
