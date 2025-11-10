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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cl.goodhealthy.cookify.ui.AppSettingsViewModel
import cl.goodhealthy.cookify.ui.AuthViewModel
import cl.goodhealthy.cookify.ui.theme.CookifyPrimary
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SettingsScreen(
    authVm: AuthViewModel,
    appSettingsVm: AppSettingsViewModel
) {
    val user = remember { FirebaseAuth.getInstance().currentUser }
    val dark by appSettingsVm.darkTheme.collectAsState()

    val cs = MaterialTheme.colorScheme
    val onSurface = cs.onSurface
    val outline = onSurface.copy(alpha = 0.12f)
    val sectionShape = RoundedCornerShape(18.dp)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(cs.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
            color = onSurface
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Personaliza tu experiencia en Cookify",
            style = MaterialTheme.typography.bodyMedium,
            color = onSurface.copy(alpha = 0.7f)
        )
        Spacer(Modifier.height(16.dp))

        // ===== Cuenta =====
        Surface(
            shape = sectionShape,
            color = cs.surface,
            border = BorderStroke(1.dp, outline),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Cuenta",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = onSurface
                )
                Text(
                    "Gestiona tu información de usuario",
                    style = MaterialTheme.typography.bodySmall,
                    color = onSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(cs.surfaceVariant)
                        .padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(CookifyPrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.Person, contentDescription = null, tint = CookifyPrimary)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Usuario",
                            style = MaterialTheme.typography.labelLarge,
                            color = onSurface.copy(alpha = 0.75f)
                        )
                        Text(user?.email ?: "—",
                            style = MaterialTheme.typography.bodyMedium,
                            color = onSurface
                        )
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

        // ===== Apariencia =====
        Surface(
            shape = sectionShape,
            color = cs.surface,
            border = BorderStroke(1.dp, outline),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Apariencia",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = onSurface
                )
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
                        Text("Activa el tema oscuro",
                            style = MaterialTheme.typography.bodySmall,
                            color = onSurface.copy(alpha = 0.7f)
                        )
                    }
                    Switch(checked = dark, onCheckedChange = { appSettingsVm.setDarkTheme(it) })
                    Spacer(Modifier.width(8.dp))
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // ===== Acerca de Cookify =====
        Surface(
            shape = sectionShape,
            color = cs.surface,
            border = BorderStroke(1.dp, outline),
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Text("Acerca de Cookify",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = onSurface
                )
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
