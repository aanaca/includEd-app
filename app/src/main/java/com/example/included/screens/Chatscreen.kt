package com.example.included.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.included.models.Message
import com.example.included.models.MessageAttachment
import com.example.included.models.MessageAttachmentType
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    conversationId: String,
    sharedViewModel: SharedViewModel,
    onBack: () -> Unit
) {
    val conversation = sharedViewModel.conversations.find { it.id == conversationId }
    val messages = conversation?.messages ?: emptyList()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val timeFormat = SimpleDateFormat("HH:mm", Locale("pt", "BR"))

    var messageText by remember { mutableStateOf("") }
    var showAttachMenu by remember { mutableStateOf(false) }

    // Marcar mensagens como lidas ao abrir
    LaunchedEffect(conversationId) {
        sharedViewModel.markConversationAsRead(conversationId)
    }

    // Scroll para última mensagem
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    // Launcher mídia
    val mediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val mimeType = context.contentResolver.getType(it) ?: ""
            val type = when {
                mimeType.startsWith("image") -> MessageAttachmentType.IMAGE
                mimeType.startsWith("video") -> MessageAttachmentType.VIDEO
                else -> MessageAttachmentType.DOCUMENT
            }
            sharedViewModel.sendMessage(
                conversationId = conversationId,
                content = "",
                attachment = MessageAttachment(
                    uri = it.toString(),
                    type = type,
                    name = it.lastPathSegment ?: "arquivo"
                )
            )
        }
        showAttachMenu = false
    }

    // Launcher documento
    val documentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            sharedViewModel.sendMessage(
                conversationId = conversationId,
                content = "",
                attachment = MessageAttachment(
                    uri = it.toString(),
                    type = MessageAttachmentType.DOCUMENT,
                    name = it.lastPathSegment ?: "documento"
                )
            )
        }
        showAttachMenu = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = conversation?.participantName?.firstOrNull()?.toString() ?: "?",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = conversation?.participantName ?: "",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Text(
                                text = conversation?.participantHandle ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        bottomBar = {
            Column {
                // Menu de anexos
                if (showAttachMenu) {
                    Surface(
                        tonalElevation = 4.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { mediaLauncher.launch("image/* video/*") },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("🖼️ Mídia")
                            }
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
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("📄 Documento")
                            }
                        }
                    }
                }

                // Barra de digitação
                Surface(tonalElevation = 2.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { showAttachMenu = !showAttachMenu }) {
                            Icon(
                                Icons.Default.AttachFile,
                                contentDescription = "Anexar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            placeholder = { Text("Digite uma mensagem...") },
                            modifier = Modifier.weight(1f),
                            maxLines = 4,
                            shape = RoundedCornerShape(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (messageText.isNotBlank()) {
                                    sharedViewModel.sendMessage(
                                        conversationId = conversationId,
                                        content = messageText.trim()
                                    )
                                    messageText = ""
                                    showAttachMenu = false
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (messageText.isNotBlank())
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Enviar",
                                tint = if (messageText.isNotBlank())
                                    MaterialTheme.colorScheme.onPrimary
                                else
                                    MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isFromMe = message.senderId == "user_atual",
                    timeFormat = timeFormat
                )
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: Message,
    isFromMe: Boolean,
    timeFormat: SimpleDateFormat
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isFromMe) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isFromMe) 16.dp else 4.dp,
                        bottomEnd = if (isFromMe) 4.dp else 16.dp
                    )
                )
                .background(
                    if (isFromMe)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Column {
                // Anexo
                if (message.attachment != null) {
                    Text(
                        text = "${
                            when (message.attachment.type) {
                                MessageAttachmentType.IMAGE -> "🖼️"
                                MessageAttachmentType.VIDEO -> "🎬"
                                MessageAttachmentType.DOCUMENT -> "📄"
                            }
                        } ${message.attachment.name.takeLast(25)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isFromMe)
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (message.content.isNotEmpty()) Spacer(modifier = Modifier.height(4.dp))
                }

                // Texto
                if (message.content.isNotEmpty()) {
                    Text(
                        text = message.content,
                        color = if (isFromMe)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Hora + lido
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = timeFormat.format(message.timestamp),
                        fontSize = 10.sp,
                        color = if (isFromMe)
                            MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        else
                            MaterialTheme.colorScheme.outline
                    )
                    if (isFromMe) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (message.isRead) "✓✓" else "✓",
                            fontSize = 10.sp,
                            color = if (message.isRead)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}
