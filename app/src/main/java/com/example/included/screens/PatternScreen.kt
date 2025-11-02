package com.example.included.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.included.models.PatternElement
import com.example.included.viewmodel.PatternViewModel
import com.example.included.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatternScreen(
    onBackClick: () -> Unit,
    viewModel: PatternViewModel = viewModel(factory = PatternViewModel.Factory)
) {
    val activity by viewModel.currentActivity.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val isCorrect by viewModel.isCorrect.collectAsState()

    val logicColor = Color(0xFFFF9800)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lógica: Padrões Visuais") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        // ÍCONE CORRIGIDO: Seta para a esquerda
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
                .background(White)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = activity.instructions,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 1. O Padrão em Si
            PatternDisplay(
                pattern = activity.pattern,
                missingElement = activity.missingElement,
                isCorrect = isCorrect,
                logicColor = logicColor
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 2. Opções de Resposta
            Text(
                text = "Escolha o próximo elemento:",
                fontSize = 16.sp,
                color = logicColor,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                activity.options.forEach { option ->
                    PatternOption(
                        element = option,
                        isSelected = option.id == selectedOption?.id,
                        isResult = isCorrect,
                        logicColor = logicColor,
                        onSelect = { viewModel.selectOption(option) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 3. Botão de Próximo / Feedback Corrigido
            if (isCorrect == true) {
                // SUCESSO: Vai para o próximo padrão
                Button(
                    onClick = viewModel::loadNextActivity,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Verde
                    modifier = Modifier.fillMaxWidth(0.6f).height(50.dp)
                ) {
                    Text("Próximo Padrão", fontSize = 18.sp)
                }
            } else if (isCorrect == false) {
                // ERRO: Mostra feedback e DOIS BOTÕES
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Resposta incorreta. Tente novamente ou avance.",
                        color = Color(0xFFF44336),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(0.85f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // BOTÃO 1: ESCOLHER NOVAMENTE (Tenta o mesmo padrão)
                        Button(
                            onClick = viewModel::resetSelection,
                            colors = ButtonDefaults.buttonColors(containerColor = logicColor), // Laranja
                            modifier = Modifier.weight(1f).height(50.dp)
                        ) {
                            Text("Escolher Novamente", fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // BOTÃO 2: PRÓXIMO PADRÃO (Desiste e avança)
                        Button(
                            onClick = viewModel::loadNextActivity,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray), // Cinza
                            modifier = Modifier.weight(1f).height(50.dp)
                        ) {
                            Text("Próximo Padrão", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PatternDisplay(
    pattern: List<PatternElement>,
    missingElement: PatternElement,
    isCorrect: Boolean?,
    logicColor: Color
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Exibir os elementos do padrão
        pattern.forEach { element ->
            Image(
                painter = painterResource(id = element.imageResId),
                contentDescription = element.id,
                modifier = Modifier.size(70.dp).padding(4.dp)
            )
            Text(">", color = Color.Gray, fontSize = 24.sp)
        }

        // Espaço Faltando (Representa a ? )
        val borderColor = when (isCorrect) {
            true -> Color(0xFF4CAF50) // Verde
            false -> Color(0xFFF44336) // Vermelho
            else -> logicColor.copy(alpha = 0.5f) // Laranja claro
        }

        Box(
            modifier = Modifier
                .size(70.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isCorrect == true) Color(0xFF4CAF50).copy(alpha = 0.2f) else Color.Transparent)
                .border(2.dp, borderColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (isCorrect == true) {
                // Se estiver correto, mostra a imagem correta
                Image(
                    painter = painterResource(id = missingElement.imageResId),
                    contentDescription = missingElement.id,
                    modifier = Modifier.size(60.dp)
                )
            } else {
                Text("?", fontSize = 36.sp, fontWeight = FontWeight.ExtraBold, color = borderColor)
            }
        }
    }
}


@Composable
fun PatternOption(
    element: PatternElement,
    isSelected: Boolean,
    isResult: Boolean?,
    logicColor: Color,
    onSelect: () -> Unit
) {
    val borderColor = when {
        isResult == true && isSelected -> Color(0xFF4CAF50)
        isResult == false && isSelected -> Color(0xFFF44336)
        isSelected -> logicColor
        else -> Color.LightGray
    }

    // O usuário só pode clicar se AINDA não tivermos um resultado (isResult == null)
    val isEnabled = isResult == null

    Box(
        modifier = Modifier
            .size(90.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(3.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(enabled = isEnabled, onClick = onSelect),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = element.imageResId),
            contentDescription = element.id,
            modifier = Modifier.size(70.dp)
        )
    }
}
