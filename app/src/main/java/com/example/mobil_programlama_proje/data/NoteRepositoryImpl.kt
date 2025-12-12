package com.example.mobil_programlama_proje.data

import com.example.mobil_programlama_proje.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * In-memory implementation of NoteRepository.
 * Uses MutableStateFlow for reactive data updates.
 */
class NoteRepositoryImpl : NoteRepository {
    
    // In-memory storage using MutableStateFlow for reactive updates
    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    
    /**
     * Get all notes as a Flow for reactive updates.
     * @return Flow emitting list of all notes
     */
    override fun getAllNotes(): Flow<List<Note>> {
        return _notes.asStateFlow()
    }
    
    /**
     * Get a specific note by its ID.
     * @param id The unique identifier of the note
     * @return The note if found, null otherwise
     */
    override suspend fun getNoteById(id: String): Note? {
        return _notes.value.find { it.id == id }
    }
    
    /**
     * Insert a new note into the repository.
     * @param note The note to insert
     */
    override suspend fun insertNote(note: Note) {
        _notes.update { currentNotes ->
            currentNotes + note
        }
    }
    
    /**
     * Update an existing note.
     * @param note The note with updated data
     */
    override suspend fun updateNote(note: Note) {
        _notes.update { currentNotes ->
            currentNotes.map { existingNote ->
                if (existingNote.id == note.id) {
                    note.copy(updatedAt = System.currentTimeMillis())
                } else {
                    existingNote
                }
            }
        }
    }
    
    /**
     * Delete a note from the repository.
     * @param note The note to delete
     */
    override suspend fun deleteNote(note: Note) {
        _notes.update { currentNotes ->
            currentNotes.filter { it.id != note.id }
        }
    }
}
