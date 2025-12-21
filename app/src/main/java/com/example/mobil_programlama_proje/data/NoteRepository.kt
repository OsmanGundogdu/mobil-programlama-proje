package com.example.mobil_programlama_proje.data

import com.example.mobil_programlama_proje.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Note data access.
 * Provides a clean API for data operations following the Repository pattern.
 */
interface NoteRepository {
    /**
     * Get all notes as a Flow for reactive updates.
     * @return Flow emitting list of all notes
     */
    fun getAllNotes(): Flow<List<Note>>
    
    /**
     * Get a specific note by its ID.
     * @param id The unique identifier of the note
     * @return The note if found, null otherwise
     */
    suspend fun getNoteById(id: String): Note?
    
    /**
     * Insert a new note into the repository.
     * @param note The note to insert
     */
    suspend fun insertNote(note: Note)
    
    /**
     * Update an existing note.
     * @param note The note with updated data
     */
    suspend fun updateNote(note: Note)
    
    /**
     * Delete a note from the repository.
     * @param note The note to delete
     */
    suspend fun deleteNote(note: Note)
}
