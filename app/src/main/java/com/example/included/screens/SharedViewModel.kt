package com.example.included.screens

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.included.models.Comment
import com.example.included.models.Conversation
import com.example.included.models.Message
import com.example.included.models.MessageAttachment
import com.example.included.models.Post
import java.util.*

data class UserProfile(
    val name: String = "Nome de Usuário",
    val handle: String = "@usuarioExemplo",
    val bio: String = "Essa é a bio do usuário. Conte algo sobre você.",
    val userType: String = "",
    val profileImageUri: Uri? = null
)

class SharedViewModel : ViewModel() {

    // --- Perfil do usuário ---
    var userProfile by mutableStateOf(UserProfile())
        private set

    fun updateProfile(name: String, handle: String, bio: String, imageUri: Uri?, userType: String = userProfile.userType) {
        userProfile = userProfile.copy(
            name = name,
            handle = handle,
            bio = bio,
            profileImageUri = imageUri,
            userType = userType
        )
        val updatedPosts = _posts.map { post ->
            if (post.userId == "user_atual") {
                post.copy(userName = name, userHandle = handle)
            } else post
        }
        _posts.clear()
        _posts.addAll(updatedPosts)
    }

    // --- Posts ---
    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> get() = _posts

    val myPosts: List<Post>
        get() = _posts.filter { it.userId == "user_atual" }

    // --- Conversas e Mensagens ---
    private val _conversations = mutableStateListOf<Conversation>()
    val conversations: List<Conversation> get() = _conversations

    init {
        _posts.addAll(generateSamplePosts())
        _conversations.addAll(generateSampleConversations())
    }

    fun handlePostAction(action: PostAction, post: Post) {
        when (action) {
            PostAction.Like -> {
                val index = _posts.indexOfFirst { it.id == post.id }
                if (index != -1) {
                    _posts[index] = _posts[index].copy(
                        likes = if (post.isLikedByCurrentUser) post.likes - 1 else post.likes + 1,
                        isLikedByCurrentUser = !post.isLikedByCurrentUser
                    )
                }
            }
            PostAction.Delete -> {
                _posts.removeIf { it.id == post.id }
            }
            PostAction.Create -> {
                _posts.add(0, post)
            }
            else -> {}
        }
    }

    fun addComment(postId: String, comment: Comment) {
        val index = _posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            _posts[index] = _posts[index].copy(
                comments = _posts[index].comments + comment,
                commentCount = _posts[index].commentCount + 1
            )
        }
    }

    fun sendMessage(
        conversationId: String,
        content: String,
        attachment: MessageAttachment? = null
    ) {
        val index = _conversations.indexOfFirst { it.id == conversationId }
        if (index == -1) return

        val newMessage = Message(
            id = UUID.randomUUID().toString(),
            senderId = "user_atual",
            content = content,
            timestamp = Date(),
            isRead = false,
            attachment = attachment
        )

        val lastText = when {
            attachment != null && content.isEmpty() -> "📎 ${attachment.name.takeLast(20)}"
            attachment != null -> "📎 $content"
            else -> content
        }

        _conversations[index] = _conversations[index].copy(
            messages = _conversations[index].messages + newMessage,
            lastMessage = lastText,
            lastMessageTime = Date()
        )

        // Simula resposta automática após 1.5s (apenas para demonstração no TCC)
        simulateReply(conversationId)
    }

    private fun simulateReply(conversationId: String) {
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            val idx = _conversations.indexOfFirst { it.id == conversationId }
            if (idx == -1) return@postDelayed

            val lastMessage = _conversations[idx].messages
                .lastOrNull { it.senderId == "user_atual" }?.content?.lowercase() ?: ""

            val replyText = when {
                lastMessage.contains("olá") || lastMessage.contains("oi") ||
                        lastMessage.contains("bom dia") || lastMessage.contains("boa tarde") ||
                        lastMessage.contains("boa noite") ->
                    "Olá! Tudo bem? Como posso te ajudar? 😊"

                lastMessage.contains("tudo") && lastMessage.contains("bem") ||
                        lastMessage.contains("tudo bem") ->
                    "Tudo ótimo, obrigado! E você, como está?"

                lastMessage.contains("inclusão") || lastMessage.contains("inclusivo") ||
                        lastMessage.contains("incluir") ->
                    "A inclusão é fundamental! Aqui no IncludEd tentamos facilitar esse processo. Tem alguma dúvida específica?"

                lastMessage.contains("atividade") || lastMessage.contains("exercício") ||
                        lastMessage.contains("tarefa") ->
                    "As atividades do app foram pensadas para diferentes perfis. Você já experimentou as de lógica e memória?"

                lastMessage.contains("ajuda") || lastMessage.contains("dúvida") ||
                        lastMessage.contains("pergunta") ->
                    "Claro, pode perguntar! Estou aqui para ajudar no que precisar 😊"

                lastMessage.contains("obrigado") || lastMessage.contains("obrigada") ||
                        lastMessage.contains("valeu") ->
                    "De nada! Qualquer coisa é só chamar 😊"

                lastMessage.contains("material") || lastMessage.contains("documento") ||
                        lastMessage.contains("arquivo") ->
                    "Recebi o material! Vou dar uma olhada e te retorno em breve."

                lastMessage.contains("reunião") || lastMessage.contains("encontro") ||
                        lastMessage.contains("quando") ->
                    "Podemos marcar para essa semana! Qual horário fica melhor para você?"

                lastMessage.contains("projeto") || lastMessage.contains("trabalho") ||
                        lastMessage.contains("tcc") ->
                    "O projeto está ficando ótimo! Posso ajudar em alguma parte específica?"

                lastMessage.contains("problema") || lastMessage.contains("erro") ||
                        lastMessage.contains("não funciona") ->
                    "Entendi o problema. Vamos resolver isso juntos, pode me dar mais detalhes?"

                lastMessage.contains("parabéns") || lastMessage.contains("ótimo") ||
                        lastMessage.contains("excelente") ->
                    "Muito obrigado! Fico feliz em saber disso 😊"

                lastMessage.isEmpty() ->
                    "Recebi seu arquivo! Vou verificar e te retorno em breve."

                else ->
                    "Entendi! Vou verificar isso e te respondo logo. Obrigado pela mensagem 😊"
            }

            val reply = Message(
                id = UUID.randomUUID().toString(),
                senderId = _conversations[idx].participantId,
                content = replyText,
                timestamp = Date(),
                isRead = true
            )
            _conversations[idx] = _conversations[idx].copy(
                messages = _conversations[idx].messages + reply,
                lastMessage = reply.content,
                lastMessageTime = Date(),
                unreadCount = _conversations[idx].unreadCount + 1
            )
        }, 1500)
    }

    fun markConversationAsRead(conversationId: String) {
        val index = _conversations.indexOfFirst { it.id == conversationId }
        if (index != -1) {
            val readMessages = _conversations[index].messages.map { it.copy(isRead = true) }
            _conversations[index] = _conversations[index].copy(
                messages = readMessages,
                unreadCount = 0
            )
        }
    }
}

private fun generateSamplePosts(): List<Post> {
    val tipos = listOf("Educador", "Especialista", "Responsável")
    return List(5) { index ->
        Post(
            id = index.toString(),
            userId = if (index == 0) "user_atual" else "user$index",
            userName = if (index == 0) "Nome de Usuário" else "Usuário $index",
            userHandle = if (index == 0) "@usuarioExemplo" else "@usuario$index",
            content = "Post exemplo $index #includEd",
            timestamp = Date(System.currentTimeMillis() - (index * 3600000L)),
            likes = (0..10).random(),
            commentCount = 0,
            userType = if (index == 0) "" else tipos.random()
        )
    }
}

private fun generateSampleConversations(): List<Conversation> {
    return listOf(
        Conversation(
            id = "conv1",
            participantId = "user1",
            participantName = "João Silva",
            participantHandle = "@joao.silva",
            messages = listOf(
                Message(
                    id = "m1",
                    senderId = "user1",
                    content = "Olá! Tudo bem?",
                    timestamp = Date(System.currentTimeMillis() - 3600000),
                    isRead = true
                ),
                Message(
                    id = "m2",
                    senderId = "user_atual",
                    content = "Tudo ótimo! E você?",
                    timestamp = Date(System.currentTimeMillis() - 3500000),
                    isRead = true
                ),
                Message(
                    id = "m3",
                    senderId = "user1",
                    content = "Bem também! Você viu o material sobre inclusão?",
                    timestamp = Date(System.currentTimeMillis() - 600000),
                    isRead = false
                )
            ),
            lastMessage = "Você viu o material sobre inclusão?",
            lastMessageTime = Date(System.currentTimeMillis() - 600000),
            unreadCount = 1
        ),
        Conversation(
            id = "conv2",
            participantId = "user2",
            participantName = "Maria Santos",
            participantHandle = "@maria.santos",
            messages = listOf(
                Message(
                    id = "m4",
                    senderId = "user2",
                    content = "Bom dia! Posso tirar uma dúvida?",
                    timestamp = Date(System.currentTimeMillis() - 7200000),
                    isRead = true
                ),
                Message(
                    id = "m5",
                    senderId = "user_atual",
                    content = "Claro! Pode falar 😊",
                    timestamp = Date(System.currentTimeMillis() - 7100000),
                    isRead = true
                )
            ),
            lastMessage = "Claro! Pode falar 😊",
            lastMessageTime = Date(System.currentTimeMillis() - 7100000),
            unreadCount = 0
        ),
        Conversation(
            id = "conv3",
            participantId = "user3",
            participantName = "Carlos Oliveira",
            participantHandle = "@carlos.oliveira",
            messages = listOf(
                Message(
                    id = "m6",
                    senderId = "user3",
                    content = "Oi! Quando você pode se reunir?",
                    timestamp = Date(System.currentTimeMillis() - 86400000),
                    isRead = false
                ),
                Message(
                    id = "m7",
                    senderId = "user3",
                    content = "Preciso falar sobre o projeto!",
                    timestamp = Date(System.currentTimeMillis() - 86000000),
                    isRead = false
                )
            ),
            lastMessage = "Preciso falar sobre o projeto!",
            lastMessageTime = Date(System.currentTimeMillis() - 86000000),
            unreadCount = 2
        )
    )
}
