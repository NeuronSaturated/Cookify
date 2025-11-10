package cl.goodhealthy.cookify.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import cl.goodhealthy.cookify.data.Recipe   // <- tu paquete real del modelo

@Composable
fun RecipeCard(
    recipe: Recipe,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onToggleFavorite: (() -> Unit)? = null,
    onOpen: (() -> Unit)? = null
) {
    val cs = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpen?.invoke() },
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // ===== Imagen + corazón =====
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = recipe.imageUrl),
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = { onToggleFavorite?.invoke() },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "Quitar de favoritos",
                        tint = cs.error
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Agregar a favoritos",
                        tint = cs.onPrimaryContainer
                    )
                }
            }
        }

        // ===== Título + chips =====
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = cs.onSurface
            )

            Spacer(Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // ---- CHIP TIEMPO (usa totalMinutes: Long?) ----
                recipe.totalMinutes
                    ?.takeIf { it > 0 }
                    ?.toInt()
                    ?.let { ChipSmall(text = "$it min") }

                // ---- CHIP "DIFICULTAD" (realmente primera categoría o fallback) ----
                val diff = recipe.categories.firstOrNull().orEmpty().ifBlank { "Fácil" }
                ChipSmall(text = diff)
            }
        }
    }
}

@Composable
private fun ChipSmall(
    text: String,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    Surface(
        color = cs.secondaryContainer,
        contentColor = cs.onSecondaryContainer,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 0.dp
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}
