package com.example.included.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // Importação necessária para os ícones
import androidx.compose.material.icons.automirrored.filled.ArrowForward // Ícone de seta
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment // Importação necessária para corrigir o erro anterior
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector // Tipo do Icon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(
    onCategoryClick: (categoryRoute: String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Atividades") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Card de Matemática
            ActivityCard(
                title = "Matemática",
                icon = Icons.Default.Calculate, // Ícone de cálculo
                onClick = { onCategoryClick("math_detail") },
                iconColor = Color(0xFF4CAF50) // Verde
            )

            // Card de Leitura
            ActivityCard(
                title = "Leitura",
                icon = Icons.Default.Book, // Ícone de livro
                onClick = { onCategoryClick("reading_detail") },
                iconColor = Color(0xFF2196F3) // Azul
            )

            // Card de Lógica
            ActivityCard(
                title = "Lógica",
                icon = Icons.Default.Lightbulb, // Ícone de lâmpada
                onClick = { onCategoryClick("logic_detail") },
                iconColor = Color(0xFFFF9800) // Laranja
            )

            // Card de Memória
            ActivityCard(
                title = "Memória",
                icon = Icons.Default.Extension, // Ícone de peça de quebra-cabeça/extensão
                onClick = { onCategoryClick("memory_game_route") },
                iconColor = Color(0xFF9C27B0) // Roxo
            )
        }
    }
}

// Componente ActivityCard atualizado para incluir um ícone
@Composable
fun ActivityCard(
    title: String,
    icon: ImageVector, // Novo parâmetro para o ícone
    onClick: () -> Unit,
    iconColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // ÍCONE DE CATEGORIA
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                // TÍTULO DA CATEGORIA
                Text(
                    text = title,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            // ÍCONE DE SETA
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Navegar",
                tint = Color.Gray
            )
        }
    }
}
