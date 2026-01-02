package com.example.mobil_programlama_proje.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mobil_programlama_proje.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    // Tablo adını 'notes' olarak değiştirdiğimiz için sorguyu da güncelledik
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: String): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteById(noteId: String)

    // Arama fonksiyonu (Repository'de vardı, buraya da ekleyelim)
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%'")
    fun searchNotes(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM notes")
    suspend fun getAllNotesSync(): List<Note>
}