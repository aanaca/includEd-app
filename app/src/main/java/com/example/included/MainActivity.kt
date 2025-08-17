package com.example.included

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.included.components.BottomBar
import com.example.included.screens.*
import com.example.included.ui.theme.Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val notificationCount = 3

                Scaffold(
                    bottomBar = {
                        if (currentRoute != "login" && currentRoute != "edit_profile" && currentRoute != "settings") {
                            BottomBar(currentRoute, { route ->
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }, notificationCount)
                        }
                    }
                ) { paddingValues ->
                    NavHost(navController, startDestination = "login", modifier = Modifier.padding(paddingValues)) {
                        composable("login") {
                            LoginScreen(
                                onLoginSuccess = { navController.navigate("home") { popUpTo("login") { inclusive = true } } },
                                onShowMessage = { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() }
                            )
                        }

                        composable("home") {
                            HomeScreen(
                                onSignOut = { Toast.makeText(this@MainActivity, "Logout clicado", Toast.LENGTH_SHORT).show(); navController.navigate("login") { popUpTo("home") { inclusive = true } } },
                                onShowMessage = { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() }
                            )
                        }

                        composable("search") { SearchScreen { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() } }

                        composable("notifications") {
                            NotificationsScreen(onNotificationClick = { postId ->
                                if (postId != null) navController.navigate("post_detail/$postId")
                            })
                        }

                        composable("profile") {
                            ProfileScreen(
                                followers = 120,
                                following = 80,
                                onShowMessage = { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() },
                                onEditProfile = { navController.navigate("edit_profile") },
                                onSettings = { navController.navigate("settings") },
                                onPostClick = { post -> navController.navigate("post_detail/${post.id}") }
                            )
                        }

                        composable("edit_profile") { EditProfileScreen(onBack = { navController.popBackStack() }, onSave = { _, _, _, _ -> Toast.makeText(this@MainActivity, "Perfil atualizado!", Toast.LENGTH_SHORT).show(); navController.popBackStack() }) }

                        composable("settings") { SettingsScreen(onBack = { navController.popBackStack() }, onShowMessage = { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() }) }

                        composable("post_detail/{postId}") { backStackEntry ->
                            val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull()
                            val post = listOf(
                                Post(1, "Primeiro post! Bem-vindo ao meu perfil.", "10/01/2023 10:00"),
                                Post(2, "Compartilhando minhas ideias.", "12/01/2023 15:30")
                            ).find { it.id == postId }
                            post?.let { PostDetailScreen(it) { navController.popBackStack() } }
                        }
                    }
                }
            }
        }
    }
}
