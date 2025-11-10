package cl.goodhealthy.cookify.ui

// ===== Imports =====
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.goodhealthy.cookify.data.FavoritesDataStore
import cl.goodhealthy.cookify.data.Recipe
import cl.goodhealthy.cookify.data.RecipesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel principal de recetas.
 *
 * - Carga todas las recetas desde assets (RecipesRepository).
 * - Expone la lista como StateFlow para la UI.
 * - Maneja FAVORITOS persistentes con DataStore (FavoritesDataStore).
 *
 * Nota: seguimos usando AndroidViewModel para acceder a applicationContext
 * (necesario para inicializar el repositorio y el DataStore).
 */
class RecipesViewModel(app: Application) : AndroidViewModel(app) {

    // --- Fuentes de datos ---
    private val repo = RecipesRepository(app.applicationContext)
    private val favStore = FavoritesDataStore(app.applicationContext)

    // --- Estado expuesto a la UI ---
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    // Favoritos como Set<String> (ids de recetas) — PERSISTENTE
    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites

    init {
        // 1) Cargar recetas (desde assets)
        _recipes.value = repo.getAll()

        // 2) Escuchar favoritos desde DataStore (persistentes)
        viewModelScope.launch {
            favStore.favoritesFlow.collect { ids ->
                _favorites.value = ids
            }
        }
    }

    // ---------- Helpers de consulta ----------
    fun getById(id: String) = _recipes.value.firstOrNull { it.id == id }

    fun byMaxMinutes(max: Int): List<Recipe> =
        _recipes.value.filter { it.totalMinutes?.let { m -> m <= max } == true }

    fun byFirstLetter(c: Char): List<Recipe> =
        _recipes.value.filter { it.title.trim().uppercase().startsWith(c.uppercaseChar()) }

    // ---------- Acciones ----------
    /**
     * Alterna un favorito de manera PERSISTENTE:
     * - Escribe en DataStore (no solo en memoria).
     * - La UI se actualiza porque estamos coleccionando favoritesFlow en init {}.
     */
    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            favStore.toggle(id)
        }
    }
}
