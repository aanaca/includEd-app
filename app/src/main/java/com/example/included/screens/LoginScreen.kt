package com.example.included.screens

import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    sharedViewModel: SharedViewModel,
    onLoginSuccess: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var confirmarEmail by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    var nome by remember { mutableStateOf("") }
    var handle by remember { mutableStateOf("") }
    var isRegistrando by remember { mutableStateOf(false) }
    var emailCadastrado by remember { mutableStateOf("") }

    val tiposPerfil = listOf(
        Pair("Educador", "🎓"),
        Pair("Especialista", "🩺"),
        Pair("Responsável", "❤️")
    )
    var tipoPerfilSelecionado by remember { mutableStateOf("") }

    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var forgotEmail by remember { mutableStateOf("") }
    var showPasswordSentScreen by remember { mutableStateOf(false) }

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
                text = "Verifique o seu e-mail ($forgotEmail) e siga as instruções para redefinir a sua senha.",
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
                .verticalScroll(scrollState),
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

            if (isRegistrando) {
                // Campo Nome
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Handle
                OutlinedTextField(
                    value = handle,
                    onValueChange = {
                        // Garante que começa com @
                        handle = if (it.startsWith("@")) it else "@$it"
                    },
                    label = { Text("Usuário (@)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Campo de E-mail
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            if (isRegistrando) {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = confirmarEmail,
                    onValueChange = { confirmarEmail = it },
                    label = { Text("Confirmar E-mail") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

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
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    tiposPerfil.forEach { (tipo, icone) ->
                        val isSelected = tipoPerfilSelecionado == tipo
                        FilterChip(
                            selected = isSelected,
                            onClick = { tipoPerfilSelecionado = tipo },
                            label = {
                                Text(
                                    text = "$icone $tipo",
                                    fontSize = 11.sp
                                )
                            },
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        if (isRegistrando) {
                            when {
                                nome.isEmpty() -> {
                                    onShowMessage("Digite o seu nome")
                                }
                                handle.isEmpty() || handle == "@" -> {
                                    onShowMessage("Digite o seu usuário (@)")
                                }
                                email.isEmpty() || confirmarEmail.isEmpty() ||
                                        senha.isEmpty() || confirmarSenha.isEmpty() -> {
                                    onShowMessage("Preencha todos os campos")
                                }
                                email != confirmarEmail -> {
                                    onShowMessage("Os e-mails não correspondem")
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
                                    // Salva as informações do perfil no SharedViewModel
                                    sharedViewModel.updateProfile(
                                        name = nome,
                                        handle = handle,
                                        bio = "Olá! Sou ${tipoPerfilSelecionado} no IncludEd.",
                                        imageUri = null,
                                        userType = tipoPerfilSelecionado
                                    )
                                    onShowMessage("Conta de $tipoPerfilSelecionado criada com sucesso!")
                                    emailCadastrado = email
                                    isRegistrando = false
                                    // Limpa campos
                                    email = emailCadastrado
                                    confirmarEmail = ""
                                    senha = ""
                                    confirmarSenha = ""
                                    nome = ""
                                    handle = ""
                                    tipoPerfilSelecionado = ""
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
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        if (isRegistrando) "Cadastrar" else "Entrar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (!isRegistrando) {
                    TextButton(onClick = { showForgotPasswordDialog = true }) {
                        Text("Esqueceu a sua senha?")
                    }
                }

                TextButton(
                    onClick = {
                        isRegistrando = !isRegistrando
                        email = ""
                        confirmarEmail = ""
                        senha = ""
                        confirmarSenha = ""
                        nome = ""
                        handle = ""
                        tipoPerfilSelecionado = ""
                    }
                ) {
                    Text(
                        if (isRegistrando)
                            "Já tem uma conta? Faça login"
                        else
                            "Não tem uma conta? Registe-se"
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
                        Text("Digite o seu e-mail para recuperar a senha:")
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = forgotEmail,
                            onValueChange = { forgotEmail = it },
                            label = { Text("E-mail") },
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
                                forgotEmail.isEmpty() -> onShowMessage("Digite um e-mail")
                                !Patterns.EMAIL_ADDRESS.matcher(forgotEmail).matches() ->
                                    onShowMessage("Digite um e-mail válido")
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
                    TextButton(onClick = {
                        showForgotPasswordDialog = false
                        forgotEmail = ""
                    }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
