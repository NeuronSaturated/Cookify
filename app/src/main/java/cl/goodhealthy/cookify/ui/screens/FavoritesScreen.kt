package cl.goodhealthy.cookify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.goodhealthy.cookify.ui.NavRoutes
import cl.goodhealthy.cookify.ui.RecipesViewModel
import cl.goodhealthy.cookify.ui.components.RecipeCard

@Composable
fun FavoritesScreen(
    vm: RecipesViewModel,
    nav: NavController
) {
    val cs = MaterialTheme.colorScheme

    // ids favoritos y recetas
    val favoriteIds by vm.favorites.collectAsState()
    val favorites = favoriteIds.mapNotNull { id -> vm.getById(id) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 18.dp, bottom = 18.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(cs.secondaryContainer.copy(alpha = 0.45f)),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.Icon(
                            imageVector = Icons.Outlined.Favorite,
                            contentDescription = null,
                            tint = cs.error
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Mis Favoritos",
                            style = MaterialTheme.typography.headlineSmall,
                            color = cs.onBackground
                        )
                        Text(
                            text = "${favorites.size} recetas guardadas",
                            style = MaterialTheme.typography.bodySmall,
                            color = cs.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (favorites.isEmpty()) {
            item { EmptyFavorites() }
        } else {
            items(favorites, key = { it.id }) { recipe ->
                Surface(tonalElevation = 0.dp, shadowElevation = 0.dp, color = Color.Transparent) {
                    RecipeCard(
                        recipe = recipe,
                        isFavorite = true,
                        onToggleFavorite = { vm.toggleFavorite(recipe.id) },
                        onOpen = { nav.navigate(NavRoutes.detail(recipe.id)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyFavorites() {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(cs.surfaceVariant.copy(alpha = 0.55f)),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = null,
                tint = cs.onSurface.copy(alpha = 0.45f),
                modifier = Modifier.size(44.dp)
            )
        }
        Spacer(Modifier.height(14.dp))
        Text(
            text = "No tienes favoritos aún",
            style = MaterialTheme.typography.titleMedium,
            color = cs.onBackground
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "Explora recetas y agrega tus favoritas aquí",
            style = MaterialTheme.typography.bodySmall,
            color = cs.onSurfaceVariant
        )
    }
}
