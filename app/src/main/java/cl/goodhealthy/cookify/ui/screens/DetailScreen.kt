package cl.goodhealthy.cookify.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import cl.goodhealthy.cookify.ui.RecipesViewModel
import cl.goodhealthy.cookify.ui.theme.CookifyCard
import cl.goodhealthy.cookify.ui.theme.CookifyPrimary
import cl.goodhealthy.cookify.ui.theme.CookifySurface
import cl.goodhealthy.cookify.ui.theme.CookifyTime
import cl.goodhealthy.cookify.ui.theme.CookifyTimeOn
import cl.goodhealthy.cookify.ui.theme.FavoriteRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    vm: RecipesViewModel,
    id: String,
    onBack: () -> Unit
) {
    // ✅ Tomamos la receta desde el estado de la lista (no necesitamos recipeById)
    val recipes by vm.recipes.collectAsState()
    val recipe = recipes.firstOrNull { it.id == id } ?: return

    // ❤️ Local por ahora (luego lo conectamos a persistencia)
    var isFav by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = CookifySurface,   // fondo crema
        topBar = {
            TopAppBar(
                title = {},                // ❌ sin “Inicio”
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding(),
                bottom = 24.dp
            )
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                    // 📷 Imagen con chip de tiempo y corazón
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth(),
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(recipe.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentScale = ContentScale.Crop,
                            contentDescription = recipe.title
                        )

                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TimeChip(minutes = recipe.totalMinutes.toString().toIntOrNull() ?: 0)
                            Spacer(Modifier.width(8.dp))
                            Surface(
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                                shape = CircleShape
                            ) {
                                IconButton(onClick = { isFav = !isFav }) {
                                    Icon(
                                        imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                        contentDescription = "Favorito",
                                        tint = if (isFav) FavoriteRed else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // 🏷️ Título grande
                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(8.dp))
                }
            }

            // 🧾 INGREDIENTES (card blanca)
            item {
                SectionCard(
                    title = "Ingredientes"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        recipe.ingredients.forEach { ing ->
                            IngredientRow(text = ing)
                        }
                    }
                }
            }

            // 👨‍🍳 PREPARACIÓN (card blanca, numerada)
            item {
                SectionCard(
                    title = "Preparación"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        // ✅ forEachIndexed (NO itemsIndexed dentro de Column)
                        recipe.steps.forEachIndexed { idx, step ->
                            StepRow(number = idx + 1, text = step)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = CookifyCard),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                content()
            }
        }
    }
}

@Composable
private fun IngredientRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Canvas(modifier = Modifier.size(10.dp)) {
            drawCircle(color = CookifyPrimary) // bullet verde menta
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun StepRow(number: Int, text: String) {
    Row(
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            color = CookifyPrimary,
            contentColor = MaterialTheme.colorScheme.surface,
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier.size(28.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TimeChip(minutes: Int) {
    Surface(
        color = CookifyTime,         // naranja claro
        contentColor = CookifyTimeOn, // texto naranja oscuro
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${minutes} min",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}
