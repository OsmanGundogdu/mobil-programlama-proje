package com.example.mobil_programlama_proje.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobil_programlama_proje.model.Note
import com.example.mobil_programlama_proje.model.NoteDetailUiState
import com.example.mobil_programlama_proje.ui.theme.Mobil_programlama_projeTheme
import com.example.mobil_programlama_proje.viewmodel.NoteDetailViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Note Detail screen composable - Displays full content of a selected note.
 * 
 * Requirements: 3.1, 3.2, 3.3, 3.4, 3.5, 3.6
 * 
 * @param noteId The unique identifier of the note to display
 * @param viewModel ViewModel for managing note detail state
 * @param onNavigateBack Callback invoked when user navigates back
 * @param onNavigateToEdit Callback invoked when user taps the edit button
 * @param modifier Optional modifier for the screen
 */
@Composable
fun NoteDetailScreen(
    noteId: String,
    viewModel: NoteDetailViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    // Load note when screen is first displayed
    LaunchedEffect(noteId) {
        viewModel.loadNote(noteId)
    }
    
    // Show error message if present
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.clearError()
        }
    }
    
    // Show success message if present
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSuccessMessage()
        }
    }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        NoteDetailContent(
            uiState = uiState,
            onEditClick = { onNavigateToEdit(noteId) },
            onDeleteClick = { showDeleteDialog = true },
            modifier = Modifier.padding(paddingValues)
        )
    }
    
    // Delete confirmation dialog
    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                coroutineScope.launch {
                    val success = viewModel.deleteNote()
                    if (success) {
                        onNavigateBack()
                    }
                }
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
}

/**
 * Content composable for the note detail screen.
 * Handles loading, error, and populated states.
 */
@Composable
private fun NoteDetailContent(
    uiState: NoteDetailUiState,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator()
            }
            uiState.note == null -> {
                NoteNotFoundMessage()
            }
            else -> {
                NoteDetailView(
                    note = uiState.note,
                    onEditClick = onEditClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

/**
 * Displays the full note details with action buttons.
 * 
 * Requirement 3.1 - Display note title in TextView
 * Requirement 3.2 - Display note content in TextView
 * Requirement 3.3 - Show Edit button
 * Requirement 3.4 - Show Delete button
 */
@Composable
private fun NoteDetailView(
    note: Note,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Note content card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Requirement 3.1 - Note title display
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Timestamps
                Text(
                    text = "Created: ${formatTimestamp(note.createdAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                
                Text(
                    text = "Updated: ${formatTimestamp(note.updatedAt)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Requirement 3.2 - Note content display
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Requirement 3.3, 3.5 - Edit button
            Button(
                onClick = onEditClick,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit note"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Edit")
            }
            
            // Requirement 3.4, 3.6 - Delete button
            OutlinedButton(
                onClick = onDeleteClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete note"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete")
            }
        }
    }
}

/**
 * Delete confirmation dialog.
 * 
 * Requirement 3.6 - Confirm before deleting note
 */
@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Delete Note")
        },
        text = {
            Text("Are you sure you want to delete this note? This action cannot be undone.")
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

/**
 * Message displayed when note is not found.
 */
@Composable
private fun NoteNotFoundMessage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Note not found",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Text(
            text = "The note you're looking for doesn't exist",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/**
 * Format timestamp to readable date string.
 */
private fun formatTimestamp(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

// Preview composables
@Preview(showBackground = true, name = "Note Detail - With Content")
@Composable
fun NoteDetailScreenPreview() {
    Mobil_programlama_projeTheme {
        Surface {
            NoteDetailContent(
                uiState = NoteDetailUiState(
                    note = Note(
                        id = "1",
                        title = "Sample Note Title",
                        content = "This is a sample note content. It can be quite long and contain multiple paragraphs.\n\nThis is the second paragraph of the note content to demonstrate how longer content is displayed in the detail view.",
                        createdAt = System.currentTimeMillis() - 86400000,
                        updatedAt = System.currentTimeMillis()
                    ),
                    isLoading = false,
                    error = null,
                    successMessage = null
                ),
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Note Detail - Loading")
@Composable
fun NoteDetailScreenLoadingPreview() {
    Mobil_programlama_projeTheme {
        Surface {
            NoteDetailContent(
                uiState = NoteDetailUiState(
                    note = null,
                    isLoading = true,
                    error = null,
                    successMessage = null
                ),
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Note Detail - Not Found")
@Composable
fun NoteDetailScreenNotFoundPreview() {
    Mobil_programlama_projeTheme {
        Surface {
            NoteDetailContent(
                uiState = NoteDetailUiState(
                    note = null,
                    isLoading = false,
                    error = null,
                    successMessage = null
                ),
                onEditClick = {},
                onDeleteClick = {}
            )
        }
    }
}
