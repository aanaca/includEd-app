package com.example.included.models

import java.util.Date

data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Date = Date(),
    val likes: Int = 0,
    val isLikedByCurrentUser: Boolean = false,
    val comments: List<Comment> = emptyList(),

    // Campo novo para guardar o URI do arquivo anexado (imagem, vídeo, doc, etc)
    val attachmentUriString: String? = null
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
