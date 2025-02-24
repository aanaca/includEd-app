package com.example.included.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

/**
 * Tela de Configurações do perfil.
 *
 * Nesta tela o usuário poderá:
 * - Alterar a senha (com confirmação)
 * - Alterar o email (com confirmação)
 * - Escolher o modo de tela (escuro/claro) e ajustar o zoom da tela
 * - Configurar notificações (silenciar ou retirar)
 *
 * @param onBack Função chamada ao clicar no botão de voltar.
 * @param onShowMessage Função para exibir mensagens, por exemplo via Toast.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    // Estados dos campos de senha e email.
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newEmail by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }

    // Estados de personalização da tela.
    var isDarkMode by remember { mutableStateOf(false) }
    var zoomLevel by remember { mutableStateOf(1f) }

    // Estados das notificações.
    var notificationsMuted by remember { mutableStateOf(false) }
    var notificationsTurnedOff by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações") },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
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
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Seção de Alterar Senha
            Text("Alterar Senha", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("Nova Senha") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Senha") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                onClick = {
                    if (newPassword.isNotEmpty() && newPassword == confirmPassword) {
                        onShowMessage("Senha atualizada com sucesso!")
                        newPassword = ""
                        confirmPassword = ""
                    } else {
                        onShowMessage("As senhas não conferem ou estão vazias.")
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Salvar Senha")
            }

            Divider()

            // Seção de Alterar Email
            Text("Alterar Email", style = MaterialTheme.typography.titleMedium)
            OutlinedTextField(
                value = newEmail,
                onValueChange = { newEmail = it },
                label = { Text("Novo Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                value = confirmEmail,
                onValueChange = { confirmEmail = it },
                label = { Text("Confirmar Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            Button(
                onClick = {
                    if (newEmail.isNotEmpty() && newEmail == confirmEmail) {
                        onShowMessage("Email atualizado com sucesso!")
                        newEmail = ""
                        confirmEmail = ""
                    } else {
                        onShowMessage("Os emails não conferem ou estão vazios.")
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Salvar Email")
            }

            Divider()

            // Seção de Preferências de Tela
            Text("Preferências de Tela", style = MaterialTheme.typography.titleMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Modo Escuro")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isDarkMode = it }
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Zoom na Tela: ${"%.1f".format(zoomLevel)}")
            Slider(
                value = zoomLevel,
                onValueChange = { zoomLevel = it },
                valueRange = 0.5f..2.0f,
                steps = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Divider()

            // Seção de Configurações das Notificações
            Text("Notificações", style = MaterialTheme.typography.titleMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Silenciar Notificações")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = notificationsMuted,
                    onCheckedChange = { notificationsMuted = it }
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Retirar Notificações")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = notificationsTurnedOff,
                    onCheckedChange = { notificationsTurnedOff = it }
                )
            }
        }
    }
}