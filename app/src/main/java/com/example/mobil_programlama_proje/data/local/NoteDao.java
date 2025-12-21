package com.example.mobil_programlama_proje.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    long insert(NoteEntity note);

    @Update
    void update(NoteEntity note);

    @Delete
    void delete(NoteEntity note);

    @Query("DELETE FROM notes WHERE id = :noteId")
    void deleteById(int noteId);

    @Query("DELETE FROM notes")
    void deleteAll();

    @Query("SELECT * FROM notes ORDER BY updated_at DESC")
    LiveData<List<NoteEntity>> getAllNotes();

    @Query("SELECT * FROM notes WHERE id = :noteId")
    LiveData<NoteEntity> getNoteById(int noteId);

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%' ORDER BY updated_at DESC")
    LiveData<List<NoteEntity>> searchNotes(String searchQuery);
}