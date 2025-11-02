package com.example.included.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Importação correta da seta
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.included.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogicMenuScreen(
    onBackClick: () -> Unit,
    onNavigateToSequencing: () -> Unit,
    onNavigateToPattern: () -> Unit
) {
    val logicColor = Color(0xFFFF9800)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Módulo de Lógica") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Selecione o tipo de atividade de Lógica:",
                style = MaterialTheme.typography.titleLarge.copy(color = Color.Black.copy(alpha = 0.7f)),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Opção 1: Sequência de Ações
            ActivityTypeCard(
                title = "Sequência de Ações",
                // TEXTO REMOVIDO: (Lavar as Mãos, Escovar os Dentes)
                description = "Organize atividades da rotina diária em ordem correta.",
                onClick = onNavigateToSequencing,
                logicColor = logicColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Opção 2: Padrões Visuais
            ActivityTypeCard(
                title = "Padrões Visuais",
                // TEXTO REMOVIDO: (Próxima Implementação)
                description = "Identifique e complete séries de formas e cores com a lógica correta.",
                onClick = onNavigateToPattern,
                logicColor = logicColor
            )
        }
    }
}

@Composable
fun ActivityTypeCard(title: String, description: String, onClick: () -> Unit, logicColor: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lightbulb,
                contentDescription = null,
                tint = logicColor,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = logicColor
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }
}
