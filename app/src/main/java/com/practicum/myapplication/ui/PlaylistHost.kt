package com.practicum.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel // ВАЖНО: Нужен этот импорт
import com.practicum.myapplication.ui.view_model.SearchViewModel

enum class AppScreen {
    Main,
    Search,
    Settings
}

@Composable
fun PlaylistHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Main.name
    ) {
        composable(route = AppScreen.Main.name) {
            MainScreen(
                onSearchClick = { navController.navigate(AppScreen.Search.name) },
                onSettingsClick = { navController.navigate(AppScreen.Settings.name) }
            )
        }

        composable(route = AppScreen.Search.name) {
            // Инициализируем ViewModel с использованием Фабрики
            val viewModel: SearchViewModel = viewModel(
                factory = SearchViewModel.getViewModelFactory()
            )

            SearchScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppScreen.Settings.name) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}