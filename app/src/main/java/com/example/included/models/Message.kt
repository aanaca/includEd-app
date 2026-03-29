package com.example.included.models

import java.util.Date

data class Message(
    val id: String = "",
    val senderId: String = "",
    val content: String = "",
    val timestamp: Date = Date(),
    val isRead: Boolean = false,
    val attachment: MessageAttachment? = null
)

data class MessageAttachment(
    val uri: String = "",
    val type: MessageAttachmentType = MessageAttachmentType.IMAGE,
    val name: String = ""
)

enum class MessageAttachmentType {
    IMAGE, VIDEO, DOCUMENT
}

data class Conversation(
    val id: String = "",
    val participantId: String = "",
    val participantName: String = "",
    val participantHandle: String = "",
    val participantImageUri: String? = null,
    val messages: List<Message> = emptyList(),
    val lastMessage: String = "",
    val lastMessageTime: Date = Date(),
    val unreadCount: Int = 0
)
