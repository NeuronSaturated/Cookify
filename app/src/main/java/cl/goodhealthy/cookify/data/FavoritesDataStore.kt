package cl.goodhealthy.cookify.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore de preferencias (archivo "cookify_user.preferences_pb")
private val Context.favDataStore by preferencesDataStore(name = "cookify_user")

private object FavKeys {
    val FAVORITE_IDS = stringSetPreferencesKey("favorite_ids")
}

/**
 * Guarda/lee los IDs de recetas favoritas en DataStore.
 * - Se usa StringSet para persistir el conjunto de IDs.
 */
class FavoritesDataStore(private val context: Context) {

    /** Flujo con el Set<String> de favoritos persistidos. */
    val favoritesFlow: Flow<Set<String>> =
        context.favDataStore.data.map { prefs ->
            prefs[FavKeys.FAVORITE_IDS] ?: emptySet()
        }

    /**
     * Alterna un ID: si está, lo quita; si no está, lo agrega.
     * Persistente e idempotente.
     */
    suspend fun toggle(id: String) {
        context.favDataStore.edit { prefs ->
            val cur = prefs[FavKeys.FAVORITE_IDS]?.toMutableSet() ?: mutableSetOf()
            if (!cur.add(id)) { // si ya estaba, add() devuelve false => lo quitamos
                cur.remove(id)
            }
            prefs[FavKeys.FAVORITE_IDS] = cur
        }
    }

    /** (Opcional) Reemplaza todos los favoritos. */
    suspend fun setAll(ids: Set<String>) {
        context.favDataStore.edit { prefs ->
            prefs[FavKeys.FAVORITE_IDS] = ids
        }
    }
}
