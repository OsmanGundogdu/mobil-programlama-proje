package com.example.mobil_programlama_proje.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobil_programlama_proje.ui.theme.Mobil_programlama_projeTheme

/**
 * Main screen composable - Entry point of the application.
 * Provides navigation options to view note list or add a new note.
 * 
 * Requirements: 1.1, 1.2, 1.3, 1.4, 1.5
 * 
 * @param onNavigateToNoteList Callback invoked when user taps "Go to note list" button
 * @param onNavigateToAddNote Callback invoked when user taps "Add new note" button
 * @param modifier Optional modifier for the screen
 */
@Composable
fun MainScreen(
    onNavigateToNoteList: () -> Unit,
    onNavigateToAddNote: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App title
            Text(
                text = "Note App",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Go to note list button - Requirement 1.2, 1.4
            Button(
                onClick = onNavigateToNoteList,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Go to note list",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Add new note button - Requirement 1.3, 1.5
            Button(
                onClick = onNavigateToAddNote,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Add new note",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

/**
 * Preview for MainScreen in light theme
 */
@Preview(showBackground = true, name = "Main Screen Light")
@Composable
fun MainScreenPreview() {
    Mobil_programlama_projeTheme(darkTheme = false) {
        MainScreen(
            onNavigateToNoteList = {},
            onNavigateToAddNote = {}
        )
    }
}

/**
 * Preview for MainScreen in dark theme
 */
@Preview(showBackground = true, name = "Main Screen Dark")
@Composable
fun MainScreenDarkPreview() {
    Mobil_programlama_projeTheme(darkTheme = true) {
        MainScreen(
            onNavigateToNoteList = {},
            onNavigateToAddNote = {}
        )
    }
}
