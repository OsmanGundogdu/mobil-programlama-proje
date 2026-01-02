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

        // LOG EKLEME 1: Butona basıldı mı?
        android.util.Log.d("SaveDebug", "Save butonuna basıldı. Başlık: ${currentState.title}, İçerik: ${currentState.content}")

        // Validate input
        if (!validateInput()) {
            // LOG EKLEME 2: Validasyona takıldı mı?
            android.util.Log.d("SaveDebug", "Validasyon hatası! Başlık veya içerik kurallara uymuyor.")
            android.util.Log.d("SaveDebug", "Hata Mesajları -> Başlık: ${currentState.titleError}, İçerik: ${currentState.contentError}")
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
                // LOG EKLEME 3: Kayıt başlıyor
                android.util.Log.d("SaveDebug", "Repository kayıt işlemi başlatılıyor...")

                val noteId = editingNoteId
                if (noteId != null) {
                    // Update logic...
                    android.util.Log.d("SaveDebug", "Güncelleme yapılıyor ID: $noteId")
                    val existingNote = repository.getNoteById(noteId)
                    if (existingNote != null) {
                        val updatedNote = existingNote.copy(
                            title = currentState.title.trim(),
                            content = currentState.content.trim(),
                            updatedAt = System.currentTimeMillis()
                        )
                        repository.updateNote(updatedNote)
                    }
                } else {
                    // Create new note
                    android.util.Log.d("SaveDebug", "Yeni not oluşturuluyor...")

                    val newNote = Note(
                        title = currentState.title.trim(),
                        content = currentState.content.trim()
                        // userId şimdilik null veya 0 gidebilir, Note.kt modeline göre değişir
                    )

                    repository.insertNote(newNote)
                }

                android.util.Log.d("SaveDebug", "Kayıt Başarılı! UI güncelleniyor.")

                _uiState.value = currentState.copy(
                    isLoading = false,
                    error = null,
                    isSaved = true, // <-- Burası true olunca ekran kapanmalı
                    titleError = null,
                    contentError = null
                )
            } catch (e: Exception) {
                // LOG EKLEME 4: Hata çıktı!
                android.util.Log.e("SaveDebug", "Kayıt sırasında hata oluştu: ${e.message}")
                e.printStackTrace()

                _uiState.value = currentState.copy(
                    isLoading = false,
                    error = "Kayıt başarısız: ${e.message}"
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
