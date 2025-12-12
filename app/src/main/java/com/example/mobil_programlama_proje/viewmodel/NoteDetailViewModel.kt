package com.example.mobil_programlama_proje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobil_programlama_proje.data.NoteRepository
import com.example.mobil_programlama_proje.model.Note
import com.example.mobil_programlama_proje.model.NoteDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Note Detail screen.
 * Manages the state of a single note and provides operations for viewing and deleting.
 * 
 * Requirements: 5.1, 5.2, 5.3, 5.4, 5.5
 */
class NoteDetailViewModel(
    private val repository: NoteRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NoteDetailUiState())
    val uiState: StateFlow<NoteDetailUiState> = _uiState.asStateFlow()
    
    /**
     * Load a specific note by its ID.
     * @param noteId The unique identifier of the note to load
     */
    fun loadNote(noteId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                val note = repository.getNoteById(noteId)
                if (note != null) {
                    _uiState.value = _uiState.value.copy(
                        note = note,
                        isLoading = false,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        note = null,
                        isLoading = false,
                        error = "The note you're looking for could not be found."
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Unable to load note. Please try again."
                )
            }
        }
    }
    
    /**
     * Delete the currently loaded note.
     * @return true if deletion was successful, false otherwise
     */
    suspend fun deleteNote(): Boolean {
        val currentNote = _uiState.value.note ?: return false
        
        return try {
            repository.deleteNote(currentNote)
            _uiState.value = _uiState.value.copy(
                successMessage = "Note deleted successfully"
            )
            true
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(
                error = "Failed to delete note. Please try again."
            )
            false
        }
    }
    
    /**
     * Clear any error message from the UI state.
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Clear any success message from the UI state.
     */
    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
}
