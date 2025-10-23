package cl.goodhealthy.cookify.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Paleta clara (usa los colores que definiste en Color.kt)
private val LightColors = lightColorScheme(
    primary = CookifyPrimary,
    onPrimary = CookifyOnPrimary,
    secondary = CookifySecondary,
    surface = CookifySurface,
    background = CookifySurface,
    onSurface = CookifyOnSurface,
    onBackground = CookifyOnSurface
)

// Si más adelante quieres tema oscuro, define aquí un DarkColors con darkColorScheme()

@Composable
fun CookifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),   // ahora compila
    content: @Composable () -> Unit
) {
    // Por ahora forzamos LightColors (si luego agregas DarkColors, selecciona según darkTheme)
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography(),
        content = content
    )
}
