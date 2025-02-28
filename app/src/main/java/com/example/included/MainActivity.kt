package com.example.included

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.included.components.BottomBar
import com.example.included.screens.*

import com.example.included.ui.theme.Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Theme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            if (currentRoute != "login" &&
                                currentRoute != "edit_profile" &&
                                currentRoute != "settings"
                            ) {
                                BottomBar(currentRoute = currentRoute, onNavigate = { route ->
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                })
                            }
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = navController,
                            // ⚠️ Agora inicia na tela de login!
                            startDestination = "login",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            // Tela de Login
                            composable("login") {
                                LoginScreen(
                                    onLoginSuccess = {
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    },
                                    onShowMessage = { message ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                            // Tela Home
                            composable("home") {
                                HomeScreen(
                                    onSignOut = {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Logout clicado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.navigate("login") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    },
                                    onShowMessage = { message ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                            composable("search") {
                                SearchScreen { message ->
                                    Toast.makeText(
                                        this@MainActivity,
                                        message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            composable("profile") {
                                ProfileScreen(
                                    onShowMessage = { message ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    onEditProfile = { navController.navigate("edit_profile") },
                                    onSettings = { navController.navigate("settings") }
                                )
                            }
                            composable("edit_profile") {
                                EditProfileScreen(
                                    onBack = { navController.popBackStack() },
                                    onSave = { _, _, _ ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Perfil atualizado!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.popBackStack()
                                    }
                                )
                            }
                            composable("settings") {
                                SettingsScreen(
                                    onBack = { navController.popBackStack() },
                                    onShowMessage = { message ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
