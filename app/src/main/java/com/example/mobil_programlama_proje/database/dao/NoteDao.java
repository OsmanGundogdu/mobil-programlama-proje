package com.example.mobil_programlama_proje.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mobil_programlama_proje.database.entity.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Query("SELECT * FROM Note")
    List<Note> getAllNotes();

    @Delete
    void delete(Note note);
}
