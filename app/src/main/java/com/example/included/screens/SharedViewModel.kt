package com.example.included.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.included.models.Comment
import com.example.included.models.Post
import java.util.*

class SharedViewModel : ViewModel() {
    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> get() = _posts

    init {
        // Adiciona alguns posts de exemplo
        _posts.addAll(generateSamplePosts())
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
            else -> { /* Other actions handled elsewhere */ }
        }
    }

    fun addComment(postId: String, comment: Comment) {
        val index = _posts.indexOfFirst { it.id == postId }
        if (index != -1) {
            val updatedPost = _posts[index].copy(
                comments = _posts[index].comments + comment,
                commentCount = _posts[index].commentCount + 1
            )
            _posts[index] = updatedPost
        }
    }
}

private fun generateSamplePosts(): List<Post> {
    return List(5) { index ->
        Post(
            id = index.toString(),
            userId = if (index == 0) "user_atual" else "user$index",
            userName = if (index == 0) "Usuário Atual" else "Usuário $index",
            userHandle = if (index == 0) "@usuario_atual" else "@usuario$index",
            content = "Post exemplo $index #includEd",
            timestamp = Date(System.currentTimeMillis() - (index * 3600000L)),
            likes = (0..10).random(),
            commentCount = (0..5).random()
        )
    }
}