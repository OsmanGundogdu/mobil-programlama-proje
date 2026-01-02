package com.example.mobil_programlama_proje.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mobil_programlama_proje.data.NoteRepository
import com.example.mobil_programlama_proje.database.AppDatabase
import com.example.mobil_programlama_proje.ui.screens.*
import com.example.mobil_programlama_proje.viewmodel.*
import com.example.mobil_programlama_proje.data.remote.AuthRepository
import com.example.mobil_programlama_proje.database.PreferenceManager // <-- IMPORT

@Composable
fun NoteAppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isConnected: Boolean
) {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val userDao = database.userDao()

    // Repository ve Manager Hazırlığı
    val authRepository = AuthRepository(userDao)

    // 1. MANAGER'I BURADA OLUŞTURUYORUZ
    val preferenceManager = remember { PreferenceManager(context) }

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Login.route,
        modifier = modifier
    ) {
        // --- LOGIN SCREEN ---
        composable(route = NavigationRoutes.Login.route) {
            // 2. FACTORY'YE HEM REPO HEM MANAGER VERİYORUZ
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository, preferenceManager)
            )

            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(NavigationRoutes.Main.route) {
                        popUpTo(NavigationRoutes.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(NavigationRoutes.Register.route)
                }
            )
        }

        // --- REGISTER SCREEN ---
        composable(route = NavigationRoutes.Register.route) {
            // Register ekranı için de aynı factory
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository, preferenceManager)
            )

            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }


        // --- MAIN SCREEN ---
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
            val noteDao = database.noteDao()
            val repository = NoteRepository(noteDao)
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
                },
                onNavigateToHome = {
                    navController.navigate(NavigationRoutes.Main.route) {
                        popUpTo(NavigationRoutes.Main.route) { inclusive = false }
                    }
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
            val noteDao = database.noteDao()
            val repository = NoteRepository(noteDao)

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
                    },
                    onNavigateToHome = {
                        navController.navigate(NavigationRoutes.Main.route) {
                            popUpTo(NavigationRoutes.Main.route) { inclusive = false }
                        }
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
            val noteDao = database.noteDao()
            val repository = NoteRepository(noteDao)

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