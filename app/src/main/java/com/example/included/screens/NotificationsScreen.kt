package com.example.included.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Tipo de notificação moderno (data object)
sealed interface NotificationType {
    data object Like : NotificationType
    data object Comment : NotificationType
    data object Follow : NotificationType
}

data class Notification(
    val id: Int,
    val type: NotificationType,
    val userName: String,
    val content: String,
    val postId: Int? = null
)

@Composable
fun NotificationsScreen(
    notifications: List<Notification> = listOf(
        Notification(1, NotificationType.Like, "@user1", "curtiu seu post", 1),
        Notification(2, NotificationType.Comment, "@user2", "comentou: 'Legal!'", 2),
        Notification(3, NotificationType.Follow, "@user3", "começou a te seguir")
    ),
    onNotificationClick: (postId: Int?) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(notifications) { notif ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNotificationClick(notif.postId) },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically, // ✅ corrigido
                    horizontalArrangement = Arrangement.Start
                ) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Box(contentAlignment = Alignment.Center) { // ✅ resolve o type mismatch
                            Text(
                                text = notif.userName.first().toString(),
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${notif.userName} ${notif.content}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    when (notif.type) {
                        NotificationType.Like -> Icon(Icons.Default.Favorite, contentDescription = "Curtir", tint = Color.Red)
                        NotificationType.Comment -> Icon(Icons.Default.ModeComment, contentDescription = "Comentário", tint = Color.Gray)
                        NotificationType.Follow -> Icon(Icons.Default.PersonAdd, contentDescription = "Seguir", tint = Color.Blue)
                    }
                }
            }
        }
    }
}
