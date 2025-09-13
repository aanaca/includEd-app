package com.example.included.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.included.components.PostItem
import com.example.included.models.Post
import com.example.included.models.Comment
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    onShowMessage: (String) -> Unit,
    onNavigateToPost: (Post) -> Unit = {},
    sharedViewModel: SharedViewModel
) {
    var showNewPostDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<Post?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IncludEd") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = onSignOut) {
                        Icon(
                            Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Sair",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showNewPostDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, "Novo Post")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(sharedViewModel.posts) { post ->
                PostItem(
                    post = post,
                    onPostClick = { onNavigateToPost(post) },
                    onLikeClick = {
                        sharedViewModel.handlePostAction(PostAction.Like, post)
                        onShowMessage(if (post.isLikedByCurrentUser) "Post descurtido" else "Post curtido")
                    },
                    onCommentClick = {
                        selectedPost = post
                        showCommentDialog = true
                    },
                    onDeleteClick = if (post.userId == "user_atual") {
                        {
                            sharedViewModel.handlePostAction(PostAction.Delete, post)
                            onShowMessage("Post deletado")
                        }
                    } else null,
                    isCurrentUserPost = post.userId == "user_atual"
                )
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            }
        }
    }

    if (showNewPostDialog) {
        NewPostDialog(
            onDismiss = { showNewPostDialog = false },
            onPostCreated = { content ->
                val newPost = Post(
                    id = UUID.randomUUID().toString(),
                    userId = "user_atual",
                    userName = "Usuário Atual",
                    userHandle = "@usuario_atual",
                    content = content,
                    timestamp = Date()
                )
                sharedViewModel.handlePostAction(PostAction.Create, newPost)
                onShowMessage("Post criado com sucesso!")
                showNewPostDialog = false
            }
        )
    }

    if (showCommentDialog && selectedPost != null) {
        CommentDialog(
            onDismiss = {
                showCommentDialog = false
                selectedPost = null
            },
            onCommentAdded = { commentText ->
                selectedPost?.let { post ->
                    val newComment = Comment(
                        id = UUID.randomUUID().toString(),
                        userId = "user_atual",
                        userName = "Usuário Atual",
                        content = commentText,
                        timestamp = Date()
                    )
                    sharedViewModel.addComment(post.id, newComment)
                    onShowMessage("Comentário adicionado!")
                }
                showCommentDialog = false
                selectedPost = null
            }
        )
    }
}

@Composable
private fun NewPostDialog(
    onDismiss: () -> Unit,
    onPostCreated: (String) -> Unit
) {
    var postContent by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Post") },
        text = {
            OutlinedTextField(
                value = postContent,
                onValueChange = { newValue -> postContent = newValue },
                label = { Text("O que você está pensando?") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = false,
                maxLines = 5
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (postContent.isNotEmpty()) {
                        onPostCreated(postContent)
                    }
                },
                enabled = postContent.isNotEmpty()
            ) {
                Text("Publicar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
private fun CommentDialog(
    onDismiss: () -> Unit,
    onCommentAdded: (String) -> Unit
) {
    var commentText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Comentário") },
        text = {
            OutlinedTextField(
                value = commentText,
                onValueChange = { commentText = it },
                label = { Text("Seu comentário") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = false,
                maxLines = 3
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (commentText.isNotEmpty()) {
                        onCommentAdded(commentText)
                    }
                },
                enabled = commentText.isNotEmpty()
            ) {
                Text("Comentar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

sealed class PostAction {
    data object Like : PostAction()
    data object Comment : PostAction()
    data object Delete : PostAction()
    data object Create : PostAction()
    data object View : PostAction()
}