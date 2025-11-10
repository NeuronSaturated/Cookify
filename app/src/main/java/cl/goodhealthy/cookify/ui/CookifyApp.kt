package cl.goodhealthy.cookify.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cl.goodhealthy.cookify.R
import cl.goodhealthy.cookify.ui.screens.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookifyApp(
    vm: RecipesViewModel,
    authVm: AuthViewModel,
    appSettingsVm: AppSettingsViewModel
) {
    val nav = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val cs = MaterialTheme.colorScheme

    val authState by authVm.state.collectAsState()
    val loggedIn = authState.user != null

    // ======= FLUJO DE AUTENTICACIÓN (sin Drawer) =======
    if (!loggedIn) {
        Scaffold { padding ->
            NavHost(
                navController = nav,
                startDestination = NavRoutes.LOGIN,
                modifier = Modifier.padding(padding)
            ) {
                composable(NavRoutes.LOGIN) { LoginScreen(nav = nav, authVm = authVm) }
                composable(NavRoutes.REGISTER) { RegisterScreen(nav = nav, authVm = authVm) }
            }
        }
        return
    }

    // ======= APP AUTENTICADA (con Drawer) =======
    val drawerItems = listOf(
        DrawerItem("Inicio", NavRoutes.HOME, Icons.Outlined.Home),
        DrawerItem("Favoritos", NavRoutes.FAVORITES, Icons.Outlined.FavoriteBorder),
        DrawerItem("Búsqueda por Tiempo", NavRoutes.BY_TIME, Icons.Outlined.AccessTime),
        DrawerItem("Filtrar por Letra", NavRoutes.BY_LETTER, Icons.Outlined.TextFields),
        DrawerItem("Configuración", NavRoutes.SETTINGS, Icons.Outlined.Settings),
    )
    val currentRoute = nav.currentBackStackEntryAsState().value?.destination?.route
    val selected = drawerItems.firstOrNull { currentRoute?.startsWith(it.route) == true } ?: drawerItems.first()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(drawerContainerColor = cs.surface) {
                // Header del Drawer
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cookify_logo),
                        contentDescription = "Cookify logo",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Cookify",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = cs.onSurface
                        )
                        Text(
                            text = "Recetas deliciosas",
                            style = MaterialTheme.typography.bodySmall,
                            color = cs.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                HorizontalDivider(color = cs.onSurface.copy(alpha = 0.1f))

                Text(
                    text = "Menú principal",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                    color = cs.onSurface.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        selected = selected.route == item.route,
                        onClick = {
                            scope.launch { drawerState.close() }
                            nav.navigate(item.route) {
                                popUpTo(nav.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }

                // Nota: "Cerrar sesión" se quitó del Drawer. Está solo en Configuración.
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selected.label) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Outlined.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            }
        ) { padding ->
            NavHost(
                navController = nav,
                startDestination = NavRoutes.HOME,
                modifier = Modifier.padding(padding)
            ) {
                composable(NavRoutes.HOME) { HomeScreen(vm, nav) }
                composable(NavRoutes.FAVORITES) { FavoritesScreen(vm, nav) }
                composable(NavRoutes.BY_TIME) { ByTimeScreen(vm, nav) }
                composable(NavRoutes.BY_LETTER) { ByLetterScreen(vm, nav) }
                composable(NavRoutes.SETTINGS) {
                    SettingsScreen(
                        authVm = authVm,
                        appSettingsVm = appSettingsVm
                    )
                }
                composable(NavRoutes.DETAIL) { backStack ->
                    val id = backStack.arguments?.getString("id") ?: return@composable
                    DetailScreen(vm = vm, id = id)
                }
            }
        }
    }
}

data class DrawerItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)
