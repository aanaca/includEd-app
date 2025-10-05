package com.example.included.models

import androidx.compose.ui.graphics.Color

// Define o modelo de dados para uma categoria de atividade,
// usando Color do Compose para estilização direta.
data class ActivityCategory(
    val name: String, // Nome da categoria (ex: "Matemática")
    val color: Color, // Cor de fundo do cartão (definida em Color.kt)
    val route: String // Rota de navegação para a tela de detalhes
)
