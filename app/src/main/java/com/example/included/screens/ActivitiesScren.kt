package com.example.included.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Definição do modelo de dados para as categorias
data class ActivityCategory(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val route: String // Rota específica para a atividade
)

/**
 * Composable principal para a tela de Atividades.
 * Esta tela lista as categorias disponíveis (Matemática, Leitura, Lógica, Memória).
 * @param onCategoryClick Lambda para notificar a MainActivity sobre qual categoria foi clicada.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(onCategoryClick: (String) -> Unit) {
    // Lista base das categorias conforme solicitado
    val categories = listOf(
        ActivityCategory(
            name = "Matemática",
            icon = Icons.Filled.Calculate,
            color = Color(0xFF4CAF50), // Verde
            route = "math_detail"
        ),
        ActivityCategory(
            name = "Leitura",
            icon = Icons.Filled.MenuBook,
            color = Color(0xFF2196F3), // Azul
            route = "reading_detail"
        ),
        ActivityCategory(
            name = "Lógica",
            icon = Icons.Filled.Lightbulb,
            color = Color(0xFFFF9800), // Laranja
            route = "logic_detail"
        ),
        ActivityCategory(
            name = "Memória",
            icon = Icons.Filled.Memory,
            color = Color(0xFF9C27B0), // Roxo
            route = "memory_detail"
        )
    )

    // O Scaffold aqui é opcional se você usar um Scaffold na MainActivity,
    // mas é útil para ter um TopAppBar específico para esta tela.
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Atividades") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Usando sua cor primária (IncludedBlue)
                    titleContentColor = MaterialTheme.colorScheme.onPrimary, // Usando branco do seu tema
                )
            )
        }
    ) { paddingValues ->
        // LazyColumn para lidar com a lista de categorias
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                CategoryCard(category = category) {
                    onCategoryClick(category.route)
                }
            }
        }
    }
}

/**
 * Composable para o cartão individual de cada categoria.
 */
@Composable
fun CategoryCard(category: ActivityCategory, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick) // Adiciona o evento de clique
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface) // Usando a cor de superfície do seu tema
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícone da categoria com a cor personalizada
            Icon(
                imageVector = category.icon,
                contentDescription = "Ícone de ${category.name}",
                tint = category.color,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(category.color.copy(alpha = 0.15f)) // Fundo suave para o ícone
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            // Nome da categoria
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            // Seta indicando que é clicável
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
