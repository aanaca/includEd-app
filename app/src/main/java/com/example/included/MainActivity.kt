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
import com.example.included.screens.EditProfileScreen
import com.example.included.screens.HomeScreen
import com.example.included.screens.LoginScreen
import com.example.included.screens.ProfileScreen
import com.example.included.screens.SearchScreen
import com.example.included.screens.SettingsScreen
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
                        // Oculta a BottomBar nas telas de login, edição de perfil e configurações.
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
                            // Inicia direto na tela "home"
                            startDestination = "home",
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            /*
                            // Tela de Login (mantida como comentário para referência futura)
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
                            */
                            composable("home") {
                                HomeScreen(
                                    onSignOut = {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Logout clicado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        // Pode direcionar para o login futuramente
                                        navController.navigate("login") {
                                            popUpTo(navController.graph.startDestinationId) {
                                                inclusive = true
                                            }
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
                                SearchScreen(
                                    onShowMessage = { message ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
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
                                    onSave = { name, handle, bio ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Perfil atualizado!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        navController.popBackStack()
                                    }
                                )
                            }
                            // Nova rota para a tela de Configurações
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