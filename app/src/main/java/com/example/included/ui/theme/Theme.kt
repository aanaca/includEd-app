package com.example.included.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Cores principais
val IncludedBlue = Color(135, 206, 235)      // Azul céu principal
val IncludedBlueDark = Color(115, 186, 215)   // Tom mais escuro
val IncludedBlueLight = Color(155, 226, 255)  // Tom mais claro

// Cores de fundo
val BackgroundLight = Color(255, 255, 255)    // Branco
val BackgroundDark = Color(18, 18, 18)        // Preto

// Cores de superfície
val SurfaceLight = Color(245, 245, 245)       // Cinza muito claro
//val SurfaceDark = Color(30, 30, 30)           // Cinza muito escuro

// Cores de texto
val TextPrimary = Color(33, 33, 33)           // Quase preto

private val DarkColorScheme = darkColorScheme(
    primary = IncludedBlue,
    secondary = IncludedBlueDark,
    tertiary = IncludedBlueLight,
    background = BackgroundDark,
    //surface = SurfaceDark,
    onPrimary = BackgroundLight,
    onSecondary = BackgroundLight,
    onTertiary = BackgroundLight,
    onBackground = BackgroundLight,
    onSurface = BackgroundLight
)

private val LightColorScheme = lightColorScheme(
    primary = IncludedBlue,
    secondary = IncludedBlueDark,
    tertiary = IncludedBlueLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = BackgroundLight,
    onSecondary = BackgroundLight,
    onTertiary = BackgroundDark,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}