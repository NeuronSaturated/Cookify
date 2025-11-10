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
import androidx.compose.material.icons.outlined.AlternateEmail
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
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
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    nav: NavController,
    authVm: AuthViewModel
) {
    val cs = MaterialTheme.colorScheme
    val state by authVm.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var showPass by remember { mutableStateOf(false) }

    // Validaciones (coinciden con Register)
    val emailValid by remember(email) {
        derivedStateOf {
            email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
        }
    }
    val passValid by remember(pass) { derivedStateOf { pass.length >= 6 } }
    val canSubmit by remember(emailValid, passValid) { derivedStateOf { emailValid && passValid } }

    val snack = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showReset by remember { mutableStateOf(false) }
    var resetEmail by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(snack) },
        containerColor = cs.background
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
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
                selected = 0,
                onLogin = { /* ya estás */ },
                onRegister = {
                    nav.navigate(NavRoutes.REGISTER) {
                        popUpTo(NavRoutes.LOGIN) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            // Email
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

            // Contraseña con ojo toggle
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
                singleLine = true,
                visualTransformation = if (showPass) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // ¿Olvidaste tu contraseña?
            TextButton(
                onClick = {
                    resetEmail = email
                    showReset = true
                },
                modifier = Modifier.align(Alignment.End)
            ) { Text("¿Olvidaste tu contraseña?") }

            Spacer(Modifier.height(6.dp))

            Button(
                onClick = { authVm.signIn(email.trim(), pass) },
                enabled = canSubmit && !state.loading,
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) { Text("Iniciar Sesión") }

            state.error?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }

    // Diálogo reset
    if (showReset) {
        AlertDialog(
            onDismissRequest = { showReset = false },
            title = { Text("Recuperar contraseña") },
            text = {
                OutlinedTextField(
                    value = resetEmail,
                    onValueChange = { resetEmail = it },
                    label = { Text("Email") },
                    leadingIcon = { Icon(Icons.Outlined.AlternateEmail, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    authVm.sendPasswordReset(resetEmail) { ok, err ->
                        showReset = false
                        scope.launch {
                            snack.showSnackbar(
                                if (ok) "Te enviamos un correo para restablecer tu contraseña."
                                else err ?: "No se pudo enviar el correo."
                            )
                        }
                    }
                }) { Text("Enviar") }
            },
            dismissButton = {
                TextButton(onClick = { showReset = false }) { Text("Cancelar") }
            }
        )
    }
}
