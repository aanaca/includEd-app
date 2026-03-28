package com.example.included.screens

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.included.models.Comment
import com.example.included.models.Post
import java.util.*

data class UserProfile(
    val name: String = "Nome de Usuário",
    val handle: String = "@usuarioExemplo",
    val bio: String = "Essa é a bio do usuário. Conte algo sobre você.",
    val userType: String = "Educador",
    val profileImageUri: Uri? = null
)

class SharedViewModel : ViewModel() {

    // --- Perfil do usuário ---
    var userProfile by mutableStateOf(UserProfile())
        private set

    fun updateProfile(name: String, handle: String, bio: String, imageUri: Uri?) {
        userProfile = userProfile.copy(
            name = name,
            handle = handle,
            bio = bio,
            profileImageUri = imageUri
        )
        // Atualiza também os posts do usuário atual com o novo nome/handle
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

    init {
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
}

private fun generateSamplePosts(): List<Post> {
    return List(5) { index ->
        Post(
            id = index.toString(),
            userId = if (index == 0) "user_atual" else "user$index",
            userName = if (index == 0) "Nome de Usuário" else "Usuário $index",
            userHandle = if (index == 0) "@usuarioExemplo" else "@usuario$index",
            content = "Post exemplo $index #includEd",
            timestamp = Date(System.currentTimeMillis() - (index * 3600000L)),
            likes = (0..10).random(),
            commentCount = (0..5).random()
        )
    }
}
