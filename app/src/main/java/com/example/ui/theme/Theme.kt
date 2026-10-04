package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = Blue600,
    onPrimary = Surface,
    primaryContainer = NeutralSlateBg,
    onPrimaryContainer = Navy900,
    secondary = Blue700,
    onSecondary = Surface,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    outline = Border,
    surfaceVariant = Background,
    onSurfaceVariant = TextMuted
)

val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(14.dp)
)

val LocalTextScale = compositionLocalOf { 1.0f }

@Composable
fun SafeSandeshTheme(
    textScale: Float = 1.0f,
    content: @Composable () -> Unit
) {
    val currentDensity = LocalDensity.current
    val customDensity = Density(
        density = currentDensity.density,
        fontScale = currentDensity.fontScale * textScale
    )

    CompositionLocalProvider(
        LocalTextScale provides textScale,
        LocalDensity provides customDensity
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = Typography,
            shapes = AppShapes,
            content = content
        )
    }
}
