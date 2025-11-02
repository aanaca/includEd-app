package com.example.included.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.included.viewmodel.ReadingViewModel
import com.example.included.viewmodel.QuizState // Adicionado
import com.example.included.ui.theme.ReadingBlue
import com.example.included.ui.theme.White
import androidx.compose.foundation.layout.FlowRow
import com.example.included.R // Adicionei esta importação por segurança

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ReadingActivitiesScreen(
    onBackClick: () -> Unit,
    viewModel: ReadingViewModel = viewModel(factory = ReadingViewModel.Factory)
) {
    // CORRIGIDO: Coletando o estado da frase e da história
    val currentStoryState by viewModel.currentStory.collectAsState()
    val currentPhraseIndex by viewModel.currentPhraseIndex.collectAsState()

    // Verifica se a história está carregada
    val currentStory = currentStoryState ?: run {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Lógica de estado
    val isQuizTime = currentPhraseIndex >= currentStory.visualSupport.size
    val isLastPhrase = currentPhraseIndex == currentStory.visualSupport.lastIndex

    val readingColor = ReadingBlue

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isQuizTime) "Questionário" else "Leitura e Compreensão") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = readingColor,
                    titleContentColor = White,
                    navigationIconContentColor = White
                )
            )
        }
    ) { paddingValues ->

        if (isQuizTime) {
            Box(modifier = Modifier.padding(paddingValues)) {
                QuizScreen(viewModel = viewModel)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // 1. Título e Tipo da História
                Text(
                    text = currentStory.title,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = readingColor,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Tipo: ${currentStory.type.label}",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // 2. Área de Suporte Visual (Imagem)
                val currentVisualItem = currentStory.visualSupport.getOrNull(currentPhraseIndex)

                currentVisualItem?.imageResId?.let { resId ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .aspectRatio(16f / 9f)
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = readingColor.copy(alpha = 0.1f)),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = resId),
                            contentDescription = "Suporte Visual para ${currentVisualItem.phrase}",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        )
                    }
                }

                // 3. Área de Leitura (Karaoke Highlighting)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { viewModel.nextPhrase() }, // Chama a função nextPhrase() do ViewModel.
                    colors = CardDefaults.cardColors(containerColor = readingColor.copy(alpha = 0.9f)),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        // A. Dica de Interação
                        val instructionText = if (!isLastPhrase)
                            "TOQUE AQUI PARA CONTINUAR"
                        else
                            "TOQUE AQUI PARA INICIAR O QUESTIONÁRIO"

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Toque para continuar",
                                tint = Color.Yellow,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = instructionText,
                                fontSize = 16.sp,
                                color = Color.Yellow,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Toque para continuar",
                                tint = Color.Yellow,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // B. Fluxo de Texto (FlowRow)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            currentStory.visualSupport.forEachIndexed { index, item ->
                                val isHighlighted = index == currentPhraseIndex

                                Text(
                                    text = item.phrase,
                                    fontSize = 24.sp,
                                    fontWeight = if (isHighlighted) FontWeight.ExtraBold else FontWeight.Normal,
                                    color = if (isHighlighted) Color.Yellow else White,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            if (isHighlighted) Color.Black.copy(alpha = 0.2f) else Color.Transparent
                                        )
                                        .padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 4. Rodapé
                if (isLastPhrase) {
                    Text(
                        text = "Toque no texto acima para ir para as perguntas.",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
