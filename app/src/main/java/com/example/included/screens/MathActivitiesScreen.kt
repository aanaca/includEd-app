package com.example.included.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.included.viewmodel.MathViewModel
import com.example.included.ui.theme.MathGreen
import com.example.included.ui.theme.White
import com.example.included.models.MathDifficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MathActivitiesScreen(
    onBackClick: () -> Unit,
    viewModel: MathViewModel = viewModel(factory = MathViewModel.Factory)
) {
    val currentExercise by viewModel.currentExercise.collectAsState()
    val userAnswer = viewModel.userAnswer
    val isCorrect = viewModel.isCorrect
    val selectedDifficulty = viewModel.selectedDifficulty
    val currentOptions = viewModel.currentOptions

    var expanded by remember { mutableStateOf(false) }

    val correctColor = Color(0xFF4CAF50)
    val errorColor = Color(0xFFF44336)

    val accentColor = when (isCorrect) {
        true -> correctColor
        false -> errorColor
        else -> MathGreen
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Matemática") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Mudar Dificuldade")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        MathDifficulty.values().forEach { difficulty ->
                            DropdownMenuItem(
                                text = { Text(difficulty.label) },
                                onClick = {
                                    viewModel.changeDifficulty(difficulty)
                                    expanded = false
                                },
                                leadingIcon = {
                                    if (selectedDifficulty == difficulty) {
                                        Icon(Icons.Filled.Check, contentDescription = "Selecionado")
                                    }
                                }
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MathGreen,
                    titleContentColor = White,
                    navigationIconContentColor = White,
                    actionIconContentColor = White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Título do Nível
                Text(
                    text = "Nível: ${selectedDifficulty.label} (Toque para Responder)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MathGreen,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Card de Exibição do Exercício
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.8f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = currentExercise.formatToString(),
                        fontSize = 54.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = White,
                        modifier = Modifier
                            .padding(24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // MODO MÚLTIPLA ESCOLHA (PADRÃO PARA TODOS OS NÍVEIS)
                val optionsEnabled = isCorrect != true

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    currentOptions.chunked(2).forEach { rowOptions ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            rowOptions.forEach { option ->
                                Button(
                                    onClick = { viewModel.checkOption(option) },
                                    enabled = optionsEnabled,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(60.dp)
                                        .padding(horizontal = 8.dp),
                                    // Feedback visual: Cor da opção clicada (correta/errada)
                                    colors = if (isCorrect != null && option == userAnswer.toIntOrNull()) {
                                        ButtonDefaults.buttonColors(containerColor = accentColor)
                                    } else {
                                        ButtonDefaults.buttonColors(containerColor = MathGreen.copy(alpha = 0.7f))
                                    }
                                ) {
                                    Text(text = option.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }

                // Feedback visual claro
                if (isCorrect != null) {
                    Text(
                        text = if (isCorrect == true) "Parabéns! 🎉" else "Resposta incorreta. O resultado é ${currentExercise.result}",
                        color = accentColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            }

            // Área do Botão (Próximo Problema)
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

                // CORREÇÃO: O botão aparece se isCorrect for TRUE ou FALSE (diferente de null)
                if (isCorrect != null) {
                    Button(
                        onClick = viewModel::moveToNextExercise,
                        colors = ButtonDefaults.buttonColors(containerColor = correctColor),
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(60.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Próximo Problema", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
