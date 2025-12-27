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
import com.example.mobil_programlama_proje.data.remote.AuthRepositoryImpl
import com.example.mobil_programlama_proje.data.remote.RetrofitClient
import com.example.mobil_programlama_proje.ui.screens.*
import com.example.mobil_programlama_proje.viewmodel.*

/**
 * Main navigation graph for the Note App.
 * Sets up all screen destinations and navigation arguments.
 */
@Composable
fun NoteAppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isConnected: Boolean
) {
    NavHost(
        navController = navController,
        // Uygulamanın giriş kapısını Login yapıyoruz
        startDestination = NavigationRoutes.Login.route,
        modifier = modifier
    ) {
        // --- LOGIN SCREEN ---
        composable(route = NavigationRoutes.Login.route) {
            // Manuel Dependency Injection: Repository -> Factory -> ViewModel
            val authRepository = AuthRepositoryImpl(RetrofitClient.authApiService)
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository)
            )

            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    // Giriş başarılı olunca Main (veya NoteList) ekranına yönlendir
                    navController.navigate(NavigationRoutes.Main.route) {
                        // Geri tuşuna basınca Login ekranına tekrar dönmesin diye stack'i temizle
                        popUpTo(NavigationRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // --- MAIN SCREEN (Entry point after login) ---
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

        // --- NOTE LIST SCREEN ---
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

        // --- NOTE DETAIL SCREEN ---
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

        // --- ADD/EDIT NOTE SCREEN ---
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