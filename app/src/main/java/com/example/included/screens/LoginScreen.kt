package com.example.included.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.included.components.PasswordTextField
import com.example.included.dialogs.RecuperarSenhaDialog
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    var isRegistrando by remember { mutableStateOf(false) }
    var nomeCompleto by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var mensagemErro by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var mostrarDialogRecuperacao by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (isRegistrando) "Criar Conta" else "Login",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isRegistrando) {
            OutlinedTextField(
                value = nomeCompleto,
                onValueChange = { nomeCompleto = it },
                label = { Text("Nome Completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuário") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField(
            value = senha,
            onValueChange = { senha = it },
            label = "Senha",
            modifier = Modifier.fillMaxWidth()
        )

        if (isRegistrando) {
            Spacer(modifier = Modifier.height(16.dp))
            PasswordTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                label = "Confirmar Senha",
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botão principal de Login/Registro
        Button(
            onClick = {
                isLoading = true
                // Remover a lógica de autenticação
                onShowMessage("Login realizado com sucesso!") // Mensagem de sucesso
                onLoginSuccess() // Redirecionar para a HomeScreen
                isLoading = false
            },
            enabled = !isLoading && email.isNotEmpty() && senha.isNotEmpty()
                    && (!isRegistrando || (nomeCompleto.isNotEmpty() && confirmarSenha.isNotEmpty())),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(if (isRegistrando) "Registrar" else "Entrar")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão "Esqueceu a senha?" (apenas no modo login)
        if (!isRegistrando) {
            TextButton(
                onClick = { mostrarDialogRecuperacao = true }
            ) {
                Text("Esqueceu a senha?")
            }
        }

        TextButton(
            onClick = {
                isRegistrando = !isRegistrando
                mensagemErro = ""
                // Limpar campos ao trocar de modo
                if (isRegistrando) {
                    nomeCompleto = ""
                    usuario = ""
                    confirmarSenha = ""
                }
            }
        ) {
            Text(
                if (isRegistrando)
                    "Já tem uma conta? Faça login"
                else
                    "Não tem uma conta? Registre-se"
            )
        }

        if (mensagemErro.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensagemErro,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    if (mostrarDialogRecuperacao) {
        RecuperarSenhaDialog(
            emailInicial = email,
            onDismiss = { mostrarDialogRecuperacao = false },
            onEmailEnviado = { mensagem ->
                onShowMessage(mensagem)
                mostrarDialogRecuperacao = false
            }
        )
    }
}