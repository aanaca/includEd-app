package com.example.included.models

import androidx.annotation.DrawableRes

data class QuizOption(
    val text: String,
    @DrawableRes val imageResId: Int
)

data class QuizQuestion(
    val id: Int,
    val storyId: Int,
    val questionText: String,
    val options: List<QuizOption>,
    val correctAnswerIndex: Int
)
