package cl.goodhealthy.cookify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import cl.goodhealthy.cookify.data.Recipe
import cl.goodhealthy.cookify.ui.RecipesViewModel
import cl.goodhealthy.cookify.ui.components.RecipeCard
import cl.goodhealthy.cookify.ui.theme.CookifyPrimary
import java.text.Normalizer
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ByLetterScreen(vm: RecipesViewModel, nav: NavController) {
    val all by vm.recipes.collectAsState()
    val favs by vm.favorites.collectAsState()

    var query by remember { mutableStateOf("") }

    // Agrupa recetas por primera letra (A-Z, # para otras)
    val groups = remember(all) { groupByFirstLetter(all) }
    val letters = remember(groups) { groups.keys.sorted() }  // [#, A..Z]
    val state = rememberLazyListState()

    // 👇 Estado para la letra elegida en la barra (dispara scroll con LaunchedEffect)
    var pickedLetter by remember { mutableStateOf<Char?>(null) }

    // Filtrado por query (si hay, ignora la barra)
    val filtered = remember(all, query) {
        if (query.isBlank()) all else all.filter { matches(it, query) }
    }

    // 👇 Efecto de scroll (AHORA está fuera del callback; ya no da error)
    LaunchedEffect(pickedLetter) {
        val letter = pickedLetter ?: return@LaunchedEffect
        val targetIndex = findFirstIndexOfLetter(groups, letters, letter)
        if (targetIndex != null) {
            state.animateScrollToItem(targetIndex)
        }
    }

    Box(Modifier.fillMaxSize()) {

        // Lista principal
        LazyColumn(
            state = state,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    placeholder = { Text("Buscar por nombre o inicial…") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }

            if (query.isNotBlank()) {
                // Resultados por búsqueda
                itemsIndexed(filtered, key = { _, it -> it.id }) { _, r ->
                    // 1) Cuando estás mostrando resultados filtrados por búsqueda:
                    RecipeCard(
                        recipe = r,
                        isFavorite = r.id in favs,
                        onToggleFavorite = { vm.toggleFavorite(id = r.id) },
                        onOpen = { nav.navigate("detail/${r.id}") }
                    )
                }
            } else {
                // Bloques por letra
                letters.forEach { letter ->
                    val list = groups[letter].orEmpty()
                    if (list.isEmpty()) return@forEach

                    item {
                        Text(
                            text = if (letter == '#') "Otros" else letter.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = CookifyPrimary
                        )
                        Spacer(Modifier.height(6.dp))
                    }
                    itemsIndexed(list, key = { _, it -> it.id }) { _, r ->
                        // 2) Dentro del bloque por letra (en el itemsIndexed de cada grupo):
                        RecipeCard(
                            recipe = r,
                            isFavorite = r.id in favs,
                            onToggleFavorite = { vm.toggleFavorite(id = r.id) },
                            onOpen = { nav.navigate("detail/${r.id}") }
                        )
                    }
                    item { Spacer(Modifier.height(8.dp)) }
                }
            }
        }

        // Barra alfabética (scroll bar) a la derecha
        if (query.isBlank()) {
            AlphabetBar(
                letters = letters,
                onPick = { letter -> pickedLetter = letter },   // 👈 solo actualiza estado
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .zIndex(2f)
            )
        }
    }
}

/* ------------------ Helpers de lista/scroll ------------------- */

// Agrupa A..Z y '#'
private fun groupByFirstLetter(list: List<Recipe>): Map<Char, List<Recipe>> {
    val map = ('A'..'Z').associateWith { mutableListOf<Recipe>() }.toMutableMap()
    map['#'] = mutableListOf()
    list.sortedBy { it.title }.forEach { r ->
        val first = r.title.trim().firstOrNull()?.uppercaseChar()
        val key = if (first != null && first in 'A'..'Z') first else '#'
        map[key]!!.add(r)
    }
    return map
}

// Índice del primer "ítem" visible de la letra (considera el TextField inicial + headers)
private fun findFirstIndexOfLetter(
    groups: Map<Char, List<Recipe>>,
    letters: List<Char>,
    letter: Char
): Int? {
    var index = 1 // 1 por el TextField inicial
    for (l in letters) {
        val items = groups[l].orEmpty()
        if (items.isEmpty()) continue
        if (l == letter) return index // header de esa letra
        index += 1 + items.size       // header + elementos del bloque
    }
    return null
}

// Normaliza (sin acentos, lower)
private fun norm(s: String): String {
    val nfd = Normalizer.normalize(s, Normalizer.Form.NFD)
    return nfd.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
        .lowercase().replace("\\s+".toRegex(), " ").trim()
}

private fun matches(r: Recipe, q: String): Boolean {
    val nt = norm(r.title)
    val nq = norm(q)
    if (nt.contains(nq)) return true
    val initials = r.title.split("\\s+".toRegex())
        .filter { it.isNotBlank() }
        .joinToString("") { it.first().uppercaseChar().toString() }
        .lowercase()
    return initials.startsWith(nq)
}

/* --------------- Composable: barra alfabética ---------------- */

@Composable
private fun AlphabetBar(
    letters: List<Char>,                  // ej. [#, A..Z]
    onPick: (Char) -> Unit,
    modifier: Modifier = Modifier
) {
    val barLetters = letters.filter { it == '#' || it in 'A'..'Z' }
    val itemHeight: Dp = 18.dp
    val barHeight = (barLetters.size * itemHeight.value).dp

    Column(
        modifier = modifier
            .width(28.dp)
            .height(barHeight)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
            .pointerInput(barLetters) {
                detectVerticalDragGestures(
                    onDragStart = { offset: Offset ->
                        onPick(indexToLetter(offset.y, barLetters, itemHeight))
                    },
                    onVerticalDrag = { change, _ ->
                        val y = change.position.y
                        onPick(indexToLetter(y, barLetters, itemHeight))
                    }
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        barLetters.forEach { c ->
            Text(
                text = c.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun indexToLetter(y: Float, letters: List<Char>, itemH: Dp): Char {
    val each = itemH.value
    val idx = floor(y / each).toInt()
    val safe = letters[max(0, min(idx, letters.lastIndex))]
    return safe
}
