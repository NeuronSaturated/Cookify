package cl.goodhealthy.cookify.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.goodhealthy.cookify.data.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppSettingsViewModel(
    private val dataStore: SettingsDataStore
) : ViewModel() {

    // Lee el valor persistido y lo expone como StateFlow
    val darkTheme: StateFlow<Boolean> =
        dataStore.darkModeFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    // Guarda la preferencia en DataStore
    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { dataStore.setDarkMode(enabled) }
    }
}
