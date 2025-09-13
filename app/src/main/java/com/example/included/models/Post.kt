package com.example.included.models

import java.util.Date

data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userHandle: String = "",        // Campo importante para exibir o @ do usuário
    val content: String = "",
    val timestamp: Date = Date(),
    val likes: Int = 0,
    val isLikedByCurrentUser: Boolean = false,
    val comments: List<Comment> = emptyList(),
    val commentCount: Int = 0,          // Campo para contar comentários
    val attachmentUriString: String? = null  // Campo para anexos
)

data class Comment(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Date = Date(),
    val likes: Int = 0,
    val isLikedByCurrentUser: Boolean = false
)
