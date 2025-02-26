package com.example.included.screens
import com.example.included.R

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Referências aos campos do layout
        val email = findViewById<EditText>(R.id.etEmail)
        val confirmEmail = findViewById<EditText>(R.id.etConfirmEmail)
        val senha = findViewById<EditText>(R.id.etSenha)
        val confirmSenha = findViewById<EditText>(R.id.etConfirmSenha)
        val btnRegistrar = findViewById<Button>(R.id.btnCriarConta)

        // Configura o clique do botão Registrar
        btnRegistrar.setOnClickListener {
            val emailText = email.text.toString().trim()
            val confirmEmailText = confirmEmail.text.toString().trim()
            val senhaText = senha.text.toString().trim()
            val confirmSenhaText = confirmSenha.text.toString().trim()

            // Validação dos campos
            if (emailText.isEmpty() || confirmEmailText.isEmpty() || senhaText.isEmpty() || confirmSenhaText.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
