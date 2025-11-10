package cl.goodhealthy.cookify.ui

/**
 * Rutas centralizadas.
 * Mantiene las rutas de Auth, App y el detalle con argumento {id}.
 */
object NavRoutes {
    // === Auth ===
    const val LOGIN    = "login"
    const val REGISTER = "register"

    // === App ===
    const val HOME      = "home"
    const val FAVORITES = "favorites"
    const val BY_TIME   = "byTime"
    const val BY_LETTER = "byLetter"
    const val SETTINGS  = "settings"

    // === Detalle con argumento ===
    const val DETAIL    = "detail/{id}"

    // Helper para navegar al detalle con un id real
    fun detail(id: String) = "detail/$id"
}
