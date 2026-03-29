package com.example.included.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.included.components.PostItem
import com.example.included.models.Attachment
import com.example.included.models.AttachmentType
import com.example.included.models.Comment
import com.example.included.models.Post
import java.io.File
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
                    isCurrentUserPost = post.userId == "user_atual",
                    userType = if (post.userId == "user_atual") sharedViewModel.userProfile.userType else ""
                )
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            }
        }
    }

    if (showNewPostDialog) {
        NewPostDialog(
            onDismiss = { showNewPostDialog = false },
            onPostCreated = { content, attachments ->
                val newPost = Post(
                    id = UUID.randomUUID().toString(),
                    userId = "user_atual",
                    userName = "Usuário Atual",
                    userHandle = "@usuario_atual",
                    content = content,
                    timestamp = Date(),
                    attachments = attachments
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
    onPostCreated: (String, List<Attachment>) -> Unit
) {
    var postContent by remember { mutableStateOf("") }
    val attachments = remember { mutableStateListOf<Attachment>() }
    val context = LocalContext.current

    // Launcher para fotos e vídeos da galeria
    val mediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        uris.forEach { uri ->
            val mimeType = context.contentResolver.getType(uri) ?: ""
            val type = when {
                mimeType.startsWith("image") -> AttachmentType.IMAGE
                mimeType.startsWith("video") -> AttachmentType.VIDEO
                else -> AttachmentType.DOCUMENT
            }
            val name = uri.lastPathSegment ?: "arquivo"
            attachments.add(Attachment(uri = uri.toString(), type = type, name = name))
        }
    }

    // Launcher para documentos
    val documentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments()
    ) { uris ->
        uris.forEach { uri ->
            val name = uri.lastPathSegment ?: "documento"
            attachments.add(Attachment(uri = uri.toString(), type = AttachmentType.DOCUMENT, name = name))
        }
    }

    // Launcher para câmera
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            attachments.add(
                Attachment(
                    uri = cameraImageUri.toString(),
                    type = AttachmentType.IMAGE,
                    name = "foto_camera_${System.currentTimeMillis()}"
                )
            )
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Novo Post") },
        text = {
            Column {
                OutlinedTextField(
                    value = postContent,
                    onValueChange = { postContent = it },
                    label = { Text("O que você está pensando?") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = false,
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Adicionar anexo:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Galeria (fotos e vídeos)
                    OutlinedButton(
                        onClick = { mediaLauncher.launch("image/* video/*") },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("🖼️ Mídia")
                    }

                    // Documentos
                    OutlinedButton(
                        onClick = {
                            documentLauncher.launch(
                                arrayOf(
                                    "application/pdf",
                                    "application/msword",
                                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                )
                            )
                        },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("📄 Doc")
                    }

                    // Câmera
                    OutlinedButton(
                        onClick = {
                            val photoFile = File(context.cacheDir, "camera_${System.currentTimeMillis()}.jpg")
                            val photoUri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                photoFile
                            )
                            cameraImageUri = photoUri
                            cameraLauncher.launch(photoUri)
                        },
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("📷 Câmera")
                    }
                }

                // Lista de anexos selecionados
                if (attachments.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Anexos (${attachments.size}):",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    attachments.forEachIndexed { index, attachment ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${
                                    when (attachment.type) {
                                        AttachmentType.IMAGE -> "🖼️"
                                        AttachmentType.VIDEO -> "🎬"
                                        AttachmentType.DOCUMENT -> "📄"
                                    }
                                } ${attachment.name.takeLast(25)}",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { attachments.removeAt(index) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Remover anexo",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (postContent.isNotEmpty() || attachments.isNotEmpty()) {
                        onPostCreated(postContent, attachments.toList())
                    }
                },
                enabled = postContent.isNotEmpty() || attachments.isNotEmpty()
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
