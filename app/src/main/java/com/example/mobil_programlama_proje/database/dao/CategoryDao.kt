package com.example.mobil_programlama_proje.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobil_programlama_proje.database.entity.Category;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category category);

    @Query("SELECT * FROM Category")
    List<Category> getAllCategories();

    @Delete
    void delete(Category category);
}
