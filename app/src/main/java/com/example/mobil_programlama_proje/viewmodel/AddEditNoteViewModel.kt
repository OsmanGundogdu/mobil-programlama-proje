package com.example.mobil_programlama_proje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobil_programlama_proje.data.NoteRepository
import com.example.mobil_programlama_proje.model.AddEditNoteUiState
import com.example.mobil_programlama_proje.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Add/Edit Note screen.
 * Manages the state of note creation and editing operations.
 * 
 * Requirements: 5.1, 5.2, 5.3, 5.4, 5.5
 */
class AddEditNoteViewModel(
    private val repository: NoteRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddEditNoteUiState())
    val uiState: StateFlow<AddEditNoteUiState> = _uiState.asStateFlow()
    
    private var editingNoteId: String? = null
    
    /**
     * Load an existing note for editing.
     * @param noteId The unique identifier of the note to edit
     */
    fun loadNoteForEditing(noteId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val note = repository.getNoteById(noteId)
                if (note != null) {
                    editingNoteId = noteId
                    _uiState.value = _uiState.value.copy(
                        title = note.title,
                        content = note.content,
                        isLoading = false,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Note not found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load note"
                )
            }
        }
    }
    
    /**
     * Update the title in the UI state.
     * @param title The new title value
     */
    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title,
            titleError = null // Clear error when user types
        )
    }
    
    /**
     * Update the content in the UI state.
     * @param content The new content value
     */
    fun updateContent(content: String) {
        _uiState.value = _uiState.value.copy(
            content = content,
            contentError = null // Clear error when user types
        )
    }
    
    /**
     * Validate note input fields.
     * @return true if validation passes, false otherwise
     */
    private fun validateInput(): Boolean {
        val currentState = _uiState.value
        var isValid = true
        var titleError: String? = null
        var contentError: String? = null
        
        // Validate title
        when {
            currentState.title.isBlank() -> {
                titleError = "Title is required"
                isValid = false
            }
            currentState.title.trim().length < 3 -> {
                titleError = "Title must be at least 3 characters"
                isValid = false
            }
            currentState.title.trim().length > 100 -> {
                titleError = "Title must not exceed 100 characters"
                isValid = false
            }
        }
        
        // Validate content
        when {
            currentState.content.isBlank() -> {
                contentError = "Content is required"
                isValid = false
            }
            currentState.content.trim().length < 10 -> {
                contentError = "Content must be at least 10 characters"
                isValid = false
            }
        }
        
        if (!isValid) {
            _uiState.value = currentState.copy(
                titleError = titleError,
                contentError = contentError
            )
        }
        
        return isValid
    }
    
    /**
     * Save the note (create new or update existing).
     * Validates input before saving.
     */
    fun saveNote() {
        val currentState = _uiState.value
        
        // Validate input
        if (!validateInput()) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = currentState.copy(
                isLoading = true, 
                error = null,
                titleError = null,
                contentError = null
            )
            
            try {
                val noteId = editingNoteId
                if (noteId != null) {
                    // Update existing note
                    val existingNote = repository.getNoteById(noteId)
                    if (existingNote != null) {
                        val updatedNote = existingNote.copy(
                            title = currentState.title.trim(),
                            content = currentState.content.trim(),
                            updatedAt = System.currentTimeMillis()
                        )
                        repository.updateNote(updatedNote)
                    } else {
                        _uiState.value = currentState.copy(
                            isLoading = false,
                            error = "Note not found. It may have been deleted."
                        )
                        return@launch
                    }
                } else {
                    // Create new note
                    val newNote = Note(
                        title = currentState.title.trim(),
                        content = currentState.content.trim()
                    )
                    repository.insertNote(newNote)
                }
                
                _uiState.value = currentState.copy(
                    isLoading = false,
                    error = null,
                    isSaved = true,
                    titleError = null,
                    contentError = null
                )
            } catch (e: Exception) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    error = "Failed to save note. Please try again."
                )
            }
        }
    }
    
    /**
     * Clear any error message from the UI state.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(
            error = null,
            titleError = null,
            contentError = null
        )
    }
    
    /**
     * Reset the saved state flag.
     */
    fun resetSavedState() {
        _uiState.value = _uiState.value.copy(isSaved = false)
    }
}
