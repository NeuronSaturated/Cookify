package cl.goodhealthy.cookify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ========= Light =========
// crema #FFFDF6 de fondo y cards blancas
private val LightColors = lightColorScheme(
    primary = CookifyPrimary,
    onPrimary = Color.White,
    secondary = CookifySecondary,

    background = CookifySurface,          // #FFFDF6
    surface = CookifySurface,             // contenedores base
    surfaceVariant = CookifyCard,         // cards blancas

    onSurface = CookifyOnSurface,
    onSurfaceVariant = CookifyOnSurface,

    // chip "tiempo"
    secondaryContainer = CookifyTime,
    onSecondaryContainer = CookifyTimeOn,
)

// ========= Dark =========
// fondo oscuro real y cards gris oscuro para contraste
private val DarkColors = darkColorScheme(
    primary = CookifyPrimary,
    onPrimary = Color.White,
    secondary = CookifySecondary,

    background = CookifyBackgroundDark,   // p.ej. #121212
    surface = CookifySurfaceDark,         // p.ej. #1B1B1B
    surfaceVariant = CookifySurfaceVariantDark, // p.ej. #262626

    onSurface = CookifyOnSurfaceDark,     // textos claros
    onSurfaceVariant = CookifyOnSurfaceDark,

    // chip "tiempo": mantenemos la pastilla clara con texto oscuro
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
