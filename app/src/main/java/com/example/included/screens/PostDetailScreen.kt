package com.example.included.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class Comment(val id: Int, val author: String, val content: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(
    post: Post,
    onBack: () -> Unit
) {
    var liked by remember { mutableStateOf(false) }
    var comments by remember { mutableStateOf(
        listOf(
            Comment(1, "@user1", "Comentário legal!"),
            Comment(2, "@user2", "Gostei do post!")
        )
    )}

    Scaffold(topBar = {
        TopAppBar(title = { Text("Post") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Voltar") } })
    }) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text(post.content, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(onClick = { liked = !liked }) { Icon(Icons.Default.Favorite, contentDescription = "Curtir", tint = if (liked) Color.Red else Color.Gray) }
                    Icon(Icons.Default.ModeComment, contentDescription = "Comentar")
                    Icon(Icons.Default.Share, contentDescription = "Compartilhar")
                }
            }
            item { Divider() }
            items(comments) { comment ->
                Column {
                    Text(comment.author, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    Text(comment.content)
                }
            }
        }
    }
}
