package cl.goodhealthy.cookify.ui

// ==== IMPORTS NECESARIOS ====
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.TextFields
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
import cl.goodhealthy.cookify.ui.theme.*

// 👇 IMPORTS QUE FALTABAN
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/**
 * Pantalla principal con Drawer, TopBar y NavHost.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookifyApp(vm: RecipesViewModel) {
    val nav = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // 👇 scope para lanzar corrutinas desde Composables
    val scope = rememberCoroutineScope()

    // Ítems del drawer
    val items = listOf(
        DrawerItem("Inicio", NavRoutes.HOME, Icons.Outlined.Home),
        DrawerItem("Favoritos", NavRoutes.FAVORITES, Icons.Outlined.FavoriteBorder),
        DrawerItem("Búsqueda por Tiempo", NavRoutes.BY_TIME, Icons.Outlined.AccessTime),
        DrawerItem("Filtrar por Letra", NavRoutes.BY_LETTER, Icons.Outlined.TextFields),
        DrawerItem("Configuración", NavRoutes.SETTINGS, Icons.Outlined.Settings)
    )

    val currentRoute = nav.currentBackStackEntryAsState().value?.destination?.route
    val selected = items.firstOrNull { currentRoute?.startsWith(it.route) == true } ?: items[0]

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = CookifySurface
            ) {
                // ===== Header del Drawer (logo + textos) =====
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cookify_logo),
                        contentDescription = "Logotipo Cookify",
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Cookify",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = CookifyOnSurface
                        )
                        Text(
                            text = "Recetas deliciosas",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = CookifyOnSurface.copy(alpha = 0.7f)
                            )
                        )
                    }
                }

                HorizontalDivider(
                    color = CookifyOnSurface.copy(alpha = 0.1f),
                    thickness = 1.dp
                )

                Text(
                    text = "Menú Principal",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                    color = CookifyOnSurface.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                // ===== Ítems del drawer =====
                items.forEach { itx ->
                    NavigationDrawerItem(
                        label = { Text(itx.label) },
                        selected = selected.route == itx.route,
                        onClick = {
                            // 👇 IMPORTANTE: open/close dentro de una corrutina
                            scope.launch { drawerState.close() }
                            nav.navigate(itx.route) {
                                popUpTo(nav.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(itx.icon, contentDescription = itx.label) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(selected.label) },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                // 👇 abrir el drawer en corrutina
                                scope.launch { drawerState.open() }
                            }
                        ) {
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
                composable(NavRoutes.SETTINGS) { SettingsScreen() }
                composable(NavRoutes.DETAIL) { backStack ->
                    val id = backStack.arguments?.getString("id") ?: return@composable
                    DetailScreen(vm, id)
                }
            }
        }
    }
}

// Modelo de ítem del Drawer
data class DrawerItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)
