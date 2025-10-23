package cl.goodhealthy.cookify.ui.screens

// ---------- Imports (Material3 + Compose + Coil) ----------
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cl.goodhealthy.cookify.data.Recipe
import cl.goodhealthy.cookify.ui.RecipesViewModel

// ======= Colores de tu tema =======
// Si tus tokens tienen otros nombres, cámbialos aquí
@Composable private fun CookifyPrimary() = MaterialTheme.colorScheme.primary
@Composable private fun CookifySurface() = MaterialTheme.colorScheme.surface
@Composable private fun CookifyOnSurface() = MaterialTheme.colorScheme.onSurface
@Composable private fun CookifyTime() = Color(0xFFFFB25B) // tu naranja del badge de tiempo

/**
 * Pantalla de Detalle de Receta (con scroll, imagen hero, chips y secciones en cards).
 */
@Composable
fun DetailScreen(vm: RecipesViewModel, id: String, nav: NavController? = null) {
    // Obtenemos la receta y favoritos del VM
    val recipe: Recipe? = remember(id) { vm.getById(id) }
    val favs by vm.favorites.collectAsState()
    val isFav = favs.contains(id)

    if (recipe == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Receta no encontrada")
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ----- Hero con imagen + overlay + botones flotantes -----
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Degradado para mejorar legibilidad del título
                Box(
                    Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                0f to Color.Transparent,
                                0.5f to Color.Transparent,
                                1f to Color(0xAA000000)
                            )
                        )
                )

                // Botón Back (izquierda)
                IconButton(
                    onClick = { nav?.popBackStack() },
                    modifier = Modifier
                        .padding(12.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CookifySurface().copy(alpha = 0.85f))
                        .align(Alignment.TopStart)
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                }

                // Botón Favorito (derecha)
                IconButton(
                    onClick = { vm.toggleFavorite(id) },
                    modifier = Modifier
                        .padding(12.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CookifySurface().copy(alpha = 0.85f))
                        .align(Alignment.TopEnd)
                ) {
                    if (isFav) {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Quitar de favoritos",
                            tint = CookifyPrimary()
                        )
                    } else {
                        Icon(
                            Icons.Outlined.FavoriteBorder,
                            contentDescription = "Agregar a favoritos",
                            tint = CookifyPrimary()
                        )
                    }
                }

                // Título sobre la imagen
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // ----- Chips (tiempo / dificultad / categoría) -----
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Tiempo (si existe; si no, no lo mostramos)
                recipe.totalMinutes?.takeIf { it > 0 }?.let { mins ->
                    MetaChip(
                        text = "$mins min",
                        background = CookifyTime(),
                        content = CookifyOnSurface()
                    )
                }
                // Puedes cambiar "Media" / "Fácil" si tienes ese dato
                MetaChip(text = "Media")
                // Tomamos la primera categoría si existe
                recipe.categories.firstOrNull()?.let { cat ->
                    MetaChip(text = cat)
                }
            }
        }

        // ----- Card Ingredientes -----
        item {
            SectionCard(
                title = "Ingredientes",
                contentPadding = PaddingValues(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    recipe.ingredients.forEach { ing ->
                        BulletItem(text = ing)
                    }
                }
            }
        }

        // ----- Card Preparación (pasos) -----
        item {
            SectionCard(
                title = "Preparación",
                contentPadding = PaddingValues(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    recipe.steps.forEachIndexed { index, step ->
                        StepItem(number = index + 1, text = step)
                    }
                }
            }
        }

        // (opcional) Fuente
//        item {
//            recipe.sourceUrl?.let { src ->
//                Text(
//                    text = "Fuente: $src",
//                    modifier = Modifier.padding(horizontal = 16.dp),
//                    style = MaterialTheme.typography.labelSmall,
//                    color = CookifyOnSurface().copy(alpha = 0.6f)
//                )
//            }
//        }
    }
}

/* ---------------------------------- UI helpers ---------------------------------- */

/** Chip simple para meta (tiempo, dificultad, categoría). */
@Composable
private fun MetaChip(
    text: String,
    background: Color = CookifyPrimary().copy(alpha = 0.12f),
    content: Color = CookifyPrimary()
) {
    Surface(
        color = background,
        contentColor = content,
        shape = RoundedCornerShape(50),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

/** Card contenedora con borde suave y fondo crema. */
@Composable
private fun SectionCard(
    title: String,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CookifySurface(),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = CookifyOnSurface()
            )
            content()
        }
    }
}

/** Ítem con viñeta verde para ingredientes. */
@Composable
private fun BulletItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(CookifyPrimary())
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = CookifyOnSurface()
        )
    }
}

/** Paso numerado con círculo menta. */
@Composable
private fun StepItem(number: Int, text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(CookifyPrimary()),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = CookifyOnSurface()
        )
    }
}
