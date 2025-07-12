package com.example.included.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    // Campos de email e senha
    var newPassword by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var newEmail by rememberSaveable { mutableStateOf("") }
    var confirmEmail by rememberSaveable { mutableStateOf("") }

    // Preferências
    var isDarkMode by rememberSaveable { mutableStateOf(false) }
    var zoomLevel by remember { mutableFloatStateOf(1f) }

    // Notificações
    var notificationsMuted by rememberSaveable { mutableStateOf(false) }
    var notificationsTurnedOff by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurações") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                .verticalScroll(rememberScrollState()) // ⬅️ ESSENCIAL!
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        )
        {
            // Seção: Conta
            Text("Conta", style = MaterialTheme.typography.titleMedium)

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

            Spacer(modifier = Modifier.height(16.dp))

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

            // Seção: Preferências
            Text("Preferências", style = MaterialTheme.typography.titleMedium)

            SettingsSwitch(
                text = "Modo Escuro",
                checked = isDarkMode,
                onCheckedChange = { isDarkMode = it }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp) // espaçamento acima e abaixo do bloco
            ) {
                Text(
                    text = "Zoom na Tela: ${"%.1f".format(zoomLevel)}",
                    modifier = Modifier.padding(bottom = 4.dp) // espaço entre texto e slider
                )
                Slider(
                    value = zoomLevel,
                    onValueChange = { zoomLevel = it },
                    valueRange = 0.5f..2.0f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Divider()

            // Seção: Notificações
            Text("Notificações", style = MaterialTheme.typography.titleMedium)

            SettingsSwitch(
                text = "Silenciar Notificações",
                checked = notificationsMuted,
                onCheckedChange = { notificationsMuted = it }
            )

            SettingsSwitch(
                text = "Desativar Notificações",
                checked = notificationsTurnedOff,
                onCheckedChange = { notificationsTurnedOff = it }
            )
        }
    }
}

@Composable
fun SettingsSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
