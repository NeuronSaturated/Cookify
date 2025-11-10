package cl.goodhealthy.cookify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ========= Light =========
// Ahora: FONDO BLANCO y las CARDS color crema para mayor contraste (como el mockup)
private val LightColors = lightColorScheme(
    primary = CookifyPrimary,
    onPrimary = Color.White,
    secondary = CookifySecondary,

    // 👇 Invertimos: fondo blanco; contenedores base blancos; CARDS = crema
    background = Color.White,            // fondo general
    surface = Color.White,               // contenedores de nivel base (pantallas, barras)
    surfaceVariant = CookifySurface,     // "cards" y bloques contrastados (crema #FFFDF6)

    onSurface = CookifyOnSurface,
    onSurfaceVariant = CookifyOnSurface,

    // chip "tiempo"
    secondaryContainer = CookifyTime,
    onSecondaryContainer = CookifyTimeOn,
)

// ========= Dark ========= (sin cambios relevantes)
private val DarkColors = darkColorScheme(
    primary = CookifyPrimary,
    onPrimary = Color.White,
    secondary = CookifySecondary,

    background = CookifyBackgroundDark,
    surface = CookifySurfaceDark,
    surfaceVariant = CookifySurfaceVariantDark,

    onSurface = CookifyOnSurfaceDark,
    onSurfaceVariant = CookifyOnSurfaceDark,

    secondaryContainer = CookifyTime,
    onSecondaryContainer = CookifyTimeOn,
)

// Bordes redondeados uniformes
private val CookifyShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(12.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun CookifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography  = Typography,
        shapes      = CookifyShapes,
        content     = content
    )
}
