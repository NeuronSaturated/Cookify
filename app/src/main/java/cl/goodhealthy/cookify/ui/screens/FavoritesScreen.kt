package cl.goodhealthy.cookify.ui.screens

// ---- Imports correctos ----
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.goodhealthy.cookify.ui.RecipesViewModel
import cl.goodhealthy.cookify.ui.components.RecipeCard

/**
 * Favoritos:
 * - Muestra solo recetas cuyo id está marcado como favorito.
 * - Usa el set de ids del ViewModel.
 */
@Composable
fun FavoritesScreen(
    vm: RecipesViewModel,
    nav: NavController
) {
    // Estados provenientes del ViewModel
    val all by vm.recipes.collectAsState()
    val favs by vm.favorites.collectAsState()

    // Filtramos solo las recetas favoritas (memoizado para no recalcular en cada recomposición)
    val list = remember(all, favs) { all.filter { it.id in favs } }

    if (list.isEmpty()) {
        Text("Aún no tienes favoritos 🙂")
        return
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 👇 OJO: aquí va 'items(list, …)', NO 'items(count = …)'
        items(list, key = { it.id }) { r ->
            RecipeCard(
                recipe = r,
                isFav = true,
                onFav = { vm.toggleFavorite(r.id) },
                onOpen = { nav.navigate("detail/${r.id}") }
            )
        }
    }
}
