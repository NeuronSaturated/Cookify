package cl.goodhealthy.cookify.ui.theme

import androidx.compose.ui.graphics.Color

// -------- Paleta base (claro) --------
val CookifyPrimary      = Color(0xFF2FBF73) // verde menta
val CookifyOnPrimary    = Color(0xFFFFFFFF) // blanco
val CookifySecondary    = Color(0xFF8F705B) // miel / acento
val CookifySurface      = Color(0xFFFFF7DF) // crema (fondo)
val CookifyOnSurface    = Color(0xFF222222) // texto principal

// Derivados (claro)
val CookifyPrimarySoft  = CookifyPrimary.copy(alpha = 0.12f) // seleccionado/hover
val CookifyCard         = Color(0xFFFFFFFF)                  // card blanca
// Chip tiempo (naranja claro)
val CookifyTime         = Color(0xFFFFE7CF) // fondo chip (naranja muy claro)
val CookifyTimeOn       = Color(0xFFB85C00) // texto/icono chip (naranja quemado)

// -------- Paleta oscura --------
val CookifyBackgroundDark      = Color(0xFF121212)
val CookifyOnBackgroundDark    = Color(0xFFEDEDED)
val CookifySurfaceDark         = Color(0xFF1B1B1B)
val CookifyOnSurfaceDark       = Color(0xFFE6E6E6)
val CookifySurfaceVariantDark  = Color(0xFF262626)

// Favoritos (rojo)
val FavoriteRed = Color(0xFFEE3935)
