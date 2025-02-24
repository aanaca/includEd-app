package com.example.included.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    //val icon: ImageVector
) {
    object Home : BottomBarScreen(
        route = "home",
        title = "Home",
        //icon = Icons.Default.Home
    )

    object Search : BottomBarScreen(
        route = "search",
        title = "Buscar",
        //icon = Icons.Default.Search
    )

    object Profile : BottomBarScreen(
        route = "profile",
        title = "Perfil",
        //icon = Icons.Default.Person
    )
}

@Composable
fun BottomBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        listOf(
            BottomBarScreen.Home,
            BottomBarScreen.Search,
            BottomBarScreen.Profile
        ).forEach { screen ->
            NavigationBarItem(
                icon = {
                    //Icon(
                        //imageVector = screen.icon,
                        //contentDescription = screen.title
                  //  )
                },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = { onNavigate(screen.route) }
            )
        }
    }
}