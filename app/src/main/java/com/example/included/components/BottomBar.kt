package com.example.included.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp 


sealed class BottomBarScreen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : BottomBarScreen("home", "Home", Icons.Default.Home)
    data object Search : BottomBarScreen("search", "Buscar", Icons.Default.Search)
    data object Activities : BottomBarScreen("activities_list", "Atividades", Icons.Default.Lightbulb)
    data object Notifications : BottomBarScreen("notifications", "Notificações", Icons.Default.Notifications)
    data object Profile : BottomBarScreen("profile", "Perfil", Icons.Default.Person)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(currentRoute: String?, onNavigate: (String) -> Unit, notificationCount: Int) {
    NavigationBar {
        val screens = listOf(
            BottomBarScreen.Home,
            BottomBarScreen.Search,
            BottomBarScreen.Activities,
            BottomBarScreen.Notifications,
            BottomBarScreen.Profile
        )
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    
                    BadgedBox(
                        badge = {
                            if (screen == BottomBarScreen.Notifications && notificationCount > 0) {
                                Badge { Text(notificationCount.toString()) }
                            }
                        }
                    ) {
                        Icon(screen.icon, contentDescription = screen.title)
                    }
                },
                label = {
                   
                    Text(
                        screen.title,
                        fontSize = 10.sp,
                        maxLines = 1,
                        
                    )
                },
                selected = currentRoute == screen.route,
                onClick = { onNavigate(screen.route) }
            )
        }
    }
}
