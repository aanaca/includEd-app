package com.example.included.screens

import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.included.models.Post
import com.example.included.models.User
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    sharedViewModel: SharedViewModel,
    followers: List<User> = emptyList(),
    following: List<User> = emptyList(),
    userType: String = "Educador",
    createdAt: String = "Criado em 01/01/2023",
    onShowMessage: (String) -> Unit,
    onEditProfile: () -> Unit,
    onSettings: () -> Unit,
    onPostClick: (Post) -> Unit,
    onFollowersClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onUserClick: (User) -> Unit
) {
    val listState = rememberLazyListState()
    val profile = sharedViewModel.userProfile
    val myPosts = sharedViewModel.myPosts
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))

    var showBadgeTooltip by remember { mutableStateOf(false) }
    if (showBadgeTooltip) {
        LaunchedEffect(Unit) {
            delay(2500)
            showBadgeTooltip = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil") },
                actions = {
                    IconButton(onClick = onEditProfile) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar Perfil")
                    }
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Configurações")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Foto de perfil
                    Surface(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape),
                        color = MaterialTheme.colorScheme.primary,
                        shadowElevation = 4.dp
                    ) {
                        if (profile.profileImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(profile.profileImageUri),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = profile.name.firstOrNull()?.toString() ?: "",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nome com badge
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AnimatedVisibility(
                            visible = showBadgeTooltip,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Text(
                                    text = "Perfil verificado: $userType",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = profile.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            val badgeEmoji = when (userType) {
                                "Educador" -> "🎓"
                                "Especialista" -> "🩺"
                                "Responsável" -> "🏠"
                                else -> "✨"
                            }
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                                    .clickable { showBadgeTooltip = !showBadgeTooltip },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(badgeEmoji, fontSize = 16.sp)
                            }
                        }
                    }

                    Text(
                        text = profile.handle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Row(horizontalArrangement = Arrangement.Center) {
                        TextButton(onClick = onFollowersClick) {
                            Text("${followers.size} seguidores", style = MaterialTheme.typography.bodyMedium)
                        }
                        Text("•", modifier = Modifier.padding(top = 10.dp), color = MaterialTheme.colorScheme.outline)
                        TextButton(onClick = onFollowingClick) {
                            Text("${following.size} seguindo", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Text(
                        text = createdAt,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = profile.bio,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Meus Posts", style = MaterialTheme.typography.titleMedium)
            }

            if (myPosts.isEmpty()) {
                item {
                    Text(
                        text = "Nenhum post ainda.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            itemsIndexed(myPosts) { _, post ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPostClick(post) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            if (profile.profileImageUri != null) {
                                Image(
                                    painter = rememberAsyncImagePainter(profile.profileImageUri),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = profile.name.firstOrNull()?.toString() ?: "",
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = profile.name, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = " ${
                                            when (userType) {
                                                "Educador" -> "🎓"
                                                "Especialista" -> "🩺"
                                                else -> "❤️"
                                            }
                                        }",
                                        fontSize = 12.sp
                                    )
                                }
                                Text(
                                    text = dateFormat.format(post.timestamp),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            Text(
                                text = post.content,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            }
        }
    }
}
