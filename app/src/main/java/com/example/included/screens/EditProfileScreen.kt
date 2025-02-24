package com.example.included.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

/**
 *
 *
 * @param onBack Função chamada ao clicar no botão de voltar.
 * @param onSave Função chamada ao clicar em salvar, passando os dados editados.
 * @param initialName Nome inicial do usuário.
 * @param initialHandle Handle (usuário) inicial.
 * @param initialBio Bio inicial do usuário.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun EditProfileScreen(
    onBack: () -> Unit,
    onSave: (String, String, String) -> Unit,
    initialName: String = "Nome de Usuário",
    initialHandle: String = "@usuarioExemplo",
    initialBio: String = "Digite sua bio aqui."
) {
    var name by remember { mutableStateOf(initialName) }
    var handle by remember { mutableStateOf(initialHandle) }
    var bio by remember { mutableStateOf(initialBio) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onSave(name, handle, bio) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Salvar alterações"
                        )
                    }
                }
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
            // Avatar com placeholder e ação de clique para editar a foto do perfil
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { /* TODO: Implemente a seleção de nova foto de perfil */ },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.firstOrNull()?.toString() ?: "",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Campo para editar o nome
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Campo para editar o usuário (handle)
            OutlinedTextField(
                value = handle,
                onValueChange = { handle = it },
                label = { Text("Usuário") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Campo para editar a biografia
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Botão para salvar alterações
            Button(
                onClick = { onSave(name, handle, bio) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Alterações")
            }
        }
    }
}