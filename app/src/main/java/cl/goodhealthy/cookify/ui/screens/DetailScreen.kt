package cl.goodhealthy.cookify.ui.screens

// ===== Imports =====
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import cl.goodhealthy.cookify.ui.RecipesViewModel

@Composable
fun DetailScreen(
    nav: NavController,
    vm: RecipesViewModel,
    recipeId: String
) {
    val cs = MaterialTheme.colorScheme
    val outline = cs.onSurface.copy(alpha = 0.12f)

    val recipe = vm.getById(recipeId)
    if (recipe == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Receta no encontrada", color = cs.onBackground)
        }
        return
    }

    // Observa favoritos correctamente
    val favs by vm.favorites.collectAsState()
    val isFav = recipe.id in favs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ===== Header compacto con imagen =====
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            AsyncImage(
                model = recipe.imageUrl,
                contentDescription = recipe.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Scrim para legibilidad del título
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color(0xAA000000)
                            )
                        )
                    )
            )

            // Botón volver (arriba-izquierda)
            IconButton(
                onClick = { nav.popBackStack() },
                modifier = Modifier
                    .padding(12.dp)
                    .size(40.dp)
                    .align(Alignment.TopStart)
                    .clip(CircleShape)
                    .background(cs.surface.copy(alpha = 0.9f))
            ) {
                Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver")
            }

            // Botón favorito (arriba-derecha)
            IconButton(
                onClick = { vm.toggleFavorite(recipe.id) },
                modifier = Modifier
                    .padding(12.dp)
                    .size(40.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(cs.surface.copy(alpha = 0.9f))
            ) {
                Icon(
                    imageVector = if (isFav) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favorito",
                    tint = if (isFav) cs.primary else cs.onSurface
                )
            }

            // Título y chips
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp, end = 16.dp, bottom = 14.dp)
            ) {
                Text(
                    recipe.title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                )
                Spacer(Modifier.height(6.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tiempo (Long? o Int?)
                    recipe.totalMinutes?.let { m ->
                        val minutesText = when (m) {
                            is Long -> "${m} min"
                            else -> "${m.toString()} min"
                        }
                        AssistChip(
                            onClick = {},
                            label = { Text(minutesText, style = MaterialTheme.typography.labelMedium) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.AccessTime,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = cs.secondaryContainer,
                                labelColor = cs.onSecondaryContainer,
                                leadingIconContentColor = cs.onSecondaryContainer
                            )
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ===== Ingredientes =====
        SectionCard(
            title = "Ingredientes",
            useWhiteCard = true
        ) {
            val items: List<String> = recipe.ingredients ?: emptyList()
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items.forEach { ingredient ->
                    Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .padding(top = 6.dp)
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(cs.primary)
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            ingredient,
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.onSurface
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ===== Preparación =====
        SectionCard(
            title = "Preparación",
            useWhiteCard = true
        ) {
            val steps: List<String> = recipe.steps ?: emptyList()
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                steps.forEachIndexed { index, step ->
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(cs.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                style = MaterialTheme.typography.labelLarge,
                                color = cs.primary
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Text(
                            step,
                            style = MaterialTheme.typography.bodyMedium,
                            color = cs.onSurface
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(18.dp))
    }
}

@Composable
private fun SectionCard(
    title: String,
    useWhiteCard: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val container = if (useWhiteCard) cs.surface else cs.surfaceVariant
    val border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.15f))

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = container,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = border,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                color = cs.onSurface
            )
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}
