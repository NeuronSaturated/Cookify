package cl.goodhealthy.cookify.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.goodhealthy.cookify.data.Recipe
import cl.goodhealthy.cookify.data.RecipesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel: expone el estado para la UI.
 * - Carga recetas desde assets a través del RecipesRepository.
 * - Luego podrás agregar favoritos, filtros, etc.
 */
class RecipesViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = RecipesRepository(app.applicationContext)

    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites

    init {
        _recipes.value = repo.getAll()
    }

    fun getById(id: String) = _recipes.value.firstOrNull { it.id == id }

    fun byMaxMinutes(max: Int): List<Recipe> =
        _recipes.value.filter { it.totalMinutes?.let { m -> m <= max } == true }

    fun byFirstLetter(c: Char): List<Recipe> =
        _recipes.value.filter { it.title.trim().uppercase().startsWith(c.uppercaseChar()) }

    fun toggleFavorite(id: String) {
        _favorites.value = _favorites.value.toMutableSet().apply {
            if (contains(id)) remove(id) else add(id)
        }
    }
}
