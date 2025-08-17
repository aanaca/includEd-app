package com.example.included.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomBarScreen("home", "Home", Icons.Default.Home)
    object Search : BottomBarScreen("search", "Buscar", Icons.Default.Search)
    object Profile : BottomBarScreen("profile", "Perfil", Icons.Default.Person)
    object Notifications : BottomBarScreen("notifications", "Notificações", Icons.Default.Notifications)
}

@Composable
fun BottomBar(currentRoute: String?, onNavigate: (String) -> Unit, notificationCount: Int) {
    NavigationBar {
        val screens = listOf(BottomBarScreen.Home, BottomBarScreen.Search, BottomBarScreen.Notifications, BottomBarScreen.Profile)
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = { onNavigate(screen.route) }
            )
        }
    }
}
