package com.example.included.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import coil.compose.rememberAsyncImagePainter
import com.example.included.models.Post
import com.example.included.models.Comment
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    post: Post,
    onBack: () -> Unit,
    userName: String = "Nome de Usuário",
    userHandle: String = "@usuarioExemplo",
    profileImageUri: String? = null,
    onShowMessage: (String) -> Unit = {}
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))

    var isLiked by remember { mutableStateOf(post.isLikedByCurrentUser) }
    var likeCount by remember { mutableStateOf(post.likes) }
    val comments = post.comments.ifEmpty {
        listOf(
            Comment(
                id = "1",
                userId = "user1",
                userName = "João Silva",
                content = "Comentário legal!",
                timestamp = Date()
            ),
            Comment(
                id = "2",
                userId = "user2",
                userName = "Maria Santos",
                content = "Gostei do post!",
                timestamp = Date()
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Post principal
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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
                                            text = post.userName.firstOrNull()?.toString() ?: "",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = post.userName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = post.userHandle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = post.content,
                            style = MaterialTheme.typography.bodyLarge
                        )


                        Text(
                            text = dateFormat.format(post.timestamp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            TextButton(
                                onClick = { onShowMessage("Comentários abaixo!") },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    "💬 ${post.commentCount}",
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }

                            TextButton(
                                onClick = {
                                    isLiked = !isLiked
                                    likeCount = if (isLiked) likeCount + 1 else likeCount - 1
                                    onShowMessage(if (isLiked) "Post curtido!" else "Curtida removida")
                                },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(
                                    "${if (isLiked) "❤️" else "♡"} $likeCount",
                                    color = if (isLiked)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    "Comentários",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Divider()
            }


            items(comments) { comment ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = comment.userName,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = dateFormat.format(comment.timestamp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = comment.content,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            }
        }
    }
}
