package com.example.included.models

data class User(
    val id: String,
    val name: String,
    val handle: String,
    val imageUrl: String? = null,
    val bio: String = "",
    val followersCount: Int = 0,
    val followingCount: Int = 0
)