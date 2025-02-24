package com.example.included.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun SearchScreen(
    onShowMessage: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Buscar") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Campo de busca
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    // TODO: Implementar lógica de busca
                    searchResults = listOf("Resultado 1", "Resultado 2", "Resultado 3")
                },
                label = { Text("Buscar") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar"
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Resultados da busca
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchResults) { result ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ListItem(
                            headlineContent = { Text(result) },
                            modifier = Modifier.clickable {
                                onShowMessage("Selecionado: $result")
                            }
                        )
                    }
                }
            }
        }
    }
}