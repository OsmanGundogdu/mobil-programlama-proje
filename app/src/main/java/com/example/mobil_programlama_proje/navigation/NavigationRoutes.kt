package com.example.mobil_programlama_proje.navigation

/**
 * Sealed class representing all navigation routes in the app.
 * Provides type-safe navigation with compile-time route validation.
 */
sealed class NavigationRoutes(val route: String) {

    data object Login : NavigationRoutes("login")
    /**
     * Main screen route - entry point of the application
     */
    data object Main : NavigationRoutes("main")
    
    /**
     * Note list screen route - displays all notes
     */
    data object NoteList : NavigationRoutes("note_list")
    
    /**
     * Note detail screen route - displays a specific note
     * Requires noteId as a navigation argument
     */
    data object NoteDetail : NavigationRoutes("note_detail/{noteId}") {
        /**
         * Creates a route with the actual noteId value
         */
        fun createRoute(noteId: String): String = "note_detail/$noteId"
        
        /**
         * Argument key for extracting noteId from navigation arguments
         */
        const val ARG_NOTE_ID = "noteId"
    }
    
    /**
     * Add/Edit note screen route - for creating new notes or editing existing ones
     * Optional noteId argument - if null, creates new note; if present, edits existing note
     */
    data object AddEditNote : NavigationRoutes("add_edit_note?noteId={noteId}") {
        /**
         * Creates a route for adding a new note (no noteId)
         */
        fun createRouteForAdd(): String = "add_edit_note"
        
        /**
         * Creates a route for editing an existing note
         */
        fun createRouteForEdit(noteId: String): String = "add_edit_note?noteId=$noteId"
        
        /**
         * Argument key for extracting noteId from navigation arguments
         */
        const val ARG_NOTE_ID = "noteId"
    }
}
