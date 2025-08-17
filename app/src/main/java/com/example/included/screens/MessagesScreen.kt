package com.example.included.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Message(
    val id: Int,
    val sender: String,
    val content: String
)

data class Conversation(
    val id: Int,
    val participant: String,
    val lastMessage: String,
    val messages: List<Message> = listOf()
)

@Composable
fun MessagesScreen(
    conversations: List<Conversation> = listOf(
        Conversation(1, "@user1", "Oi, como vai?", messages = listOf(
            Message(1, "@user1", "Oi, como vai?"),
            Message(2, "@me", "Tudo bem! E você?")
        )),
        Conversation(2, "@user2", "Vamos marcar?", messages = listOf(
            Message(3, "@user2", "Vamos marcar?"),
            Message(4, "@me", "Claro, quando?")
        ))
    ),
    onConversationClick: (Conversation) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(conversations) { conv ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onConversationClick(conv) },
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(conv.participant, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(conv.lastMessage, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}
