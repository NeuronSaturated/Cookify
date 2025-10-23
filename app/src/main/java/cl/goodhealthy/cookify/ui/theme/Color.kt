package cl.goodhealthy.cookify.ui.theme

import androidx.compose.ui.graphics.Color

// Paleta base
val CookifyPrimary      = Color(0xFF2FB37B)   // verde menta
val CookifyOnPrimary    = Color(0xFFFFFFFF)   // blanco
val CookifySecondary    = Color(0xFFFFC56B)   // miel
val CookifySurface      = Color(0xFFF7F6F2)   // crema (fondo)
val CookifyOnSurface    = Color(0xFF222222)   // texto principal

// Derivados / helpers
val CookifyPrimarySoft  = CookifyPrimary.copy(alpha = 0.12f) // fondo seleccionado en drawer
val CookifyCard         = Color(0xFFEFF2EA)   // fondo suave de card

// Píldora de tiempo (naranja)
val CookifyTime         = Color(0xFFFFE6CC)   // fondo
val CookifyTimeOn       = Color(0xFFBF4E00)   // texto/ícono
