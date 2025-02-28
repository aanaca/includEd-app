package com.example.included.components

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.included.models.Post
import com.example.included.models.Comment
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostItem(
    post: Post,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isCurrentUserPost: Boolean = false
) {
    val context = LocalContext.current
    var showMenu by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cabeçalho
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = post.userName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
                            .format(post.timestamp),
                        style = MaterialTheme.typography.bodySmall
                    )

                    Box {
                        IconButton(
                            onClick = { showMenu = true }
                        ) {
                            Icon(
                                Icons.Outlined.MoreVert,
                                contentDescription = "Mais opções"
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            if (isCurrentUserPost) {
                                DropdownMenuItem(
                                    text = { Text("Excluir") },
                                    onClick = {
                                        onDeleteClick()
                                        showMenu = false
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.error
                                    )
                                )
                            }
                            DropdownMenuItem(
                                text = { Text("Compartilhar") },
                                onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, post.content)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, null))
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }

            // Conteúdo
            Text(
                text = post.content,
                modifier = Modifier.padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            // Linha de ações (Like e Comentários)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botão de Like
                IconButton(onClick = onLikeClick) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (post.isLikedByCurrentUser)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.Favorite,
                            contentDescription = "Curtir",
                            tint = if (post.isLikedByCurrentUser)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = post.likes.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Botão de Comentários
                IconButton(onClick = onCommentClick) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.ChatBubbleOutline,
                            contentDescription = "Comentários",
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = post.comments.size.toString(),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Seção de Comentários
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                if (post.comments.isNotEmpty()) {
                    Text(
                        text = "Comentários",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    post.comments.forEach { comment ->
                        CommentItem(comment = comment)
                    }
                } else {
                    Text(
                        text = "Nenhum comentário ainda",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = comment.userName,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
                    .format(comment.timestamp),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Text(
            text = comment.content,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
