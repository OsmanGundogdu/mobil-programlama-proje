package com.example.mobil_programlama_proje.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobil_programlama_proje.model.Note
import com.example.mobil_programlama_proje.model.NoteListUiState
import com.example.mobil_programlama_proje.ui.theme.Mobil_programlama_projeTheme
import com.example.mobil_programlama_proje.viewmodel.NoteListViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Note List screen composable - Displays all notes in a scrollable list.
 * 
 * Requirements: 2.1, 2.2, 2.3, 2.4, 2.5, 2.6
 * 
 * @param viewModel ViewModel for managing note list state
 * @param onNavigateToDetail Callback invoked when user taps on a note
 * @param onNavigateToAdd Callback invoked when user taps the add button
 * @param modifier Optional modifier for the screen
 */
@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            // Requirement 2.4, 2.5 - FloatingActionButton for adding new notes
            FloatingActionButton(
                onClick = onNavigateToAdd,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new note"
                )
            }
        }
    ) { paddingValues ->
        NoteListContent(
            uiState = uiState,
            onNoteClick = onNavigateToDetail,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

/**
 * Content composable for the note list screen.
 * Handles loading, empty, and populated states.
 */
@Composable
private fun NoteListContent(
    uiState: NoteListUiState,
    onNoteClick: (String) -> Unit,
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
            uiState.notes.isEmpty() -> {
                // Requirement 2.1 - Handle empty state
                EmptyNoteListMessage()
            }
            else -> {
                // Requirement 2.1, 2.6 - Display all notes with reactive updates
                NoteList(
                    notes = uiState.notes,
                    onNoteClick = onNoteClick
                )
            }
        }
    }
}

/**
 * LazyColumn displaying the list of notes.
 * 
 * Requirement 2.1 - Display all notes using LazyColumn
 * Requirement 2.6 - Reactive updates when data changes
 */
@Composable
private fun NoteList(
    notes: List<Note>,
    onNoteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = notes,
            key = { note -> note.id }
        ) { note ->
            // Requirement 2.2, 2.3 - Note item with preview and navigation
            NoteItem(
                note = note,
                onClick = { onNoteClick(note.id) }
            )
        }
    }
}

/**
 * Individual note item composable with preview information.
 * 
 * Requirement 2.2 - Show each note with a preview
 * Requirement 2.3 - Navigate to detail on tap
 */
@Composable
private fun NoteItem(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
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
            // Note title
            Text(
                text = note.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            // Note content preview
            if (note.content.isNotEmpty()) {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            // Timestamp
            Text(
                text = formatTimestamp(note.updatedAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * Empty state message when no notes exist.
 */
@Composable
private fun EmptyNoteListMessage(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No notes yet",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )
        Text(
            text = "Tap the + button to create your first note",
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
@Preview(showBackground = true, name = "Note List - With Notes")
@Composable
fun NoteListScreenPreview() {
    Mobil_programlama_projeTheme {
        NoteListContent(
            uiState = NoteListUiState(
                notes = listOf(
                    Note(
                        id = "1",
                        title = "First Note",
                        content = "This is the content of the first note. It can be quite long and will be truncated in the preview.",
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    ),
                    Note(
                        id = "2",
                        title = "Second Note",
                        content = "Another note with different content.",
                        createdAt = System.currentTimeMillis() - 86400000,
                        updatedAt = System.currentTimeMillis() - 86400000
                    ),
                    Note(
                        id = "3",
                        title = "Third Note with a Very Long Title That Will Be Truncated",
                        content = "Short content",
                        createdAt = System.currentTimeMillis() - 172800000,
                        updatedAt = System.currentTimeMillis() - 172800000
                    )
                ),
                isLoading = false,
                error = null,
                successMessage = null
            ),
            onNoteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Note List - Empty")
@Composable
fun NoteListScreenEmptyPreview() {
    Mobil_programlama_projeTheme {
        NoteListContent(
            uiState = NoteListUiState(
                notes = emptyList(),
                isLoading = false,
                error = null,
                successMessage = null
            ),
            onNoteClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Note List - Loading")
@Composable
fun NoteListScreenLoadingPreview() {
    Mobil_programlama_projeTheme {
        NoteListContent(
            uiState = NoteListUiState(
                notes = emptyList(),
                isLoading = true,
                error = null,
                successMessage = null
            ),
            onNoteClick = {}
        )
    }
}
