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

    // ARTIK SADECE O MAILE AIT NOTLAR GELİYOR
    @Query("SELECT * FROM notes WHERE userEmail = :email ORDER BY createdAt DESC")
    fun getNotesByUser(email: String): Flow<List<Note>>

    // ... (getNoteById, insert, update, delete aynı kalıyor) ...
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

    // Arama da kullanıcıya özel olmalı
    @Query("SELECT * FROM notes WHERE userEmail = :email AND (title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%')")
    fun searchNotesByUser(email: String, searchQuery: String): Flow<List<Note>>

    // Worker için senkronize getirme (Opsiyonel: Bunu da filtreleyebilirsin)
    @Query("SELECT * FROM notes WHERE userEmail = :email")
    suspend fun getAllNotesByUserSync(email: String): List<Note>

    @Query("SELECT * FROM notes")
    suspend fun getAllNotesSync(): List<Note>
}