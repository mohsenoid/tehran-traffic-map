package com.mohsenoid.tehran.traffic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Amber200,
    primaryVariant = Amber700,
    secondary = Pink200,
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
)

private val LightColorPalette = lightColors(
    primary = Amber500,
    primaryVariant = Amber700,
    secondary = Pink200,
    background = Color.White,
    surface = Color.White,
    /* Other default colors to override
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun TehranTrafficTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = /*if (darkTheme) {*/
//        DarkColorPalette
//    } else {
    LightColorPalette
//    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}