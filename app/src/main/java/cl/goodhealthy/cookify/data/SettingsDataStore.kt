package cl.goodhealthy.cookify.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extensión top-level (debe estar FUERA de la clase)
private val Context.dataStore by preferencesDataStore(name = "cookify_settings")

private object SettingsKeys {
    val DARK_MODE = booleanPreferencesKey("dark_mode")
}

class SettingsDataStore(private val context: Context) {

    // Flow que emite el valor persistido (false por defecto)
    val darkModeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[SettingsKeys.DARK_MODE] ?: false
    }

    // Guarda el valor
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[SettingsKeys.DARK_MODE] = enabled
        }
    }
}
