package com.example.testwinkotlin.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.testWinkotlin.ui.theme.*


private val DarkColorPalette = darkColorScheme(
    primary = White,
    onPrimary = zLuxLightGrey,
    secondary = Teal200,
    outline = messageLine,
    scrim = messageContainerInternal
)

private val LightColorPalette = lightColorScheme(
    primary = White,
    onPrimary = zLuxLightGrey,
    secondary = Black,
    onSecondary = zLuxDarkGrey,
    outline = messageLine,
    scrim = messageContainerInternal
)


@Composable
fun TestWinKotlinTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    androidx.compose.material3.MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}