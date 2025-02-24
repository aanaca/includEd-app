package com.example.included.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class Post(
    val id: Int,
    val content: String,
    val timestamp: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onShowMessage: (String) -> Unit,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    posts: List<Post> = listOf(
        Post(1, "Primeiro post! Bem-vindo ao meu perfil.", "10/01/2023 10:00"),
        Post(2, "Compartilhando minhas ideias.", "12/01/2023 15:30")
    ),
    userName: String = "Nome de Usuário",
    userHandle: String = "@usuarioExemplo",
    userBio: String = "Essa é a bio do usuário. Conte algo sobre você.",
    createdAt: String = "Criado em 01/01/2023"
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil") },
                actions = {
                    IconButton(onClick = onEditProfile) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar Perfil"
                        )
                    }
                    IconButton(onClick = onSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configurações"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabeçalho do perfil
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        modifier = Modifier.size(100.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 4.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = userName.firstOrNull()?.toString() ?: "",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = userName,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                    )
                    Text(
                        text = userHandle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = userBio,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                    OutlinedButton(
                        onClick = onEditProfile,
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Text("Editar Perfil")
                    }
                }
            }
            // Divisor e título para a seção de posts
            item {
                Divider()
                Text(
                    text = "Posts",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            // Lista de posts
            items(posts) { post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onShowMessage("Post ID: ${post.id}") },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = post.content,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = post.timestamp,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}