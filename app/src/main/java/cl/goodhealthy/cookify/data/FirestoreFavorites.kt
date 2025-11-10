package cl.goodhealthy.cookify.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Fuente remota en Firestore para favoritos.
 *
 * Estructura propuesta:
 *   users/{uid}/prefs (documento)
 *     - favoriteIds: array<string>
 *
 * Reglas (sugeridas):
 * match /databases/{database}/documents {
 *   match /users/{uid}/prefs {
 *     allow read, write: if request.auth != null && request.auth.uid == uid;
 *   }
 * }
 */
class FirestoreFavorites(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    /** Referencia al doc de preferencias del usuario. */
    private fun prefsRef(uid: String): DocumentReference =
        db.collection("users").document(uid).collection("meta").document("prefs")
    // Nota: uso subcolección "meta" para dejar espacio a otros docs (stats, profile, etc.)
    // Si prefieres users/{uid}/prefs directamente, cambia a:
    // db.collection("users").document(uid).collection("prefs").document("default")

    /**
     * Escucha en tiempo real el array de favoritos del usuario.
     * Devuelve un Flow<Set<String>>.
     */
    fun observe(uid: String): Flow<Set<String>> = callbackFlow {
        val reg = prefsRef(uid).addSnapshotListener { snap, err ->
            if (err != null) {
                // En caso de error, no cerramos el flujo; emitimos conjunto vacío
                trySend(emptySet())
                return@addSnapshotListener
            }
            val list = (snap?.get("favoriteIds") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
            trySend(list.toSet())
        }
        awaitClose { reg.remove() }
    }

    /**
     * Lee una vez el set remoto (sincrónico con await).
     */
    suspend fun getOnce(uid: String): Set<String> {
        val doc = prefsRef(uid).get().await()
        val list = (doc.get("favoriteIds") as? List<*>)?.filterIsInstance<String>() ?: emptyList()
        return list.toSet()
    }

    /**
     * Reemplaza completamente el set remoto.
     */
    suspend fun setAll(uid: String, ids: Set<String>) {
        prefsRef(uid).set(mapOf("favoriteIds" to ids.toList())).await()
    }

    /**
     * Alterna un id en el array remoto usando operaciones atómicas.
     * (arrayUnion / arrayRemove)
     */
    suspend fun toggle(uid: String, id: String) {
        val ref = prefsRef(uid)
        // Intentamos añadir; si ya existía, hacemos remove.
        // Esto requiere dos pasos para saber si estaba o no.
        val cur = getOnce(uid)
        if (cur.contains(id)) {
            ref.update("favoriteIds", FieldValue.arrayRemove(id)).await()
        } else {
            ref.update("favoriteIds", FieldValue.arrayUnion(id)).await()
        }
    }
}
