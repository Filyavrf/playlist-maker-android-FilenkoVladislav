package com.practicum.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.myapplication.ui.view_model.SearchViewModel

enum class AppScreen {
    Main,
    Search,
    Playlists,
    Favorites,
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
                onPlaylistsClick = { navController.navigate(AppScreen.Playlists.name) },
                onFavoritesClick = { navController.navigate(AppScreen.Favorites.name) },
                onSettingsClick = { navController.navigate(AppScreen.Settings.name) }
            )
        }

        composable(route = AppScreen.Search.name) {
            val viewModel: SearchViewModel = viewModel(
                factory = SearchViewModel.getViewModelFactory()
            )
            SearchScreen(
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppScreen.Playlists.name) {
            PlaylistsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppScreen.Favorites.name) {
            FavoritesScreen(
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