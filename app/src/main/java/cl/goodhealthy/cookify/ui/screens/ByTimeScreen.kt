package cl.goodhealthy.cookify.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.goodhealthy.cookify.ui.RecipesViewModel
import cl.goodhealthy.cookify.ui.components.RecipeCard

/**
 * Búsqueda por tiempo:
 * - Slider (5–120 min) y lista de resultados.
 * - Usa totalMinutes de cada receta.
 */
@Composable
fun ByTimeScreen(
    vm: RecipesViewModel,
    nav: NavController
) {
    var max by remember { mutableStateOf(60f) }    // valor por defecto
    val results = remember(max) { vm.byMaxMinutes(max.toInt()) }
    val favs by vm.favorites.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("Tiempo máximo de preparación: ${max.toInt()} minutos")
        Spacer(Modifier.height(8.dp))

        Slider(
            value = max,
            onValueChange = { max = it },
            valueRange = 5f..120f
        )

        Spacer(Modifier.height(12.dp))
        Text("${results.size} recetas encontradas")

        Spacer(Modifier.height(12.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(results, key = { it.id }) { r ->
                RecipeCard(
                    recipe = r,
                    isFavorite = r.id in favs,
                    onToggleFavorite = { vm.toggleFavorite(id = r.id) },
                    onOpen = { nav.navigate("detail/${r.id}") }
                )
            }
        }
    }
}
