package com.example.included.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.included.R
import com.example.included.models.MemoryCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Define a estrutura para os níveis de dificuldade.
 */
data class MemoryDifficulty(
    val level: String,
    val pairs: Int // Número de pares de cartas
)

class MemoryViewModel : ViewModel() {

    // --- ESTADO DO JOGO ---
    private val _cards = MutableStateFlow<List<MemoryCard>>(emptyList())
    val cards: StateFlow<List<MemoryCard>> = _cards.asStateFlow()

    private val _moves = MutableStateFlow(0)
    val moves: StateFlow<Int> = _moves.asStateFlow()

    private val _pairsFound = MutableStateFlow(0)
    val pairsFound: StateFlow<Int> = _pairsFound.asStateFlow()

    // NOVO ESTADO: Dificuldade atual
    private val _currentDifficulty = MutableStateFlow(getDifficulties().first())
    val currentDifficulty: StateFlow<MemoryDifficulty> = _currentDifficulty.asStateFlow()

    private val flippedCardIndices = mutableListOf<Int>()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing: StateFlow<Boolean> = _isProcessing.asStateFlow()

    init {
        // O jogo é inicializado com a dificuldade padrão, para que a tela possa exibir o diálogo de seleção.
        resetGame(getDifficulties().first())
    }

    /**
     * Inicializa o jogo com o número de pares baseado na dificuldade.
     * @param difficulty O nível de dificuldade selecionado.
     */
    fun resetGame(difficulty: MemoryDifficulty = _currentDifficulty.value) {
        // 1. Atualiza a dificuldade
        _currentDifficulty.value = difficulty

        // 2. Gera os pares com base na dificuldade
        val pairs = generateMemoryPairs(difficulty.pairs)

        // 3. Duplica, embaralha e reatribui o índice único de cada carta
        val shuffledCards = pairs.flatMap { listOf(it, it.copy(cardIndex = it.cardIndex + pairs.size)) }
            .shuffled()
            .mapIndexed { index, card -> card.copy(cardIndex = index, isFlipped = false, isMatched = false) }

        _cards.value = shuffledCards
        _moves.value = 0
        _pairsFound.value = 0
        flippedCardIndices.clear()
        _isProcessing.value = false
    }

    fun flipCard(index: Int) {
        if (_isProcessing.value || index in flippedCardIndices || _cards.value[index].isMatched) {
            return
        }

        val newCards = _cards.value.toMutableList()
        newCards[index] = newCards[index].copy(isFlipped = true)
        _cards.value = newCards
        flippedCardIndices.add(index)

        if (flippedCardIndices.size == 2) {
            _moves.update { it + 1 }
            checkMatch()
        }
    }

    private fun checkMatch() = viewModelScope.launch {
        _isProcessing.value = true
        delay(1000) // Espera 1 segundo para visualização

        val card1Index = flippedCardIndices[0]
        val card2Index = flippedCardIndices[1]
        val card1 = _cards.value[card1Index]
        val card2 = _cards.value[card2Index]

        if (card1.id == card2.id) {
            // Combinação Correta - Marca como combinada
            val newCards = _cards.value.toMutableList()
            newCards[card1Index] = card1.copy(isMatched = true)
            newCards[card2Index] = card2.copy(isMatched = true)
            _cards.value = newCards
            _pairsFound.update { it + 1 }
        } else {
            // Combinação Incorreta - Vira as cartas de volta
            val newCards = _cards.value.toMutableList()
            newCards[card1Index] = card1.copy(isFlipped = false)
            newCards[card2Index] = card2.copy(isFlipped = false)
            _cards.value = newCards
        }

        flippedCardIndices.clear()
        _isProcessing.value = false
    }

    /**
     * Gera os recursos (Drawables) para os pares de cartas do jogo, limitando pelo número de pares.
     */
    private fun generateMemoryPairs(numberOfPairs: Int): List<MemoryCard> {
        // LISTA DE 15 ANIMAIS ATUALIZADA (Coruja e Dinossauro incluídos)
        val allDrawableResources = listOf(
            R.drawable.leao,
            R.drawable.elefante,
            R.drawable.macaco,
            R.drawable.girafa,
            R.drawable.cachorro,
            R.drawable.gato,
            R.drawable.vaca,
            R.drawable.porco,
            R.drawable.galinha,
            R.drawable.peixe,
            R.drawable.coruja, // Substituindo Pássaro
            R.drawable.baleia,
            R.drawable.tartaruga,
            R.drawable.dinossauro, // Substituindo Cobra
            R.drawable.urso,
        ).take(numberOfPairs) // Limita a lista ao número de pares desejado

        // Cria os pares
        return allDrawableResources.mapIndexed { index, resId ->
            MemoryCard(
                id = index + 1, // ID exclusivo para o par
                imageResId = resId,
                cardIndex = index
            )
        }
    }

    /**
     * Retorna a lista de dificuldades disponíveis para a UI.
     */
    fun getDifficulties(): List<MemoryDifficulty> = listOf(
        MemoryDifficulty("Fácil", 6),  // 12 cartas (4x3)
        MemoryDifficulty("Médio", 10), // 20 cartas (4x5 ou 5x4)
        MemoryDifficulty("Difícil", 15) // 30 cartas (5x6 ou 6x5)
    )

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MemoryViewModel()
            }
        }
    }
}
