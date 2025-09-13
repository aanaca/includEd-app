package com.example.included.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.included.models.Post
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PostItem(
    post: Post,
    onPostClick: (Post) -> Unit,
    onLikeClick: (Post) -> Unit,
    onCommentClick: (Post) -> Unit,
    onDeleteClick: ((Post) -> Unit)? = null,
    isCurrentUserPost: Boolean = false,
    profileImageUrl: String? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPostClick(post) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cabeçalho do post
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Foto de perfil e informações do usuário
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Foto de perfil
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        if (profileImageUrl != null) {
                            Image(
                                painter = rememberAsyncImagePainter(profileImageUrl),
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

                    // Nome e handle do usuário
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

                // Menu de opções (apenas para posts do usuário atual)
                if (isCurrentUserPost && onDeleteClick != null) {
                    var showMenu by remember { mutableStateOf(false) }
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "Mais opções"
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Excluir") },
                                onClick = {
                                    onDeleteClick(post)
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }

            // Conteúdo do post
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            // Data
            Text(
                text = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
                    .format(post.timestamp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botões de interação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = { onCommentClick(post) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        "💬 ${post.commentCount}",
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                TextButton(
                    onClick = { onLikeClick(post) },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        "${if (post.isLikedByCurrentUser) "❤️" else "♡"} ${post.likes}",
                        color = if (post.isLikedByCurrentUser)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}