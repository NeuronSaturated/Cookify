package cl.goodhealthy.cookify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cl.goodhealthy.cookify.ui.CookifyApp
import cl.goodhealthy.cookify.ui.NavRoutes
import cl.goodhealthy.cookify.ui.RecipesViewModel
import cl.goodhealthy.cookify.ui.screens.DetailScreen
import cl.goodhealthy.cookify.ui.screens.HomeScreen
import cl.goodhealthy.cookify.ui.theme.CookifyTheme

class MainActivity : ComponentActivity() {

    private val vm: RecipesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookifyTheme {
                CookifyApp(vm)   // 👈 ahora todo cuelga del drawer + NavHost
            }
        }
    }
}
