package com.example.included.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.included.viewmodel.ReadingViewModel
import com.example.included.viewmodel.QuizState
import com.example.included.ui.theme.ReadingBlue
import com.example.included.ui.theme.White
import com.example.included.models.QuizQuestion

@Composable
fun QuizScreen(
    viewModel: ReadingViewModel
) {
    val quizQuestion by viewModel.currentQuiz.collectAsState()
    val quizResult by viewModel.quizResult.collectAsState()
    val readingColor = ReadingBlue

    if (quizQuestion == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val quiz = quizQuestion!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Pergunta de Compreensão",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = readingColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Título da Pergunta
        Text(
            text = quiz.questionText,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // CORREÇÃO: MENSAGEM DE AJUDA MOVIdA PARA CIMA (ANTES DA GRADE)
        if (quizResult == QuizState.UNANSWERED) {
            Text(
                text = "Toque na imagem que mostra a resposta correta.",
                fontSize = 16.sp, // Aumentei o tamanho para destaque
                fontWeight = FontWeight.SemiBold,
                color = readingColor.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Grade de Opções de Resposta
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp, max = 500.dp),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(quiz.options) { index, option ->
                val isEnabled = quizResult != QuizState.CORRECT

                QuizOptionCard(
                    optionText = option.text,
                    imageResId = option.imageResId,
                    onClick = { viewModel.submitAnswer(index) },
                    readingColor = readingColor,
                    isEnabled = isEnabled,
                    isCorrect = if (quizResult == QuizState.CORRECT) (index == quiz.correctAnswerIndex) else null,
                    isIncorrectAttempt = quizResult == QuizState.INCORRECT && index != quiz.correctAnswerIndex
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // BLOCO DE FEEDBACK (PERMANECE NO RODAPÉ)
        when (quizResult) {
            QuizState.CORRECT -> {
                FeedbackMessage(
                    text = "CORRETO! Você entendeu a lição.",
                    color = Color(0xFF4CAF50),
                    icon = Icons.Filled.CheckCircle
                )
                Spacer(modifier = Modifier.height(24.dp))
                // Botão "Próxima História"
                Button(
                    onClick = viewModel::loadNextStory,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(50.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Próxima História")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Próxima História", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            QuizState.INCORRECT -> {
                FeedbackMessage(
                    text = "ERRADO. Tente novamente.",
                    color = Color(0xFFF44336),
                    icon = Icons.Filled.Warning
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            QuizState.UNANSWERED -> {
                // Conteúdo vazio aqui, pois a mensagem foi movida para o topo.
                // Isso evita que a grade se mova ao ser respondida.
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }
}

// COMPOSABLE para a mensagem de feedback (Mantido)
@Composable
fun FeedbackMessage(text: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(12.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}


@Composable
fun QuizOptionCard(
    optionText: String,
    imageResId: Int,
    onClick: () -> Unit,
    readingColor: Color,
    isEnabled: Boolean,
    isCorrect: Boolean?,
    isIncorrectAttempt: Boolean
) {
    val opacity = if (isEnabled) 1f else 0.5f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable(enabled = isEnabled, onClick = onClick)
            .background(Color.Transparent),
        colors = CardDefaults.cardColors(containerColor = White),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .alpha(opacity),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = imageResId),
                contentDescription = optionText,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Text(
                text = optionText,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = readingColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
            )
        }
    }
}
