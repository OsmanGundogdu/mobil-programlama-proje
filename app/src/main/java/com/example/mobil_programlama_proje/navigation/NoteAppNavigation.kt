package com.example.mobil_programlama_proje.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // Context için gerekli
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mobil_programlama_proje.data.NoteRepositoryImpl
import com.example.mobil_programlama_proje.database.AppDatabase
import com.example.mobil_programlama_proje.ui.screens.*
import com.example.mobil_programlama_proje.viewmodel.*
import com.example.mobil_programlama_proje.data.remote.AuthRepository

@Composable
fun NoteAppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isConnected: Boolean
) {
    // 1. ADIM: Veritabanı ve DAO'ya erişim (Local Veritabanı için)
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val userDao = database.userDao()

    // Repository'yi burada oluşturuyoruz (UserDao istiyor)
    val authRepository = AuthRepository(userDao)

    NavHost(
        navController = navController,
        startDestination = NavigationRoutes.Login.route,
        modifier = modifier
    ) {
        // --- LOGIN SCREEN ---
        composable(route = NavigationRoutes.Login.route) {
            // Factory içine artık Retrofit değil, yukarıda oluşturduğumuz local repository'yi veriyoruz
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository)
            )

            LoginScreen(
                viewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(NavigationRoutes.Main.route) {
                        popUpTo(NavigationRoutes.Login.route) { inclusive = true }
                    }
                },
                // BURASI EKSİKTİ, ŞİMDİ EKLENDİ:
                onNavigateToRegister = {
                    navController.navigate(NavigationRoutes.Register.route)
                }
            )
        }

        // --- REGISTER SCREEN (YENİ EKLENDİ) ---
        composable(route = NavigationRoutes.Register.route) {
            val authViewModel: AuthViewModel = viewModel(
                factory = AuthViewModelFactory(authRepository)
            )

            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = {
                    // Kayıt başarılı olunca Login ekranına geri dönüp giriş yapmasını sağlayabiliriz
                    // veya direkt Main'e atabiliriz. Şimdilik Login'e dönsün:
                    navController.popBackStack()
                },
                onNavigateToLogin = {
                    // "Zaten hesabım var"a basınca geri dön
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