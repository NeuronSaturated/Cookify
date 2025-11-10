package cl.goodhealthy.cookify.ui.screens

// ===== Imports =====
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.goodhealthy.cookify.ui.RecipesViewModel
import cl.goodhealthy.cookify.ui.components.RecipeCard
import kotlinx.coroutines.flow.collectLatest

/**
 * Home con lazy loading:
 * - Renderiza vm.visibleRecipes (paginado).
 * - Observa el último índice visible y pide más cuando te acercas al final.
 * - Footer con indicador mientras haya más por cargar.
 */
@Composable
fun HomeScreen(
    vm: RecipesViewModel,
    nav: NavController
) {
    // Lista paginada y favoritos
    val recipes by vm.visibleRecipes.collectAsState()
    val favs by vm.favorites.collectAsState()
    val hasMore by vm.hasMore.collectAsState()

    // Estado del scroll para detectar "near end"
    val listState = rememberLazyListState()

    // Al entrar a la pantalla, aseguramos el estado inicial del paginado
    LaunchedEffect(Unit) { vm.resetPaging() }

    // Observa el último índice visible y dispara loadMoreIfNearEnd()
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collectLatest { lastIndex ->
                if (lastIndex != null) vm.loadMoreIfNearEnd(lastIndex)
            }
    }

    if (recipes.isEmpty()) {
        // Estado inicial vacío (por si la fuente está vacía)
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No hay recetas para mostrar", color = MaterialTheme.colorScheme.onSurface)
        }
        return
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Items paginados
        items(recipes, key = { it.id }) { r ->
            RecipeCard(
                recipe = r,
                isFavorite = r.id in favs,
                onToggleFavorite = { vm.toggleFavorite(id = r.id) },
                onOpen = { nav.navigate("detail/${r.id}") }
            )
        }

        // Footer — indicador mientras hay más por cargar
        if (hasMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .height(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
