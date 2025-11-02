package com.example.included.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.* // Importação necessária para remember e mutableStateOf
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
import com.example.included.models.MemoryCard
import com.example.included.viewmodel.MemoryViewModel
import com.example.included.viewmodel.MemoryDifficulty // Importação necessária para a classe Difficulty
import com.example.included.ui.theme.White

// Definição de cores para o Módulo de Memória (Roxo)
val MemoryColor = Color(0xFF9C27B0) // Cor principal (Roxo)
val CardColor = Color(0xFFE6E0F8) // Cor de Fundo do Cartão (Roxo claro/Lavanda)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryGameScreen(
    onBackClick: () -> Unit,
    viewModel: MemoryViewModel = viewModel(factory = MemoryViewModel.Factory)
) {
    val cards by viewModel.cards.collectAsState()
    val moves by viewModel.moves.collectAsState()
    val pairsFound by viewModel.pairsFound.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val currentDifficulty by viewModel.currentDifficulty.collectAsState() // Novo estado

    val (showDifficultyDialog, setShowDifficultyDialog) = remember { mutableStateOf(false) }

    val totalPairs = cards.size / 2
    val isGameComplete = pairsFound == totalPairs

    // Calcula o número de colunas para o grid (ajusta de acordo com a dificuldade)
    val gridColumns = when (currentDifficulty.pairs) {
        6 -> 4 // Fácil: 12 cartas -> 4 colunas
        10 -> 4 // Médio: 20 cartas -> 4 colunas
        15 -> 5 // Difícil: 30 cartas -> 5 colunas
        else -> 4
    }

    // Efeito para forçar a seleção de dificuldade na primeira vez
    LaunchedEffect(cards.isEmpty()) {
        if (cards.isEmpty()) {
            setShowDifficultyDialog(true)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Jogo da Memória", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(${currentDifficulty.level})", // Exibe a dificuldade
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Light
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MemoryColor,
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
                .background(Color(0xFFF5F5DC)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Placar do Jogo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreItem(label = "Movimentos", value = moves.toString(), color = MemoryColor)
                ScoreItem(label = "Pares Encontrados", value = "$pairsFound / $totalPairs", color = MemoryColor)
            }

            // Exibição de Conclusão do Jogo
            if (isGameComplete) {
                // Altera o GameCompleteMessage para abrir o diálogo de seleção
                GameCompleteMessage(onResetGame = { setShowDifficultyDialog(true) })
            }

            // Grade de Cartas
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns), // Colunas dinâmicas
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(cards, key = { it.cardIndex }) { card -> // Adicionado key para performance
                    MemoryCardItem(
                        card = card,
                        onCardClick = { viewModel.flipCard(card.cardIndex) },
                        isGameProcessing = isProcessing
                    )
                }
            }

            // Botão de Reiniciar (agora abre o diálogo de seleção)
            if (!isGameComplete) {
                Button(
                    onClick = { setShowDifficultyDialog(true) },
                    enabled = !isProcessing,
                    colors = ButtonDefaults.buttonColors(containerColor = MemoryColor),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(bottom = 16.dp)
                ) {
                    Text("Reiniciar Jogo", color = White, fontSize = 18.sp)
                }
            }
        }
    }

    // Diálogo de Seleção de Dificuldade
    if (showDifficultyDialog) {
        DifficultySelectionDialog(
            difficulties = viewModel.getDifficulties(),
            onSelect = { difficulty ->
                viewModel.resetGame(difficulty)
                setShowDifficultyDialog(false)
            },
            onDismiss = {
                // Permite fechar apenas se o jogo já estiver rodando
                if (cards.isNotEmpty()) setShowDifficultyDialog(false)
            }
        )
    }
}

// --- Componentes Reutilizáveis ---

// ... (ScoreItem e MemoryCardItem permanecem inalterados)

@Composable
fun ScoreItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun MemoryCardItem(
    card: MemoryCard,
    onCardClick: () -> Unit,
    isGameProcessing: Boolean
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                enabled = !card.isFlipped && !card.isMatched && !isGameProcessing,
                onClick = onCardClick
            ),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = CardColor),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (card.isMatched) MemoryColor.copy(alpha = 0.2f) else CardColor),
            contentAlignment = Alignment.Center
        ) {
            if (card.isFlipped || card.isMatched) {
                Image(
                    painter = painterResource(id = card.imageResId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(0.8f)
                        .padding(4.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo_tcc),
                    contentDescription = "Verso do Cartão (Logo)",
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }
        }
    }
}

@Composable
fun GameCompleteMessage(onResetGame: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD4AF37)),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Parabéns!",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Você encontrou todos os pares!",
                fontSize = 18.sp,
                color = White
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onResetGame, // Chama a função para redefinir o jogo (abre o diálogo)
                colors = ButtonDefaults.buttonColors(containerColor = White)
            ) {
                Text("Escolher Dificuldade", color = MemoryColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// NOVO COMPONENTE: Diálogo para Seleção de Dificuldade
@Composable
fun DifficultySelectionDialog(
    difficulties: List<MemoryDifficulty>,
    onSelect: (MemoryDifficulty) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecione a Dificuldade") },
        text = {
            Column {
                difficulties.forEach { difficulty ->
                    Button(
                        onClick = { onSelect(difficulty) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MemoryColor)
                    ) {
                        Text("${difficulty.level} (${difficulty.pairs} Pares)")
                    }
                }
            }
        },
        confirmButton = {}
    )
}
