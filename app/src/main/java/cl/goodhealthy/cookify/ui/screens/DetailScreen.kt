package cl.goodhealthy.cookify.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.goodhealthy.cookify.ui.RecipesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    vm: RecipesViewModel,
    id: String,
    onBack: () -> Unit
) {
    // Tomamos la lista desde el VM y buscamos por id
    val recipes by vm.recipes.collectAsState()
    val recipe = recipes.firstOrNull { it.id == id }

    // Back del sistema (gesto/botón) → navegar hacia atrás
    BackHandler { onBack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(recipe?.title ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (recipe == null) {
                Text("Receta no encontrada", style = MaterialTheme.typography.titleMedium)
                return@Column
            }

            Text(recipe.title, style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(12.dp))

            Text("Ingredientes", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            recipe.ingredients.forEach { item ->
                Text("• $item")
            }

            Spacer(Modifier.height(12.dp))

            Text("Preparación", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            recipe.steps.forEachIndexed { i, step ->
                Text("${i + 1}. $step")
            }
        }
    }
}
