package cl.goodhealthy.cookify.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val user: FirebaseUser? = null,
    val error: String? = null
)

class AuthViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState(user = auth.currentUser))
    val state: StateFlow<AuthUiState> = _state

    /* -------------------- LOGIN -------------------- */
    fun signIn(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(error = "Completa email y contraseña")
            return
        }
        _state.value = _state.value.copy(loading = true, error = null)
        auth.signInWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                _state.value = if (task.isSuccessful) {
                    _state.value.copy(loading = false, user = auth.currentUser, error = null)
                } else {
                    _state.value.copy(loading = false, error = task.exception?.localizedMessage ?: "Error al iniciar sesión")
                }
            }
    }

    /* -------------------- REGISTER con displayName opcional -------------------- */
    fun signUp(email: String, password: String, displayName: String?) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = _state.value.copy(error = "Completa email y contraseña")
            return
        }
        if (password.length < 6) {
            _state.value = _state.value.copy(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        _state.value = _state.value.copy(loading = true, error = null)
        auth.createUserWithEmailAndPassword(email.trim(), password)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = task.exception?.localizedMessage ?: "Error al registrarte"
                    )
                    return@addOnCompleteListener
                }

                // Si el usuario ingresó un nombre, lo guardamos como displayName
                val user = auth.currentUser
                val name = displayName?.trim().orEmpty()
                if (user != null && name.isNotEmpty()) {
                    val req = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()
                    user.updateProfile(req)
                        .addOnCompleteListener {
                            // Actualizamos el estado con el usuario (refresca Settings y demás)
                            _state.value = _state.value.copy(loading = false, user = auth.currentUser, error = null)
                        }
                } else {
                    _state.value = _state.value.copy(loading = false, user = auth.currentUser, error = null)
                }
            }
    }

    /* -------------------- RESET PASSWORD -------------------- */
    fun sendPasswordReset(
        email: String,
        onResult: (success: Boolean, error: String?) -> Unit
    ) {
        val mail = email.trim()
        if (mail.isEmpty()) {
            onResult(false, "Ingresa tu email.")
            return
        }
        auth.sendPasswordResetEmail(mail)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) onResult(true, null)
                else onResult(false, task.exception?.localizedMessage ?: "No se pudo enviar el correo.")
            }
    }

    /* -------------------- SIGN OUT -------------------- */
    fun signOut() {
        auth.signOut()
        _state.value = _state.value.copy(user = null)
    }

    fun clearError() {
        if (_state.value.error != null) {
            _state.value = _state.value.copy(error = null)
        }
    }
}
