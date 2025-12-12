package com.example.mobil_programlama_proje.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobil_programlama_proje.data.NoteRepository
import com.example.mobil_programlama_proje.model.Note
import com.example.mobil_programlama_proje.model.NoteListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for the Note List screen.
 * Manages the state of the note list and provides operations for note management.
 * 
 * Requirements: 5.1, 5.2, 5.3, 5.4, 5.5
 */
class NoteListViewModel(
    private val repository: NoteRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NoteListUiState())
    val uiState: StateFlow<NoteListUiState> = _uiState.asStateFlow()
    
    init {
        loadNotes()
    }
    
    /**
     * Load all notes from the repository.
     * Updates UI state with loading status and note data.
     */
    private fun loadNotes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            repository.getAllNotes()
                .catch { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message ?: "Unknown error occurred"
                    )
                }
                .collect { notes ->
                    _uiState.value = _uiState.value.copy(
                        notes = notes,
                        isLoading = false,
                        error = null
                    )
                }
        }
    }
    
    /**
     * Delete a note from the repository.
     * @param note The note to delete
     */
    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.deleteNote(note)
                _uiState.value = _uiState.value.copy(
                    successMessage = "Note deleted successfully"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Failed to delete note. Please try again."
                )
            }
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
