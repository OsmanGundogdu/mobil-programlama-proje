package com.example.mobil_programlama_proje.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mobil_programlama_proje.data.NoteRepositoryImpl
import com.example.mobil_programlama_proje.ui.screens.AddEditNoteScreen
import com.example.mobil_programlama_proje.ui.screens.MainScreen
import com.example.mobil_programlama_proje.ui.screens.NoteDetailScreen
import com.example.mobil_programlama_proje.ui.screens.NoteListScreen
import com.example.mobil_programlama_proje.viewmodel.AddEditNoteViewModel
import com.example.mobil_programlama_proje.viewmodel.NoteDetailViewModel
import com.example.mobil_programlama_proje.viewmodel.NoteListViewModel

/**
 * Main navigation graph for the Note App.
 * Sets up all screen destinations and navigation arguments.
 * 
 * @param navController The navigation controller for managing navigation
 * @param modifier Optional modifier for the NavHost
 */
@Composable
fun NoteAppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Main.route,
        modifier = modifier
    ) {
        // Main Screen - Entry point
        composable(route = NavigationRoutes.Main.route) {
            MainScreen(
                onNavigateToNoteList = {
                    navController.navigate(NavigationRoutes.NoteList.route)
                },
                onNavigateToAddNote = {
                    navController.navigate(NavigationRoutes.AddEditNote.createRouteForAdd())
                }
            )
        }
        
        // Note List Screen
        composable(route = NavigationRoutes.NoteList.route) {
            val repository = NoteRepositoryImpl()
            val viewModel: NoteListViewModel = viewModel(
                factory = NoteListViewModelFactory(repository)
            )
            
            NoteListScreen(
                viewModel = viewModel,
                onNavigateToDetail = { noteId ->
                    navController.navigate(NavigationRoutes.NoteDetail.createRoute(noteId))
                },
                onNavigateToAdd = {
                    navController.navigate(NavigationRoutes.AddEditNote.createRouteForAdd())
                }
            )
        }
        
        // Note Detail Screen - requires noteId argument
        composable(
            route = NavigationRoutes.NoteDetail.route,
            arguments = listOf(
                navArgument(NavigationRoutes.NoteDetail.ARG_NOTE_ID) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString(NavigationRoutes.NoteDetail.ARG_NOTE_ID)
            val repository = NoteRepositoryImpl()
            val viewModel: NoteDetailViewModel = viewModel(
                factory = NoteDetailViewModelFactory(repository)
            )
            
            noteId?.let {
                NoteDetailScreen(
                    noteId = it,
                    viewModel = viewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onNavigateToEdit = { id ->
                        navController.navigate(NavigationRoutes.AddEditNote.createRouteForEdit(id))
                    }
                )
            }
        }
        
        // Add/Edit Note Screen - optional noteId argument
        composable(
            route = NavigationRoutes.AddEditNote.route,
            arguments = listOf(
                navArgument(NavigationRoutes.AddEditNote.ARG_NOTE_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString(NavigationRoutes.AddEditNote.ARG_NOTE_ID)
            val repository = NoteRepositoryImpl()
            val viewModel: AddEditNoteViewModel = viewModel(
                factory = AddEditNoteViewModelFactory(repository)
            )
            
            AddEditNoteScreen(
                noteId = noteId,
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
