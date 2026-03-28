package com.example.included.models

import java.util.Date

data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userHandle: String = "",
    val content: String = "",
    val timestamp: Date = Date(),
    val likes: Int = 0,
    val isLikedByCurrentUser: Boolean = false,
    val comments: List<Comment> = emptyList(),
    val commentCount: Int = 0,
    val attachments: List<Attachment> = emptyList()
)

data class Attachment(
    val uri: String = "",
    val type: AttachmentType = AttachmentType.IMAGE,
    val name: String = ""
)

enum class AttachmentType {
    IMAGE, VIDEO, DOCUMENT
}

data class Comment(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Date = Date(),
    val likes: Int = 0,
    val isLikedByCurrentUser: Boolean = false
)
