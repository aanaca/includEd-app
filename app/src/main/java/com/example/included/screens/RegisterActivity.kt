package com.example.included.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

// Tela de registro que permite aos usuários criar uma nova conta
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    // Callback chamado quando o registro é bem-sucedido
    onRegisterSuccess: () -> Unit,
    // Callback para mostrar mensagens ao usuário
    onShowMessage: (String) -> Unit
) {
    // Estados para armazenar os valores dos campos do formulário
    var email by remember { mutableStateOf("") }
    var confirmEmail by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmSenha by remember { mutableStateOf("") }

    // Layout principal em coluna
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Campo de email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de confirmação de email
        OutlinedTextField(
            value = confirmEmail,
            onValueChange = { confirmEmail = it },
            label = { Text("Confirmar Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de senha com visualização protegida
        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de confirmação de senha com visualização protegida
        OutlinedTextField(
            value = confirmSenha,
            onValueChange = { confirmSenha = it },
            label = { Text("Confirmar Senha") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de criar conta com validações
        Button(
            onClick = {
                // Validação de campos vazios
                if (email.isEmpty() || confirmEmail.isEmpty() ||
                    senha.isEmpty() || confirmSenha.isEmpty()) {
                    onShowMessage("Preencha todos os campos!")
                    return@Button
                }

                // Validação de emails correspondentes
                if (email != confirmEmail) {
                    onShowMessage("Os emails não coincidem!")
                    return@Button
                }

                // Validação de senhas correspondentes
                if (senha != confirmSenha) {
                    onShowMessage("As senhas não coincidem!")
                    return@Button
                }

                // Se todas as validações passarem, chama o callback de sucesso
                onRegisterSuccess()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Criar Conta")
        }
    }
}