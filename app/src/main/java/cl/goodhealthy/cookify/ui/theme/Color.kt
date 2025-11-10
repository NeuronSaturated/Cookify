package cl.goodhealthy.cookify.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta base
val CookifyPrimary      = Color(0xFF2FB37B)   // verde menta
val CookifyOnPrimary    = Color(0xFFFFFFFF) // blanco
val CookifySecondary    = Color(0xFFFFC56B)   // miel/dorado suave


// Neutros
val CookifySurface      = Color(0xFFFFFDF6)   // crema (FONDO de la app)
val CookifyOnSurface    = Color(0xFF222222)   // texto principal

// Derivados (claro)
val CookifyPrimarySoft  = CookifyPrimary.copy(alpha = 0.12f) // seleccionado/hover


// Cards y helpers
val CookifyCard         = Color(0xFFFFFFFF)   // blanco puro para Cards
val CookifyTime         = Color(0xFFFFE6CC)   // pastilla "tiempo" (fondo)
val CookifyTimeOn       = Color(0xFFBF8400)   // pastilla "tiempo" (texto/ícono)


// Favoritos (rojo)
val FavoriteRed = Color(0xFFEE3935)


// -------- Paleta oscura --------
val CookifyBackgroundDark      = Color(0xFF121212)
val CookifyOnBackgroundDark    = Color(0xFFEDEDED)
val CookifySurfaceDark         = Color(0xFF1B1B1B)
val CookifyOnSurfaceDark       = Color(0xFFE6E6E6)
val CookifySurfaceVariantDark  = Color(0xFF262626)
