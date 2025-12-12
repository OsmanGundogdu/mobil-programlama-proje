package com.example.mobil_programlama_proje.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobil_programlama_proje.model.AddEditNoteUiState
import com.example.mobil_programlama_proje.ui.theme.Mobil_programlama_projeTheme
import com.example.mobil_programlama_proje.viewmodel.AddEditNoteViewModel

/**
 * Add/Edit Note screen composable - For creating new notes or editing existing ones.
 * 
 * Requirements: 1.5, 2.5, 3.5
 * 
 * @param noteId Optional note ID - if null, creates new note; if present, edits existing note
 * @param viewModel ViewModel for managing add/edit note state
 * @param onNavigateBack Callback invoked when user cancels or successfully saves
 * @param modifier Optional modifier for the screen
 */
@Composable
fun AddEditNoteScreen(
    noteId: String?,
    viewModel: AddEditNoteViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Load note for editing if noteId is provided
    LaunchedEffect(noteId) {
        noteId?.let {
            viewModel.loadNoteForEditing(it)
        }
    }
    
    // Show error message if present
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }
    
    // Navigate back when note is successfully saved
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            viewModel.resetSavedState()
            onNavigateBack()
        }
    }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        AddEditNoteContent(
            uiState = uiState,
            isEditMode = noteId != null,
            onTitleChange = viewModel::updateTitle,
            onContentChange = viewModel::updateContent,
            onSaveClick = viewModel::saveNote,
            onCancelClick = onNavigateBack,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

/**
 * Content composable for the add/edit note screen.
 * Handles loading and input states.
 */
@Composable
private fun AddEditNoteContent(
    uiState: AddEditNoteUiState,
    isEditMode: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading && uiState.title.isEmpty() && uiState.content.isEmpty()) {
            // Show loading indicator only when initially loading a note for editing
            CircularProgressIndicator()
        } else {
            AddEditNoteForm(
                title = uiState.title,
                content = uiState.content,
                titleError = uiState.titleError,
                contentError = uiState.contentError,
                isEditMode = isEditMode,
                isSaving = uiState.isLoading,
                onTitleChange = onTitleChange,
                onContentChange = onContentChange,
                onSaveClick = onSaveClick,
                onCancelClick = onCancelClick
            )
        }
    }
}

/**
 * Form composable for note input fields and action buttons.
 * 
 * Requirement 1.5, 2.5, 3.5 - Text input fields for title and content
 * Requirement 1.5, 2.5, 3.5 - Save and cancel buttons with validation
 */
@Composable
private fun AddEditNoteForm(
    title: String,
    content: String,
    titleError: String?,
    contentError: String?,
    isEditMode: Boolean,
    isSaving: Boolean,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .imePadding()
    ) {
        // Screen title
        Text(
            text = if (isEditMode) "Edit Note" else "Add New Note",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Title input field
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Title") },
            placeholder = { Text("Enter note title") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge,
            isError = titleError != null,
            supportingText = titleError?.let { 
                { 
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    ) 
                } 
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Content input field
        OutlinedTextField(
            value = content,
            onValueChange = onContentChange,
            label = { Text("Content") },
            placeholder = { Text("Enter note content") },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            enabled = !isSaving,
            textStyle = MaterialTheme.typography.bodyLarge,
            maxLines = Int.MAX_VALUE,
            isError = contentError != null,
            supportingText = contentError?.let { 
                { 
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    ) 
                } 
            }
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cancel button
            OutlinedButton(
                onClick = onCancelClick,
                modifier = Modifier.weight(1f),
                enabled = !isSaving
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cancel")
            }
            
            // Save button
            Button(
                onClick = onSaveClick,
                modifier = Modifier.weight(1f),
                enabled = !isSaving && title.isNotBlank() && content.isNotBlank()
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .height(20.dp)
                            .width(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isEditMode) "Update" else "Save")
            }
        }
        
        // Validation hint (only show if no specific field errors)
        if ((title.isBlank() || content.isBlank()) && titleError == null && contentError == null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Both title and content are required",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

// Preview composables
@Preview(showBackground = true, name = "Add Note Screen")
@Composable
fun AddEditNoteScreenAddPreview() {
    Mobil_programlama_projeTheme {
        Surface {
            AddEditNoteContent(
                uiState = AddEditNoteUiState(
                    title = "",
                    content = "",
                    isLoading = false,
                    error = null,
                    isSaved = false,
                    titleError = null,
                    contentError = null
                ),
                isEditMode = false,
                onTitleChange = {},
                onContentChange = {},
                onSaveClick = {},
                onCancelClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Edit Note Screen")
@Composable
fun AddEditNoteScreenEditPreview() {
    Mobil_programlama_projeTheme {
        Surface {
            AddEditNoteContent(
                uiState = AddEditNoteUiState(
                    title = "Sample Note Title",
                    content = "This is the content of the note that is being edited.",
                    isLoading = false,
                    error = null,
                    isSaved = false,
                    titleError = null,
                    contentError = null
                ),
                isEditMode = true,
                onTitleChange = {},
                onContentChange = {},
                onSaveClick = {},
                onCancelClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Add Note Screen - Loading")
@Composable
fun AddEditNoteScreenLoadingPreview() {
    Mobil_programlama_projeTheme {
        Surface {
            AddEditNoteContent(
                uiState = AddEditNoteUiState(
                    title = "",
                    content = "",
                    isLoading = true,
                    error = null,
                    isSaved = false,
                    titleError = null,
                    contentError = null
                ),
                isEditMode = true,
                onTitleChange = {},
                onContentChange = {},
                onSaveClick = {},
                onCancelClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Add Note Screen - With Validation Errors")
@Composable
fun AddEditNoteScreenValidationErrorsPreview() {
    Mobil_programlama_projeTheme {
        Surface {
            AddEditNoteContent(
                uiState = AddEditNoteUiState(
                    title = "Hi",
                    content = "Short",
                    isLoading = false,
                    error = null,
                    isSaved = false,
                    titleError = "Title must be at least 3 characters",
                    contentError = "Content must be at least 10 characters"
                ),
                isEditMode = false,
                onTitleChange = {},
                onContentChange = {},
                onSaveClick = {},
                onCancelClick = {}
            )
        }
    }
}
