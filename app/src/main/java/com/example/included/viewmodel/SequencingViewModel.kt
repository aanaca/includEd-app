package com.example.included.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.included.R
import com.example.included.models.SequencingActivity
import com.example.included.models.SequenceStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class SequencingViewModel : ViewModel() {

    private val allActivities = generateSampleSequences()

    // Estado da atividade atual
    private val _currentActivity = MutableStateFlow(allActivities.first())
    val currentActivity: StateFlow<SequencingActivity> = _currentActivity.asStateFlow()

    // Estado dos passos atualmente organizados (inicia embaralhado)
    private val _userSteps = MutableStateFlow(
        _currentActivity.value.correctSteps.shuffled(Random(System.currentTimeMillis()))
    )
    val userSteps: StateFlow<List<SequenceStep>> = _userSteps.asStateFlow()

    // Estado da resposta (para feedback visual)
    private val _isCorrect = MutableStateFlow(false)
    val isCorrect: StateFlow<Boolean> = _isCorrect.asStateFlow()

    // Indica se a verificação foi realizada
    private val _isCheckPerformed = MutableStateFlow(false)
    val isCheckPerformed: StateFlow<Boolean> = _isCheckPerformed.asStateFlow()

    /**
     * Move um item dentro da lista de passos do usuário (Drag and Drop/Rearranjo).
     */
    fun moveStep(fromIndex: Int, toIndex: Int) {
        val currentList = _userSteps.value.toMutableList()
        val item = currentList.removeAt(fromIndex)
        currentList.add(toIndex, item)
        _userSteps.value = currentList
        // Reseta o feedback ao mover
        _isCheckPerformed.value = false
        _isCorrect.value = false
    }

    /**
     * Verifica se a sequência atual do usuário é igual à sequência correta.
     */
    fun checkSequence() {
        _isCheckPerformed.value = true
        val correctSteps = _currentActivity.value.correctSteps
        val userSteps = _userSteps.value

        // Compara cada passo na ordem
        val isSequenceCorrect = userSteps.indices.all { i ->
            userSteps[i].description == correctSteps[i].description
        }

        _isCorrect.value = isSequenceCorrect
    }

    /**
     * Carrega a próxima atividade (ou recarrega a única existente), embaralhando os passos.
     */
    fun loadNextActivity() {
        val currentIndex = allActivities.indexOfFirst { it.id == _currentActivity.value.id }
        val nextIndex = (currentIndex + 1) % allActivities.size

        val nextActivity = allActivities[nextIndex]
        _currentActivity.value = nextActivity
        _userSteps.value = nextActivity.correctSteps.shuffled(Random(System.currentTimeMillis()))
        _isCorrect.value = false
        _isCheckPerformed.value = false
    }

    // --- Dados de Exemplo ATUALIZADOS: APENAS LAVAR AS MÃOS ---

    private fun generateSampleSequences(): List<SequencingActivity> {
        return listOf(
            SequencingActivity(
                id = 201,
                title = "Como Lavar as Mãos",
                correctSteps = listOf(
                    SequenceStep(R.drawable.abrir_torneira_tcc, "Abrir a torneira e molhar as mãos."),
                    SequenceStep(R.drawable.ensaboar_tcc, "Passar o sabão e esfregar as mãos."),
                    SequenceStep(R.drawable.enxaguar_tcc, "Enxaguar as mãos para tirar o sabão."),
                    SequenceStep(R.drawable.fechar_torneira_tcc, "Fechar a torneira."),
                    SequenceStep(R.drawable.secar_tcc, "Secar as mãos com a toalha.")
                )
            )
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SequencingViewModel()
            }
        }
    }
}
