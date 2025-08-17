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
import android.util.Patterns

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

    // Estados para recuperação de senha
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }
    var showPasswordSentScreen by remember { mutableStateOf(false) }

    if (showPasswordSentScreen) {
        // Tela de confirmação de envio do link
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Link de recuperação enviado!",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Verifique seu e-mail ($forgotEmail) e siga as instruções para redefinir sua senha.",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                showPasswordSentScreen = false
                forgotEmail = ""
            }) {
                Text("Voltar para login")
            }
        }
    } else {
        // Tela de login/cadastro
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

            // Container para botões e links
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
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

                if (!isRegistrando) {
                    TextButton(
                        onClick = { showForgotPasswordDialog = true }
                    ) {
                        Text("Esqueceu sua senha?")
                    }
                }

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

        // Dialog "Esqueceu sua senha?" com validação de email
        if (showForgotPasswordDialog) {
            AlertDialog(
                onDismissRequest = { showForgotPasswordDialog = false },
                title = { Text("Recuperar Senha") },
                text = {
                    Column {
                        Text("Digite seu email para recuperar a senha:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = forgotEmail,
                            onValueChange = { forgotEmail = it },
                            label = { Text("Email") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            when {
                                forgotEmail.isEmpty() -> onShowMessage("Digite um email")
                                !Patterns.EMAIL_ADDRESS.matcher(forgotEmail).matches() -> onShowMessage("Digite um email válido")
                                else -> {
                                    showForgotPasswordDialog = false
                                    showPasswordSentScreen = true
                                }
                            }
                        }
                    ) {
                        Text("Enviar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showForgotPasswordDialog = false
                            forgotEmail = ""
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
