package com.example.mobil_programlama_proje.model

/**
 * UI state for the Add/Edit Note screen.
 * 
 * @property title The current title input
 * @property content The current content input
 * @property isLoading Whether a save operation is in progress
 * @property error Error message if an error occurred, null otherwise
 * @property isSaved Whether the note has been successfully saved
 * @property titleError Validation error for title field, null if valid
 * @property contentError Validation error for content field, null if valid
 */
data class AddEditNoteUiState(
    val title: String = "",
    val content: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,
    val titleError: String? = null,
    val contentError: String? = null
)
