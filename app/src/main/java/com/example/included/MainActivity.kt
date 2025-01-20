package com.example.included

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.included.screens.HomeScreen
import com.example.included.screens.LoginScreen
import com.example.included.ui.theme.IncludEdTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IncludEdTheme {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember {
                        mutableStateOf(FirebaseAuth.getInstance().currentUser != null)
                    }

                    if (isLoggedIn) {
                        HomeScreen(
                            onSignOut = {
                                isLoggedIn = false
                            },
                            onShowMessage = { message ->
                                Toast.makeText(
                                    this@MainActivity,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    } else {
                        LoginScreen(
                            onLoginSuccess = {
                                isLoggedIn = true // Permitir acesso à HomeScreen
                            },
                            onShowMessage = { message ->
                                Toast.makeText(
                                    this@MainActivity,
                                    message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }
    }
}