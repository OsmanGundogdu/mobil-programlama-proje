package com.example.mobil_programlama_proje.model

/**
 * UI state for the Note List screen.
 * 
 * @property notes List of notes to display
 * @property isLoading Whether data is currently being loaded
 * @property error Error message if an error occurred, null otherwise
 * @property successMessage Success message to display, null otherwise
 */
data class NoteListUiState(
    val notes: List<Note> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)
