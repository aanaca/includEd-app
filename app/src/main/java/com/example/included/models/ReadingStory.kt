package com.example.included.models

import androidx.annotation.DrawableRes


enum class StoryType(val label: String) {
    SOCIAL("História Social"),
    EMOTIONAL("Habilidade Emocional"), 
    SEQUENTIAL("Sequência de Ações") 
}


data class VisualSupportItem(
    val phrase: String,
    @get:DrawableRes val imageResId: Int 
)


data class ReadingStory(
    val id: Int,
    val title: String,
    val text: String,
    val type: StoryType,
    val visualSupport: List<VisualSupportItem>
) 
