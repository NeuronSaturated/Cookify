package cl.goodhealthy.cookify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores base (definidos en Color.kt)
import cl.goodhealthy.cookify.ui.theme.CookifyPrimary
import cl.goodhealthy.cookify.ui.theme.CookifyOnPrimary
import cl.goodhealthy.cookify.ui.theme.CookifySecondary
import cl.goodhealthy.cookify.ui.theme.CookifySurface
import cl.goodhealthy.cookify.ui.theme.CookifyOnSurface
import cl.goodhealthy.cookify.ui.theme.CookifyBackgroundDark
import cl.goodhealthy.cookify.ui.theme.CookifySurfaceDark
import cl.goodhealthy.cookify.ui.theme.CookifyOnSurfaceDark
import cl.goodhealthy.cookify.ui.theme.CookifySurfaceVariantDark
import cl.goodhealthy.cookify.ui.theme.CookifyTime
import cl.goodhealthy.cookify.ui.theme.CookifyTimeOn

// --- Claro: fondo crema, tarjetas blancas ---
private val LightColors = lightColorScheme(
    primary = CookifyPrimary,
    onPrimary = CookifyOnPrimary,
    secondary = CookifySecondary,

    background = CookifySurface,               // crema (fondo de la app)
    surface = Color(0xFFFFFFFF),               // BLANCO para tarjetas, barras, etc.
    onSurface = CookifyOnSurface,
    surfaceVariant = Color(0xFFF1EFE9),        // sutil para chips y fondos internos

    // Para la píldora de tiempo (naranja)
    tertiaryContainer = CookifyTime,
    onTertiaryContainer = CookifyTimeOn
)

// --- Oscuro: superficies oscuras con buen contraste ---
private val DarkColors = darkColorScheme(
    primary = CookifyPrimary,
    onPrimary = CookifyOnPrimary,
    secondary = CookifySecondary,

    background = CookifyBackgroundDark,
    surface = CookifySurfaceDark,
    onSurface = CookifyOnSurfaceDark,
    surfaceVariant = CookifySurfaceVariantDark,

    // Naranja atenuado para la píldora en oscuro
    tertiaryContainer = Color(0xFF4A3623),
    onTertiaryContainer = Color(0xFFFFD8B5)
)

@Composable
fun CookifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
