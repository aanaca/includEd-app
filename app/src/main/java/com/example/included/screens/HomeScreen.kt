package com.example.included.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.included.components.PostItem
import com.example.included.dialogs.CommentDialog
import com.example.included.dialogs.DeleteConfirmationDialog
import com.example.included.dialogs.NewPostDialog
import com.example.included.models.Comment
import com.example.included.models.Post
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSignOut: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    var showNewPostDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf<Post?>(null) }
    var showCommentDialog by remember { mutableStateOf<Post?>(null) }
    var posts by remember { mutableStateOf(listOf<Post>()) }

    // Posts de exemplo
    LaunchedEffect(Unit) {
        posts = List(5) { index ->
            Post(
                id = index.toString(),
                userId = if (index == 0) "user_atual" else "user$index",
                userName = "Usuário $index",
                content = "Este é um post de exemplo número $index #includEd",
                timestamp = Date(),
                likes = (0..50).random(),
                isLikedByCurrentUser = false,
                comments = if (index == 0) {
                    listOf(
                        Comment(
                            id = "comment1",
                            userId = "user2",
                            userName = "Usuário 2",
                            content = "Ótimo post!",
                            timestamp = Date(),
                            likes = 5,
                            isLikedByCurrentUser = true
                        )
                    )
                } else {
                    emptyList()
                }
            )
        }
    }

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
                            imageVector = Icons.Default.ExitToApp,
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
                Icon(Icons.Default.Add, contentDescription = "Novo Post")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(posts) { post ->
                PostItem(
                    post = post,
                    onLikeClick = {
                        posts = posts.map { currentPost ->
                            if (currentPost.id == post.id) {
                                currentPost.copy(
                                    likes = if (post.isLikedByCurrentUser)
                                        post.likes - 1
                                    else
                                        post.likes + 1,
                                    isLikedByCurrentUser = !post.isLikedByCurrentUser
                                )
                            } else {
                                currentPost
                            }
                        }
                    },
                    onCommentClick = { showCommentDialog = post },
                    onDeleteClick = { showDeleteConfirmation = post },
                    isCurrentUserPost = post.userId == "user_atual"
                )
                Divider()
            }
        }
    }

    // Dialog de novo post
    if (showNewPostDialog) {
        NewPostDialog(
            onDismiss = { showNewPostDialog = false },
            onPostCreated = { content ->
                val newPost = Post(
                    id = UUID.randomUUID().toString(),
                    userId = "user_atual",
                    userName = "Usuário Atual",
                    content = content,
                    timestamp = Date()
                )
                posts = listOf(newPost) + posts
                showNewPostDialog = false
                onShowMessage("Post publicado!")
            }
        )
    }

    // Dialog de confirmação de exclusão
    showDeleteConfirmation?.let { post ->
        DeleteConfirmationDialog(
            onConfirm = {
                posts = posts.filter { it.id != post.id }
                onShowMessage("Post deletado!")
                showDeleteConfirmation = null
            },
            onDismiss = { showDeleteConfirmation = null }
        )
    }

    // Dialog de comentários
    showCommentDialog?.let { post ->
        CommentDialog(
            comments = post.comments,
            postUserId = post.userId,
            onDismiss = { showCommentDialog = null },
            onCommentSend = { commentText ->
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
                    } else {
                        it
                    }
                }
                onShowMessage("Comentário adicionado!")
            },
            onCommentLike = { comment ->
                posts = posts.map { currentPost ->
                    if (currentPost.id == post.id) {
                        currentPost.copy(
                            comments = currentPost.comments.map { currentComment ->
                                if (currentComment.id == comment.id) {
                                    currentComment.copy(
                                        likes = if (comment.isLikedByCurrentUser)
                                            comment.likes - 1
                                        else
                                            comment.likes + 1,
                                        isLikedByCurrentUser = !comment.isLikedByCurrentUser
                                    )
                                } else {
                                    currentComment
                                }
                            }
                        )
                    } else {
                        currentPost
                    }
                }
            },
            onCommentDelete = { comment ->
                posts = posts.map { currentPost ->
                    if (currentPost.id == post.id) {
                        currentPost.copy(
                            comments = currentPost.comments.filterNot { it.id == comment.id }
                        )
                    } else {
                        currentPost
                    }
                }
                onShowMessage("Comentário excluído!")
            }
        )
    }
}