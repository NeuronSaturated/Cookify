package cl.goodhealthy.cookify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.goodhealthy.cookify.ui.RecipesViewModel
import cl.goodhealthy.cookify.ui.components.RecipeCard

/**
 * Pantalla Home:
 * - Lista todas las recetas en tarjetas.
 * - Permite abrir detalle y marcar/desmarcar favorito.
 */
@Composable
fun HomeScreen(
    vm: RecipesViewModel,
    nav: NavController
) {
    val recipes by vm.recipes.collectAsState()
    val favs by vm.favorites.collectAsState()

    if (recipes.isEmpty()) {
        Text("Cargando recetas…")
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(recipes, key = { it.id }) { r ->
            RecipeCard(
                recipe = r,
                isFav = r.id in favs,
                onFav = { vm.toggleFavorite(r.id) },
                onOpen = { nav.navigate("detail/${r.id}") }
            )
        }
    }
}
