package com.example.included.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = IncludedBlue,
    secondary = IncludedBlueDark,
    tertiary = IncludedBlueLight,
    background = BackgroundDark,
    surface = SurfaceDark,
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
fun IncludEdTheme(
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