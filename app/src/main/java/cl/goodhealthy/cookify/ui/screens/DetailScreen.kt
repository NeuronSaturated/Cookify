package cl.goodhealthy.cookify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.goodhealthy.cookify.ui.RecipesViewModel
import cl.goodhealthy.cookify.ui.theme.CookifyCard
import cl.goodhealthy.cookify.ui.theme.CookifyOnSurface
import cl.goodhealthy.cookify.ui.theme.CookifyPrimary
import cl.goodhealthy.cookify.ui.theme.CookifySurface
import cl.goodhealthy.cookify.ui.theme.CookifyTime
import cl.goodhealthy.cookify.ui.theme.CookifyTimeOn
import cl.goodhealthy.cookify.ui.theme.FavoriteRed
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    vm: RecipesViewModel,
    id: String,
    onBack: () -> Unit
) {
    // ✅ usa el método real del VM
    val recipe = vm.getById(id) ?: return
    var isFav by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        containerColor = CookifySurface,
        topBar = {
            TopAppBar(
                title = { Text("Inicio") }, // solo el breadcrumb del AppBar (no repetimos título grande)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Imagen hero + acciones + título grande
            item {
                Column(Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.extraLarge)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(recipe.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = recipe.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(210.dp)
                        )

                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Píldora de tiempo (naranja)
                            Surface(
                                color = CookifyTime,
                                contentColor = CookifyTimeOn,
                                shape = MaterialTheme.shapes.large
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.AccessTime,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text("${(recipe.totalMinutes ?: 0L).toInt()} min")
                                }
                            }

                            // Corazón de favorito (rojo)
                            IconButton(onClick = { isFav = !isFav }) {
                                Icon(
                                    imageVector = if (isFav) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                    contentDescription = "Favorito",
                                    tint = if (isFav) FavoriteRed else MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(
                        text = recipe.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            // INGREDIENTES (card blanca)
            item {
                Surface(
                    color = CookifyCard,
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = "Ingredientes",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(Modifier.height(8.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            recipe.ingredients.forEach { ing ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    MintDot()
                                    Spacer(Modifier.width(10.dp))
                                    Text(ing, style = MaterialTheme.typography.bodyLarge)
                                }
                            }
                        }
                    }
                }
            }

            // PREPARACIÓN (una card por paso con número menta)
            itemsIndexed(recipe.steps ?: emptyList()) { index, step ->
                Surface(
                    color = CookifyCard,
                    shape = MaterialTheme.shapes.extraLarge
                ) {
                    Row(Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        NumberBadge(number = index + 1)
                        Spacer(Modifier.width(12.dp))
                        Text(step, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}

@Composable
private fun MintDot() {
    Box(
        modifier = Modifier
            .size(10.dp)
            .clip(MaterialTheme.shapes.small)
            .background(CookifyPrimary)
    )
}

@Composable
private fun NumberBadge(number: Int) {
    Surface(
        color = CookifyPrimary,
        contentColor = CookifyOnSurface,
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Text(
            text = number.toString(),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelLarge
        )
    }
}
