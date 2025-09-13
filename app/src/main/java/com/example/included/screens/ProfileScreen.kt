package com.example.included.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.included.models.User

data class Post(
    val id: Int,
    val content: String,
    val timestamp: String,
    val commentCount: Int = 0,
    val likeCount: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    followers: List<User>,
    following: List<User>,
    posts: List<Post> = listOf(
        Post(1, "Primeiro post! Bem-vindo ao meu perfil.", "10/01/2023 10:00", 5, 10),
        Post(2, "Compartilhando minhas ideias.", "12/01/2023 15:30", 2, 7)
    ),
    userName: String = "Nome de Usuário",
    userHandle: String = "@usuarioExemplo",
    userBio: String = "Essa é a bio do usuário. Conte algo sobre você.",
    createdAt: String = "Criado em 01/01/2023",
    onShowMessage: (String) -> Unit,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    onPostClick: (Post) -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onUserClick: (User) -> Unit
) {
    val listState = rememberLazyListState()
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri -> profileImageUri = uri }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil") },
                actions = {
                    IconButton(onClick = onEditProfile) { Icon(Icons.Default.Edit, contentDescription = "Editar Perfil") }
                    IconButton(onClick = onSettings) { Icon(Icons.Default.Settings, contentDescription = "Configurações") }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .clickable { launcher.launch("image/*") },
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 4.dp
                    ) {
                        if (profileImageUri != null) {
                            Image(painter = rememberAsyncImagePainter(profileImageUri), contentDescription = "Foto de Perfil", modifier = Modifier.fillMaxSize())
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = userName.firstOrNull()?.toString() ?: "", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = userName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    Text(text = userHandle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(4.dp))

                    // Followers and Following counts with click handlers
                    Row(
                        modifier = Modifier.clickable { },
                        horizontalArrangement = Arrangement.Center
                    ) {
                        TextButton(onClick = onFollowersClick) {
                            Text(
                                text = "${followers.size} seguidores",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Text(
                            text = "•",
                            modifier = Modifier.padding(horizontal = 4.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        TextButton(onClick = onFollowingClick) {
                            Text(
                                text = "${following.size} seguindo",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = createdAt, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = userBio, style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
                    OutlinedButton(onClick = { launcher.launch("image/*") }, modifier = Modifier.padding(top = 12.dp)) {
                        Text("Alterar Foto de Perfil")
                    }
                }
            }

            item { Divider(); Text("Posts", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp)) }

            itemsIndexed(posts) { _, post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPostClick(post) },
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Foto de perfil do post (usando a mesma do perfil)
                        Surface(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            if (profileImageUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(profileImageUri),
                                    contentDescription = "Foto de Perfil",
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = userName.firstOrNull()?.toString() ?: "",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Conteúdo do post
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Cabeçalho com nome e handle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row {
                                    Text(
                                        text = userName,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = userHandle,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                Text(
                                    text = post.timestamp,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Conteúdo do post
                            Text(
                                text = post.content,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Ícones de interação com estados
                            var isLiked by remember { mutableStateOf(false) }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                // Botão de comentários
                                TextButton(
                                    onClick = { onShowMessage("Comentários em breve!") },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            "💬 ${post.commentCount}",
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }

                                // Botão de curtir
                                TextButton(
                                    onClick = {
                                        isLiked = !isLiked
                                        onShowMessage(if (isLiked) "Post curtido!" else "Curtida removida")
                                    },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            "${if (isLiked) "❤️" else "♡"} ${post.likeCount + (if (isLiked) 1 else 0)}",
                                            color = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            }
        }
    }
}