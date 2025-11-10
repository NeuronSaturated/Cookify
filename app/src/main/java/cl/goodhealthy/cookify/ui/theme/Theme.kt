package cl.goodhealthy.cookify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ---------- Colores ----------
private val LightColors = lightColorScheme(
    primary = CookifyPrimary,
    onPrimary = Color.White,
    secondary = CookifySecondary,

    background = CookifySurface,   // crema #FFFDF6
    surface = CookifySurface,      // crema
    surfaceVariant = CookifyCard,  // blanco para Cards

    onSurface = CookifyOnSurface,
    onSurfaceVariant = CookifyOnSurface,
)

private val DarkColors = darkColorScheme(
    primary = CookifyPrimary,
    onPrimary = Color.White,
    secondary = CookifySecondary,

    // mantenemos la estética: fondo crema + cards blancas
    background = CookifySurface,
    surface = CookifySurface,
    surfaceVariant = CookifyCard,

    onSurface = CookifyOnSurface,
    onSurfaceVariant = CookifyOnSurface,
)

// ---------- Shapes (instancia) ----------
private val CookifyShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small      = RoundedCornerShape(12.dp),
    medium     = RoundedCornerShape(16.dp),
    large      = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

// ---------- Theme ----------
@Composable
fun CookifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography  = Typography,     // tu Typography de Type.kt
        shapes      = CookifyShapes,  // <- ahora sí existe
        content     = content
    )
}
