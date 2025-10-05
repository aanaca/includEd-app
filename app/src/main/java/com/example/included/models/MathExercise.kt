package com.example.included.models


enum class MathDifficulty(val label: String, val maxNumber: Int) {
    EASY(label = "Fácil (0-10)", maxNumber = 10),
    MEDIUM(label = "Médio (0-20)", maxNumber = 20),
    HARD(label = "Difícil (0-50)", maxNumber = 50),
    EXPERT(label = "Expert (0-100)", maxNumber = 100)
}

data class MathExercise(
    val id: Int,
    val operand1: Int,
    val operand2: Int,
    val operation: String, // Operação (+, -, x, ÷)
    val result: Int,
    val difficulty: MathDifficulty = MathDifficulty.EASY
) {
    fun formatToString(): String {
        return "$operand1 $operation $operand2 = ?"
    }
}
