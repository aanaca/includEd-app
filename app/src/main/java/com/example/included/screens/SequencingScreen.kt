package com.example.included.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.included.R
import com.example.included.models.SequenceStep
import com.example.included.viewmodel.SequencingViewModel
import com.example.included.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SequencingScreen(
    onBackClick: () -> Unit,
    viewModel: SequencingViewModel = viewModel(factory = SequencingViewModel.Factory)
) {
    val currentActivity by viewModel.currentActivity.collectAsState()
    val userSteps by viewModel.userSteps.collectAsState()
    val isCorrect by viewModel.isCorrect.collectAsState()
    val isCheckPerformed by viewModel.isCheckPerformed.collectAsState()

    val logicColor = Color(0xFFFF9800) // Laranja da categoria Lógica

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lógica: ${currentActivity.title}") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = logicColor,
                    titleContentColor = White,
                    navigationIconContentColor = White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Use as setas para ordenar os passos:",
                fontSize = 18.sp,
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // --- LISTA DE PASSOS COM REORDENAÇÃO MANUAL (SETAS) ---
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(userSteps) { index, step ->
                    SequencingStepCard(
                        step = step,
                        stepNumber = index + 1,
                        activityColor = logicColor,
                        modifier = Modifier.fillMaxWidth(),
                        // AÇÕES PARA AS SETAS
                        onMoveUp = {
                            if (index > 0) viewModel.moveStep(index, index - 1)
                        },
                        onMoveDown = {
                            if (index < userSteps.size - 1) viewModel.moveStep(index, index + 1)
                        },
                        // Se o resultado já foi verificado, não permite mais movimentos
                        isMovementEnabled = !isCheckPerformed || !isCorrect
                    )
                }
            }
            // --- FIM DA LISTA ---

            Spacer(modifier = Modifier.height(24.dp))

            // Botão de Verificação ou Feedback
            if (!isCheckPerformed) {
                Button(
                    onClick = viewModel::checkSequence,
                    colors = ButtonDefaults.buttonColors(containerColor = logicColor),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Verificar Ordem", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                FeedbackButton(
                    isCorrect = isCorrect,
                    onNextOrRetry = viewModel::loadNextActivity,
                    activityColor = logicColor
                )
            }
        }
    }
}


@Composable
fun SequencingStepCard(
    step: SequenceStep,
    stepNumber: Int,
    activityColor: Color,
    modifier: Modifier,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    isMovementEnabled: Boolean
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .padding(vertical = 8.dp)
            .height(100.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Número do Passo
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(activityColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$stepNumber",
                    color = White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            // Imagem
            Image(
                painter = painterResource(id = step.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Descrição do Passo
            Text(
                text = step.description,
                modifier = Modifier.weight(1f),
                fontSize = 15.sp,
                color = Color.Black.copy(alpha = 0.8f)
            )

            // --- CONTROLES DE MOVIMENTO (SETAS) ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Seta para Cima
                IconButton(
                    onClick = onMoveUp,
                    enabled = isMovementEnabled // Desabilita após o acerto
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Mover para cima",
                        tint = if (isMovementEnabled) activityColor else Color.LightGray,
                        modifier = Modifier.size(30.dp)
                    )
                }

                // Seta para Baixo
                IconButton(
                    onClick = onMoveDown,
                    enabled = isMovementEnabled
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Mover para baixo",
                        tint = if (isMovementEnabled) activityColor else Color.LightGray,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            // --- FIM CONTROLES ---
        }
    }
}


@Composable
fun FeedbackButton(isCorrect: Boolean, onNextOrRetry: () -> Unit, activityColor: Color) {
    val successColor = Color(0xFF4CAF50)
    val errorColor = Color(0xFFF44336)

    if (isCorrect) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Icon(Icons.Default.ThumbUp, contentDescription = null, tint = successColor, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("CORRETO! Ótimo raciocínio.", color = successColor, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onNextOrRetry,
                colors = ButtonDefaults.buttonColors(containerColor = successColor),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Próxima Atividade", fontSize = 18.sp)
            }
        }
    } else {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(errorColor.copy(alpha = 0.1f))
                    .padding(12.dp)
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = errorColor, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ERRADO. Tente reorganizar a sequência.", color = errorColor, fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = onNextOrRetry,
                colors = ButtonDefaults.buttonColors(containerColor = errorColor),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Tentar Novamente")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Embaralhar e Tentar Novamente", fontSize = 18.sp)
            }
        }
    }
}
