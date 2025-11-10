package cl.goodhealthy.cookify.ui

// ===== Imports =====
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.goodhealthy.cookify.data.FavoritesDataStore
import cl.goodhealthy.cookify.data.FirestoreFavorites
import cl.goodhealthy.cookify.data.Recipe
import cl.goodhealthy.cookify.data.RecipesRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel de recetas con:
 * - Recetas desde assets (repo)
 * - Favoritos persistentes (DataStore) + sincronización Firestore
 * - Paginado simple para Home (lazy loading por tramos)
 */
class RecipesViewModel(app: Application) : AndroidViewModel(app) {

    // --- Fuentes de datos ---
    private val repo = RecipesRepository(app.applicationContext)
    private val favStore = FavoritesDataStore(app.applicationContext)
    private val cloud = FirestoreFavorites()
    private val auth = FirebaseAuth.getInstance()

    // --- Estado base ---
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites

    // --- Paginado (Home) ---
    private val pageSize = 10
    private val _visibleRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val visibleRecipes: StateFlow<List<Recipe>> = _visibleRecipes

    private val _hasMore = MutableStateFlow(false)
    val hasMore: StateFlow<Boolean> = _hasMore

    init {
        // 1) Cargar recetas (assets)
        _recipes.value = repo.getAll()

        // Inicializar paginado (primer tramo)
        resetPaging()

        // 2) Reflejar DataStore en estado UI
        viewModelScope.launch {
            favStore.favoritesFlow.collect { ids -> _favorites.value = ids }
        }

        // 3) Si hay usuario, sincronizar con Firestore
        val uid = auth.currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                // Merge inicial: local ∪ cloud
                val local = favStore.favoritesFlow.first()
                val remote = cloud.getOnce(uid)
                val union = (local + remote).toSet()
                if (union != local) favStore.setAll(union)
                if (union != remote) cloud.setAll(uid, union)

                // Escucha remota → refleja en local si cambia (evita bucles)
                cloud.observe(uid).collect { remoteSet ->
                    if (remoteSet != _favorites.value) {
                        favStore.setAll(remoteSet)
                    }
                }
            }
        }
    }

    // ---------- Helpers ----------
    fun getById(id: String) = _recipes.value.firstOrNull { it.id == id }

    fun byMaxMinutes(max: Int): List<Recipe> =
        _recipes.value.filter { it.totalMinutes?.let { m -> m <= max } == true }

    fun byFirstLetter(c: Char): List<Recipe> =
        _recipes.value.filter { it.title.trim().uppercase().startsWith(c.uppercaseChar()) }

    // ---------- Favoritos ----------
    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            favStore.toggle(id) // local
            auth.currentUser?.uid?.let { uid ->
                runCatching { cloud.toggle(uid, id) }
            }
        }
    }

    // ---------- Paginado (Home) ----------
    /** Reinicia el paginado (útil si cambias la fuente o aplicas un filtro global). */
    fun resetPaging() {
        val all = _recipes.value
        val first = all.take(pageSize)
        _visibleRecipes.value = first
        _hasMore.value = first.size < all.size
    }

    /** Carga el siguiente tramo de recetas si hay más. */
    fun loadMore() {
        val all = _recipes.value
        val current = _visibleRecipes.value
        if (current.size >= all.size) {
            _hasMore.value = false
            return
        }
        val nextEnd = (current.size + pageSize).coerceAtMost(all.size)
        _visibleRecipes.value = all.subList(0, nextEnd)
        _hasMore.value = nextEnd < all.size
    }

    /**
     * Llámalo desde la UI cuando el usuario se acerque al final.
     * Ej: si el último índice visible >= tamaño - 3, pedimos más.
     */
    fun loadMoreIfNearEnd(lastVisibleIndex: Int) {
        val size = _visibleRecipes.value.size
        if (lastVisibleIndex >= size - 3) loadMore()
    }
}
