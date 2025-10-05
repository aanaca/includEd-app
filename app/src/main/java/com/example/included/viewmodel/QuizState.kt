package com.example.included.viewmodel

/**
 * Rastreia o estado da resposta do quiz para controlar o feedback na tela.
 */
enum class QuizState {
    UNANSWERED, // Ainda não respondeu
    INCORRECT,  // Resposta incorreta (pode tentar novamente)
    CORRECT     // Resposta correta (mostrará o botão para avançar)
}
