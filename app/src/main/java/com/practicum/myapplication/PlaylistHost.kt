package com.practicum.myapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

enum class AppScreen {
    Main, Search, Settings
}

@Composable
fun PlaylistHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppScreen.Main.name
    ) {
        composable(AppScreen.Main.name) {
            MainScreen(
                onSearchClick = { navController.navigate(AppScreen.Search.name) },
                onSettingsClick = { navController.navigate(AppScreen.Settings.name) }
            )
        }
        composable(AppScreen.Search.name) { SearchScreen() }
        composable(AppScreen.Settings.name) { SettingsScreen() }
    }
}