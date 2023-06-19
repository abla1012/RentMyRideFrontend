package com.example.androidapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

// Navigation der App
sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Favorites : Screen("favorites", "Favoriten", Icons.Filled.Favorite)
    object Search : Screen("search", "Suche", Icons.Filled.Search)
    object Post : Screen("post", "Inserieren", Icons.Filled.Add)
    object Messages : Screen("messages", "Nachrichten", Icons.Filled.Phone)
    object Settings : Screen("settings", "Einstellungen", Icons.Filled.Settings)
    object Details : Screen("details/{fahrzeugId}", "Details", Icons.Filled.Search)
}