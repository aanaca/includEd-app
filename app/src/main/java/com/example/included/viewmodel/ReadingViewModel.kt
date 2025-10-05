package com.example.included.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.included.R
import com.example.included.models.ReadingStory
import com.example.included.models.StoryType
import com.example.included.models.VisualSupportItem
import com.example.included.models.QuizQuestion
import com.example.included.models.QuizOption
// O enum QuizState está no mesmo pacote, então o import não é necessário aqui,
// mas a classe precisa existir em seu próprio arquivo.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReadingViewModel : ViewModel() {

    private val allStories = generateSampleStories()
    private val allQuizzes = generateQuizQuestions()

    // Estados de Leitura
    private val _currentStory = MutableStateFlow(allStories.first())
    val currentStory: StateFlow<ReadingStory> = _currentStory.asStateFlow()
    private val _currentPhraseIndex = MutableStateFlow(0)
    val currentPhraseIndex: StateFlow<Int> = _currentPhraseIndex.asStateFlow()

    // Estados do Quiz
    private val _isQuizActive = MutableStateFlow(false)
    val isQuizActive: StateFlow<Boolean> = _isQuizActive.asStateFlow()

    // NOVO ESTADO: Resultado da última tentativa de resposta
    private val _quizResult = MutableStateFlow(QuizState.UNANSWERED)
    val quizResult: StateFlow<QuizState> = _quizResult.asStateFlow()

    val currentQuiz: StateFlow<QuizQuestion?> = _currentStory.asStateFlow().value.let { story ->
        MutableStateFlow(allQuizzes.find { it.storyId == story.id })
    }

    /**
     * Move para a próxima frase da história. Se for a última, ativa o quiz.
     */
    fun nextPhrase() {
        val nextIndex = _currentPhraseIndex.value + 1
        if (nextIndex < _currentStory.value.visualSupport.size) {
            _currentPhraseIndex.value = nextIndex
        } else {
            _isQuizActive.value = true
            _quizResult.value = QuizState.UNANSWERED
        }
    }

    /**
     * Carrega a próxima história da lista e reseta todos os estados.
     */
    fun loadNextStory() {
        val currentIndex = allStories.indexOfFirst { it.id == _currentStory.value.id }
        val nextIndex = (currentIndex + 1) % allStories.size

        _currentStory.value = allStories[nextIndex]
        _currentPhraseIndex.value = 0
        _isQuizActive.value = false
        _quizResult.value = QuizState.UNANSWERED
    }

    /**
     * Lógica de resposta do quiz: atualiza o estado do resultado.
     */
    fun submitAnswer(selectedIndex: Int) {
        // Ignora a submissão se o quiz já estiver correto
        if (_quizResult.value == QuizState.CORRECT) return

        val quiz = currentQuiz.value ?: return

        if (selectedIndex == quiz.correctAnswerIndex) {
            _quizResult.value = QuizState.CORRECT
        } else {
            _quizResult.value = QuizState.INCORRECT
        }
    }

    // --- Dados do Questionário e Histórias (Mantidos) ---

    private fun generateQuizQuestions(): List<QuizQuestion> {
        return listOf(
            QuizQuestion(
                id = 101,
                storyId = 1,
                questionText = "O que você deve fazer quando está confuso na aula?",
                options = listOf(
                    QuizOption("Pedir ajuda", R.drawable.pedindo_ajuda_tcc),
                    QuizOption("Ficar confuso", R.drawable.confuso_tcc),
                    QuizOption("Esperar o sinal", R.drawable.lampada_tcc)
                ),
                correctAnswerIndex = 0
            ),
            QuizQuestion(
                id = 102,
                storyId = 2,
                questionText = "O que você faz para esperar a sua vez com calma?",
                options = listOf(
                    QuizOption("Brincar", R.drawable.brincar_tcc),
                    QuizOption("Respirar Fundo e Contar", R.drawable.respirar_tcc),
                    QuizOption("Levantar a mão", R.drawable.mao_levantada_tcc)
                ),
                correctAnswerIndex = 1
            )
        )
    }

    private fun generateSampleStories(): List<ReadingStory> {
        return listOf(
            ReadingStory(
                id = 1,
                title = "Pedindo Ajuda na Aula",
                text = "Quando estou confuso, eu sei que é importante pedir ajuda. Eu levanto a mão e digo 'Professor, pode me ajudar?'. Assim eu aprendo mais rápido.",
                type = StoryType.SOCIAL,
                visualSupport = listOf(
                    VisualSupportItem("Quando estou confuso,", R.drawable.confuso_tcc),
                    VisualSupportItem("eu sei que é importante pedir ajuda.", R.drawable.pedindo_ajuda_tcc),
                    VisualSupportItem("Eu levanto a mão", R.drawable.mao_levantada_tcc),
                    VisualSupportItem("e digo 'Professor, pode me ajudar?'.", R.drawable.falando_tcc),
                    VisualSupportItem("Assim eu aprendo mais rápido.", R.drawable.lampada_tcc)
                )
            ),
            ReadingStory(
                id = 2,
                title = "Esperando a Minha Vez",
                text = "Eu devo esperar a minha vez de falar ou brincar. Eu respiro fundo e conto até três. Esperar é um sinal de respeito.",
                type = StoryType.SOCIAL,
                visualSupport = listOf(
                    VisualSupportItem("Eu devo esperar a minha vez", R.drawable.esperando_vez_tcc),
                    VisualSupportItem("de falar ou brincar.", R.drawable.brincar_tcc),
                    VisualSupportItem("Eu respiro fundo", R.drawable.respirar_tcc),
                    VisualSupportItem("e conto até três.", R.drawable.contando_tcc),
                    VisualSupportItem("Esperar é um sinal de respeito.", R.drawable.bom_trabalho_tcc)
                )
            )
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ReadingViewModel()
            }
        }
    }
}
