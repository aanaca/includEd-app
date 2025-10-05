package com.example.included.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.included.models.MathDifficulty
import com.example.included.models.MathExercise
import kotlin.random.Random
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MathViewModel : ViewModel() {

    // Lista de todas as operações disponíveis (CORRIGIDO: Definida antes de ser usada no init)
    private val operations = listOf("+", "-", "x", "÷")

    // VARIÁVEIS DE ESTADO (privadas para o ViewModel)
    private var _currentExercise = MutableStateFlow(generateExercise(MathDifficulty.EASY))
    private var _selectedDifficulty by mutableStateOf(MathDifficulty.EASY)
    private var _userAnswer by mutableStateOf("")
    private var _isCorrect by mutableStateOf<Boolean?>(null)
    private var _currentOptions by mutableStateOf(emptyList<Int>())

    // PROPRIEDADES DE EXPOSIÇÃO (públicas para a Composable)
    val currentExercise: StateFlow<MathExercise> = _currentExercise.asStateFlow()
    val selectedDifficulty: MathDifficulty
        get() = _selectedDifficulty
    val userAnswer: String
        get() = _userAnswer
    val isCorrect: Boolean?
        get() = _isCorrect
    val currentOptions: List<Int>
        get() = _currentOptions

    init {
        generateNewExercise(MathDifficulty.EASY)
    }

    private fun generateNewExercise(difficulty: MathDifficulty) {
        val exercise = generateExercise(difficulty)
        _currentExercise.value = exercise
        _userAnswer = ""
        _isCorrect = null

        // Gerando opções de múltipla escolha para TODOS os níveis
        _currentOptions = generateOptions(exercise.result, difficulty)
    }

    private fun generateExercise(difficulty: MathDifficulty): MathExercise {
        val maxVal = difficulty.maxNumber

        // Randomiza a operação
        val operator = operations.random()

        var num1: Int
        var num2: Int
        var result: Int

        // --- Lógica de Geração por Operação ---

        when (operator) {
            "+" -> {
                // Adição: Números aleatórios dentro do limite
                num1 = Random.nextInt(1, maxVal / 2)
                num2 = Random.nextInt(1, maxVal / 2)
                result = num1 + num2
            }
            "-" -> {
                // Subtração: Garante que o resultado seja positivo ou zero
                num1 = Random.nextInt(1, maxVal)
                num2 = Random.nextInt(1, num1 + 1) // num2 é sempre menor ou igual a num1
                result = num1 - num2
            }
            "x" -> {
                // Multiplicação: Mantém os operandos pequenos para que o resultado caiba no limite
                val multMax = when (difficulty) {
                    MathDifficulty.EASY -> 5
                    MathDifficulty.MEDIUM -> 7
                    MathDifficulty.HARD -> 10
                    MathDifficulty.EXPERT -> 15
                }
                num1 = Random.nextInt(1, multMax)
                num2 = Random.nextInt(1, multMax)
                result = num1 * num2
            }
            "÷" -> {
                // Divisão: Garante que o resultado seja inteiro (começa pelo quociente)
                val quotientMax = when (difficulty) {
                    MathDifficulty.EASY -> 4
                    MathDifficulty.MEDIUM -> 7
                    MathDifficulty.HARD -> 10
                    MathDifficulty.EXPERT -> 15
                }

                val quotient = Random.nextInt(1, quotientMax)
                num2 = Random.nextInt(1, maxVal / 2) // Divisor
                num1 = quotient * num2 // Dividendo
                result = quotient // O resultado é o quociente
            }
            else -> {
                num1 = 1
                num2 = 1
                result = 2
            }
        }

        return MathExercise(
            id = 0,
            operand1 = num1,
            operand2 = num2,
            operation = operator,
            result = result
        )
    }

    private fun generateOptions(correctAnswer: Int, difficulty: MathDifficulty): List<Int> {
        val options = mutableSetOf(correctAnswer)

        val maxOffset = when (difficulty) {
            MathDifficulty.EASY -> 5
            MathDifficulty.MEDIUM -> 8
            MathDifficulty.HARD -> 12
            MathDifficulty.EXPERT -> 15
        }

        while (options.size < 4) {
            val offset = Random.nextInt(-maxOffset, maxOffset + 1)
            val wrongAnswer = correctAnswer + offset

            if (wrongAnswer > 0 && wrongAnswer != correctAnswer) {
                options.add(wrongAnswer)
            }
        }
        return options.toList().shuffled()
    }

    fun changeDifficulty(newDifficulty: MathDifficulty) {
        _selectedDifficulty = newDifficulty
        generateNewExercise(newDifficulty)
    }

    fun updateAnswer(newAnswer: String) {
        _userAnswer = newAnswer
        _isCorrect = null
    }

    fun checkAnswer() {
        _userAnswer.toIntOrNull()?.let { answer ->
            _isCorrect = answer == _currentExercise.value.result
        }
    }

    fun checkOption(option: Int) {
        _userAnswer = option.toString()
        _isCorrect = option == _currentExercise.value.result
    }

    fun moveToNextExercise() {
        generateNewExercise(_selectedDifficulty)
    }

    // Factory necessário para inicializar o ViewModel
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MathViewModel()
            }
        }
    }
}
