package cl.goodhealthy.cookify.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import cl.goodhealthy.cookify.R

// ===== Poppins con los nombres EXACTOS que tienes en res/font =====
// Regular  -> R.font.poppins
// Medium   -> R.font.poppins_medium
// SemiBold -> R.font.poppins_semibold
// Bold     -> R.font.poppins_bold
private val Poppins = FontFamily(
    Font(R.font.poppins,            FontWeight.Normal),
    Font(R.font.poppins_medium,     FontWeight.Medium),
    Font(R.font.poppins_semibold,   FontWeight.SemiBold),
    Font(R.font.poppins_bold,       FontWeight.Bold),
)

// Escala inspirada en el mockup
val Typography = Typography(

    // App titles grandes (e.g., "Configuración", "Cookify")
    headlineLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp,
        lineHeight = 40.sp
    ),

    // Titulares de secciones (e.g., "Ingredientes", "Mis Favoritos")
    headlineMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 34.sp
    ),

    // Subtítulos destacados dentro de cards
    titleLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),

    // Títulos normales (tarjetas, filas)
    titleMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 24.sp
    ),

    // Texto de cuerpo por defecto
    bodyMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp
    ),

    // Texto de apoyo/ayuda (descripciones, subtítulos)
    bodySmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),

    // Etiquetas compactas (chips, badges)
    labelMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 16.sp
    ),

    // Botones principales
    labelLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 18.sp
    )
)
