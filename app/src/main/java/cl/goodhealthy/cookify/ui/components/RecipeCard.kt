package cl.goodhealthy.cookify.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import cl.goodhealthy.cookify.data.Recipe
import androidx.compose.ui.graphics.Color


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
        colors = CardDefaults.cardColors(containerColor = cs.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onOpen)
    ) {
        // Header con imagen y botón de favorito
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(18.dp))
        ) {
            // Imagen remota (o local si tu Recipe trae un resId)
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            IconButton(
                onClick = onFav,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFav) Color(0xFFE53935) else cs.outline
                )
            }
        }

        // Cuerpo: título + chip de tiempo
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleMedium,
                color = cs.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(10.dp))

            TimeChip(minutes = (recipe.totalMinutes ?: 0).toInt())
        }
    }
}

@Composable
private fun TimeChip(minutes: Int) {
    val cs = MaterialTheme.colorScheme
    Surface(
        color = cs.secondaryContainer,
        contentColor = cs.onSecondaryContainer,
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
