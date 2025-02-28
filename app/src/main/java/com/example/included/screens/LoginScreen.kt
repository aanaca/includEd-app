package com.example.included.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.included.R

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var confirmarEmail by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var isRegistrando by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isRegistrando) {
            Text(
                text = "Criar Conta",
                style = MaterialTheme.typography.headlineMedium
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.logo_tcc),
                contentDescription = "Logo IncludEd",
                modifier = Modifier.size(200.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        if (isRegistrando) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmarEmail,
                onValueChange = { confirmarEmail = it },
                label = { Text("Confirmar Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        if (isRegistrando) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                label = { Text("Confirmar Senha") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (isRegistrando) {
                    when {
                        email.isEmpty() || confirmarEmail.isEmpty() ||
                                senha.isEmpty() || confirmarSenha.isEmpty() -> {
                            onShowMessage("Preencha todos os campos")
                        }
                        email != confirmarEmail -> {
                            onShowMessage("Os emails não correspondem")
                        }
                        senha != confirmarSenha -> {
                            onShowMessage("As senhas não correspondem")
                        }
                        else -> {
                            onShowMessage("Conta criada com sucesso!")
                            isRegistrando = false
                        }
                    }
                } else {
                    if (email.isEmpty() || senha.isEmpty()) {
                        onShowMessage("Preencha todos os campos")
                    } else {
                        onLoginSuccess()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRegistrando) "Cadastrar" else "Entrar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                isRegistrando = !isRegistrando
                email = ""
                confirmarEmail = ""
                senha = ""
                confirmarSenha = ""
            }
        ) {
            Text(
                if (isRegistrando)
                    "Já tem uma conta? Faça login"
                else
                    "Não tem uma conta? Cadastre-se"
            )
        }
    }
}
