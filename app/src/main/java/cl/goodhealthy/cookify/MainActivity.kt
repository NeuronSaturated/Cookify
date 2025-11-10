package cl.goodhealthy.cookify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.goodhealthy.cookify.data.SettingsDataStore
import cl.goodhealthy.cookify.ui.AppSettingsViewModel
import cl.goodhealthy.cookify.ui.AuthViewModel
import cl.goodhealthy.cookify.ui.CookifyApp
import cl.goodhealthy.cookify.ui.RecipesViewModel
import cl.goodhealthy.cookify.ui.theme.CookifyTheme

class MainActivity : ComponentActivity() {

    private val vm: RecipesViewModel by viewModels()
    private val authVm: AuthViewModel by viewModels()

    // Importante: creamos AppSettingsViewModel con DataStore mediante un Factory
    private val appSettingsVm: AppSettingsViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val ds = SettingsDataStore(applicationContext)
                @Suppress("UNCHECKED_CAST")
                return AppSettingsViewModel(ds) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dark by appSettingsVm.darkTheme.collectAsState()

            CookifyTheme(darkTheme = dark) {
                CookifyApp(
                    vm = vm,
                    authVm = authVm,
                    appSettingsVm = appSettingsVm
                )
            }
        }
    }
}
