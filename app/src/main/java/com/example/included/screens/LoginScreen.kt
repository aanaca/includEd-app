package com.example.included.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.included.R
import android.util.Patterns

@OptIn(ExperimentalMaterial3Api::class)
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

    // Novo estado para o tipo de perfil
    val tiposPerfil = listOf("Educador", "Especialista", "Responsável")
    var tipoPerfilSelecionado by remember { mutableStateOf("") }

    // Estados para recuperação de senha
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }
    var showPasswordSentScreen by remember { mutableStateOf(false) }

    // ScrollState para garantir que campos não fiquem escondidos pelo teclado no cadastro
    val scrollState = rememberScrollState()

    if (showPasswordSentScreen) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .verticalScroll(scrollState), // Adicionado scroll para telas menores
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            if (isRegistrando) {
                Text(
                    text = "Criar Conta",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.logo_tcc),
                    contentDescription = "Logo IncludEd",
                    modifier = Modifier.size(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            if (isRegistrando) {
                Spacer(modifier = Modifier.height(16.dp))
                // Campo Confirmar Email
                OutlinedTextField(
                    value = confirmarEmail,
                    onValueChange = { confirmarEmail = it },
                    label = { Text("Confirmar Email") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // SEÇÃO DE SELEÇÃO DE TIPO DE PERFIL
                Text(
                    text = "Eu sou:",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Start)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tiposPerfil.forEach { tipo ->
                        val isSelected = tipoPerfilSelecionado == tipo
                        FilterChip(
                            selected = isSelected,
                            onClick = { tipoPerfilSelecionado = tipo },
                            label = { Text(tipo) },
                            modifier = Modifier.weight(1f),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Senha
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
                // Campo Confirmar Senha
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
                                tipoPerfilSelecionado.isEmpty() -> {
                                    onShowMessage("Selecione o seu tipo de perfil")
                                }
                                senha != confirmarSenha -> {
                                    onShowMessage("As senhas não correspondem")
                                }
                                senha.length < 6 -> {
                                    onShowMessage("A senha deve ter pelo menos 6 caracteres")
                                }
                                else -> {
                                    onShowMessage("Conta de $tipoPerfilSelecionado criada com sucesso!")
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
                    modifier = Modifier.fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (isRegistrando) "Cadastrar" else "Entrar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                        // Reset de campos ao alternar
                        email = ""
                        confirmarEmail = ""
                        senha = ""
                        confirmarSenha = ""
                        tipoPerfilSelecionado = ""
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
            Spacer(modifier = Modifier.height(32.dp))
        }

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
