package com.example.mobil_programlama_proje.model

/**
 * UI state for the Note Detail screen.
 * 
 * @property note The note being displayed, null if not loaded yet
 * @property isLoading Whether data is currently being loaded
 * @property error Error message if an error occurred, null otherwise
 * @property successMessage Success message to display, null otherwise
 */
data class NoteDetailUiState(
    val note: Note? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)
