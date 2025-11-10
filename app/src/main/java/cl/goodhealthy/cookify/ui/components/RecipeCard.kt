package cl.goodhealthy.cookify.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.goodhealthy.cookify.data.Recipe

@Composable
fun RecipeCard(
    recipe: Recipe,
    isFav: Boolean,
    onFav: () -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cs = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)

    Card(
        colors = CardDefaults.cardColors(containerColor = cs.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = shape,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpen() }
    ) {
        Column(Modifier.padding(16.dp)) {
            // Título + corazón
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = cs.onSurface,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onFav) {
                    Icon(
                        imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFav) "Quitar de favoritos" else "Agregar a favoritos",
                        // 🔴 Rojo cuando es favorito, gris tenue cuando no
                        tint = if (isFav) cs.error else cs.onSurface.copy(alpha = 0.45f)
                    )
                }
            }

            Spacer(Modifier.height(10.dp))

            // Etiqueta de tiempo (si tu modelo la tiene como Int)
            // Si en tu Recipe no existe totalMinutes, puedes borrar este bloque.
            AssistChip(
                onClick = { },
                label = { Text("${recipe.totalMinutes} min") },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = cs.secondaryContainer,
                    labelColor = cs.onSecondaryContainer
                )
            )
        }
    }
}
