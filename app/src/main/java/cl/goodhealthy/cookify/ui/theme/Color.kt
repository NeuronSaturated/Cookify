package cl.goodhealthy.cookify.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta base (claro)
val CookifyPrimary      = Color(0xFF2FB37B)   // verde menta
val CookifyOnPrimary    = Color(0xFFFFFFFF)   // blanco
val CookifySecondary    = Color(0xFFFFC56B)   // miel
val CookifySurface      = Color(0xFFF7F6F2)   // crema (fondo)
val CookifyOnSurface    = Color(0xFF222222)   // texto principal

// Derivados / helpers (claro)
val CookifyPrimarySoft  = CookifyPrimary.copy(alpha = 0.12f) // seleccionado en drawer
val CookifyCard         = Color(0xFFEFF2EA)                  // fondo suave de card
val CookifyTime         = Color(0xFFFFE6CC)                  // píldora tiempo (fondo)
val CookifyTimeOn       = Color(0xFFBF4E00)                  // píldora tiempo (texto/ícono)

// -------- Paleta oscura --------
val CookifyBackgroundDark   = Color(0xFF121212)
val CookifySurfaceDark      = Color(0xFF1B1B1B)
val CookifyOnSurfaceDark    = Color(0xFFEAEAEA)
val CookifySurfaceVariantDark= Color(0xFF262626)
