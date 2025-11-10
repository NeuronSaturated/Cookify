package cl.goodhealthy.cookify.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cl.goodhealthy.cookify.ui.AppSettingsViewModel
import cl.goodhealthy.cookify.ui.AuthViewModel

@Composable
fun SettingsScreen(
    authVm: AuthViewModel,
    appSettingsVm: AppSettingsViewModel
) {
    // Tomamos SIEMPRE el usuario desde el estado del ViewModel (así se actualiza cuando cambia el displayName)
    val authState by authVm.state.collectAsState()
    val user = authState.user
    val dark by appSettingsVm.darkTheme.collectAsState()

    val cs = MaterialTheme.colorScheme
    val onSurface = cs.onSurface
    val outline = onSurface.copy(alpha = 0.12f)
    val sectionShape = RoundedCornerShape(18.dp)

    // Nombre a mostrar: displayName > parte antes del arroba > "—"
    val displayName = remember(user) {
        user?.displayName?.takeIf { it.isNotBlank() }
            ?: user?.email?.substringBefore('@')
            ?: "—"
    }
    val emailShown = user?.email ?: "—"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium,
            color = onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Personaliza tu experiencia en Cookify",
            style = MaterialTheme.typography.bodyMedium,
            color = onSurface.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(16.dp))

        /* ===== Cuenta ===== */
        Surface(
            shape = sectionShape,
            color = cs.surfaceVariant,                     // crema (contraste sobre fondo blanco)
            border = BorderStroke(1.dp, outline),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Cuenta", style = MaterialTheme.typography.titleLarge, color = onSurface)
                Text(
                    "Gestiona tu información de usuario",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(12.dp))

                // Tarjeta pequeña con el avatar + nombre + email
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(cs.surface)             // blanco
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(cs.primary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null, tint = cs.primary)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(displayName, style = MaterialTheme.typography.titleMedium, color = onSurface)
                        Text(emailShown, style = MaterialTheme.typography.bodySmall, color = onSurface.copy(alpha = 0.7f))
                    }
                }

                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { authVm.signOut() },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Outlined.Logout, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar Sesión")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        /* ===== Apariencia ===== */
        Surface(
            shape = sectionShape,
            color = cs.surfaceVariant,
            border = BorderStroke(1.dp, outline),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Apariencia", style = MaterialTheme.typography.titleLarge, color = onSurface)
                Text(
                    "Personaliza cómo se ve la aplicación",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(cs.surface),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icono
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(cs.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.DarkMode, contentDescription = null, tint = onSurface)
                    }
                    Column(Modifier.weight(1f)) {
                        Text("Modo oscuro", style = MaterialTheme.typography.titleMedium, color = onSurface)
                        Text("Activa el tema oscuro", style = MaterialTheme.typography.bodySmall, color = onSurface.copy(alpha = 0.7f))
                    }
                    Switch(checked = dark, onCheckedChange = { appSettingsVm.setDarkTheme(it) })
                    Spacer(Modifier.width(8.dp))
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        /* ===== Acerca de Cookify ===== */
        Surface(
            shape = sectionShape,
            color = cs.surfaceVariant,
            border = BorderStroke(1.dp, outline),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Acerca de Cookify", style = MaterialTheme.typography.titleLarge, color = onSurface)
                Spacer(Modifier.height(8.dp))
                Text("Versión: 1.0.0", style = MaterialTheme.typography.bodyMedium, color = onSurface)
                Spacer(Modifier.height(8.dp))
                Text(
                    "© 2025 Cookify. Todos los derechos reservados.\nUna aplicación moderna de recetas diseñada para hacer tu experiencia culinaria más fácil y placentera.",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurface.copy(alpha = 0.8f)
                )
            }
        }
    }
}
