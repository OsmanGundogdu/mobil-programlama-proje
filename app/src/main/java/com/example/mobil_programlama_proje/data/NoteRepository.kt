package com.example.mobil_programlama_proje.data

import com.example.mobil_programlama_proje.database.dao.NoteDao
import com.example.mobil_programlama_proje.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Repository implementation for Note data access.
 * Interface yerine Class yaptık ki Navigation dosyasında "NoteRepository(dao)" diyebilelim.
 */
class NoteRepository(private val noteDao: NoteDao) {

    /**
     * Get all notes as a Flow for reactive updates.
     */
    fun getAllNotes(): Flow<List<Note>> {
        return noteDao.getAllNotes()
    }

    /**
     * Get a specific note by its ID.
     */
    suspend fun getNoteById(id: String): Note? {
        return noteDao.getNoteById(id)
    }

    /**
     * Insert a new note into the repository.
     */
    suspend fun insertNote(note: Note) {
        noteDao.insert(note)
    }

    /**
     * Update an existing note.
     */
    suspend fun updateNote(note: Note) {
        noteDao.update(note)
    }

    /**
     * Delete a note from the repository.
     */
    suspend fun deleteNote(note: Note) {
        noteDao.delete(note)
    }
}