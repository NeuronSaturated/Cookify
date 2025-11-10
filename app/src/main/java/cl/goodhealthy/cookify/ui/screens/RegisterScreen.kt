package cl.goodhealthy.cookify.ui.screens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cl.goodhealthy.cookify.R
import cl.goodhealthy.cookify.ui.AuthViewModel
import cl.goodhealthy.cookify.ui.NavRoutes
import cl.goodhealthy.cookify.ui.components.AuthTabs

@Composable
fun RegisterScreen(
    nav: NavController,
    authVm: AuthViewModel
) {
    val cs = MaterialTheme.colorScheme
    val state by authVm.state.collectAsState()

    // --- Estados de formulario ---
    var displayName by remember { mutableStateOf("") }   // Nombre opcional
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }

    // --- Validaciones con remember + derivedStateOf ---
    val emailValid by remember(email) {
        derivedStateOf {
            email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        }
    }
    val passValid by remember(pass) { derivedStateOf { pass.length >= 6 } }
    val canSubmit by remember(emailValid, passValid) { derivedStateOf { emailValid && passValid } }

    Surface(color = cs.background, modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .systemBarsPadding()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            // Logo
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(cs.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_cookify_logo),
                    contentDescription = "Cookify"
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Cookify",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                color = cs.onBackground
            )
            Text(
                "Descubre y guarda tus recetas favoritas",
                style = MaterialTheme.typography.bodyMedium,
                color = cs.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(20.dp))

            // Tabs
            AuthTabs(
                selected = 1,
                onLogin = {
                    nav.navigate(NavRoutes.LOGIN) {
                        popUpTo(NavRoutes.REGISTER) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onRegister = { /* ya estás */ },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // ===== Nombre opcional =====
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text("Nombre de usuario (opcional)") },
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            // ===== Email =====
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                isError = email.isNotBlank() && !emailValid,
                label = { Text("Email") },
                supportingText = {
                    if (!emailValid && email.isNotBlank()) {
                        Text("Ingresa un correo válido (ej: nombre@dominio.com)")
                    }
                },
                leadingIcon = { Icon(Icons.Outlined.AlternateEmail, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            // ===== Contraseña =====
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                isError = pass.isNotEmpty() && !passValid,
                label = { Text("Contraseña") },
                supportingText = {
                    val msg = when {
                        pass.isEmpty() -> "Mínimo 6 caracteres"
                        !passValid -> "La contraseña es demasiado corta"
                        else -> null
                    }
                    if (msg != null) Text(msg)
                },
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { showPass = !showPass }) {
                        Icon(
                            imageVector = if (showPass) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            contentDescription = if (showPass) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(18.dp))

            // ===== Botón Crear Cuenta =====
            Button(
                onClick = { authVm.signUp(email.trim(), pass, displayName.ifBlank { null }) },
                enabled = canSubmit && !state.loading,
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) { Text("Crear Cuenta") }

            // Errores backend
            state.error?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
