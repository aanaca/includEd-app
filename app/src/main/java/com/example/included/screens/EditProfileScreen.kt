package com.example.included.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

/**
 * Tela de edição de perfil
 *
 * @param onBack Função chamada ao clicar em voltar.
 * @param onSave Função chamada ao salvar (name, handle, bio, profileImageUri).
 * @param initialName Nome inicial do usuário.
 * @param initialHandle Handle inicial.
 * @param initialBio Bio inicial.
 * @param initialProfileImage Imagem de perfil inicial.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBack: () -> Unit,
    onSave: (String, String, String, Uri?) -> Unit,
    initialName: String = "Nome de Usuário",
    initialHandle: String = "@usuarioExemplo",
    initialBio: String = "Digite sua bio aqui.",
    initialProfileImage: Uri? = null
) {
    var name by remember { mutableStateOf(initialName) }
    var handle by remember { mutableStateOf(initialHandle) }
    var bio by remember { mutableStateOf(initialBio) }
    var profileImageUri by remember { mutableStateOf<Uri?>(initialProfileImage) }

    // Launcher para escolher imagem da galeria
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { profileImageUri = it }
    }

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
                    IconButton(onClick = { onSave(name, handle, bio, profileImageUri) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
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
            // Foto de perfil com clique para alterar
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable { imagePickerLauncher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri),
                        contentDescription = "Foto de Perfil",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(
                        text = name.firstOrNull()?.toString() ?: "",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Nome
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo Usuário
            OutlinedTextField(
                value = handle,
                onValueChange = { handle = it },
                label = { Text("Usuário") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Campo Bio
            OutlinedTextField(
                value = bio,
                onValueChange = { bio = it },
                label = { Text("Bio") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão de salvar
            Button(
                onClick = { onSave(name, handle, bio, profileImageUri) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Alterações")
            }
        }
    }
}
