package com.practicum.myapplication.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.practicum.myapplication.ui.view_model.PlaylistsViewModel
import com.practicum.myapplication.ui.view_model.SearchViewModel
import com.practicum.myapplication.ui.screens.SearchScreen

enum class AppScreen {
    Main, Search, Playlists, Favorites, Settings, NewPlaylist, TrackDetails
}

@Composable
fun PlaylistHost(navController: NavHostController) {
    val playlistsViewModel: PlaylistsViewModel = viewModel()

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
            val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory())
            SearchScreen(
                viewModel = searchViewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { track ->
                    navController.navigate("${AppScreen.TrackDetails.name}/${track.id}")
                }
            )
        }

        composable(route = AppScreen.Playlists.name) {
            PlaylistsScreen(
                playlistsViewModel = playlistsViewModel,
                onBackClick = { navController.popBackStack() },
                onAddNewPlaylistClick = { navController.navigate(AppScreen.NewPlaylist.name) }
            )
        }

        composable(route = AppScreen.NewPlaylist.name) {
            NewPlaylistScreen(
                viewModel = playlistsViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(route = AppScreen.Favorites.name) {
            FavoritesScreen(
                viewModel = playlistsViewModel,
                onBackClick = { navController.popBackStack() },
                onTrackClick = { trackId ->
                    navController.navigate("${AppScreen.TrackDetails.name}/$trackId")
                }
            )
        }

        composable(route = "${AppScreen.TrackDetails.name}/{trackId}") { backStackEntry ->
            val trackIdString = backStackEntry.arguments?.getString("trackId")
            val trackId = trackIdString?.toLongOrNull()
            if (trackId != null) {
                TrackDetailsScreen(
                    trackId = trackId,
                    viewModel = playlistsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            } else {
                navController.popBackStack()
            }
        }

        composable(route = AppScreen.Settings.name) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
    }
}