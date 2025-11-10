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
 * ViewModel principal de recetas con sincronización de favoritos:
 * - Local persistente: DataStore (FavoritesDataStore)
 * - Remoto (opcional, si hay usuario logueado): Firestore
 *
 * Estrategia:
 *  1) Carga recetas.
 *  2) Colecciona DataStore -> _favorites (estado de UI).
 *  3) Si hay uid:
 *      - Merge 1 vez (local ∪ remoto) y setea en ambos lados.
 *      - Observa remoto y refleja en local solo si hay diferencia (evita bucles).
 *  4) toggleFavorite(): escribe local y remoto (si hay uid).
 */
class RecipesViewModel(app: Application) : AndroidViewModel(app) {

    // --- Fuentes de datos ---
    private val repo = RecipesRepository(app.applicationContext)
    private val favStore = FavoritesDataStore(app.applicationContext)
    private val cloud = FirestoreFavorites()
    private val auth = FirebaseAuth.getInstance()

    // --- Estado expuesto a la UI ---
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites: StateFlow<Set<String>> = _favorites

    init {
        // 1) Cargar recetas (assets)
        _recipes.value = repo.getAll()

        // 2) Reflejar DataStore en estado UI
        viewModelScope.launch {
            favStore.favoritesFlow.collect { ids ->
                _favorites.value = ids
            }
        }

        // 3) Si hay usuario, sincronizar con Firestore
        val uid = auth.currentUser?.uid
        if (uid != null) {
            viewModelScope.launch {
                // ---- Merge inicial: local ∪ cloud ----
                val local = favStore.favoritesFlow.first()
                val remote = cloud.getOnce(uid)
                val union = (local + remote).toSet()
                if (union != local) favStore.setAll(union)
                if (union != remote) cloud.setAll(uid, union)

                // ---- Escucha remota: refleja en local si cambia ----
                cloud.observe(uid).collect { remoteSet ->
                    // Evita bucles: solo escribe si hay diferencia real
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

    // ---------- Acciones ----------
    /**
     * Alterna favorito:
     * - Siempre actualiza local (DataStore) -> UI reacciona.
     * - Si hay uid, replica en Firestore con operación atómica.
     */
    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            favStore.toggle(id) // local
            auth.currentUser?.uid?.let { uid ->
                runCatching { cloud.toggle(uid, id) }
            }
        }
    }
}
