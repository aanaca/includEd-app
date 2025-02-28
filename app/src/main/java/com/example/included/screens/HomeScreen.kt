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
import androidx.compose.ui.text.PlatformTextStyle
import com.example.included.components.PostItem
import com.example.included.models.Post
import com.example.included.models.Comment
import java.util.*

@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    var posts by remember { mutableStateOf(emptyList<Post>()) }
    var showNewPostDialog by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<Post?>(null) }

    LaunchedEffect(Unit) {
        posts = generateSamplePosts()
    }

    Scaffold(
        topBar = { HomeTopBar(onSignOut) },
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
        PostsList(
            posts = posts,
            padding = padding,
            onPostAction = { actionType: PostAction, targetPost: Post ->
                when (actionType) {
                    PostAction.Comment -> {
                        selectedPost = targetPost
                        showCommentDialog = true
                    }
                    else -> handlePostAction(
                        action = actionType,
                        post = targetPost,
                        currentPosts = posts,
                        updatePosts = { updatedPosts -> posts = updatedPosts },
                        onShowMessage = onShowMessage
                    )
                }
            }
        )
    }

    if (showNewPostDialog) {
        NewPostDialog(
            onDismiss = { showNewPostDialog = false },
            onPostCreated = { content ->
                handlePostAction(
                    PostAction.Create,
                    Post(content = content),
                    posts,
                    { updatedPosts -> posts = updatedPosts },
                    onShowMessage
                )
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

                    posts = posts.map {
                        if (it.id == post.id) {
                            it.copy(comments = it.comments + newComment)
                        } else it
                    }
                    onShowMessage("Comentário adicionado!")
                }
                showCommentDialog = false
                selectedPost = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(onSignOut: () -> Unit) {
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
                onValueChange = { newValue ->
                    commentText = newValue
                },
                label = { Text("Seu comentário") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
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

@Composable
private fun PostsList(
    posts: List<Post>,
    padding: PaddingValues,
    onPostAction: (PostAction, Post) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        items(posts) { post ->
            PostItem(
                post = post,
                onLikeClick = { onPostAction(PostAction.Like, post) },
                onCommentClick = { onPostAction(PostAction.Comment, post) },
                onDeleteClick = { onPostAction(PostAction.Delete, post) },
                isCurrentUserPost = post.userId == "user_atual"
            )
            Divider()
        }
    }
}

private fun generateSamplePosts(): List<Post> {
    return List(5) { index ->
        Post(
            id = index.toString(),
            userId = if (index == 0) "user_atual" else "user$index",
            userName = "Usuário $index",
            content = "Post exemplo $index #includEd",
            timestamp = Date(),
            likes = 0,
            isLikedByCurrentUser = false,
            comments = emptyList()
        )
    }
}

sealed class PostAction {
    data object Like : PostAction()
    data object Comment : PostAction()
    data object Delete : PostAction()
    data object Create : PostAction()
}

private fun handlePostAction(
    action: PostAction,
    post: Post,
    currentPosts: List<Post>,
    updatePosts: (List<Post>) -> Unit,
    onShowMessage: (String) -> Unit
) {
    when (action) {
        PostAction.Like -> {
            updatePosts(currentPosts.map {
                if (it.id == post.id) {
                    it.copy(
                        likes = if (post.isLikedByCurrentUser) post.likes - 1 else post.likes + 1,
                        isLikedByCurrentUser = !post.isLikedByCurrentUser
                    )
                } else it
            })
            onShowMessage(if (post.isLikedByCurrentUser) "Post descurtido" else "Post curtido")
        }
        PostAction.Delete -> {
            updatePosts(currentPosts.filterNot { it.id == post.id })
            onShowMessage("Post deletado")
        }
        PostAction.Create -> {
            val newPost = Post(
                id = UUID.randomUUID().toString(),
                userId = "user_atual",
                userName = "Usuário Atual",
                content = post.content,
                timestamp = Date()
            )
            updatePosts(listOf(newPost) + currentPosts)
            onShowMessage("Post criado com sucesso!")
        }
        else -> { /* Handled in CommentDialog */ }
    }
}
