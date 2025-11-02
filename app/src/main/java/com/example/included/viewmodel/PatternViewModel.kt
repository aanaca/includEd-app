package com.example.included.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.included.R
import com.example.included.models.PatternActivity
import com.example.included.models.PatternElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random
import kotlin.random.nextInt

class PatternViewModel : ViewModel() {

    // Lista base dos elementos (formas) que serão usados nos padrões
    // Deve haver 3 ou mais para os padrões A B C funcionarem
    private val allPatternElements = listOf(
        PatternElement(R.drawable.ic_square, "SQ"),
        PatternElement(R.drawable.ic_circle, "CR"),
        PatternElement(R.drawable.ic_triangle, "TR")
        // Se você adicionar mais formas (ex: estrela, losango), adicione-as aqui.
    )

    // O estado inicial é o primeiro padrão gerado aleatoriamente
    private val _currentActivity = MutableStateFlow(generateRandomPattern())
    val currentActivity: StateFlow<PatternActivity> = _currentActivity.asStateFlow()

    private val _selectedOption = MutableStateFlow<PatternElement?>(null)
    val selectedOption: StateFlow<PatternElement?> = _selectedOption.asStateFlow()

    private val _isCorrect = MutableStateFlow<Boolean?>(null)
    val isCorrect: StateFlow<Boolean?> = _isCorrect.asStateFlow()

    // O restante do ViewModel (selectOption, resetSelection, loadNextActivity, Factory) permanece o mesmo.

    fun selectOption(element: PatternElement) {
        if (_isCorrect.value != null) return

        _selectedOption.value = element
        val correct = element.id == _currentActivity.value.missingElement.id
        _isCorrect.value = correct
    }

    fun resetSelection() {
        _selectedOption.value = null
        _isCorrect.value = null
    }

    fun loadNextActivity() {
        _currentActivity.value = generateRandomPattern()

        _selectedOption.value = null
        _isCorrect.value = null
    }

    // --- Lógica de Geração Dinâmica ATUALIZADA ---

    private fun generateRandomPattern(): PatternActivity {
        // Agora escolhe entre 5 tipos de padrão (0 a 4)
        val patternType = Random.nextInt(0..4)

        // Seleciona 3 elementos base (A, B e C) de forma aleatória e garante que sejam únicos
        val elements = allPatternElements.shuffled(Random(System.currentTimeMillis()))
        val elementA = elements[0]
        val elementB = elements[1]
        // Usa o terceiro elemento se o padrão for complexo, caso contrário, será redundante
        val elementC = elements.getOrElse(2) { elementB }

        val fullSequence: List<PatternElement>
        val instructions: String

        when (patternType) {
            0 -> { // Padrão 1 (Original): A B A B A B
                fullSequence = listOf(elementA, elementB, elementA, elementB, elementA, elementB)
                instructions = "Complete a sequência A B A B A ?"
            }
            1 -> { // Padrão 2 (Original): A A B A A B
                fullSequence = listOf(elementA, elementA, elementB, elementA, elementA, elementB)
                instructions = "Complete a sequência A A B A A ?"
            }
            2 -> { // NOVO Padrão 3: A B C A B C
                fullSequence = listOf(elementA, elementB, elementC, elementA, elementB, elementC)
                instructions = "Complete a sequência A B C A B ?"
            }
            3 -> { // NOVO Padrão 4: A A B B C C
                fullSequence = listOf(elementA, elementA, elementB, elementB, elementC, elementC)
                instructions = "Complete a sequência A A B B C ?"
            }
            4 -> { // NOVO Padrão 5: A B C C B A (Palíndromo)
                fullSequence = listOf(elementA, elementB, elementC, elementC, elementB, elementA)
                instructions = "Complete a sequência A B C C B ?"
            }
            else -> throw IllegalStateException("Tipo de padrão inválido.")
        }

        // Sempre omite o último elemento da sequência completa (índice 5)
        val sequence = fullSequence.take(5)
        val missingElement = fullSequence[5]

        // Cria as opções de resposta: Usando os três elementos base (A, B, C)
        val options = allPatternElements
            .shuffled(Random(System.currentTimeMillis()))
            .take(3)
            .distinctBy { it.id }

        // Garante que a opção correta esteja sempre nas opções
        val finalOptions = if (options.any { it.id == missingElement.id }) {
            options
        } else {
            (options + missingElement).shuffled(Random(System.currentTimeMillis()))
        }

        return PatternActivity(
            id = Random.nextInt(1000, 9999),
            instructions = instructions,
            pattern = sequence,
            missingElement = missingElement,
            options = finalOptions
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PatternViewModel()
            }
        }
    }
}
