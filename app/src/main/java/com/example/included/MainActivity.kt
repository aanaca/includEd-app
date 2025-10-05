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
import androidx.activity.viewModels
import com.example.included.components.BottomBar
import com.example.included.models.Post // Importação para o modelo Post (necessário para o PostDetailScreen)
import com.example.included.models.User
import com.example.included.screens.*
import com.example.included.ui.theme.Theme

class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Dados de exemplo para followers e following
        val exampleFollowers = listOf(
            User("1", "João Silva", "@joao.silva", null, "Desenvolvedor Android", 50, 45),
            User("2", "Maria Santos", "@maria.santos", null, "Designer UX/UI", 120, 80),
            User("3", "Carlos Oliveira", "@carlos.oliveira", null, "Product Manager", 200, 150)
        )

        val exampleFollowing = listOf(
            User("4", "Ana Costa", "@ana.costa", null, "Tech Lead", 300, 200),
            User("5", "Pedro Souza", "@pedro.souza", null, "iOS Developer", 150, 100),
            User("6", "Laura Lima", "@laura.lima", null, "Full Stack Developer", 180, 160)
        )

        setContent {
            Theme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val notificationCount = 3

                Scaffold(
                    bottomBar = {
                        // ADIÇÃO: Incluindo 'reading_detail' para esconder o BottomBar.
                        if (currentRoute != "login" && currentRoute != "edit_profile" && currentRoute != "settings" &&
                            currentRoute != "followers" && currentRoute != "following" && currentRoute != "math_detail" &&
                            currentRoute != "reading_detail") { // Rota de Leitura
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
                                onSignOut = {
                                    Toast.makeText(this@MainActivity, "Logout clicado", Toast.LENGTH_SHORT).show()
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                onShowMessage = {
                                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show()
                                },
                                onNavigateToPost = { post ->
                                    navController.navigate("post_detail/${post.id}")
                                },
                                sharedViewModel = sharedViewModel
                            )
                        }

                        composable("search") {
                            SearchScreen { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() }
                        }

                        composable("activities_list") {
                            ActivitiesScreen(
                                onCategoryClick = { categoryRoute ->
                                    // Navega para a rota de detalhe da atividade (ex: "math_detail" ou "reading_detail")
                                    navController.navigate(categoryRoute)
                                }
                            )
                        }

                        // ROTA: Matemática
                        composable("math_detail") {
                            MathActivitiesScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }

                        // ROTA: Leitura (NOVA ADIÇÃO)
                        composable("reading_detail") {
                            ReadingActivitiesScreen(
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        // --- FIM ROTA DE LEITURA ---

                        composable("notifications") {
                            NotificationsScreen(onNotificationClick = { postId ->
                                if (postId != null) navController.navigate("post_detail/$postId")
                            })
                        }

                        composable("profile") {
                            ProfileScreen(
                                followers = exampleFollowers,
                                following = exampleFollowing,
                                onShowMessage = { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() },
                                onEditProfile = { navController.navigate("edit_profile") },
                                onSettings = { navController.navigate("settings") },
                                onPostClick = { post -> navController.navigate("post_detail/${post.id}") },
                                onFollowersClick = { navController.navigate("followers") },
                                onFollowingClick = { navController.navigate("following") },
                                onUserClick = { user ->
                                    Toast.makeText(this@MainActivity, "Clicou no usuário: ${user.name}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        composable("followers") {
                            FollowersScreen(
                                followers = exampleFollowers,
                                onBackClick = { navController.popBackStack() },
                                onUserClick = { user ->
                                    Toast.makeText(this@MainActivity, "Clicou no seguidor: ${user.name}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        composable("following") {
                            FollowingScreen(
                                following = exampleFollowing,
                                onBackClick = { navController.popBackStack() },
                                onUserClick = { user ->
                                    Toast.makeText(this@MainActivity, "Clicou no seguindo: ${user.name}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }

                        composable("edit_profile") {
                            EditProfileScreen(
                                onBack = { navController.popBackStack() },
                                onSave = { _, _, _, _ ->
                                    Toast.makeText(this@MainActivity, "Perfil atualizado!", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                            )
                        }

                        composable("settings") {
                            SettingsScreen(
                                onBack = { navController.popBackStack() },
                                onShowMessage = { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() }
                            )
                        }

                        composable("post_detail/{postId}") { backStackEntry ->
                            val postId = backStackEntry.arguments?.getString("postId")?.toIntOrNull()
                            val post = listOf(
                                Post(1, "Primeiro post! Bem-vindo ao meu perfil.", "10/01/2023 10:00"),
                                Post(2, "Compartilhando minhas ideias.", "12/01/2023 15:30")
                            ).find { it.id == postId }
                            post?.let {
                                PostDetailScreen(
                                    post = it,
                                    onBack = { navController.popBackStack() },
                                    userName = "Nome de Usuário",
                                    userHandle = "@usuarioExemplo",
                                    onShowMessage = { message ->
                                        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }

                        // Rotas restantes (Logic, Memory) serão adicionadas aqui
                    }
                }
            }
        }
    }
}
