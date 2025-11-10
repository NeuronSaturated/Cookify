package cl.goodhealthy.cookify.ui.components

// ===== Imports =====
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import cl.goodhealthy.cookify.data.Recipe

/**
 * Tarjeta de receta estilo mockup:
 * - Card blanca (surfaceVariant) para contrastar en modo claro.
 * - Imagen con esquinas redondeadas.
 * - Título sobre la imagen (abajo-izquierda) con scrim/gradiente.
 * - Botón de favorito flotante (arriba-derecha).
 * - Chip de tiempo en la misma “franja” inferior de la imagen.
 */
@Composable
fun RecipeCard(
    recipe: Recipe,
    isFav: Boolean,
    onOpen: () -> Unit,
    onFav: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant), // blanco en modo claro
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen)
    ) {
        Column(Modifier.padding(12.dp)) {

            // ======= Imagen + overlays =======
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(18.dp))
            ) {
                // Imagen principal
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Scrim/gradiente inferior para mejorar lectura del título y chips
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .fillMaxWidth()
                        .height(90.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.0f),
                                    Color.Black.copy(alpha = 0.50f)
                                )
                            )
                        )
                )

                // Botón favorito flotante (arriba-derecha)
                IconButton(
                    onClick = onFav,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFav) Color(0xFFE53935) else cs.onSurface.copy(alpha = 0.5f)
                    )
                }

                // Título + chip de tiempo en la franja inferior
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(8.dp))
                    TimeChip(minutes = (recipe.totalMinutes ?: 0).toInt())
                }
            }
        }
    }
}

/* ---------- Chip de tiempo (igual al de Detail) ---------- */
@Composable
private fun TimeChip(minutes: Int) {
    Surface(
        color = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(text = "$minutes min", style = MaterialTheme.typography.labelMedium)
        }
    }
}
